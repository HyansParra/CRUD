package com.example.myapplicationsss;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private String tipoUsuario; // Para detectar si es admin

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recibir tipo de usuario desde Inicio.java
        tipoUsuario = getIntent().getStringExtra("tipo_usuario");

        // ================================
        // Barra de navegación inferior
        // ================================
        LinearLayout navCoffee = findViewById(R.id.navCoffee);
        LinearLayout navQR = findViewById(R.id.navQR);
        LinearLayout navUser = findViewById(R.id.navUser);

        // ================================
        // Click en Coffee -> abrir CatalogoActivity
        // ================================
        navCoffee.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CatalogoActivity.class);
            startActivity(intent);
        });

        // ================================
        // Click en QR -> abrir QRActivity
        // ================================
        navQR.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, QRActivity.class);
            startActivity(intent);
        });

        // ================================
        // Click en User -> abrir UsuarioActivity
        // ================================
        navUser.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UsuarioActivity.class);
            intent.putExtra("tipo_usuario", tipoUsuario); // 'administrador' o 'normal'
            startActivity(intent);
        });

        // ================================
        // Agregar botones extras si es admin
        // ================================
        LinearLayout bottomNavBar = findViewById(R.id.bottomNavBar);
        if (tipoUsuario != null && tipoUsuario.equals("administrador")) {
            agregarBotonAdmin(bottomNavBar, "REGLAS");
            agregarBotonAdmin(bottomNavBar, "BENEFICIOS");
        }
    }

    // Función para agregar botones dinámicos de administrador
    private void agregarBotonAdmin(LinearLayout parent, String texto) {
        Button boton = new Button(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 1f);
        boton.setLayoutParams(params);
        boton.setText(texto);
        boton.setTextSize(12f);
        boton.setGravity(Gravity.CENTER);
        boton.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        boton.setBackgroundColor(getResources().getColor(android.R.color.holo_purple)); // color llamativo seguro
        boton.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Botón " + texto + " presionado", Toast.LENGTH_SHORT).show();
            // Aquí puedes poner la acción real de abrir pantalla de Beneficios o Reglas
        });

        parent.addView(boton);
    }
}
