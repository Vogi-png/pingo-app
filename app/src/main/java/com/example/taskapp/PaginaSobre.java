package com.example.taskapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PaginaSobre extends AppCompatActivity {

    FloatingActionButton fab;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pagina_sobre);

        fab = findViewById(R.id.botaomais);
        bottomNav = findViewById(R.id.bottomNavigationView);

        fab.setOnClickListener(v -> startActivity(new Intent(this, CriarTarefa.class)));
        BottomNavHelper.setup(this, bottomNav);
    }
}