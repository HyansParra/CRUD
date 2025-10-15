package com.example.myapplicationsss;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private ImageView logo1;
    private TextView cargando, bienvenida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        logo1 = findViewById(R.id.logo1);
        cargando = findViewById(R.id.cargando);
        bienvenida = findViewById(R.id.bienvenida);


        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation scaleIn = AnimationUtils.loadAnimation(this, R.anim.scale_in);


        logo1.startAnimation(scaleIn);
        bienvenida.startAnimation(fadeIn);
        cargando.startAnimation(fadeIn);


        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, Inicio.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 3000);
    }
}
