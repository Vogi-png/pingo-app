package com.example.taskapp.ui;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.taskapp.R;

public class TrocarTema extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_trocar_tema);

        Button btnTemaClaro = findViewById(R.id.btnTemaClaro);
        btnTemaClaro.setOnClickListener(v -> {
            //TEMA CLARO
        });

        Button btnTemaEscuro = findViewById(R.id.btnTemaEscuro);
        btnTemaEscuro.setOnClickListener(v -> {
            //TEMA ESCURO
        });
    }
}