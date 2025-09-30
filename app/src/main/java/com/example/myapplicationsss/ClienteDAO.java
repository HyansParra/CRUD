package com.example.myapplicationsss;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
public class ClienteDAO {
    private SqlBasedeDatos dbHelper;

    public ClienteDAO(SqlBasedeDatos dbHelper) {
        this.dbHelper = dbHelper;
    }

    public int obtenerPuntosPorUsuario(int idUsuario) {
        int puntos = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT puntos FROM Clientes WHERE idUsuario = ?", new String[]{String.valueOf(idUsuario)});
        if (cursor.moveToFirst()) {
            puntos = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return puntos;
    }

    public void sumarPuntosPorUsuario(int idUsuario, int puntosASumar) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int puntosActuales = obtenerPuntosPorUsuario(idUsuario);

        ContentValues cv = new ContentValues();
        cv.put("puntos", puntosActuales + puntosASumar);

        db.update("Clientes", cv, "idUsuario = ?", new String[]{String.valueOf(idUsuario)});
        db.close();


    }
}
