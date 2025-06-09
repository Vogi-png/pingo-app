package com.example.taskapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.taskapp.databinding.ActivityListaTarefasBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityListaTarefasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        } else {
            Toast.makeText(this, "Usuário não autenticado.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        listaTarefas = new ArrayList<>();
        adapter = new TarefaAdapter(listaTarefas);
        binding.recyclerTarefas.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerTarefas.setAdapter(adapter);

        binding.botaomais.setOnClickListener(v -> {
            startActivity(new Intent(this, CriarTarefa.class));
        });

        buscarTarefas();

        //barra de navegacao
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        BottomNavHelper.setup(this, bottomNavigationView);
    }

    private void buscarTarefas() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("tarefas");

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