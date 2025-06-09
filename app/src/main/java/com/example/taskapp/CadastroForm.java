package com.example.taskapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroForm extends AppCompatActivity {

    private EditText inputNome, inputEmail, inputSenha;
    private Button btnCadastrar;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_form);

        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");
        progressDialog = new ProgressDialog(this);

        inputNome = findViewById(R.id.id_cadastro_insertNome);
        inputEmail = findViewById(R.id.id_cadastro_insertEmail);
        inputSenha = findViewById(R.id.id_cadastro_insertSenha);
        btnCadastrar = findViewById(R.id.button_cadastrar);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = inputNome.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String senha = inputSenha.getText().toString().trim();

                if (TextUtils.isEmpty(nome) || TextUtils.isEmpty(email) || TextUtils.isEmpty(senha)) {
                    Toast.makeText(CadastroForm.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.setMessage("Cadastrando...");
                progressDialog.show();

                auth.createUserWithEmailAndPassword(email, senha)
                        .addOnCompleteListener(task -> {
                            progressDialog.dismiss();
                            if (task.isSuccessful()) {
                                String userId = auth.getCurrentUser().getUid();

                                // Criar objeto com os dados do usuário
                                Usuario usuario = new Usuario(nome, email, senha);

                                // Salvar no Realtime Database em /usuarios/userId
                                databaseReference.child(userId).setValue(usuario)
                                        .addOnCompleteListener(task2 -> {
                                            if (task2.isSuccessful()) {
                                                Toast.makeText(CadastroForm.this, "Cadastro concluído!", Toast.LENGTH_SHORT).show();

                                                // Passa userId para NomearPingo
                                                Intent intent = new Intent(CadastroForm.this, NomearPingo.class);
                                                intent.putExtra("userId", userId);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(CadastroForm.this, "Erro ao salvar no banco: " + task2.getException().getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(CadastroForm.this, "Erro no cadastro: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    public static class Usuario {
        public String nome;
        public String email;
        public String senha;
        public String plano; // novo campo

        public Usuario() {} // Necessário para Firebase

        public Usuario(String nome, String email, String senha) {
            this.nome = nome;
            this.email = email;
            this.senha = senha;
            this.plano = "free"; // valor padrão
        }
    }
}