package com.example.taskapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.taskapp.databinding.ActivityListaTarefasBinding;
import com.example.taskapp.ui.home.HomeFragment;
import com.example.taskapp.R;


public class ListaTarefas extends AppCompatActivity {

    private ActivityListaTarefasBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListaTarefasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicia com o fragmento padrão
        loadFragment(new HomeFragment());

//        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
//            Fragment selectedFragment = null;
//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    selectedFragment = new HomeFragment();
//                    break;
//                case R.id.navigation_dashboard:
//                    selectedFragment = new DashboardFragment();
//                    break;
//                case R.id.navigation_notifications:
//                    selectedFragment = new NotificationsFragment();
//                    break;
//            }
//
//            if (selectedFragment != null) {
//                loadFragment(selectedFragment);
//            }
//
//            return true;
//        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit();
    }
}
