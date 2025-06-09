package com.example.taskapp;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

        listaTarefas = new ArrayList<>();
        adapter = new TarefaAdapter(listaTarefas);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.botaomais);
        fab.setOnClickListener(v -> startActivity(new Intent(this, CriarTarefa.class)));

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        BottomNavHelper.setup(this, bottomNav);

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
                            FirebaseDatabase.getInstance()
                                    .getReference("tarefas")
                                    .child(t.getId())
                                    .removeValue();
                            Toast.makeText(ListaTarefas.this, "Tarefa excluída", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(ListaTarefas.this, CriarTarefa.class);
                            intent.putExtra("tarefaId", t.getId());
                            intent.putExtra("titulo", t.getTitulo());
                            intent.putExtra("descricao", t.getDescricao());
                            intent.putExtra("data", t.getDtcriacao());
                            startActivity(intent);
                        }

                        adapter.notifyItemChanged(pos);
                    }

                    @Override
                    public void onChildDraw(@NonNull Canvas c,
                                            @NonNull RecyclerView recyclerView,
                                            @NonNull RecyclerView.ViewHolder viewHolder,
                                            float dX, float dY,
                                            int actionState, boolean isCurrentlyActive) {

                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                        View itemView = viewHolder.itemView;
                        Paint paint = new Paint();

                        int iconMargin = 50;
                        int iconTop = itemView.getTop() + (itemView.getHeight() - 100) / 2;
                        int iconBottom = iconTop + 100;

                        if (dX > 0) { // Direita (editar)
                            paint.setColor(Color.parseColor("#2196F3")); // azul
                            c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(),
                                    (float) itemView.getLeft() + dX, (float) itemView.getBottom(), paint);

                            Drawable icon = ContextCompat.getDrawable(ListaTarefas.this, R.drawable.ic_editar);
                            if (icon != null) {
                                int iconLeft = itemView.getLeft() + iconMargin;
                                int iconRight = iconLeft + 100;
                                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                                icon.draw(c);
                            }

                        } else if (dX < 0) { // Esquerda (excluir)
                            paint.setColor(Color.parseColor("#F44336")); // vermelho
                            c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(),
                                    (float) itemView.getRight(), (float) itemView.getBottom(), paint);

                            Drawable icon = ContextCompat.getDrawable(ListaTarefas.this, R.drawable.ic_lixeira);
                            if (icon != null) {
                                int iconRight = itemView.getRight() - iconMargin;
                                int iconLeft = iconRight - 100;
                                icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                                icon.draw(c);
                            }
                        }
                    }
                };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.recyclerView);
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
                    if (t != null && t.getConcluida() == 0) {
                        listaTarefas.add(t);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError err) {
                Toast.makeText(ListaTarefas.this, "Erro ao carregar: " + err.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
