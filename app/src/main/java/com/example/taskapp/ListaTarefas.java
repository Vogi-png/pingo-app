package com.example.taskapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.taskapp.databinding.ActivityListaTarefasBinding;
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
    private String userId = "123"; // Use seu userId real aqui

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityListaTarefasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configura RecyclerView
        listaTarefas = new ArrayList<>();
        adapter = new TarefaAdapter(listaTarefas);
        binding.recyclerTarefas.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerTarefas.setAdapter(adapter);

        // Barra de navegação
        binding.bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, PaginaUsuario.class));
                return true;
            } else if (itemId == R.id.navigation_dashboard) {
                startActivity(new Intent(this, ListaTarefas.class));
                return true;
            } else if (itemId == R.id.navigation_info) {
                startActivity(new Intent(this, PaginaSobre.class));
                return true;
            } else if (itemId == R.id.navigation_pingo) {
                startActivity(new Intent(this, LoginForm.class));
                return true;
            } else if (itemId == R.id.navigation_centro) {
                return false;
            }
            return false;
        });

        // Botão para criar nova tarefa
        binding.botaomais.setOnClickListener(v -> {
            startActivity(new Intent(this, CriarTarefa.class));
        });

        // Buscar tarefas do usuário no Firebase
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("tarefas");

        // Filtra as tarefas do usuário pelo campo fk_id_usuarios igual a userId (string)
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
