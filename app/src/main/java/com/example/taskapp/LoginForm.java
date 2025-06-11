package com.example.taskapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginForm extends AppCompatActivity {

    private static final String TAG = "DEBUG_LOGIN";
    private EditText inputEmail, inputSenha;
    private Button btnLogin;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    private DatabaseReference usersRef;
    private CheckBox checkboxLembrarMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("usuarios");

        SharedPreferences sharedPreferences = getSharedPreferences("com.example.taskapp.PREFS", MODE_PRIVATE);
        boolean lembrarMe = sharedPreferences.getBoolean("KEY_REMEMBER_ME", false);

        // Verifica se há usuário logado
        if (auth.getCurrentUser() != null && lembrarMe) {
            FirebaseUser user = auth.getCurrentUser();
            String uid = user.getUid();

            usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String nome = snapshot.child("nome").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);

                        Intent intent = new Intent(LoginForm.this, ListaTarefas.class);
                        intent.putExtra("nomeUsuario", nome);
                        intent.putExtra("emailUsuario", email);
                        startActivity(intent);
                        finish();
                    } else {
                        // Se não encontrar o usuário no banco, continua para tela de login
                        setContentView(R.layout.activity_login_form);
                        inicializarComponentes();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(LoginForm.this, "Erro ao acessar banco: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    setContentView(R.layout.activity_login_form);
                    inicializarComponentes();
                }
            });
        } else {
            // Não está logado OU não quer continuar logado
            setContentView(R.layout.activity_login_form);
            inicializarComponentes();
        }
    }

    private void inicializarComponentes() {
        inputEmail = findViewById(R.id.id_login_insertEmail);
        inputSenha = findViewById(R.id.id_login_insertSenha);
        btnLogin = findViewById(R.id.id_confirmLogin);
        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("usuarios");
        checkboxLembrarMe = findViewById(R.id.checkbox_lembrar_me);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ... (código do listener do botão de login)
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
                                    FirebaseUser user = auth.getCurrentUser();
                                    if (user != null) {
                                        usersRef.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    boolean isChecked = checkboxLembrarMe.isChecked();
                                                    Log.d(TAG, "Login bem-sucedido. Checkbox está marcado? " + isChecked);
                                                    salvarPreferenciaDeLogin(isChecked);

                                                    Intent intent = new Intent(LoginForm.this, ListaTarefas.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else { // ...
                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) { // ...
                                            }
                                        });
                                    }
                                } else { // ...
                                }
                            }
                        });
            }
        });
        TextView tvNaoPossuiCadastro = findViewById(R.id.id_redirectCadastro);
        tvNaoPossuiCadastro.setOnClickListener(v -> startActivity(new Intent(LoginForm.this, CadastroForm.class)));
    }

    private void salvarPreferenciaDeLogin(boolean isChecked) {
        Log.d(TAG, "Executando salvarPreferenciaDeLogin. Salvando valor: " + isChecked);
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.taskapp.PREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("KEY_REMEMBER_ME", isChecked);
        editor.apply();
    }
}