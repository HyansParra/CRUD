package com.example.myapplicationsss;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Ajustar bordes con EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Botones inferiores
        Button btnCatalogo = findViewById(R.id.btnCatalogo);
        Button btnEscanearQR = findViewById(R.id.btnEscanearQR);
        Button btnUsuario = findViewById(R.id.btnUsuario);

        // Ir a Catálogo
        btnCatalogo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CatalogoActivity.class);
            startActivity(intent);
        });

        // Escanear QR (por ahora no implementado)
        btnEscanearQR.setOnClickListener(v -> {
            // Aquí puedes implementar la lógica para escanear QR más adelante
        });

        // Ir a Usuario
        btnUsuario.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UsuarioActivity.class);
            startActivity(intent);
        });
    }
}
