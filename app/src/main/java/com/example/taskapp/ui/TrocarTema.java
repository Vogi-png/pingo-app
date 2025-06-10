package com.example.taskapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
