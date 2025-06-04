package com.example.taskapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginForm extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        //Ir para a tela de Cadastro
        TextView tvNaoPossuiCadastro = findViewById(R.id.id_redirectCadastro);
        tvNaoPossuiCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginForm.this, CadastroForm.class);
                startActivity(intent);
            }
        });

        //Ir para a tela de escolher o nome do pingo (sem verificação de input ainda)
        Button btnLogin = findViewById(R.id.id_confirmLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginForm.this, NomearPingo.class);
                startActivity(intent);
            }
        });

    }
}