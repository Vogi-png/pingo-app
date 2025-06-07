package com.example.taskapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class CadastroForm extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro_form);

        ImageView voltarBtn = findViewById(R.id.voltarBtn);

        //Ir para a tela de escolher o nome do pingo (sem verificação de input ainda)
        Button btnCadastrar = findViewById(R.id.button_cadastrar);
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadastroForm.this, LoginForm.class);
                startActivity(intent);
            }
        });

    }

}