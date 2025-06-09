package com.example.taskapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public abstract class BaseActivity extends AppCompatActivity {

    protected BottomNavigationView bottomNavigationView;
    protected FloatingActionButton fabMais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Use este método para inflar o layout principal da Activity,
     * ele automaticamente injeta o layout da barra de navegação no final.
     *
     * @param layoutResID layout principal da Activity (sem a barra de navegação)
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);

        // Inicializa componentes da barra de navegação presentes no layout da Activity
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fabMais = findViewById(R.id.botaomais);

        if (bottomNavigationView != null) {
            setupBottomNavigation();
        }

        if (fabMais != null) {
            setupFabMais();
        }
    }

    private void setupBottomNavigation() {
        // Aqui você pode setar o item selecionado, ex:
        // bottomNavigationView.setSelectedItemId(getSelectedMenuItemId());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                startActivity(new Intent(this, PaginaUsuario.class));
                finish();
                return true;
            } else if (id == R.id.navigation_dashboard) {
                // Activity atual
                return true;
            } else if (id == R.id.navigation_info) {
                startActivity(new Intent(this, PaginaSobre.class));
                finish();
                return true;
            } else if (id == R.id.navigation_pingo) {
                startActivity(new Intent(this, PinguHome.class));
                finish();
                return true;
            }
            return false;
        });
    }

    private void setupFabMais() {
        fabMais.setOnClickListener(v -> {
            // ação do botão +, pode ser sobrescrita
            onFabMaisClicked();
        });
    }

    /**
     * Retorna o ID do item do menu que deve estar selecionado na BottomNavigationView.
     * Cada Activity deve sobrescrever para indicar o item selecionado correto.
     */
    protected int getSelectedMenuItemId() {
        return 0; // padrão: nenhum selecionado, sobrescreva nas subclasses
    }

    /**
     * Ação padrão ao clicar no FAB "+", pode ser sobrescrita pelas Activities.
     */
    protected void onFabMaisClicked() {
        // pode ser vazio ou abrir uma Activity padrão
    }
}
