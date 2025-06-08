package com.example.taskapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.taskapp.databinding.ActivityPinguHomeBinding;

public class PinguHome extends AppCompatActivity {

    private MediaPlayer mediaPlayer;


    private ActivityPinguHomeBinding binding;

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