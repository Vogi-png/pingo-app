package com.example.taskapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.taskapp.databinding.ActivityListaTarefasBinding;
import com.google.firebase.auth.FirebaseAuth; // Importe para pegar o usuário logado
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListaTarefas extends AppCompatActivity {

    private ActivityListaTarefasBinding binding;
    private List<Tarefa> listaTarefas;
    private TarefaAdapter adapter;
    private String userId; // ID do usuário logado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Inicializa o View Binding corretamente
        binding = ActivityListaTarefasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Pega o usuário atual do Firebase Authentication
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        } else {
            // Se não houver usuário logado, redirecione para a tela de login
            // ou trate o caso conforme a necessidade do seu app.
            // Por enquanto, vamos exibir um aviso e encerrar.
            Toast.makeText(this, "Usuário não autenticado.", Toast.LENGTH_LONG).show();
            finish();
            return; // Encerra o onCreate para evitar crashes
        }

        // 2. Configura RecyclerView
        listaTarefas = new ArrayList<>();
        adapter = new TarefaAdapter(listaTarefas);
        binding.recyclerTarefas.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerTarefas.setAdapter(adapter);

        // 3. Barra de navegação com a lógica corrigida
        binding.bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, PaginaUsuario.class));
                return true;
            } else if (itemId == R.id.navigation_dashboard) {
                // Já estamos nesta tela, não faz nada para evitar o loop
                return true;
            } else if (itemId == R.id.navigation_info) {
                startActivity(new Intent(this, PaginaSobre.class));
                return true;
            } else if (itemId == R.id.navigation_pingo) {
                startActivity(new Intent(this, PinguHome.class));
                return true;
            }
            return false;
        });

        // Botão para criar nova tarefa
        binding.botaomais.setOnClickListener(v -> {
            startActivity(new Intent(this, CriarTarefa.class));
        });

        // 4. Buscar tarefas do usuário no Firebase
        buscarTarefas();
    }

    private void buscarTarefas() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("tarefas");

        // Filtra as tarefas do usuário pelo campo fk_id_usuarios igual ao ID do usuário logado
        Query query = database.orderByChild("fk_id_usuarios").equalTo(userId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                listaTarefas.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Tarefa tarefa = snap.getValue(Tarefa.class);
                    if (tarefa != null) {
                        listaTarefas.add(tarefa);
                    }
                }
                adapter.notifyDataSetChanged();

                if (listaTarefas.isEmpty()) {
                    Toast.makeText(ListaTarefas.this, "Nenhuma tarefa encontrada", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ListaTarefas.this, "Erro ao carregar tarefas: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}