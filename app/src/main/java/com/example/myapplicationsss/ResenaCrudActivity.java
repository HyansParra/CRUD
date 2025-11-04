package com.example.myapplicationsss;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar; // <-- IMPORTANTE
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class ResenaCrudActivity extends AppCompatActivity {

    private SqlBasedeDatos dbHelper;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listaNombresReseñas; // Lista para mostrar en el ListView
    private ArrayList<SqlBasedeDatos.Reseña> listaReseñas; // Lista de objetos Reseña
    private ListView listReseñas;
    private int idUsuarioLogueado; // ID del usuario actual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Usar el layout NUEVO que creamos
        setContentView(R.layout.activity_resena_crud);

        dbHelper = new SqlBasedeDatos(this);

        // Obtener el ID del usuario que pasaremos desde UsuarioActivity
        idUsuarioLogueado = getIntent().getIntExtra("id_usuario", 0);
        if (idUsuarioLogueado == 0) {
            Toast.makeText(this, "Error: Usuario no identificado", Toast.LENGTH_SHORT).show();
            finish(); // Salir si no hay ID
            return;
        }

        Button btnVolver = findViewById(R.id.btnVolver);
        Button btnAgregar = findViewById(R.id.btnAgregar);
        Button btnEditar = findViewById(R.id.btnEditar);
        Button btnEliminar = findViewById(R.id.btnEliminar);
        listReseñas = findViewById(R.id.listReseñas);

        cargarReseñasDelUsuario();

        btnVolver.setOnClickListener(v -> finish());
        btnAgregar.setOnClickListener(v -> mostrarDialogoAgregar());
        btnEditar.setOnClickListener(v -> mostrarDialogoEditar());
        btnEliminar.setOnClickListener(v -> mostrarDialogoEliminar());
    }

    private void cargarReseñasDelUsuario() {
        // Obtenemos las reseñas solo de este usuario
        listaReseñas = dbHelper.obtenerReseñasPorUsuario(idUsuarioLogueado);
        listaNombresReseñas = new ArrayList<>();

        for (SqlBasedeDatos.Reseña r : listaReseñas) {
            // Formateamos el texto para el ListView
            String texto = r.calificacion + "★ - " + r.comentario;
            // Acortamos el texto si es muy largo
            listaNombresReseñas.add(texto.substring(0, Math.min(texto.length(), 40)) + "...");
        }

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice,
                listaNombresReseñas);
        listReseñas.setAdapter(adapter);
        listReseñas.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    private void mostrarDialogoAgregar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar Reseña");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        // TODO: Idealmente, aquí pones un Spinner para que elijan un producto
        // Por simplicidad, usaremos un ID de producto fijo (ej. 1)
        final int idProductoPrueba = 1;

        final RatingBar inputCalificacion = new RatingBar(this);
        inputCalificacion.setNumStars(5);
        inputCalificacion.setStepSize(0.5f);
        layout.addView(inputCalificacion);

        final EditText inputComentario = new EditText(this);
        inputComentario.setHint("Comentario");
        layout.addView(inputComentario);

        builder.setView(layout);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            float calificacion = inputCalificacion.getRating();
            String comentario = inputComentario.getText().toString().trim();

            if (calificacion > 0 && !comentario.isEmpty()) {
                dbHelper.insertarReseña(idUsuarioLogueado, idProductoPrueba, calificacion, comentario);
                Toast.makeText(this, "Reseña agregada", Toast.LENGTH_SHORT).show();
                cargarReseñasDelUsuario(); // Recargar lista
            } else {
                Toast.makeText(this, "Completa la calificación y el comentario", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoEditar() {
        int pos = listReseñas.getCheckedItemPosition();
        if (pos == -1) {
            Toast.makeText(this, "Selecciona una reseña", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el ID de la reseña seleccionada
        final SqlBasedeDatos.Reseña reseñaActual = listaReseñas.get(pos);
        final int idReseña = reseñaActual.id;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Reseña");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        final RatingBar inputCalificacion = new RatingBar(this);
        inputCalificacion.setNumStars(5);
        inputCalificacion.setStepSize(0.5f);
        inputCalificacion.setRating(reseñaActual.calificacion); // Cargar calificación actual
        layout.addView(inputCalificacion);

        final EditText inputComentario = new EditText(this);
        inputComentario.setHint("Comentario");
        inputComentario.setText(reseñaActual.comentario); // Cargar comentario actual
        layout.addView(inputComentario);

        builder.setView(layout);

        builder.setPositiveButton("Actualizar", (dialog, which) -> {
            float calificacion = inputCalificacion.getRating();
            String comentario = inputComentario.getText().toString().trim();

            if (calificacion > 0 && !comentario.isEmpty()) {
                dbHelper.actualizarReseña(idReseña, calificacion, comentario);
                Toast.makeText(this, "Reseña actualizada", Toast.LENGTH_SHORT).show();
                cargarReseñasDelUsuario();
            } else {
                Toast.makeText(this, "Completa los campos", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoEliminar() {
        int pos = listReseñas.getCheckedItemPosition();
        if (pos == -1) {
            Toast.makeText(this, "Selecciona una reseña", Toast.LENGTH_SHORT).show();
            return;
        }

        final int idReseña = listaReseñas.get(pos).id;

        new AlertDialog.Builder(this)
                .setTitle("Eliminar Reseña")
                .setMessage("¿Seguro que quieres eliminar esta reseña?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    dbHelper.eliminarReseña(idReseña);
                    Toast.makeText(this, "Reseña eliminada", Toast.LENGTH_SHORT).show();
                    cargarReseñasDelUsuario();
                })
                .setNegativeButton("No", null)
                .show();
    }
}