package com.example.taskapp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import android.widget.Toast;

public class TarefaAdapter extends RecyclerView.Adapter<TarefaAdapter.TarefaViewHolder> {

    private List<Tarefa> lista;
    private Context context;
    private int maxFavoritos = 3;

    public TarefaAdapter(List<Tarefa> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public TarefaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View item = LayoutInflater.from(context).inflate(R.layout.item_tarefa, parent, false);
        return new TarefaViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull TarefaViewHolder holder, int position) {
        Tarefa tarefa = lista.get(position);

        holder.tvTitulo.setText(tarefa.getTitulo());

        boolean isFavorito = "sim".equals(tarefa.getFavoritos());
        if (isFavorito) {
            holder.imgFavorito.setColorFilter(Color.parseColor("#ffd700")); // amarelo
        } else {
            holder.imgFavorito.setColorFilter(Color.GRAY); // cinza padrão
        }

        holder.imgFavorito.setOnClickListener(v -> {
            // Conta quantos já estão favoritos
            int favoritosCount = 0;
            for (Tarefa t : lista) {
                if ("sim".equals(t.getFavoritos())) {
                    favoritosCount++;
                }
            }

            if (isFavorito) {
                atualizarFavorito(tarefa, "nao");
                holder.imgFavorito.setColorFilter(Color.GRAY);
            } else {
                if (favoritosCount >= maxFavoritos) {
                    Toast.makeText(context, "Limite de 3 tarefas favoritas atingido", Toast.LENGTH_SHORT).show();
                } else {
                    // Favoritar
                    atualizarFavorito(tarefa, "sim");
                    holder.imgFavorito.setColorFilter(Color.parseColor("#ffd700"));
                }
            }
        });
    }

    private void atualizarFavorito(Tarefa tarefa, String valor) {
        // Atualiza localmente para refletir o estado imediatamente
        tarefa.setFavoritos(valor);

        // Atualiza no Firebase
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("tarefas")
                .child(tarefa.getId());
        ref.child("favoritos").setValue(valor);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class TarefaViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvData, tvDescricao;
        CheckBox checkConcluida;
        ImageView imgFavorito;

        public TarefaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvData = itemView.findViewById(R.id.tvData);
            tvDescricao = itemView.findViewById(R.id.tvDescricao);
            checkConcluida = itemView.findViewById(R.id.checkConcluida);
            imgFavorito = itemView.findViewById(R.id.imgFavorito);
        }
    }
}
