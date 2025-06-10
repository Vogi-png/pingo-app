package com.example.taskapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PaginaSobre extends AppCompatActivity {

    // 1. Apenas declare as variáveis aqui, no escopo da classe.
    // Não inicialize ou configure nada aqui.
    FloatingActionButton fab;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pagina_sobre); // O layout é carregado aqui.

        // O código do WindowInsetsListener é gerado pelo Android Studio e está correto.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // --- CÓDIGO MOVIDO PARA CÁ ---
        // 2. Agora que o layout existe, inicialize suas views usando findViewById.
        fab = findViewById(R.id.botaomais);
        bottomNav = findViewById(R.id.bottomNavigationView);

        // 3. Com as views inicializadas, configure os listeners e helpers.
        fab.setOnClickListener(v -> startActivity(new Intent(this, CriarTarefa.class)));
        BottomNavHelper.setup(this, bottomNav);
    }
}