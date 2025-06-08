package com.example.taskapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class LoginForm extends AppCompatActivity {

    private EditText inputEmail, inputSenha;
    private Button btnLogin;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_form);

        auth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.id_login_insertEmail);
        inputSenha = findViewById(R.id.id_login_insertSenha);
        btnLogin = findViewById(R.id.id_confirmLogin);
        progressDialog = new ProgressDialog(this);

        // Login com Firebase
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String senha = inputSenha.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) {
                    Toast.makeText(LoginForm.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("Entrando...");
                progressDialog.show();

                auth.signInWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(LoginForm.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginForm.this, "Login realizado com sucesso", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(LoginForm.this, NomearPingo.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginForm.this, "Erro ao fazer login: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        // Ir para Cadastro
        TextView tvNaoPossuiCadastro = findViewById(R.id.id_redirectCadastro);
        tvNaoPossuiCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginForm.this, CadastroForm.class));
            }
        });
    }
}