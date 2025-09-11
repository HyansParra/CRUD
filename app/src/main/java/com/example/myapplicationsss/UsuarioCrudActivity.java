package com.example.myapplicationsss;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class UsuarioCrudActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario_crud); // Layout asociado

        // Botón volver → cierra esta actividad
        Button btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());

        // Botón Agregar
        Button btnAgregar = findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(v -> {
            Toast.makeText(this, "Agregar Usuario", Toast.LENGTH_SHORT).show();
            // Aquí iría la lógica para agregar un usuario
        });

        // Botón Editar
        Button btnEditar = findViewById(R.id.btnEditar);
        btnEditar.setOnClickListener(v -> {
            Toast.makeText(this, "Editar Usuario", Toast.LENGTH_SHORT).show();
            // Aquí iría la lógica para editar un usuario
        });

        // Botón Eliminar
        Button btnEliminar = findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(v -> {
            Toast.makeText(this, "Eliminar Usuario", Toast.LENGTH_SHORT).show();
            // Aquí iría la lógica para eliminar un usuario
        });

        // Lista de usuarios
        ListView listUsuarios = findViewById(R.id.listUsuarios);
        // Aquí puedes asociar un Adapter para mostrar la lista de usuarios
    }
}
