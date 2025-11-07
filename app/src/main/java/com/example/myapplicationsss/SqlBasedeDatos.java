package com.example.myapplicationsss;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SqlBasedeDatos extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cafeteria.db";
    // Versión 3, esto está correcto
    private static final int DATABASE_VERSION = 3;

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
        // Nueva Tabla: Reseñas
        // ==========================
        db.execSQL("CREATE TABLE Reseñas (" +
                "idReseña INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "idUsuario INTEGER NOT NULL, " +
                "idProducto INTEGER NOT NULL, " +
                "calificacion REAL NOT NULL CHECK(calificacion >= 0 AND calificacion <= 5), " +
                "comentario TEXT, " +
                "fecha TEXT NOT NULL, " +
                "FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario) ON DELETE CASCADE, " +
                "FOREIGN KEY (idProducto) REFERENCES Productos(idProducto) ON DELETE CASCADE" +
                ");");

        // ==========================
        // Índices
        // ==========================
        db.execSQL("CREATE INDEX ix_visita_cliente_fecha ON Visita(id_cliente, fecha_hora);");
        db.execSQL("CREATE INDEX ix_visita_sucursal_fecha ON Visita(id_sucursal, fecha_hora);");
        db.execSQL("CREATE INDEX ix_canje_cliente_fecha ON Canje(id_cliente, fecha_hora);");
        db.execSQL("CREATE INDEX ix_canje_beneficio_fecha ON Canje(id_beneficio, fecha_hora);");
        db.execSQL("CREATE INDEX ix_beneficio_estado_vig ON Beneficios(estado, vigencia_ini, vigencia_fin);");


        // ================================================================
        // == INICIO DE BLOQUE AÑADIDO (LA SOLUCIÓN) ==
        // ================================================================
        // Insertamos un producto por defecto para que el idProductoPrueba = 1 exista
        ContentValues productoDefault = new ContentValues();
        // Usamos .put("nombre", ...) y dejamos que el ID sea autoincremental
        // La base de datos le asignará el ID 1 automáticamente por ser el primero.
        productoDefault.put("nombre", "Café de Prueba");
        productoDefault.put("categoria", "Bebida");
        productoDefault.put("precio", 1000);
        productoDefault.put("estado", "activo");
        db.insert("Productos", null, productoDefault);
        // ================================================================
        // == FIN DE BLOQUE AÑADIDO ==
        // ================================================================
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
        db.execSQL("DROP TABLE IF EXISTS Reseñas"); // Tu Drop está correcto
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
// MÉTODO: Insertar Reseña
// ================================
    public long insertarReseña(int idUsuario, int idProducto, float calificacion, String comentario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("idUsuario", idUsuario);
        cv.put("idProducto", idProducto);
        cv.put("calificacion", calificacion);
        cv.put("comentario", comentario);
        cv.put("fecha", String.valueOf(System.currentTimeMillis())); // Fecha actual

        long id = db.insert("Reseñas", null, cv);
        db.close();
        return id;
    }

    // ================================
// MÉTODO: Leer Reseñas por Usuario
// ================================
    public ArrayList<Reseña> obtenerReseñasPorUsuario(int idUsuario) {
        ArrayList<Reseña> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT idReseña, idUsuario, idProducto, calificacion, comentario, fecha FROM Reseñas WHERE idUsuario = ?",
                new String[]{String.valueOf(idUsuario)}
        );

        if (cursor.moveToFirst()) {
            do {
                lista.add(new Reseña(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getInt(2),
                        cursor.getFloat(3),
                        cursor.getString(4),
                        cursor.getString(5)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    // ================================
    // MÉTODO: Actualizar Reseña
    // ================================
    public boolean actualizarReseña(int idReseña, float calificacion, String comentario) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("calificacion", calificacion);
        cv.put("comentario", comentario);
        cv.put("fecha", String.valueOf(System.currentTimeMillis()));

        int filas = db.update("Reseñas", cv, "idReseña = ?", new String[]{String.valueOf(idReseña)});
        db.close();
        return filas > 0;
    }

    // ================================
    // MÉTODO: Eliminar Reseña
    // ================================
    public boolean eliminarReseña(int idReseña) {
        SQLiteDatabase db = this.getWritableDatabase();
        int filas = db.delete("Reseñas", "idReseña = ?", new String[]{String.valueOf(idReseña)});
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

    // ================================
    // CLASE INTERNA: Reseña
    // ================================
    public static class Reseña {
        public int id;
        public int idUsuario;
        public int idProducto;
        public float calificacion;
        public String comentario;
        public String fecha;

        // Constructor
        public Reseña(int id, int idUsuario, int idProducto, float calificacion, String comentario, String fecha) {
            this.id = id;
            this.idUsuario = idUsuario;
            this.idProducto = idProducto;
            this.calificacion = calificacion;
            this.comentario = comentario;
            this.fecha = fecha;
        }
    }
}
