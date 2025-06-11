package com.example.taskapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PaginaPlano extends AppCompatActivity {

    Button btnAssinar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pagina_plano);

        btnAssinar = findViewById(R.id.btnAssinar);
        btnAssinar.setOnClickListener(v -> {
            Intent intent = new Intent(PaginaPlano.this, TodosCartoes.class);
            startActivity(intent);
        });

    }
}