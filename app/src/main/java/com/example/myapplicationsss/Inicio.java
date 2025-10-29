package com.example.myapplicationsss;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Inicio extends AppCompatActivity {

    // --- UI ---
    private EditText etUsuario, etPassword, etConfirmarPassword;
    private Button btnAccion;
    private CheckBox cbRegistrar;


    private SqlBasedeDatos dbHelper;


    private static final String PREFS_NAME = "cafefidelidad_prefs";
    private static final String KEY_NOMBRE = "logged_name";
    private static final String KEY_TIPO = "logged_tipo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);

        // Referencias UI
        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        etConfirmarPassword = findViewById(R.id.etConfirmarPassword);
        btnAccion = findViewById(R.id.btnAccion);
        cbRegistrar = findViewById(R.id.cbRegistrar);

        // Base de datos
        dbHelper = new SqlBasedeDatos(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Insertar usuarios predeterminados
        insertarUsuarioSiNoExiste(db, "admin", "admin123", "administrador");
        insertarUsuarioSiNoExiste(db, "usuario", "usuario123", "normal");
        insertarUsuarioSiNoExiste(db, "rodrigo", "123", "normal");

        // Cambiar entre login y registro
        cbRegistrar.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etConfirmarPassword.setVisibility(EditText.VISIBLE);
                btnAccion.setText("Registrar");
            } else {
                etConfirmarPassword.setVisibility(EditText.GONE);
                btnAccion.setText("Iniciar sesión");
            }
        });

        // Acción de botón (login / registro)
        btnAccion.setOnClickListener(view -> {
            String usuario = etUsuario.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (usuario.isEmpty() || password.isEmpty()) {
                Toast.makeText(Inicio.this, "Complete los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (cbRegistrar.isChecked()) {
                // --- Registro ---
                String confirmar = etConfirmarPassword.getText().toString().trim();

                if (!password.equals(confirmar)) {
                    Toast.makeText(Inicio.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }

                registrarUsuario(db, usuario, password);
                guardarUsuarioEnPrefs(usuario, "normal");

            } else {
                // --- Login ---
                String tipoUsuario = verificarUsuario(db, usuario, password);

                if (tipoUsuario == null) {
                    Toast.makeText(Inicio.this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                } else {
                    // Obtener idUsuario del usuario logueado
                    int idUsuario = obtenerIdUsuario(db, usuario);

                    // Verificar si existe cliente asociado, si no crear
                    if (!existeCliente(db, idUsuario)) {
                        crearCliente(db, idUsuario, usuario);
                    }

                    guardarUsuarioEnPrefs(usuario, tipoUsuario);
                    Toast.makeText(Inicio.this, "Bienvenido " + usuario, Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Inicio.this, MainActivity.class);
                    intent.putExtra("tipo_usuario", tipoUsuario);
                    intent.putExtra("nombre_usuario", usuario);
                    intent.putExtra("id_usuario", idUsuario); // <-- nuevo
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    // --- Guardar usuario en SharedPreferences ---
    private void guardarUsuarioEnPrefs(String nombre, String tipo) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_NOMBRE, nombre);
        editor.putString(KEY_TIPO, tipo);
        editor.apply();
    }

    // --- Insertar usuario por defecto si no existe ---
    private void insertarUsuarioSiNoExiste(SQLiteDatabase db, String usuario, String password, String tipo) {
        Cursor cursor = db.rawQuery("SELECT * FROM Usuarios WHERE nombre=?", new String[]{usuario});

        if (cursor.getCount() == 0) {
            ContentValues values = new ContentValues();
            values.put("nombre", usuario);
            values.put("email", usuario + "@cafefidelidad.com");
            values.put("telefono", "");
            values.put("fecha_nac", "");
            values.put("estado", "activo");
            values.put("creado_en", String.valueOf(System.currentTimeMillis()));
            values.put("password", password);
            values.put("tipo", tipo);

            long idUsuario = db.insert("Usuarios", null, values); // <-- ID generado

            // Crear cliente asociado
            crearCliente(db, idUsuario, usuario);
        }

        cursor.close();
    }

    // --- Registrar un nuevo usuario ---
    private void registrarUsuario(SQLiteDatabase db, String usuario, String password) {
        Cursor cursor = db.rawQuery("SELECT * FROM Usuarios WHERE nombre=?", new String[]{usuario});

        if (cursor.getCount() > 0) {
            Toast.makeText(this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues values = new ContentValues();
            values.put("nombre", usuario);
            values.put("email", usuario + "@cafefidelidad.com");
            values.put("telefono", "");
            values.put("fecha_nac", "");
            values.put("estado", "activo");
            values.put("creado_en", String.valueOf(System.currentTimeMillis()));
            values.put("password", password);
            values.put("tipo", "normal");

            long idUsuario = db.insert("Usuarios", null, values); // <-- ID generado

            // Crear cliente asociado
            crearCliente(db, idUsuario, usuario);

            Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }

    // --- Verificar login ---
    private String verificarUsuario(SQLiteDatabase db, String usuario, String password) {
        Cursor cursor = db.rawQuery(
                "SELECT tipo FROM Usuarios WHERE nombre=? AND password=?",
                new String[]{usuario, password}
        );

        if (cursor.moveToFirst()) {
            String tipo = cursor.getString(0);
            cursor.close();
            return tipo;
        }

        cursor.close();
        return null;
    }

    // --- Obtener idUsuario a partir del nombre ---
    private int obtenerIdUsuario(SQLiteDatabase db, String nombre) {
        int id = 0;
        Cursor cursor = db.rawQuery("SELECT idUsuario FROM Usuarios WHERE nombre=?", new String[]{nombre});
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        return id;
    }

    // --- Verificar si existe cliente ---
    private boolean existeCliente(SQLiteDatabase db, int idUsuario) {
        Cursor cursor = db.rawQuery("SELECT * FROM Clientes WHERE idUsuario=?", new String[]{String.valueOf(idUsuario)});
        boolean existe = cursor.moveToFirst();
        cursor.close();
        return existe;
    }

    // --- Crear cliente ---
    private void crearCliente(SQLiteDatabase db, long idUsuario, String nombre) {
        ContentValues clienteValues = new ContentValues();
        clienteValues.put("idUsuario", idUsuario);
        clienteValues.put("nombre", nombre);
        clienteValues.put("email", nombre + "@cafefidelidad.com");
        clienteValues.put("puntos", 0);
        clienteValues.put("creado_en", String.valueOf(System.currentTimeMillis()));
        db.insert("Clientes", null, clienteValues);


    }
}
