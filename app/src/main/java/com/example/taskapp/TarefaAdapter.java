package com.example.taskapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taskapp.R;
import com.example.taskapp.Tarefa;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class TarefaAdapter extends RecyclerView.Adapter<TarefaAdapter.TarefaViewHolder> {

    private List<Tarefa> listaTarefas;

    public TarefaAdapter(List<Tarefa> listaTarefas) {
        this.listaTarefas = listaTarefas;
    }

    @NonNull
    @Override
    public TarefaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tarefa, parent, false);
        return new TarefaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TarefaViewHolder holder, int position) {
        Tarefa tarefa = listaTarefas.get(position);
        holder.tvTitulo.setText(tarefa.getTitulo());

        // Formatando timestamp
        Date date = new Date(tarefa.getDtcriacao());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        holder.tvData.setText(sdf.format(date));

        holder.tvDescricao.setText(tarefa.getDescricao());

        // Mostrar/ocultar descrição ao clicar
        holder.itemView.setOnClickListener(v -> {
            if (holder.tvDescricao.getVisibility() == View.GONE) {
                holder.tvDescricao.setVisibility(View.VISIBLE);
            } else {
                holder.tvDescricao.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaTarefas.size();
    }

    public static class TarefaViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvData, tvDescricao;

        public TarefaViewHolder(View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvData = itemView.findViewById(R.id.tvData);
            tvDescricao = itemView.findViewById(R.id.tvDescricao);
        }
    }
}
