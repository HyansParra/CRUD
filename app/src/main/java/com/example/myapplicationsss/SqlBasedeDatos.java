package com.example.myapplicationsss;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SqlBasedeDatos extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cafeteria.db";
    private static final int DATABASE_VERSION = 2;

    public SqlBasedeDatos(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");

        // ==========================
        // Tablas existentes
        // ==========================
        db.execSQL("CREATE TABLE Usuarios (" +
                "idUsuario INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL UNIQUE, " +
                "email TEXT, " +
                "telefono TEXT, " +
                "fecha_nac TEXT, " +
                "estado TEXT DEFAULT 'activo', " +
                "creado_en TEXT NOT NULL, " +
                "password TEXT NOT NULL, " +
                "tipo TEXT NOT NULL DEFAULT 'normal');");

        db.execSQL("CREATE TABLE Clientes (" +
                "idCliente INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idUsuario INTEGER UNIQUE, " +
                "nombre TEXT NOT NULL, " +
                "email TEXT NOT NULL UNIQUE, " +
                "telefono TEXT, " +
                "fecha_nac TEXT, " +
                "estado TEXT DEFAULT 'activo', " +
                "puntos INTEGER DEFAULT 1000, " +
                "creado_en TEXT NOT NULL, " +
                "FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario) ON DELETE CASCADE" +
                ");");

        db.execSQL("CREATE TABLE Sucursales (" +
                "idSucursal INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "direccion TEXT, " +
                "lat REAL, " +
                "lon REAL, " +
                "horario TEXT, " +
                "estado TEXT DEFAULT 'activa');");

        db.execSQL("CREATE TABLE Productos (" +
                "idProducto INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "categoria TEXT, " +
                "precio NUMERIC NOT NULL CHECK(precio > 0), " +
                "estado TEXT DEFAULT 'activo');");

        db.execSQL("CREATE TABLE Regla (" +
                "id_regla INTEGER PRIMARY KEY, " +
                "descripcion TEXT NOT NULL, " +
                "expresion TEXT NOT NULL);");

        db.execSQL("CREATE TABLE Beneficios (" +
                "idBeneficio INTEGER PRIMARY KEY, " +
                "regla INTEGER NOT NULL, " +
                "producto_premio INTEGER, " +
                "nombre TEXT NOT NULL, " +
                "tipo TEXT NOT NULL, " +
                "requisito_visitas INTEGER DEFAULT 0 CHECK(requisito_visitas >= 0), " +
                "descuento_pct NUMERIC CHECK (descuento_pct IS NULL OR (descuento_pct >= 0 AND descuento_pct <= 100)), " +
                "vigencia_ini TEXT NOT NULL, " +
                "vigencia_fin TEXT NOT NULL, " +
                "estado TEXT DEFAULT 'activo', " +
                "FOREIGN KEY (regla) REFERENCES Regla(id_regla), " +
                "FOREIGN KEY (producto_premio) REFERENCES Productos(idProducto), " +
                "CHECK (date(vigencia_fin) >= date(vigencia_ini)));");

        db.execSQL("CREATE TABLE Visita (" +
                "id_visita INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_cliente INTEGER NOT NULL, " +
                "id_sucursal INTEGER NOT NULL, " +
                "fecha_hora TEXT NOT NULL, " +
                "origen TEXT, " +
                "estado_sync TEXT, " +
                "hash_qr TEXT UNIQUE, " +
                "FOREIGN KEY(id_cliente) REFERENCES Clientes(idCliente), " +
                "FOREIGN KEY(id_sucursal) REFERENCES Sucursales(idSucursal));");

        db.execSQL("CREATE TABLE Canje (" +
                "id_canje INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_cliente INTEGER NOT NULL, " +
                "id_beneficio INTEGER NOT NULL, " +
                "id_sucursal INTEGER NOT NULL, " +
                "fecha_hora TEXT NOT NULL, " +
                "codigo_qr TEXT, " +
                "estado_sync TEXT, " +
                "FOREIGN KEY(id_cliente) REFERENCES Clientes(idCliente), " +
                "FOREIGN KEY(id_beneficio) REFERENCES Beneficios(idBeneficio), " +
                "FOREIGN KEY(id_sucursal) REFERENCES Sucursales(idSucursal));");

        // ==========================
        // Índices
        // ==========================
        db.execSQL("CREATE INDEX ix_visita_cliente_fecha ON Visita(id_cliente, fecha_hora);");
        db.execSQL("CREATE INDEX ix_visita_sucursal_fecha ON Visita(id_sucursal, fecha_hora);");
        db.execSQL("CREATE INDEX ix_canje_cliente_fecha ON Canje(id_cliente, fecha_hora);");
        db.execSQL("CREATE INDEX ix_canje_beneficio_fecha ON Canje(id_beneficio, fecha_hora);");
        db.execSQL("CREATE INDEX ix_beneficio_estado_vig ON Beneficios(estado, vigencia_ini, vigencia_fin);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Canje");
        db.execSQL("DROP TABLE IF EXISTS Visita");
        db.execSQL("DROP TABLE IF EXISTS Beneficios");
        db.execSQL("DROP TABLE IF EXISTS Regla");
        db.execSQL("DROP TABLE IF EXISTS Productos");
        db.execSQL("DROP TABLE IF EXISTS Sucursales");
        db.execSQL("DROP TABLE IF EXISTS Clientes");
        db.execSQL("DROP TABLE IF EXISTS Usuarios");
        onCreate(db);
    }

    // ================================
    // MÉTODO: Insertar producto
    // ================================
    public long insertarProducto(String nombre, String categoria, double precio) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nombre", nombre);
        cv.put("categoria", categoria);
        cv.put("precio", precio);
        cv.put("estado", "activo");

        long id = db.insert("Productos", null, cv);
        db.close();
        return id;
    }

    // ================================
    // MÉTODO: Leer productos activos
    // ================================
    public ArrayList<Producto> obtenerProductos() {
        ArrayList<Producto> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT idProducto, nombre, categoria, precio, estado FROM Productos WHERE estado='activo'", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String nombre = cursor.getString(1);
                String categoria = cursor.getString(2);
                double precio = cursor.getDouble(3);
                String estado = cursor.getString(4);

                lista.add(new Producto(id, nombre, categoria, precio, estado));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return lista;
    }

    // ================================
    // MÉTODO: Eliminar producto por ID
    // ================================
    public boolean eliminarProducto(int idProducto) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filas = db.delete("Productos", "idProducto=?", new String[]{String.valueOf(idProducto)});
        db.close();
        return filas > 0;
    }

    // ================================
    // CLASE INTERNA: Producto
    // ================================
    public static class Producto {
        public int id;
        public String nombre;
        public String categoria;
        public double precio;
        public String estado;

        public Producto(int id, String nombre, String categoria, double precio, String estado) {
            this.id = id;
            this.nombre = nombre;
            this.categoria = categoria;
            this.precio = precio;
            this.estado = estado;
        }
    }
}
