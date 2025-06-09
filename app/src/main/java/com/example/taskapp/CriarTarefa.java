package com.example.taskapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.taskapp.databinding.ActivityCriarTarefaBinding;
import com.example.taskapp.databinding.ActivityListaTarefasBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;
import java.util.Map;

public class CriarTarefa extends AppCompatActivity {

    private EditText editTitulo, editDescricao;
    private Button btnSalvar;
    private ActivityCriarTarefaBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityCriarTarefaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTitulo = findViewById(R.id.editTitulo);
        editDescricao = findViewById(R.id.editDescricao);
        btnSalvar = findViewById(R.id.btnSalvar);

        btnSalvar.setOnClickListener(v -> salvarTarefaNoFirebase());

        //barra de navegacao
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        BottomNavHelper.setup(this, bottomNavigationView);
    }


    private void salvarTarefaNoFirebase() {
        String titulo = editTitulo.getText().toString().trim();
        String descricao = editDescricao.getText().toString().trim();

        if (titulo.isEmpty()) {
            editTitulo.setError("Digite o título");
            editTitulo.requestFocus();
            return;
        }

        // ID de usuário - autenticacao com login
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        //String userId = "123";

        DatabaseReference database = FirebaseDatabase.getInstance().getReference("tarefas");
        String tarefaId = database.push().getKey();

        if (tarefaId == null) {
            Toast.makeText(this, "Erro ao gerar ID da tarefa", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> tarefa = new HashMap<>();
        tarefa.put("id", tarefaId);
        tarefa.put("fk_id_usuarios", userId);
        tarefa.put("titulo", titulo);
        tarefa.put("descricao", descricao);
        tarefa.put("status", "pendente");
        tarefa.put("dtcriacao", ServerValue.TIMESTAMP);

        database.child(tarefaId).setValue(tarefa).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Tarefa salva com sucesso!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, ListaTarefas.class));
                finish(); // volta pra ListaTarefas
            } else {
                Toast.makeText(this, "Erro ao salvar tarefa", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
