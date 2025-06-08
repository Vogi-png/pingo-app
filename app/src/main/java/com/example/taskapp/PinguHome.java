package com.example.taskapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class PinguHome extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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