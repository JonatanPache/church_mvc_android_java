package com.jonatan.church_mvc.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "church.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS TipoRelacion ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "nombre TEXT NOT NULL ,"+
                "created_at DATETIME NOT NULL ,"+
                "updated_at DATETIME NOT NULL)");
//
        db.execSQL("CREATE TABLE IF NOT EXISTS Cargo ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "nombre TEXT NOT NULL ,"+
                "descripcion TEXT ,"+
                "created_at DATETIME NOT NULL ,"+
                "updated_at DATETIME NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS Ingreso ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "nombre TEXT NOT NULL ,"+
                "descripcion TEXT ,"+
                "created_at DATETIME NOT NULL ,"+
                "updated_at DATETIME NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS Persona ("+
                "ci INTEGER PRIMARY KEY NOT NULL,"+
                "nombre TEXT NOT NULL,"+
                "telefono TEXT ,"+
                "rol TEXT DEFAULT 'VISITANTE',"+
                "invita_id INTEGER,"+
                "created_at DATETIME NOT NULL ,"+
                "updated_at DATETIME NOT NULL, "+
                "FOREIGN KEY (invita_id) REFERENCES Persona(ci) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE IF NOT EXISTS Actividad ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "nombre TEXT NOT NULL,"+
                "descripcion TEXT NOT NULL,"+
                "fecha_inicio DATETIME NOT NULL ,"+
                "fecha_final DATETIME NOT NULL , "+
                "created_at DATETIME NOT NULL ,"+
                "updated_at DATETIME NOT NULL)");
//
        db.execSQL("CREATE TABLE IF NOT EXISTS RelacionFamiliar ("+
                "persona_id INTEGER NOT NULL,"+
                "pariente_id INTEGER NOT NULL,"+
                "relacion_id INTEGER NOT NULL,"+
                "created_at DATETIME NOT NULL ,"+
                "updated_at DATETIME NOT NULL,"+
                "FOREIGN KEY (persona_id) REFERENCES Persona(ci) ON DELETE CASCADE," +
                "FOREIGN KEY (pariente_id) REFERENCES Persona(ci) ON DELETE CASCADE, "+
                "FOREIGN KEY (relacion_id) REFERENCES TipoRelacionFamiliar(id) ON DELETE CASCADE )");
//
        db.execSQL("CREATE TABLE IF NOT EXISTS BitacoraCargo ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "persona_ci INTEGER NOT NULL ,"+
                "cargo_id INTEGER NOT NULL ,"+
                "fecha_inicio DATETIME NOT NULL ,"+
                "fecha_final DATETIME NOT NULL , "+
                "estado INTEGER DEFAULT 1,"+
                "created_at DATETIME NOT NULL ,"+
                "updated_at DATETIME NOT NULL,"+
                "FOREIGN KEY (persona_ci) REFERENCES Persona(ci)," +
                "FOREIGN KEY (cargo_id) REFERENCES Cargo(id) )");
//
//        db.execSQL("CREATE TABLE IF NOT EXISTS Visita ("+
//                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
//                //idVisita
//                "ci_persona_1 INTEGER NOT NULL,"+ //miemrbroo quien invito
//                "ci_persona_2 INTEGER NOT NULL,"+ //quine fue invitado
//                "actividad_id INTEGER NOT NULL,"+ //quine fue invitado
//                //nombew invitado visitante
//                "created_at DATETIME NOT NULL ,"+
//                "updated_at DATETIME NOT NULL,"+
//                "FOREIGN KEY (actividad_id) REFERENCES Actividad(id)," +
//                "FOREIGN KEY (ci_persona_1) REFERENCES Persona(ci)," +
//                "FOREIGN KEY (ci_persona_2) REFERENCES Persona(ci) ) ");
//
        db.execSQL("CREATE TABLE IF NOT EXISTS NotaIngreso ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "actividad_id INTEGER NOT NULL ,"+
                "titulo TEXT NOT NULL ,"+
                "total INTEGER NOT NULL ,"+
                "created_at DATETIME NOT NULL ,"+
                "updated_at DATETIME NOT NULL,"+
                "FOREIGN KEY (actividad_id) REFERENCES Actividad(id) ON DELETE CASCADE)");
//
        db.execSQL("CREATE TABLE IF NOT EXISTS DetalleIngreso ("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "nota_ingreso_id INTEGER NOT NULL ,"+
                "ingreso_id INTEGER NOT NULL ,"+
                "monto INTEGER NOT NULL ,"+
                "created_at DATETIME NOT NULL ,"+
                "updated_at DATETIME NOT NULL ,"+
                "FOREIGN KEY (nota_ingreso_id) REFERENCES NotaIngreso(id) ON DELETE CASCADE,"+
                "FOREIGN KEY (ingreso_id) REFERENCES Ingreso(id) ON DELETE CASCADE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Ingreso");
//        db.execSQL("DROP TABLE IF EXISTS Persona");
        onCreate(db);
    }
}
