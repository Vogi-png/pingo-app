package com.example.taskapp;

import android.app.Activity;
import android.content.Intent;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomNavHelper {

    public static void setup(Activity activity, BottomNavigationView bottomNavigationView) {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            Class<?> currentActivity = activity.getClass();

            if (id == R.id.navigation_home && currentActivity != PaginaUsuario.class) {
                activity.startActivity(new Intent(activity, PaginaUsuario.class));
                activity.finish();
                return true;
            } else if (id == R.id.navigation_dashboard && currentActivity != ListaTarefas.class) {
                activity.startActivity(new Intent(activity, ListaTarefas.class));
                activity.finish();
                return true;
            } else if (id == R.id.navigation_info && currentActivity != PaginaSobre.class) {
                activity.startActivity(new Intent(activity, PaginaSobre.class));
                activity.finish();
                return true;
            } else if (id == R.id.navigation_pingo && currentActivity != PinguHome.class) {
                activity.startActivity(new Intent(activity, PinguHome.class));
                activity.finish();
                return true;
            }

            return true;
        });
    }
}
