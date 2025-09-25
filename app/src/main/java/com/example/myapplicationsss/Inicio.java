package com.example.myapplicationsss;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

public class Inicio extends AppCompatActivity {

    private EditText etUsuario, etPassword, etConfirmarPassword;
    private Button btnAccion;
    private CheckBox cbRegistrar;
    private SqlBasedeDatos dbHelper;
    private NestedScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);

        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        etConfirmarPassword = findViewById(R.id.etConfirmarPassword);
        btnAccion = findViewById(R.id.btnAccion);
        cbRegistrar = findViewById(R.id.cbRegistrar);
        scrollView = findViewById(R.id.scrollView);

        dbHelper = new SqlBasedeDatos(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        //  Insertar usuarios predeterminados si no existen
        insertarUsuarioSiNoExiste(db, "admin", "admin123", "administrador");
        insertarUsuarioSiNoExiste(db, "usuario", "usuario123", "normal");

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

        // Botón de acción
        btnAccion.setOnClickListener(view -> {
            String usuario = etUsuario.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (usuario.isEmpty() || password.isEmpty()) {
                Toast.makeText(Inicio.this, "Complete los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (cbRegistrar.isChecked()) {
                // Registro
                String confirmar = etConfirmarPassword.getText().toString().trim();
                if (!password.equals(confirmar)) {
                    Toast.makeText(Inicio.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }
                registrarUsuario(db, usuario, password);
            } else {
                // Login
                String tipoUsuario = verificarUsuario(db, usuario, password);
                if (tipoUsuario == null) {
                    Toast.makeText(Inicio.this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Inicio.this, "Bienvenido " + tipoUsuario, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Inicio.this, MainActivity.class);
                    intent.putExtra("tipo_usuario", tipoUsuario);
                    startActivity(intent);
                    finish();
                }
            }
        });

        //  Detectar deslizar hacia arriba para ir al login
        scrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener)
                (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    if (scrollY > 200) { // Deslizó hacia abajo
                        scrollView.post(() -> scrollView.smoothScrollTo(0, v.getBottom()));
                    }
                });
    }

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
            db.insert("Usuarios", null, values);
        }
        cursor.close();
    }

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
            db.insert("Usuarios", null, values);
            Toast.makeText(this, "Usuario registrado con éxito", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
    }

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
}
