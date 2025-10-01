package com.example.myapplicationsss;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RatingBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class CatalogoActivity extends AppCompatActivity {

    private String tipoUsuario;
    private Button btnCatalogoCRUD, btnVolver;
    private LinearLayout linearProductos; // contenedor dinámico
    private SqlBasedeDatos db; // base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalogo); // tu XML adaptado

        // ===============================
        // Inicializar botones existentes
        // ===============================
        btnCatalogoCRUD = findViewById(R.id.btnCatalogoCRUD);
        btnVolver = findViewById(R.id.btnVolver);

        // ===============================
        // Contenedor de productos
        // ===============================
        linearProductos = findViewById(R.id.llProductos);

        // ===============================
        // Base de datos
        // ===============================
        db = new SqlBasedeDatos(this);

        // ===============================
        // Recibir tipo de usuario
        // ===============================
        tipoUsuario = getIntent().getStringExtra("tipo_usuario");

        // ===============================
        // Mostrar u ocultar botón CRUD solo si es admin
        // ===============================
        if (tipoUsuario != null && tipoUsuario.equalsIgnoreCase("admin")) {
            btnCatalogoCRUD.setVisibility(Button.VISIBLE);
        } else {
            btnCatalogoCRUD.setVisibility(Button.VISIBLE);
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

        // ===============================
        // Cargar productos dinámicamente
        // ===============================
        cargarProductos();
    }

    private void cargarProductos() {
        linearProductos.removeAllViews(); // Limpiar antes de agregar

        ArrayList<SqlBasedeDatos.Producto> productos = db.obtenerProductos();

        for (SqlBasedeDatos.Producto p : productos) {
            // Card
            MaterialCardView card = new MaterialCardView(this);
            card.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            card.setRadius(12);
            card.setCardElevation(4);
            card.setContentPadding(16, 16, 16, 16);

            // Contenedor horizontal
            LinearLayout contenedor = new LinearLayout(this);
            contenedor.setOrientation(LinearLayout.HORIZONTAL);
            contenedor.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            // Imagen
            ImageView img = new ImageView(this);
            img.setLayoutParams(new LinearLayout.LayoutParams(180, 180));
            img.setImageResource(R.drawable.cafe_placeholder); // placeholder
            contenedor.addView(img);

            // Info vertical
            LinearLayout info = new LinearLayout(this);
            info.setOrientation(LinearLayout.VERTICAL);
            info.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            info.setPadding(16, 0, 0, 0);

            TextView nombre = new TextView(this);
            nombre.setText(p.nombre);
            nombre.setTextSize(18);
            nombre.setTypeface(null, android.graphics.Typeface.BOLD);

            TextView precio = new TextView(this);
            precio.setText("$" + p.precio);
            precio.setTextSize(16);
            precio.setTypeface(null, android.graphics.Typeface.BOLD);

            // Botón agregar al carrito
            Button btnAgregar = new Button(this);
            btnAgregar.setText("Agregar al carrito");
            btnAgregar.setBackgroundColor(getResources().getColor(R.color.brown_600));
            btnAgregar.setTextColor(getResources().getColor(android.R.color.white));

            btnAgregar.setOnClickListener(v ->
                    Toast.makeText(this, "Agregaste " + p.nombre + " al carrito", Toast.LENGTH_SHORT).show()
            );

            // Agregar vistas al contenedor
            info.addView(nombre);
            info.addView(precio);
            info.addView(btnAgregar);

            contenedor.addView(info);
            card.addView(contenedor);

            // Agregar card al LinearLayout
            linearProductos.addView(card);
        }
    }
}
