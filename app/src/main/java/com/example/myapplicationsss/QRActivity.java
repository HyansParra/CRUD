package com.example.myapplicationsss;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class QRActivity extends AppCompatActivity {

    private Button btnEscanear, btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_apartado);

        btnEscanear = findViewById(R.id.btnEscanear);
        btnVolver = findViewById(R.id.btnVolver);

        // Escaneo QR
        btnEscanear.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
            } else {
                iniciarEscaneoQR();
            }
        });

        // Volver
        btnVolver.setOnClickListener(v -> finish());
    }

    private void iniciarEscaneoQR() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Coloca el QR dentro del marco");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        qrLauncher.launch(options);
    }

    private final ActivityResultLauncher<ScanOptions> qrLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    mostrarResultadoDialog(result.getContents());
                }
            }
    );

    private void mostrarResultadoDialog(String contenido) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("QR Escaneado");
        builder.setMessage(contenido);
        builder.setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
