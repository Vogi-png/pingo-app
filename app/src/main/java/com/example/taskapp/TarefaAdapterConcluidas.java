package com.example.taskapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TarefaAdapterConcluidas extends RecyclerView.Adapter<TarefaAdapterConcluidas.TarefaViewHolder> {

    private List<Tarefa> lista;
    private Context context;

    public TarefaAdapterConcluidas(List<Tarefa> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public TarefaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View item = LayoutInflater.from(context).inflate(R.layout.item_tarefa_concluida, parent, false);
        return new TarefaViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull TarefaViewHolder holder, int position) {
        Tarefa tarefa = lista.get(position);

        holder.tvTitulo.setText(tarefa.getTitulo());
        holder.tvDescricao.setText(tarefa.getDescricao());

        holder.tvData.setText("Criada: " + formatarData(tarefa.getDtcriacao()));
        holder.tvDataConclusao.setText("Concluída: " + formatarData(tarefa.getDtconclusao()));

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
        return lista.size();
    }

    private String formatarData(Long timestamp) {
        if (timestamp == null) return "N/A";
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(new Date(timestamp));
    }

    public static class TarefaViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvDescricao, tvData, tvDataConclusao;

        public TarefaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvDescricao = itemView.findViewById(R.id.tvDescricao);
            tvData = itemView.findViewById(R.id.tvData);
            tvDataConclusao = itemView.findViewById(R.id.tvDataConclusao);
        }
    }
}