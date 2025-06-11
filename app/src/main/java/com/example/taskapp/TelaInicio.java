package com.example.taskapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TelaInicio extends AppCompatActivity {

    private static final String TAG = "DEBUG_LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tela_inicio); // Certifique-se que o nome do layout está correto
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Usamos o seu Handler existente com o seu delay de 3000ms
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // INÍCIO DA LÓGICA DE VERIFICAÇÃO
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                SharedPreferences sharedPreferences = getSharedPreferences("com.example.taskapp.PREFS", MODE_PRIVATE);

                FirebaseUser currentUser = mAuth.getCurrentUser();
                boolean lembrarUsuario = sharedPreferences.getBoolean("KEY_REMEMBER_ME", false);

                Log.d(TAG, "TelaInicio: Verificando login...");
                Log.d(TAG, "TelaInicio: currentUser é nulo? " + (currentUser == null));
                Log.d(TAG, "TelaInicio: 'lembrarUsuario' é: " + lembrarUsuario);

                // Se o usuário já está logado no Firebase E ele marcou "Manter conectado"...
                if (currentUser != null && lembrarUsuario) {
                    // ...ele vai direto para a tela principal.
                    Log.d(TAG, "TelaInicio: Decisão -> Indo para ListaTarefas.");
                    startActivity(new Intent(TelaInicio.this, ListaTarefas.class));
                } else {
                    // ...caso contrário, ele precisa fazer login.
                    Log.d(TAG, "TelaInicio: Decisão -> Indo para LoginForm.");
                    startActivity(new Intent(TelaInicio.this, LoginForm.class));
                }
                // FIM DA LÓGICA DE VERIFICAÇÃO

                finish();
            }
        }, 3000); // Mantivemos o seu delay de 3 segundos
    }
}