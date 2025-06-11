package com.example.taskapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.taskapp.databinding.ActivityPinguHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
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

    private int currentSkinIndex = -1;

    private int[] id_Skins = {
            R.drawable.pingo_default,
            R.drawable.pingo_jabba,
            R.drawable.pingo_carioca,
            R.drawable.pingo_ciborgue,
            R.drawable.pingo_emo,
            R.drawable.pingo_namorados,
            R.drawable.pingo_mother_monster,
            R.drawable.pingo_kovalski,
            R.drawable.pingo_clube_penguin,
            R.drawable.pingo_pablo,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPinguHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        BottomNavHelper.setup(this, bottomNavigationView);

        binding.pinguImageHome.setOnClickListener(v -> {
            startActivity(new Intent(this, CriarTarefa.class));
        });

        binding.pinguImageHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }

                if (currentSkinIndex != -1) {
                    int currentSkinResourceId = id_Skins[currentSkinIndex];

                    if (currentSkinResourceId == R.drawable.pingo_kovalski) {
                        // Som especial para a skin Kovalski
                        mediaPlayer = MediaPlayer.create(PinguHome.this, R.raw.somkovalski);
                    } else if (currentSkinResourceId == R.drawable.pingo_mother_monster) {
                        // Som especial para a skin gaga
                        mediaPlayer = MediaPlayer.create(PinguHome.this, R.raw.somgaga);
                    } else if (currentSkinResourceId == R.drawable.pingo_pablo) {
                        // Som especial para a skin gaga
                        mediaPlayer = MediaPlayer.create(PinguHome.this, R.raw.sompablo);
                    } else {
                        // Som padrão para TODAS as outras skins
                        mediaPlayer = MediaPlayer.create(PinguHome.this, R.raw.sompingu);
                    }
                }

                if (mediaPlayer != null) {
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mp -> {
                        mp.release();
                        mediaPlayer = null;
                    });
                }
            }
        });

        usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        txtNomePingo = findViewById(R.id.id_pinguNameHome);

        String uid = usuarioLogado.getUid();
        DatabaseReference userRef = database.child("usuarios").child(uid).child("nomePingo");
        DatabaseReference userRefSkin = database.child("usuarios").child(uid).child("skin");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nomePingo = snapshot.getValue(String.class);
                txtNomePingo.setText(nomePingo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("myTag", "Failed to read fk_nome_pingo", error.toException());
            }
        });

        userRefSkin.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getValue() != null) {
                    int index = snapshot.getValue(int.class);
                    if (index >= 0 && index < id_Skins.length) {
                        currentSkinIndex = index;
                        binding.pinguImageHome.setImageResource(id_Skins[index]);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("myTag", "Failed to read skin index", error.toException());
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
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}