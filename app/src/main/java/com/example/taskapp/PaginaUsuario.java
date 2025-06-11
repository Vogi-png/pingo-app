package com.example.taskapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taskapp.ui.TrocarTema;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PaginaUsuario extends AppCompatActivity {

    Button button7, button9, button10;
    TextView tvNome, tvEmail;

    FirebaseAuth auth;
    DatabaseReference databaseReference;

    FloatingActionButton fab;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pagina_usuario);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // --- CÓDIGO MOVIDO PARA CÁ ---
        // 2. Agora que o layout existe, inicialize suas views usando findViewById.
        fab = findViewById(R.id.botaomais);
        bottomNav = findViewById(R.id.bottomNavigationView);

        // 3. Com as views inicializadas, configure os listeners e helpers.
        fab.setOnClickListener(v -> startActivity(new Intent(this, CriarTarefa.class)));
        BottomNavHelper.setup(this, bottomNav);


        //barra de navegacao
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        BottomNavHelper.setup(this, bottomNavigationView);

        tvNome = findViewById(R.id.tvNome);
        tvEmail = findViewById(R.id.tvEmail);

        button7 = findViewById(R.id.button7);
        button7.setOnClickListener(v -> {
            startActivity(new Intent(PaginaUsuario.this, PaginaPlano.class));
        });

        button10 = findViewById(R.id.button10);
        button10.setOnClickListener(v -> {
            startActivity(new Intent(PaginaUsuario.this, TodosCartoes.class));
        });

        button9 = findViewById(R.id.button9);
        button9.setOnClickListener(v -> {
            startActivity(new Intent(PaginaUsuario.this, TrocarTema.class));
        });

        // Inicializar Firebase
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        // Carregar dados do usuário autenticado
        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        if (userId != null) {
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String nome = snapshot.child("nome").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);

                        tvNome.setText(nome != null ? nome : "Nome não encontrado");
                        tvEmail.setText(email != null ? email : "Email não encontrado");
                    } else {
                        Toast.makeText(PaginaUsuario.this, "Dados do usuário não encontrados", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    Toast.makeText(PaginaUsuario.this, "Erro ao carregar dados: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
        }
    }
}
