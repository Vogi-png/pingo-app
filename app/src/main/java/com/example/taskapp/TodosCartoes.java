// Arquivo: TodosCartoes.java
package com.example.taskapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog; // <-- IMPORT ADICIONADO
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.taskapp.databinding.ActivityTodosCartoesBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

// PASSO 1: Fazer a Activity implementar as DUAS interfaces, separando por vírgula
public class TodosCartoes extends AppCompatActivity implements CartaoAdapter.OnItemDeleteListener, CartaoAdapter.OnItemClickListener {

    private ActivityTodosCartoesBinding binding;
    private CartaoAdapter adapter;
    private List<Cartao> listaDeCartoes;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTodosCartoesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializa Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Configura o RecyclerView
        listaDeCartoes = new ArrayList<>();
        adapter = new CartaoAdapter(listaDeCartoes);
        binding.recyclerViewCartoes.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewCartoes.setAdapter(adapter);

        // Registra o listener para o clique no botão de apagar
        adapter.setOnItemDeleteListener(this);
        // PASSO 2: Registrar esta Activity como o "ouvinte" (listener) dos cliques no item inteiro
        adapter.setOnItemClickListener(this);

        // Listener para o botão de adicionar cartão
        binding.btnAdicionarCartao.setOnClickListener(v -> {
            startActivity(new Intent(this, CadastrarCartao.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        buscarCartoes();
    }

    private void buscarCartoes() {
        FirebaseUser usuarioAtual = auth.getCurrentUser();
        if (usuarioAtual == null) {
            Toast.makeText(this, "Usuário não logado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        String userId = usuarioAtual.getUid();

        db.collection("usuarios").document(userId).collection("credit_cards")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listaDeCartoes.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Cartao cartao = document.toObject(Cartao.class);
                            listaDeCartoes.add(cartao);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(TodosCartoes.this, "Erro ao buscar cartões.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Método para apagar o cartão (já existente)
    @Override
    public void onDeleteClick(int position) {
        Cartao cartaoParaApagar = listaDeCartoes.get(position);
        String documentId = cartaoParaApagar.getDocumentId();
        FirebaseUser usuarioAtual = auth.getCurrentUser();
        if (usuarioAtual == null || documentId == null || documentId.isEmpty()) {
            Toast.makeText(this, "Erro: Não foi possível apagar o cartão.", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = usuarioAtual.getUid();
        db.collection("usuarios").document(userId).collection("credit_cards").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    listaDeCartoes.remove(position);
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(this, "Cartão removido com sucesso.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Falha ao remover o cartão.", Toast.LENGTH_SHORT).show();
                });
    }

    // PASSO 3: Implementação do método da NOVA interface. Este código roda quando o cartão é clicado.
    @Override
    public void onItemClick(Cartao cartao) {
        // Constrói a mensagem para a caixa de diálogo
        String mensagem = "Deseja usar o cartão terminado em " + cartao.getLastFourDigits() + " para esta operação?";

        // Cria a caixa de diálogo (AlertDialog)
        new AlertDialog.Builder(this)
                .setTitle("Adquirir Prêmio?") // Define o título
                .setMessage(mensagem) // Define a mensagem principal
                .setCancelable(true) // Permite fechar clicando fora

                // Define o botão "Confirmar" (Positivo)
                .setPositiveButton("Confirmar", (dialog, which) -> {
                    // CÓDIGO A SER EXECUTADO QUANDO O USUÁRIO CLICAR EM "CONFIRMAR"
                    // Aqui você chamaria a função para resgatar o prêmio
                    Toast.makeText(TodosCartoes.this, "Prêmio adquirido com o cartão " + cartao.getLastFourDigits(), Toast.LENGTH_LONG).show();
                    dialog.dismiss(); // Fecha a caixa de diálogo
                })

                // Define o botão "Cancelar" (Negativo)
                .setNegativeButton("Cancelar", (dialog, which) -> {
                    // Apenas fecha a caixa de diálogo
                    dialog.dismiss();
                })
                .show(); // Mostra a caixa de diálogo na tela
    }
}