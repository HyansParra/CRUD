package com.example.myapplicationsss;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class CatalogoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalogo); // Asegúrate de que tu XML se llame catalogo.xml

        // Botón volver
        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());

        // Botón CRUD
        Button btnCatalogoCRUD = findViewById(R.id.btnCatalogoCRUD);
        btnCatalogoCRUD.setOnClickListener(v -> {
            Intent intent = new Intent(CatalogoActivity.this, CatalogoCrudActivity.class);
            startActivity(intent);
        });

        // Botones de café (evita NullPointerException si los agregas luego)
        Button btnAddEspresso = findViewById(R.id.btnAddEspresso);
        if(btnAddEspresso != null) {
            btnAddEspresso.setOnClickListener(v -> {
                // Aquí la lógica de agregar al carrito
            });
        }

        Button btnAddLatte = findViewById(R.id.btnAddLatte);
        if(btnAddLatte != null) {
            btnAddLatte.setOnClickListener(v -> {
                // Aquí la lógica de agregar al carrito
            });
        }

        Button btnAddCappuccino = findViewById(R.id.btnAddCappuccino);
        if(btnAddCappuccino != null) {
            btnAddCappuccino.setOnClickListener(v -> {
                // Aquí la lógica de agregar al carrito
            });
        }
    }
}
