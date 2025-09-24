package com.example.myapplicationsss;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class UsuarioActivity extends AppCompatActivity {

    private Button btnAbrirCrud, btnVolver;
    private MaterialButton btnLogout;
    private String tipoUsuario;
    private LinearLayout layoutExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario);

        tipoUsuario = getIntent().getStringExtra("tipo_usuario");

        btnAbrirCrud = findViewById(R.id.btnAbrirCrud);
        btnLogout = findViewById(R.id.btnLogout);
        btnVolver = findViewById(R.id.btnVolver);
        layoutExtras = findViewById(R.id.layoutExtras); // Layout donde van botones admin

        // Visibilidad de CRUD
        if (tipoUsuario == null || !tipoUsuario.equals("administrador")) {
            btnAbrirCrud.setVisibility(Button.GONE);
        } else {
            btnAbrirCrud.setVisibility(Button.VISIBLE);
            agregarBotonAdmin(layoutExtras, "REGLAS");
            agregarBotonAdmin(layoutExtras, "BENEFICIOS");
        }

        btnAbrirCrud.setOnClickListener(v -> {
            Intent intent = new Intent(UsuarioActivity.this, UsuarioCrudActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> finish());
        btnVolver.setOnClickListener(v -> finish());

        // Barra inferior
        LinearLayout navCoffee = findViewById(R.id.navCoffee);
        LinearLayout navQR = findViewById(R.id.navQR);
        LinearLayout navUser = findViewById(R.id.navUser);

        navCoffee.setOnClickListener(v -> Toast.makeText(this, "Coffee seleccionado", Toast.LENGTH_SHORT).show());

        navQR.setOnClickListener(v -> {
            Intent intent = new Intent(UsuarioActivity.this, QRActivity.class);
            intent.putExtra("tipo_usuario", tipoUsuario);
            startActivity(intent);
        });

        navUser.setOnClickListener(v -> {
            // Ya estamos aquí
        });
    }

    private void agregarBotonAdmin(LinearLayout parent, String texto) {
        MaterialButton boton = new MaterialButton(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 16, 0, 0);
        boton.setLayoutParams(params);
        boton.setText(texto);
        boton.setAllCaps(false);
        boton.setTextColor(Color.WHITE);

        // Estilo similar a btnLogout
        boton.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#6F4E37"))); // Tono café
        boton.setCornerRadius(12);
        boton.setElevation(4f);

        // Acción según el texto
        boton.setOnClickListener(v -> {
            if (texto.equals("REGLAS")) {
                Intent intent = new Intent(UsuarioActivity.this, ReglasActivity.class);
                startActivity(intent);
            } else if (texto.equals("BENEFICIOS")) {
                Intent intent = new Intent(UsuarioActivity.this, BeneficiosActivity.class);
                startActivity(intent);
            }
        });

        parent.addView(boton);
    }
}
