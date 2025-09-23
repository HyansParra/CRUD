package com.example.myapplicationsss;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class QRActivity extends AppCompatActivity {

    private Button btnEscanear, btnGenerar;
    private ImageView ivQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_apartado);

        btnEscanear = findViewById(R.id.btnEscanear);
        btnGenerar = findViewById(R.id.btnGenerar);
        ivQR = findViewById(R.id.ivQR);

        // Escaneo QR
        btnEscanear.setOnClickListener(v -> {
            // Verifica permisos cámara
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 101);
            } else {
                iniciarEscaneoQR();
            }
        });

        // Generar QR
        btnGenerar.setOnClickListener(v -> {
            try {
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.encodeBitmap(
                        "Producto de ejemplo", // Puedes cambiar por cualquier texto
                        BarcodeFormat.QR_CODE,
                        400,
                        400
                );
                ivQR.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error generando QR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void iniciarEscaneoQR() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Escanea un código QR");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        qrLauncher.launch(options);
    }

    private final ActivityResultLauncher<ScanOptions> qrLauncher = registerForActivityResult(
            new ScanContract(),
            result -> {
                if (result.getContents() != null) {
                    Toast.makeText(QRActivity.this, "QR Escaneado: " + result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
    );
}
