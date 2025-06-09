package com.example.taskapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;

public class PinguSkin extends AppCompatActivity {

    private FirebaseUser usuarioLogado;
    private DatabaseReference database;
    private String nomePingo;
    private EditText txtPingo;
    private Button btnSalvarPingo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pingu_skin);

        //barra de navegacao
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        BottomNavHelper.setup(this, bottomNavigationView);

        usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        txtPingo = findViewById(R.id.id_editPingoNome);
        btnSalvarPingo = findViewById(R.id.id_btnSalvarPingo);

        if(usuarioLogado != null){
            String uid = usuarioLogado.getUid();
            Log.d("myTag", "Current User ID: " + uid);
            DatabaseReference userRef = database.child("usuarios").child(uid).child("nomePingo");

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    nomePingo = snapshot.getValue(String.class);
                    Log.d("myTag", "fk_nome_pingo = " + nomePingo);
                    txtPingo.setText(nomePingo);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.e("myTag", "Failed to read fk_nome_pingo", error.toException());
                }
            });
        }

        btnSalvarPingo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String novoNome = txtPingo.getText().toString();
                String uid = usuarioLogado.getUid();

                if(!novoNome.isEmpty()){
                    btnSalvarPingo.setEnabled(false);
                    database.child("usuarios").child(uid).child("nomePingo").setValue(novoNome)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getApplicationContext(), "Nome salvo com sucesso!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getApplicationContext(), "Erro ao salvar nome", Toast.LENGTH_SHORT).show();
                                Log.e("Firebase", "Erro ao salvar nome", e);
                            });
                    btnSalvarPingo.setEnabled(true);
                    startActivity(new Intent(PinguSkin.this, PinguHome.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Digite um nome válido", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}