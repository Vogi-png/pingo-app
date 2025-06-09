package com.example.taskapp;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import android.os.Handler; // Corrigido import aqui

public class TarefaAdapter extends RecyclerView.Adapter<TarefaAdapter.TarefaViewHolder> {

    private List<Tarefa> lista;
    private Context context;

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
        if (tarefa.getDtcriacao() != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");
            String dataFormatada = sdf.format(new java.util.Date(tarefa.getDtcriacao()));
            holder.tvData.setText(dataFormatada);
        } else {
            holder.tvData.setText("Data não disponível");
        }
        holder.tvDescricao.setText(tarefa.getDescricao());

        holder.checkConcluida.setChecked(false); // sempre começa desmarcada
        holder.checkConcluida.setButtonTintList(
                ColorStateList.valueOf(ContextCompat.getColor(context, R.color.cinzaEscuro)));

        holder.checkConcluida.setOnCheckedChangeListener((btn, isChecked) -> {
            if (isChecked) {
                // Riscado e verde
                holder.tvTitulo.setPaintFlags(holder.tvTitulo.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                holder.checkConcluida.setButtonTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.verde)));

                // Delay de 5s para marcar como concluída
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    DatabaseReference ref = FirebaseDatabase.getInstance()
                            .getReference("tarefas")
                            .child(tarefa.getId());

                    ref.child("status").setValue("concluida");

                    // Remover da lista com animação
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        lista.remove(pos);
                        notifyItemRemoved(pos);
                    }
                }, 5000);

            } else {
                holder.tvTitulo.setPaintFlags(0);
                holder.checkConcluida.setButtonTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(context, R.color.cinzaEscuro)));
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class TarefaViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvData, tvDescricao;
        CheckBox checkConcluida;

        public TarefaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvData = itemView.findViewById(R.id.tvData);
            tvDescricao = itemView.findViewById(R.id.tvDescricao);
            checkConcluida = itemView.findViewById(R.id.checkConcluida);
        }
    }
}
