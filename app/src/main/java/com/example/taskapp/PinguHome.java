package com.example.taskapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.taskapp.databinding.ActivityListaTarefasBinding;

public class PinguHome extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    private ActivityListaTarefasBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Barra de navegação
        binding.bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);
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
                startActivity(new Intent(this, PinguHome.class));
                return true;
            } else if (itemId == R.id.navigation_centro) {
                return false;
            }
            return false;
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pingu_home);

        ImageView pinguImage = findViewById(R.id.pinguImageHome);

        // Cria o MediaPlayer com o som do raw
        mediaPlayer = MediaPlayer.create(this, R.raw.sompingu);

        // Define o clique na imagem
        pinguImage.setOnClickListener(new View.OnClickListener() {
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