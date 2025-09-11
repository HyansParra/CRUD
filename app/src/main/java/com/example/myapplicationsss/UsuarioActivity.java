package com.example.myapplicationsss;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class UsuarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario);

        // Botón volver → ahora solo cierra la actividad actual
        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());

        // Botón logout → vuelve al MainActivity
        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(UsuarioActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Navegación inferior
        LinearLayout navCoffee = findViewById(R.id.navCoffee);
        LinearLayout navQR = findViewById(R.id.navQR);
        LinearLayout navUser = findViewById(R.id.navUser);

        navCoffee.setOnClickListener(v -> {
            Intent intent = new Intent(UsuarioActivity.this, CatalogoActivity.class);
            startActivity(intent);
            finish();
        });

        navQR.setOnClickListener(v -> {
            // Aquí puedes implementar la lógica de escanear QR si deseas
        });

        navUser.setOnClickListener(v -> {
            // Ya estamos en UsuarioActivity → no hacemos nada
        });
    }
}
