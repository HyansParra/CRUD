package com.example.myapplicationsss;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.google.android.material.button.MaterialButton;

public class UsuarioActivity extends AppCompatActivity {

    private Button btnAbrirCrud, btnVolver, btnMiQR;
    private MaterialButton btnLogout;
    private String tipoUsuario;
    private LinearLayout layoutExtras;
    private TextView tvUserName, tvUserSubtitle;

    private static final String PREFS_NAME = "cafefidelidad_prefs";
    private static final String KEY_NOMBRE = "logged_name";
    private static final String KEY_TIPO = "logged_tipo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usuario);

        // Referencias UI
        btnAbrirCrud = findViewById(R.id.btnAbrirCrud);
        btnLogout = findViewById(R.id.btnLogout);
        btnVolver = findViewById(R.id.btnVolver);
        btnMiQR = findViewById(R.id.btnMiQR);
        layoutExtras = findViewById(R.id.layoutExtras);
        tvUserName = findViewById(R.id.tvUserName);
        tvUserSubtitle = findViewById(R.id.tvUserSubtitle);

        // Intent extras
        tipoUsuario = getIntent().getStringExtra("tipo_usuario");
        String nombreDesdeIntent = getIntent().getStringExtra("nombre_usuario");

        // SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String nombrePrefs = prefs.getString(KEY_NOMBRE, null);
        String tipoPrefs = prefs.getString(KEY_TIPO, null);

        String nombreFinal = (nombreDesdeIntent != null && !nombreDesdeIntent.isEmpty())
                ? nombreDesdeIntent
                : (nombrePrefs != null && !nombrePrefs.isEmpty() ? nombrePrefs : "Nombre del usuario");

        if (tipoUsuario == null || tipoUsuario.isEmpty()) {
            tipoUsuario = (tipoPrefs != null) ? tipoPrefs : "normal";
        }

        tvUserName.setText(nombreFinal);
        tvUserSubtitle.setText(tipoUsuario.equals("administrador") ? "Administrador" : "Usuario");

        // Visibilidad de CRUD
        if (!tipoUsuario.equals("administrador")) {
            btnAbrirCrud.setVisibility(Button.GONE);
        } else {
            // Si es administrador, mostrar botones extra REGLAS y BENEFICIOS
            agregarBotonAdmin(layoutExtras, "REGLAS");
            agregarBotonAdmin(layoutExtras, "BENEFICIOS");
        }

        // Botón MI QR siempre disponible (solo 1)
        btnMiQR.setOnClickListener(v -> {
            Intent intent = new Intent(UsuarioActivity.this, MiQRActivity.class);
            intent.putExtra("usuario_id", nombreFinal);
            startActivity(intent);
        });

        // Botón CRUD
        btnAbrirCrud.setOnClickListener(v -> {
            Intent intent = new Intent(UsuarioActivity.this, UsuarioCrudActivity.class);
            startActivity(intent);
        });

        // Cerrar sesión → volver a InicioActivity
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(UsuarioActivity.this, Inicio.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Volver
        btnVolver.setOnClickListener(v -> finish());

        // Barra inferior
        LinearLayout navCoffee = findViewById(R.id.navCoffee);
        LinearLayout navQR = findViewById(R.id.navQR);
        LinearLayout navUser = findViewById(R.id.navUser);

        navCoffee.setOnClickListener(v -> {
            Intent intent = new Intent(UsuarioActivity.this, CatalogoActivity.class);
            intent.putExtra("tipo_usuario", tipoUsuario);
            startActivity(intent);
        });

        navQR.setOnClickListener(v ->
                Toast.makeText(this, "Usa el botón MI QR de arriba", Toast.LENGTH_SHORT).show()
        );

        navUser.setOnClickListener(v -> { /* no-op */ });
    }

    private void agregarBotonAdmin(LinearLayout parent, String texto) {
        Button boton = new Button(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 16, 0, 0);
        boton.setLayoutParams(params);
        boton.setText(texto);
        boton.setTextColor(getResources().getColor(android.R.color.white));
        boton.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark));
        boton.setAllCaps(false);

        boton.setOnClickListener(v -> {
            if (texto.equals("REGLAS")) {
                startActivity(new Intent(UsuarioActivity.this, ReglasActivity.class));
            } else if (texto.equals("BENEFICIOS")) {
                startActivity(new Intent(UsuarioActivity.this, BeneficiosActivity.class));
            } else if (texto.equalsIgnoreCase("MI QR")) {
                Intent intent = new Intent(UsuarioActivity.this, MiQRActivity.class);
                SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                String usuarioId = prefs.getString(KEY_NOMBRE, "Invitado");
                intent.putExtra("usuario_id", usuarioId);
                startActivity(intent);
            }
        });

        parent.addView(boton);
    }
}
