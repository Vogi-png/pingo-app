package com.example.taskapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NomearPingo extends AppCompatActivity {

    private EditText editTextNomePingo;
    private ImageView nomear_seguirBtn;
    private DatabaseReference databaseReference;
    private String userId; // ID do usuário recebido da Activity anterior

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nomear_pingo);

        editTextNomePingo = findViewById(R.id.id_editPingoNome);
        nomear_seguirBtn = findViewById(R.id.nomear_seguirBtn);

        //barra de navegacao
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        BottomNavHelper.setup(this, bottomNavigationView);

        // Receber o ID do usuário passado na Intent
        userId = getIntent().getStringExtra("userId");
        if (userId == null) {
            Toast.makeText(this, "Erro: usuário não identificado!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("usuarios");

        nomear_seguirBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomePingo = editTextNomePingo.getText().toString().trim();
                if (nomePingo.isEmpty()) {
                    Toast.makeText(NomearPingo.this, "Por favor, digite um nome para o pingo.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Atualizar o nome do pingo no usuário
                databaseReference.child(userId).child("nomePingo").setValue(nomePingo)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // Ir para próxima tela
                                Intent intent = new Intent(NomearPingo.this, PaginaUsuario.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(NomearPingo.this, "Erro ao salvar nome do pingo: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }
}