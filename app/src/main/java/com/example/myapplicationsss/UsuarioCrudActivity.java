package com.example.myapplicationsss;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class UsuarioCrudActivity extends AppCompatActivity {

    private SqlBasedeDatos dbHelper;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listaUsuarios;
    private ListView listUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario_crud);

        dbHelper = new SqlBasedeDatos(this);

        Button btnVolver = findViewById(R.id.btnVolver);
        Button btnAgregar = findViewById(R.id.btnAgregar);
        Button btnEditar = findViewById(R.id.btnEditar);
        Button btnEliminar = findViewById(R.id.btnEliminar);
        listUsuarios = findViewById(R.id.listUsuarios);

        cargarUsuarios();

        btnVolver.setOnClickListener(v -> finish());
        btnAgregar.setOnClickListener(v -> mostrarDialogoAgregar());
        btnEditar.setOnClickListener(v -> mostrarDialogoEditar());
        btnEliminar.setOnClickListener(v -> mostrarDialogoEliminar());
    }

    private void cargarUsuarios() {
        listaUsuarios = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT idUsuario, nombre, email, tipo, password FROM Usuarios", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String nombre = cursor.getString(1);
                String email = cursor.getString(2);
                String tipo = cursor.getString(3);
                String password = cursor.getString(4);
                listaUsuarios.add(id + " - " + nombre + " (" + email + ") [" + tipo + "] - " + password);
            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_single_choice,
                listaUsuarios);
        listUsuarios.setAdapter(adapter);
        listUsuarios.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    private void mostrarDialogoAgregar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar Usuario");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        EditText inputNombre = new EditText(this);
        inputNombre.setHint("Nombre");
        layout.addView(inputNombre);

        EditText inputPassword = new EditText(this);
        inputPassword.setHint("Contraseña");
        layout.addView(inputPassword);

        Spinner spinnerRol = new Spinner(this);
        ArrayAdapter<String> adapterRol = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"normal", "administrador"});
        adapterRol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRol.setAdapter(adapterRol);
        layout.addView(spinnerRol);

        builder.setView(layout);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String nombre = inputNombre.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();
            String rol = spinnerRol.getSelectedItem().toString();

            if (!nombre.isEmpty() && !password.isEmpty()) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("nombre", nombre);
                values.put("email", nombre.toLowerCase() + "@mail.com");
                values.put("telefono", "000000000");
                values.put("fecha_nac", "2000-01-01");
                values.put("creado_en", String.valueOf(System.currentTimeMillis()));
                values.put("password", password);
                values.put("tipo", rol);

                db.insert("Usuarios", null, values);
                Toast.makeText(this, "Usuario agregado como " + rol, Toast.LENGTH_SHORT).show();
                cargarUsuarios();
            } else {
                Toast.makeText(this, "Ingresa un nombre y contraseña válidos", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoEditar() {
        int pos = listUsuarios.getCheckedItemPosition();
        if (pos == -1) {
            Toast.makeText(this, "Selecciona un usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        String seleccionado = listaUsuarios.get(pos);
        int id = Integer.parseInt(seleccionado.split(" - ")[0]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Usuario");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 20, 40, 20);

        EditText inputNombre = new EditText(this);
        inputNombre.setHint("Nuevo nombre");
        layout.addView(inputNombre);

        EditText inputPassword = new EditText(this);
        inputPassword.setHint("Nueva contraseña");
        layout.addView(inputPassword);

        builder.setView(layout);

        builder.setPositiveButton("Actualizar", (dialog, which) -> {
            String nuevoNombre = inputNombre.getText().toString().trim();
            String nuevaPassword = inputPassword.getText().toString().trim();

            if (!nuevoNombre.isEmpty() && !nuevaPassword.isEmpty()) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("nombre", nuevoNombre);
                values.put("password", nuevaPassword);
                db.update("Usuarios", values, "idUsuario=?", new String[]{String.valueOf(id)});
                Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_SHORT).show();
                cargarUsuarios();
            } else {
                Toast.makeText(this, "Ingresa un nombre y contraseña válidos", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private void mostrarDialogoEliminar() {
        int pos = listUsuarios.getCheckedItemPosition();
        if (pos == -1) {
            Toast.makeText(this, "Selecciona un usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        String seleccionado = listaUsuarios.get(pos);
        int id = Integer.parseInt(seleccionado.split(" - ")[0]);

        new AlertDialog.Builder(this)
                .setTitle("Eliminar Usuario")
                .setMessage("¿Seguro que quieres eliminar este usuario?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.delete("Usuarios", "idUsuario=?", new String[]{String.valueOf(id)});
                    Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                    cargarUsuarios();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
