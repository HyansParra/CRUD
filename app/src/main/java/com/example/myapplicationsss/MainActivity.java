package com.example.myapplicationsss;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Tu XML actual

        // Botón Catálogo
        Button btnCatalogo = findViewById(R.id.btnCatalogo);
        btnCatalogo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CatalogoActivity.class);
            startActivity(intent);
        });

        // Botón Escanear QR
        Button btnEscanearQR = findViewById(R.id.btnEscanearQR);
        btnEscanearQR.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainActivity.class); // O tu actividad QR
            startActivity(intent);
        });

        // Botón Usuario
        Button btnUsuario = findViewById(R.id.btnUsuario);
        btnUsuario.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UsuarioActivity.class);
            startActivity(intent);
        });
    }
}
