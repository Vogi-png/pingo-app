package com.example.taskapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.taskapp.BottomNavHelper;
import com.example.taskapp.CriarTarefa;
import com.example.taskapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TrocarTema extends AppCompatActivity {
    FloatingActionButton fab;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trocar_tema);

        fab = findViewById(R.id.botaomais);
        bottomNav = findViewById(R.id.bottomNavigationView);

        fab.setOnClickListener(v -> startActivity(new Intent(this, CriarTarefa.class)));
        BottomNavHelper.setup(this, bottomNav);

        Button btnTemaClaro = findViewById(R.id.btnTemaClaro);
        btnTemaClaro.setOnClickListener(v -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        });

        Button btnTemaEscuro = findViewById(R.id.btnTemaEscuro);
        btnTemaEscuro.setOnClickListener(v -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        });
    }
}
