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

        TextView tvNaoPossuiCadastro = findViewById(R.id.id_redirectCadastro);

        tvNaoPossuiCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginForm.this, CadastroForm.class);
                startActivity(intent);
            }
        });

        // O restante do seu código (configuração do botão de login)
    }
}