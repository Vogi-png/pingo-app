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
        setContentView(R.layout.activity_login_form);

        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("usuarios");

        inputEmail = findViewById(R.id.id_login_insertEmail);
        inputSenha = findViewById(R.id.id_login_insertSenha);
        btnLogin = findViewById(R.id.id_confirmLogin);
        progressDialog = new ProgressDialog(this);
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