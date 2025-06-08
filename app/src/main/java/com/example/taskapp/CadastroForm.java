package com.example.taskapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CadastroForm extends AppCompatActivity {

    private FirebaseAuth auth; // Firebase Auth

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro_form);

        auth = FirebaseAuth.getInstance();

        // Referência dos campos
        EditText nomeField = findViewById(R.id.id_cadastro_insertNome);
        EditText emailField = findViewById(R.id.id_cadastro_insertEmail);
        EditText senhaField = findViewById(R.id.id_cadastro_insertSenha);
        EditText confirmarSenhaField = findViewById(R.id.id_cadastro_insertConfirmarSenha);

        // Botão voltar
        ImageView voltarBtn = findViewById(R.id.voltarBtn);
        voltarBtn.setOnClickListener(v -> {
            startActivity(new Intent(CadastroForm.this, LoginForm.class));
            finish();
        });

        // Botão cadastrar
        Button btnCadastrar = findViewById(R.id.button_cadastrar);
        btnCadastrar.setOnClickListener(v -> {
            String nome = nomeField.getText().toString().trim();
            String email = emailField.getText().toString().trim();
            String senha = senhaField.getText().toString();
            String confirmarSenha = confirmarSenhaField.getText().toString();

            // Validações básicas
            if (nome.isEmpty()) {
                Toast.makeText(this, "Por favor, insira seu nome.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (email.isEmpty() || !email.contains("@")) {
                Toast.makeText(this, "Insira um email válido.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (senha.isEmpty() || senha.length() < 6) {
                Toast.makeText(this, "A senha deve ter no mínimo 6 caracteres.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!senha.equals(confirmarSenha)) {
                Toast.makeText(this, "As senhas não coincidem.", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(CadastroForm.this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser user = auth.getCurrentUser();
                            Toast.makeText(CadastroForm.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();

                            // Vai para próxima tela
                            Intent intent = new Intent(CadastroForm.this, NomearPingo.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(CadastroForm.this, "Erro ao cadastrar: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            Log.w("Cadastro", "createUserWithEmail:failure", task.getException());
                        }
                    });
        });
    }
}
