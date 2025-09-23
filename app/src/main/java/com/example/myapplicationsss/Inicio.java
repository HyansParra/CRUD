package com.example.myapplicationsss;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class Inicio extends AppCompatActivity {

    private EditText etUsuario, etPassword;
    private Button btnLogin;
    private SqlBasedeDatos dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);

        etUsuario = findViewById(R.id.etUsuario);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        dbHelper = new SqlBasedeDatos(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        insertarUsuarioSiNoExiste(db, "admin", "admin123", "administrador");
        insertarUsuarioSiNoExiste(db, "usuario", "usuario123", "normal");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = etUsuario.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (usuario.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Inicio.this, "Ingresa usuario y contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                String tipoUsuario = verificarUsuario(db, usuario, password);

                if (tipoUsuario == null) {
                    Toast.makeText(Inicio.this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Inicio.this, "Bienvenido " + tipoUsuario, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Inicio.this, MainActivity.class);
                    intent.putExtra("tipo_usuario", tipoUsuario); // ✅ Pasamos el tipo de usuario
                    startActivity(intent);
                    finish();
                }
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
