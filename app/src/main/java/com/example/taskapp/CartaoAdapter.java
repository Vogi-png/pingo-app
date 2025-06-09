// Local: app/src/main/java/com/example/taskapp/CartaoAdapter.java
package com.example.taskapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartaoAdapter extends RecyclerView.Adapter<CartaoAdapter.CartaoViewHolder> {

    private List<Cartao> listaDeCartoes;
    private OnItemDeleteListener deleteListener;
    private OnItemClickListener itemClickListener;

    // --- INTERFACES ---
    public interface OnItemDeleteListener {
        void onDeleteClick(int position);
    }

    public interface OnItemClickListener {
        void onItemClick(Cartao cartao);
    }

    // --- SETTERS PARA OS LISTENERS ---
    public void setOnItemDeleteListener(OnItemDeleteListener listener) {
        this.deleteListener = listener;
    }

    // ▼▼▼ ESTE ERA O MÉTODO QUE FALTAVA ▼▼▼
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    // --- CONSTRUTOR ---
    public CartaoAdapter(List<Cartao> listaDeCartoes) {
        this.listaDeCartoes = listaDeCartoes;
    }

    // --- MÉTODOS DO RECYCLERVIEW ---
    @NonNull
    @Override
    public CartaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cartao, parent, false);
        return new CartaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartaoViewHolder holder, int position) {
        Cartao cartao = listaDeCartoes.get(position);

        String bandeiraFormatada = formatarNomeBandeira(cartao.getCardBrand());
        String info = bandeiraFormatada + " - ***** " + cartao.getLastFourDigits();
        holder.tvInfoCartao.setText(info);

        // Listener para o ícone de apagar
        holder.ivApagar.setOnClickListener(view -> {
            if (deleteListener != null) {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    deleteListener.onDeleteClick(currentPosition);
                }
            }
        });

        // Listener para o item inteiro
        holder.itemView.setOnClickListener(view -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(cartao);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaDeCartoes.size();
    }

    private String formatarNomeBandeira(String bandeira) {
        if (bandeira == null || bandeira.isEmpty()) return "Cartão";
        String minusculo = bandeira.toLowerCase();
        return minusculo.substring(0, 1).toUpperCase() + minusculo.substring(1);
    }

    // --- VIEWHOLDER ---
    static class CartaoViewHolder extends RecyclerView.ViewHolder {
        TextView tvInfoCartao;
        ImageView ivApagar;

        public CartaoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInfoCartao = itemView.findViewById(R.id.tv_info_cartao);
            ivApagar = itemView.findViewById(R.id.iv_apagar_cartao);
        }
    }
}