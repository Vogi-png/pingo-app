package com.example.taskapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.taskapp.databinding.ActivityListaTarefasBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
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

        // Autenticação
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Usuário não autenticado.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        userId = user.getUid();

        // RecyclerView e Adapter
        listaTarefas = new ArrayList<>();
        adapter = new TarefaAdapter(listaTarefas);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        // FAB (botão +) direto pelo findViewById
        FloatingActionButton fab = findViewById(R.id.botaomais);
        fab.setOnClickListener(v -> startActivity(new Intent(this, CriarTarefa.class)));

        // BottomNavigationView direto pelo findViewById
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        BottomNavHelper.setup(this, bottomNav);

        // Carregar e configurar swipe
        buscarTarefas();
        configurarSwipe();
    }

    private void configurarSwipe() {
        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull androidx.recyclerview.widget.RecyclerView rv,
                                          @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder vh,
                                          @NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder target) {
                        return false;
                    }
                    @Override
                    public void onSwiped(@NonNull androidx.recyclerview.widget.RecyclerView.ViewHolder vh,
                                         int direction) {
                        int pos = vh.getAdapterPosition();
                        Tarefa t = listaTarefas.get(pos);

                        if (direction == ItemTouchHelper.LEFT) {
                            // Excluir
                            FirebaseDatabase.getInstance()
                                    .getReference("tarefas")
                                    .child(t.getId())
                                    .removeValue();
                            Toast.makeText(ListaTarefas.this, "Tarefa excluída", Toast.LENGTH_SHORT).show();
                        } else {
                            // Editar
                            Intent intent = new Intent(ListaTarefas.this, CriarTarefa.class);
                            intent.putExtra("tarefaId", t.getId());
                            intent.putExtra("titulo", t.getTitulo());
                            intent.putExtra("descricao", t.getDescricao());
                            intent.putExtra("data", t.getDtcriacao());
                            startActivity(intent);
                        }

                        // Refresh item (para não sumir)
                        adapter.notifyItemChanged(pos);
                    }
                };
        new ItemTouchHelper(simpleCallback)
                .attachToRecyclerView(binding.recyclerView);
    }

    private void buscarTarefas() {
        Query q = FirebaseDatabase.getInstance()
                .getReference("tarefas")
                .orderByChild("fk_id_usuarios")
                .equalTo(userId);

        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snap) {
                listaTarefas.clear();
                for (DataSnapshot s : snap.getChildren()) {
                    Tarefa t = s.getValue(Tarefa.class);
                    if (t != null) listaTarefas.add(t);
                }
                adapter.notifyDataSetChanged();
                if (listaTarefas.isEmpty()) {
                    Toast.makeText(ListaTarefas.this,
                            "Nenhuma tarefa encontrada", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError err) {
                Toast.makeText(ListaTarefas.this,
                        "Erro ao carregar: " + err.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
