package com.example.myapplicationsss;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MiQRActivity extends AppCompatActivity {

    private ImageView imgQR;
    private TextView tvQRInfo;
    private Button btnGenerarQR, btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mi_qr);

        imgQR = findViewById(R.id.imgQR);
        tvQRInfo = findViewById(R.id.tvQRInfo);
        btnGenerarQR = findViewById(R.id.btnGenerarQR);
        btnVolver = findViewById(R.id.btnVolver);

        // Obtener datos del usuario
        String usuarioIdExtra = getIntent().getStringExtra("usuario_id");
        final String usuarioId = (usuarioIdExtra != null && !usuarioIdExtra.isEmpty()) ? usuarioIdExtra : "Invitado";

        tvQRInfo.setText("QR de: " + usuarioId);

        // Generar QR inicial
        generarQR(usuarioId);

        // Botón Generar QR único
        btnGenerarQR.setOnClickListener(v -> {
            // Creamos un QR único con timestamp
            String qrUnico = usuarioId + "_" + String.valueOf(System.currentTimeMillis());
            generarQR(qrUnico);
        });

        // Botón Volver
        btnVolver.setOnClickListener(v -> finish());
    }

    private void generarQR(String texto) {
        try {
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.encodeBitmap(texto, BarcodeFormat.QR_CODE, 600, 600);
            imgQR.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
