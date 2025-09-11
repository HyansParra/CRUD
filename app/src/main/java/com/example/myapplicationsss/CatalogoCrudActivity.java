package com.example.myapplicationsss;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CatalogoCrudActivity extends AppCompatActivity {

    private EditText etNombre, etDescripcion, etPrecio, etImagenUrl;
    private Button btnAgregar, btnEditar, btnEliminar, btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.catalogo_crud);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        etPrecio = findViewById(R.id.etPrecio);
        etImagenUrl = findViewById(R.id.etImagenUrl);

        btnAgregar = findViewById(R.id.btnAgregar);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnVolver = findViewById(R.id.btnVolver);

        // Botón volver
        btnVolver.setOnClickListener(v -> finish());

        // Botón agregar
        btnAgregar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString();
            String desc = etDescripcion.getText().toString();
            String precio = etPrecio.getText().toString();
            String img = etImagenUrl.getText().toString();

            if (nombre.isEmpty() || desc.isEmpty() || precio.isEmpty() || img.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Café agregado: " + nombre, Toast.LENGTH_SHORT).show();
            }
        });

        btnEditar.setOnClickListener(v -> Toast.makeText(this, "Función editar aún no implementada", Toast.LENGTH_SHORT).show());
        btnEliminar.setOnClickListener(v -> Toast.makeText(this, "Función eliminar aún no implementada", Toast.LENGTH_SHORT).show());
    }
}
