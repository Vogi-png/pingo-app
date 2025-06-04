package com.example.taskapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.taskapp.databinding.ActivityBaseBinding;

public class BaseActivity extends AppCompatActivity {

    protected ActivityBaseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                startActivity(new Intent(this, CadastroForm.class)); //mudar para PaginaUsuario
                return true;
            } else if (itemId == R.id.navigation_dashboard) {
                startActivity(new Intent(this, ListaTarefas.class));
                return true;
            } else if (itemId == R.id.navigation_info) {
                startActivity(new Intent(this, PaginaSobre.class));
                return true;
            } else if (itemId == R.id.navigation_pingo) {
                startActivity(new Intent(this, LoginForm.class)); //mudar para PaginaPingoHome
                return true;
            }
            return false;
        });
    }
}
