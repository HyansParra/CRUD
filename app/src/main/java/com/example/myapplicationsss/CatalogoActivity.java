package com.example.myapplicationsss;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CatalogoActivity extends AppCompatActivity {

    private String tipoUsuario;
    private Button btnCatalogoCRUD, btnVolver;
    private Button btnAddEspresso, btnAddLatte, btnAddCappuccino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalogo); // tu XML adaptado

        // ===============================
        // Recibir tipo de usuario
        // ===============================
        tipoUsuario = getIntent().getStringExtra("tipo_usuario");

        // ===============================
        // Referencias a los botones
        // ===============================
        btnCatalogoCRUD = findViewById(R.id.btnCatalogoCRUD);
        btnVolver = findViewById(R.id.btnVolver);
        btnAddEspresso = findViewById(R.id.btnAddEspresso);
        btnAddLatte = findViewById(R.id.btnAddLatte);
        btnAddCappuccino = findViewById(R.id.btnAddCappuccino);

        // ===============================
        // Mostrar u ocultar botón CRUD solo si es admin
        // ===============================
        if (tipoUsuario != null && tipoUsuario.equalsIgnoreCase("admin")) {
            btnCatalogoCRUD.setVisibility(Button.VISIBLE);
        } else {
            btnCatalogoCRUD.setVisibility(Button.GONE);
        }

        // ===============================
        // Botón volver
        // ===============================
        btnVolver.setOnClickListener(v -> finish());

        // ===============================
        // Botón CRUD
        // ===============================
        btnCatalogoCRUD.setOnClickListener(v -> {
            Intent intent = new Intent(CatalogoActivity.this, CatalogoCrudActivity.class);
            startActivity(intent);
        });

        // ===============================
        // Botones de café
        // ===============================
        btnAddEspresso.setOnClickListener(v ->
                Toast.makeText(this, "Agregaste Café Espresso al carrito", Toast.LENGTH_SHORT).show()
        );

        btnAddLatte.setOnClickListener(v ->
                Toast.makeText(this, "Agregaste Café Latte al carrito", Toast.LENGTH_SHORT).show()
        );

        btnAddCappuccino.setOnClickListener(v ->
                Toast.makeText(this, "Agregaste Cappuccino al carrito", Toast.LENGTH_SHORT).show()
        );

        // ===============================
        // Barra de navegación inferior
        // ===============================
        LinearLayout navCoffee = findViewById(R.id.navCoffee);
        LinearLayout navQR = findViewById(R.id.navQR);
        LinearLayout navUser = findViewById(R.id.navUser);

        navCoffee.setOnClickListener(v ->
                Toast.makeText(this, "Ya estás en Catálogo", Toast.LENGTH_SHORT).show()
        );

        navQR.setOnClickListener(v -> {
            Intent intent = new Intent(CatalogoActivity.this, QRActivity.class);
            intent.putExtra("tipo_usuario", tipoUsuario);
            startActivity(intent);
        });

        navUser.setOnClickListener(v -> {
            Intent intent = new Intent(CatalogoActivity.this, UsuarioActivity.class);
            intent.putExtra("tipo_usuario", tipoUsuario);
            startActivity(intent);
        });
    }
}
