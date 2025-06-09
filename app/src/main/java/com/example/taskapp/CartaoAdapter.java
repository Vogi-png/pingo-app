// Arquivo: CartaoAdapter.java
package com.example.taskapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartaoAdapter extends RecyclerView.Adapter<CartaoAdapter.CartaoViewHolder> {

    private List<Cartao> listaDeCartoes;

    public CartaoAdapter(List<Cartao> listaDeCartoes) {
        this.listaDeCartoes = listaDeCartoes;
    }

    @NonNull
    @Override
    public CartaoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cartao, parent, false);
        return new CartaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartaoViewHolder holder, int position) {
        Cartao cartao = listaDeCartoes.get(position);

        // CORREÇÃO APLICADA AQUI: de getcardBrand() para getCardBrand()
        String bandeiraFormatada = formatarNomeBandeira(cartao.getCardBrand());
        String info = bandeiraFormatada + " - ***** " + cartao.getLastFourDigits();

        holder.tvInfoCartao.setText(info);
    }

    @Override
    public int getItemCount() {
        return listaDeCartoes.size();
    }

    // Método auxiliar para deixar o nome da bandeira mais apresentável
    // Ex: transforma "MASTERCARD" em "Mastercard"
    private String formatarNomeBandeira(String bandeira) {
        if (bandeira == null || bandeira.isEmpty()) {
            return "Cartão";
        }
        // Converte para minúsculas e depois coloca a primeira letra em maiúscula
        String minusculo = bandeira.toLowerCase();
        return minusculo.substring(0, 1).toUpperCase() + minusculo.substring(1);
    }

    static class CartaoViewHolder extends RecyclerView.ViewHolder {
        TextView tvInfoCartao;

        public CartaoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInfoCartao = itemView.findViewById(R.id.tv_info_cartao);
        }
    }
}