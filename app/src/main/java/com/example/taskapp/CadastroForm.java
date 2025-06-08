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
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class CadastroForm extends AppCompatActivity {

    private EditText inputEmail, inputSenha, inputConfirmarSenha;
    private Button btnCadastrar;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_form);

        auth = FirebaseAuth.getInstance();

        inputEmail = findViewById(R.id.id_cadastro_insertEmail);
        inputSenha = findViewById(R.id.id_cadastro_insertSenha);
        inputConfirmarSenha = findViewById(R.id.id_cadastro_insertConfirmarSenha);
        btnCadastrar = findViewById(R.id.button_cadastrar);
        progressDialog = new ProgressDialog(this);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();
                String senha = inputSenha.getText().toString().trim();
                String confirmarSenha = inputConfirmarSenha.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(senha) || TextUtils.isEmpty(confirmarSenha)) {
                    Toast.makeText(CadastroForm.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!senha.equals(confirmarSenha)) {
                    Toast.makeText(CadastroForm.this, "As senhas não coincidem", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("Cadastrando...");
                progressDialog.show();

                auth.createUserWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(CadastroForm.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(CadastroForm.this, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(CadastroForm.this, NomearPingo.class));
                                    finish();
                                } else {
                                    Toast.makeText(CadastroForm.this, "Erro ao cadastrar: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        ImageView voltarBtn = findViewById(R.id.voltarBtn);
        voltarBtn.setOnClickListener(v -> startActivity(new Intent(CadastroForm.this, LoginForm.class)));
    }
}