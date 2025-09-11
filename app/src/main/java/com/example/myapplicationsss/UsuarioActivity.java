package com.example.myapplicationsss;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class UsuarioActivity extends AppCompatActivity {

    private Button btnAbrirCrud;
    private MaterialButton btnLogout;
    private Button btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario);

        // Referencias
        btnAbrirCrud = findViewById(R.id.btnAbrirCrud);
        btnLogout = findViewById(R.id.btnLogout);
        btnVolver = findViewById(R.id.btnVolver);

        // Botón para abrir UsuarioCrudActivity
        btnAbrirCrud.setOnClickListener(v -> {
            Intent intent = new Intent(UsuarioActivity.this, UsuarioCrudActivity.class);
            startActivity(intent);
        });

        // Botón Cerrar sesión (solo ejemplo)
        btnLogout.setOnClickListener(v -> {
            finish(); // cierra la actividad actual
        });

        // Botón Volver (solo ejemplo)
        btnVolver.setOnClickListener(v -> {
            finish(); // cierra la actividad actual
        });
    }
}
