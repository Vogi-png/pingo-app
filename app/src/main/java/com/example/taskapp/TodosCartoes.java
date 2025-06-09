// Arquivo: TodosCartoes.java

package com.example.taskapp; // Verifique se o pacote está correto

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
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

public class TodosCartoes extends AppCompatActivity {

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

        // Listener para o botão de adicionar cartão
        binding.btnAdicionarCartao.setOnClickListener(v -> {
            startActivity(new Intent(this, CadastrarCartao.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recarrega os cartões caso o usuário volte da tela de cadastro
        buscarCartoes();
    }

    private void buscarCartoes() {
        FirebaseUser usuarioAtual = auth.getCurrentUser();
        if (usuarioAtual == null) {
            Toast.makeText(this, "Usuário não logado.", Toast.LENGTH_SHORT).show();
            finish(); // Fecha a tela se não houver usuário
            return;
        }

        String userId = usuarioAtual.getUid();

        db.collection("usuarios").document(userId).collection("credit_cards")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            listaDeCartoes.clear(); // Limpa a lista antes de adicionar os novos dados
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Cartao cartao = document.toObject(Cartao.class);
                                listaDeCartoes.add(cartao);
                            }
                            adapter.notifyDataSetChanged(); // Notifica o adaptador que os dados mudaram
                        } else {
                            Toast.makeText(TodosCartoes.this, "Erro ao buscar cartões.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}