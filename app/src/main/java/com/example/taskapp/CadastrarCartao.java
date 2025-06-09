package com.example.taskapp; // Verifique se este é o nome do seu pacote

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.taskapp.databinding.ActivityCadastrarCartaoBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CadastrarCartao extends AppCompatActivity {

    private ActivityCadastrarCartaoBinding binding;
    private boolean isUpdatingCardNumber = false;
    private boolean isUpdatingDate = false;

    // MUDANÇA 1: O enum foi movido para o nível da classe, não dentro de um método.
    public enum BandeiraCartao {
        VISA, MASTERCARD, AMEX, ELO, HIPERCARD, DINERS, DESCONHECIDA
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCadastrarCartaoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupCardNumberFormatting();
        setupExpirationDateFormatting();

        binding.buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cadastrarCartao();
            }
        });
    }

    private void setupCardNumberFormatting() {
        binding.inputCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isUpdatingCardNumber) {
                    isUpdatingCardNumber = false;
                    return;
                }
                isUpdatingCardNumber = true;
                String digitsOnly = s.toString().replaceAll("\\D", "");
                StringBuilder formatted = new StringBuilder();
                for (int i = 0; i < digitsOnly.length(); i++) {
                    formatted.append(digitsOnly.charAt(i));
                    if ((i + 1) % 4 == 0 && (i + 1) != digitsOnly.length()) {
                        formatted.append(" ");
                    }
                }
                binding.inputCardNumber.setText(formatted.toString());
                binding.inputCardNumber.setSelection(formatted.length());
            }
        });
    }

    private void setupExpirationDateFormatting() {
        binding.inputValidade.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (isUpdatingDate) {
                    isUpdatingDate = false;
                    return;
                }
                isUpdatingDate = true;
                String digitsOnly = s.toString().replaceAll("\\D", "");
                if (digitsOnly.length() > 4) {
                    digitsOnly = digitsOnly.substring(0, 4);
                }
                if (digitsOnly.length() > 2) {
                    String formatted = digitsOnly.substring(0, 2) + "/" + digitsOnly.substring(2);
                    binding.inputValidade.setText(formatted);
                    binding.inputValidade.setSelection(formatted.length());
                } else {
                    binding.inputValidade.setText(digitsOnly);
                    binding.inputValidade.setSelection(digitsOnly.length());
                }
            }
        });
    }

    private void cadastrarCartao() {
        String nomeNoCartao = binding.inputName.getText().toString().trim();
        String numeroCompleto = binding.inputCardNumber.getText().toString().replaceAll("\\s", "");
        String cvc = binding.inputCvc.getText().toString().trim();
        String validade = binding.inputValidade.getText().toString().trim();

        if (nomeNoCartao.isEmpty() || numeroCompleto.length() < 13 || cvc.length() < 3 || validade.length() < 5) {
            Toast.makeText(this, "Por favor, preencha todos os campos corretamente.", Toast.LENGTH_SHORT).show();
            return;
        }

        // MUDANÇA 2: Chamamos o método para identificar a bandeira
        BandeiraCartao bandeira = identificarBandeira(numeroCompleto);

        String ultimos4Digitos = numeroCompleto.substring(numeroCompleto.length() - 4);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FirebaseUser usuarioAtual = auth.getCurrentUser();
        if (usuarioAtual == null) {
            Toast.makeText(this, "Erro: Faça login novamente.", Toast.LENGTH_LONG).show();
            return;
        }

        String userId = usuarioAtual.getUid();

        Map<String, Object> dadosCartao = new HashMap<>();
        dadosCartao.put("cardholderName", nomeNoCartao);
        dadosCartao.put("lastFourDigits", ultimos4Digitos);
        dadosCartao.put("expirationDate", validade);
        // MUDANÇA 3: Adicionamos a bandeira identificada aos dados para salvar
        dadosCartao.put("cardBrand", bandeira.name()); // .name() converte o enum para String (ex: "VISA")

        binding.buttonAdd.setText("Salvando...");
        binding.buttonAdd.setEnabled(false);

        db.collection("usuarios").document(userId)
                .collection("credit_cards")
                .add(dadosCartao)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(CadastrarCartao.this, "Cartão salvo com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CadastrarCartao.this, "Falha ao salvar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        binding.buttonAdd.setText("Adicionar");
                        binding.buttonAdd.setEnabled(true);
                    }
                });
    }

    // MUDANÇA 1: O método de identificação foi movido para o nível da classe.
    public BandeiraCartao identificarBandeira(String numeroCartao) {
        if (numeroCartao == null || numeroCartao.isEmpty()) {
            return BandeiraCartao.DESCONHECIDA;
        }
        String numeroLimpo = numeroCartao.replaceAll("\\D", "");

        if (numeroLimpo.startsWith("4")) {
            return BandeiraCartao.VISA;
        }
        if (numeroLimpo.startsWith("51") || numeroLimpo.startsWith("52") || numeroLimpo.startsWith("53") || numeroLimpo.startsWith("54") || numeroLimpo.startsWith("55")) {
            return BandeiraCartao.MASTERCARD;
        }
        try {
            int prefixoMastercardNovo = Integer.parseInt(numeroLimpo.substring(0, 4));
            if (prefixoMastercardNovo >= 2221 && prefixoMastercardNovo <= 2720) {
                return BandeiraCartao.MASTERCARD;
            }
        } catch (Exception e) { /* Ignora */ }

        if (numeroLimpo.startsWith("34") || numeroLimpo.startsWith("37")) {
            return BandeiraCartao.AMEX;
        }
        if (numeroLimpo.startsWith("606282")) {
            return BandeiraCartao.HIPERCARD;
        }
        if (numeroLimpo.startsWith("5067") || numeroLimpo.startsWith("401178") || numeroLimpo.startsWith("457631") || numeroLimpo.startsWith("636297") || numeroLimpo.startsWith("650")) {
            return BandeiraCartao.ELO;
        }
        if (numeroLimpo.startsWith("36") || numeroLimpo.startsWith("38") || numeroLimpo.startsWith("39")) {
            return BandeiraCartao.DINERS;
        }
        return BandeiraCartao.DESCONHECIDA;
    }
}