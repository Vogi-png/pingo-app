package com.example.taskapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.List;

public class TarefasConcluidas extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TarefaAdapterConcluidas adapter;
    private List<Tarefa> listaTarefas = new ArrayList<>();
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarefas_concluidas);

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new TarefaAdapterConcluidas(listaTarefas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            userId = auth.getCurrentUser().getUid();
            buscarTarefasConcluidas();
        } else {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(TarefasConcluidas.this, LoginForm.class));
        }

        buscarTarefasConcluidas();
    }

    private void buscarTarefasConcluidas() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("tarefas");

        ref.orderByChild("fk_id_usuarios").equalTo(userId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snap) {
                        listaTarefas.clear();
                        for (DataSnapshot s : snap.getChildren()) {
                            Tarefa t = s.getValue(Tarefa.class);
                            if (t != null && "concluida".equals(t.getStatus())) {
                                listaTarefas.add(t);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(TarefasConcluidas.this,
                                "Erro: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
