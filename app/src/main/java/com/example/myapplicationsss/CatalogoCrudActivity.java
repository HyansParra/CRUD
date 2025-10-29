package com.example.myapplicationsss;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CatalogoCrudActivity extends AppCompatActivity {

    private EditText etNombre, etDescripcion, etPrecio;
    private Button btnAgregar, btnEditar, btnEliminar, btnVolver;
    private LinearLayout llProductos; // Contenedor dinámico para mostrar los productos
    private SqlBasedeDatos db; // Instancia de la base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalogo_crud);


        // Referencias a los EditText y Buttons

        etNombre = findViewById(R.id.etNombre);
        etDescripcion = findViewById(R.id.etDescripcion);
        etPrecio = findViewById(R.id.etPrecio);

        btnAgregar = findViewById(R.id.btnAgregar);
        btnEditar = findViewById(R.id.btnEditar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnVolver = findViewById(R.id.btnVolver);

        llProductos = findViewById(R.id.llProductos);


        // Inicializar la base de datos

        db = new SqlBasedeDatos(this);


        // Cargar productos existentes

        cargarProductos();


        // Botón volver

        btnVolver.setOnClickListener(v -> finish());


        // Botón agregar

        btnAgregar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String desc = etDescripcion.getText().toString().trim();
            String precioStr = etPrecio.getText().toString().trim();

            if (nombre.isEmpty() || desc.isEmpty() || precioStr.isEmpty()) {
                Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            double precio;
            try {
                precio = Double.parseDouble(precioStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Precio inválido", Toast.LENGTH_SHORT).show();
                return;
            }


            // Insertar en la base de datos

            long id = db.insertarProducto(nombre, "Café", precio); // categoría fija "Café"
            if (id > 0) {
                Toast.makeText(this, "Café agregado: " + nombre, Toast.LENGTH_SHORT).show();
                cargarProductos(); // Refrescar la lista
                etNombre.setText("");
                etDescripcion.setText("");
                etPrecio.setText("");
            } else {
                Toast.makeText(this, "Error al agregar el producto", Toast.LENGTH_SHORT).show();
            }
        });


        // Botones editar y eliminar generales

        btnEditar.setOnClickListener(v ->
                Toast.makeText(this, "Función editar aún no implementada", Toast.LENGTH_SHORT).show()
        );
        btnEliminar.setOnClickListener(v ->
                Toast.makeText(this, "Función eliminar aún no implementada", Toast.LENGTH_SHORT).show()
        );
    }


    // Método para cargar productos desde la DB y mostrarlos

    private void cargarProductos() {
        llProductos.removeAllViews(); // Limpiar antes de cargar

        ArrayList<SqlBasedeDatos.Producto> productos = db.obtenerProductos();

        for (SqlBasedeDatos.Producto p : productos) {
            View itemView = getLayoutInflater().inflate(R.layout.item_producto, llProductos, false);

            // Referencias dentro del item_producto
            Button btnEditarItem = itemView.findViewById(R.id.btnEditarProducto);
            Button btnEliminarItem = itemView.findViewById(R.id.btnEliminarProducto);

            // TextViews
            TextView tvNombre = itemView.findViewById(R.id.tvNombre);
            TextView tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            TextView tvPrecio = itemView.findViewById(R.id.tvPrecio);

            tvNombre.setText(p.nombre);
            tvDescripcion.setText("Descripción pendiente"); // placeholder
            tvPrecio.setText("$" + p.precio);

            // Listeners de botones individuales
            btnEditarItem.setOnClickListener(edit ->
                    Toast.makeText(this, "Editar " + p.nombre + " (falta implementación)", Toast.LENGTH_SHORT).show()
            );
            btnEliminarItem.setOnClickListener(del -> {
                llProductos.removeView(itemView);
                db.eliminarProducto(p.id); // elimina de la DB
            });

            llProductos.addView(itemView);
        }
    }
}
