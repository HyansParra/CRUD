package com.example.myapplicationsss;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class Inicio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio);

        // Al tocar cualquier parte de la pantalla, ir a MainActivity
        View mainLayout = findViewById(R.id.main);
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Inicio.this, MainActivity.class);
                startActivity(intent);
                finish(); // Cierra la actividad de inicio
            }
        });
    }
}
