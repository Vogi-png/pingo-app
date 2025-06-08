package com.example.taskapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taskapp.ui.TrocarTema;

public class PaginaUsuario extends AppCompatActivity {

    Button button7;
    Button button10;

    Button button9;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pagina_usuario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        button7 = findViewById(R.id.button7);
        button7.setOnClickListener(v -> {
            Intent intent = new Intent(PaginaUsuario.this, PaginaPlano.class);
            startActivity(intent);
        });

        button10 = findViewById(R.id.button10);
        button10.setOnClickListener(v -> {
            Intent intent = new Intent(PaginaUsuario.this, TodosCartoes.class);
            startActivity(intent);
        });

        button9 = findViewById(R.id.button9);
        button9.setOnClickListener(v -> {
            Intent intent = new Intent(PaginaUsuario.this, TrocarTema.class);
            startActivity(intent);
        });
    }
}