package com.example.taskapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import androidx.annotation.NonNull;


public class PinguSkin extends AppCompatActivity {

    private FirebaseUser usuarioLogado;
    private DatabaseReference database;
    private String nomePingo;
    private boolean plano;
    private EditText txtPingo;
    private Button btnSalvarPingo;
    private ImageView skinAtual;
    private Integer skinAplicada;

    private int[] id_Skins = {
            R.drawable.pingo_default,
            R.drawable.pingo_jabba,
            R.drawable.pingo_carioca,
            R.drawable.pingo_ciborgue,
            R.drawable.pingo_emo,
            R.drawable.pingo_namorados,
            R.drawable.pingo_kovalski,
            R.drawable.pingo_clube_penguin,
            R.drawable.pingo_pablo,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pingu_skin);

        usuarioLogado = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        txtPingo = findViewById(R.id.id_editPingoNome);
        btnSalvarPingo = findViewById(R.id.id_btnSalvarPingo);
        skinAtual = findViewById(R.id.pinguPreview);

        ImageView[] imageViews = {
                findViewById(R.id.skinSlot_1),
                findViewById(R.id.skinSlot_2),
                findViewById(R.id.skinSlot_3),
                findViewById(R.id.skinSlot_4),
                findViewById(R.id.skinSlot_5),
                findViewById(R.id.skinSlot_6),
                findViewById(R.id.skinSlot_7),
                findViewById(R.id.skinSlot_8),
                findViewById(R.id.skinSlot_9),
        };

        String uid = usuarioLogado.getUid();
        Log.d("myTag", "Current User ID: " + uid);
        DatabaseReference userRefPingo = database.child("usuarios").child(uid).child("nomePingo");
        DatabaseReference userRefPlano = database.child("usuarios").child(uid).child("plano");
        DatabaseReference userRefSkin = database.child("usuarios").child(uid).child("skin");

        userRefPingo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                nomePingo = snapshot.getValue(String.class);
                Log.d("myTag", "fk_nome_pingo = " + nomePingo);
                txtPingo.setText(nomePingo);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Erro ao ler o nome do Pingo", Toast.LENGTH_SHORT).show();
                Log.e("myTag", "Failed to read fk_nome_pingo", error.toException());
            }
        });

        userRefPlano.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue(String.class).equals("free")){
                    plano = false;
                }
                else{
                    plano = true;
                }
                Log.d("myTag", "User has " + plano + " plan.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Erro ao ler Plano", Toast.LENGTH_SHORT).show();
                Log.e("myTag", "Failed to read plano", error.toException());
            }
        });

        userRefSkin.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int index = snapshot.getValue(int.class);
                skinAtual.setImageResource(id_Skins[index]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("myTag", "Failed to read fk_nome_pingo", error.toException());
            }
        });

        btnSalvarPingo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String novoNome = txtPingo.getText().toString();
                String uid = usuarioLogado.getUid();

                if(!novoNome.isEmpty()){
                    btnSalvarPingo.setEnabled(false);
                    database.child("usuarios").child(uid).child("nomePingo").setValue(novoNome)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getApplicationContext(), "Nome salvo com sucesso!", Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getApplicationContext(), "Erro ao salvar nome", Toast.LENGTH_SHORT).show();
                                Log.e("Firebase", "Erro ao salvar nome", e);
                            });

                    if(skinAplicada != null){
                        database.child("usuarios").child(uid).child("skin").setValue(skinAplicada);
                    }

                    btnSalvarPingo.setEnabled(true);
                    startActivity(new Intent(PinguSkin.this, PinguHome.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Digite um nome válido", Toast.LENGTH_SHORT).show();
                }
            }
        });

        final int[] selectedIndex = { -1 };

        for (int i = 0; i < imageViews.length; i++) {
            ImageView currentImage = imageViews[i];
            currentImage.setImageResource(id_Skins[i]);
            final int index = i;

            currentImage.setOnClickListener(view -> {
                int selectedImage_id = id_Skins[index];
                Log.d("MyTag", "Index da skin: " + selectedImage_id);

                if(index <= 1 || plano) {
                    for (ImageView image : imageViews) {
                        image.setBackgroundResource(R.drawable.scroll_background_unselected);
                    }

                    currentImage.setBackgroundResource(R.drawable.scroll_background_selected);

                    selectedIndex[0] = index;

                    aplicarSkin(selectedImage_id, index);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Faça upgrade para o plano Premium para desbloquear esta skin.", Toast.LENGTH_LONG).show();
                }
            });
        }

    }

    private void aplicarSkin(int skinId, int index){
        skinAplicada = index;
        skinAtual.setImageResource(skinId);
    }
}