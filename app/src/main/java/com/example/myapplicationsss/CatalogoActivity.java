package com.example.myapplicationsss;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CatalogoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.catalogo);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Botón volver
        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());

        // Navegación inferior
        LinearLayout navCoffee = findViewById(R.id.navCoffee);
        LinearLayout navQR = findViewById(R.id.navQR);
        LinearLayout navUser = findViewById(R.id.navUser);

        // Ya estamos en catálogo
        navCoffee.setOnClickListener(v -> {});

        navQR.setOnClickListener(v -> {
            Intent intent = new Intent(CatalogoActivity.this, MainActivity.class);
            startActivity(intent);
        });

        navUser.setOnClickListener(v -> {
            Intent intent = new Intent(CatalogoActivity.this, UsuarioActivity.class);
            startActivity(intent);
        });
    }
}
