package com.example.taskapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.taskapp.databinding.ActivityListaTarefasBinding;

public class ListaTarefas extends AppCompatActivity {

    private ActivityListaTarefasBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaTarefasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setSelectedItemId(R.id.navigation_dashboard);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, CadastroForm.class)); //mudar para PaginaUsuario
                return true;
            } else if (itemId == R.id.navigation_dashboard) {
                // Já está na tela ListaTarefas, não faz nada
                return true;
            } else if (itemId == R.id.navigation_info) {
                startActivity(new Intent(this, PaginaSobre.class));
                return true;
            } else if (itemId == R.id.navigation_pingo) {
                startActivity(new Intent(this, LoginForm.class)); //mudar para PaginaPingoHome
                return true;
            } else if (itemId == R.id.navigation_centro) {
                // logica de criar tarefa
                return false;
            }

            return false;
        });
    }
}
