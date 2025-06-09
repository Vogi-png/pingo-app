package com.example.taskapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.taskapp.databinding.ActivityPinguHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PinguHome extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private ActivityPinguHomeBinding binding;
    private FirebaseUser usuarioLogado;
    private DatabaseReference database;
    private String nomePingo;
    private TextView txtNomePingo;

    private int[] id_Skins = {
            R.drawable.pingo_default,
            R.drawable.pingo_jabba,
            R.drawable.pingo_carioca,
            R.drawable.pingo_ciborgue,
            R.drawable.pingo_emo,
            R.drawable.pingo_namorados,
            R.drawable.pingo_kovalski,
            R.drawable.pingo_clube_penguin,
            R.drawable.pingo_pablo,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityPinguHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // --- Início da Lógica da Barra de Navegação ---
        binding.bottomNavigationView.setSelectedItemId(R.id.navigation_pingo); // Marca o ícone correto

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, PaginaUsuario.class));
                return true;
            } else if (itemId == R.id.navigation_dashboard) {
                startActivity(new Intent(this, ListaTarefas.class));
                return true;
            } else if (itemId == R.id.navigation_info) {
                startActivity(new Intent(this, PaginaSobre.class));
                return true;
            } else if (itemId == R.id.navigation_pingo) {

                return true;
            }
            return false;
        });

        binding.botaomais.setOnClickListener(v -> {
            startActivity(new Intent(this, CriarTarefa.class));
        });

        mediaPlayer = MediaPlayer.create(this, R.raw.sompingu);

        binding.pinguImageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    mediaPlayer.start(); // Toca o som
                }
            }
        });

        usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        txtNomePingo = findViewById(R.id.id_pinguNameHome);

        String uid = usuarioLogado.getUid();
        Log.d("myTag", "Current User ID: " + uid);
        DatabaseReference userRef = database.child("usuarios").child(uid).child("nomePingo");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nomePingo = snapshot.getValue(String.class);
                Log.d("myTag", "fk_nome_pingo = " + nomePingo);
                txtNomePingo.setText(nomePingo);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("myTag", "Failed to read fk_nome_pingo", error.toException());
            }
        });

        binding.btnPersonalizar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PinguHome.this, PinguSkin.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release(); // Libera o recurso
            mediaPlayer = null;
        }
    }
}