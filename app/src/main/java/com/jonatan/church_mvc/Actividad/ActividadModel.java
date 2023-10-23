package com.jonatan.church_mvc.Actividad;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.jonatan.church_mvc.Ingreso.IngresoDato;
import com.jonatan.church_mvc.Utils.Template;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActividadModel extends Template<ActividadDato> {
    private String id;
    private String nombre , descripcion, fecha_inicio, fecha_final;
    private SQLiteDatabase db;
    private static final String TABLE_NAME = "Actividad";
    private static final String KEY_ID = "id";
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_DESCRIPTION = "descripcion";
    private static final String KEY_FECHA_INICIO = "fecha_inicio";
    private static final String KEY_FECHA_FINAL = "fecha_final";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    public ActividadModel(SQLiteDatabase db) {
        this.id = "";
        this.nombre = "";
        this.descripcion = "";
        this.fecha_inicio = "";
        this.fecha_final = "";
        this.db = db;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    public void setAttributesData(ActividadDato data) {
        id = data.id;
        nombre = data.nombre;
        descripcion = data.descripcion;
        fecha_inicio = data.fecha_inicio;
        fecha_final = data.fecha_final;
    }
    public ActividadDato insert() {
        try {
            calendar = Calendar.getInstance();
            String fecha = dateFormat.format(calendar.getTime());

            ContentValues values = new ContentValues();
            values.put(KEY_NOMBRE, nombre);
            values.put(KEY_DESCRIPTION, descripcion);
            values.put(KEY_FECHA_INICIO, fecha_inicio);
            values.put(KEY_FECHA_FINAL, fecha_final);
            values.put(KEY_CREATED_AT, fecha);
            values.put(KEY_UPDATED_AT, fecha);

            if (!id.isEmpty()) {
                values.put(KEY_ID, id);
            }

            long affectedRows = !id.isEmpty() ?
                    db.update(TABLE_NAME, values, "id="+id, null) :
                    db.insert(TABLE_NAME, null, values);
            if (affectedRows == 0) return null;
        }catch (SQLException e){
            e.getMessage();
        }
        ActividadDato dto = !id.isEmpty() ?
                findRecordInDatabase(KEY_ID, id) :
                findRecordInDatabase(KEY_NOMBRE, nombre);
        return dto;
    }
    public ActividadDato findRecordInDatabase(String columnName, String value) {
        ActividadDato dto = null;
        String sql = String.format("select * from "+TABLE_NAME+" where %s='%s';", columnName, value);
        Cursor row = db.rawQuery(sql, null);
        if (row.moveToFirst()) {
            dto = new ActividadDato(
                    row.getString(0),
                    row.getString(1),
                    row.getString(2),
                    row.getString(3),
                    row.getString(4)
            );
        }
        return dto;
    }
    public boolean deleteRecordInDatabase() {
        return db.delete(TABLE_NAME, "id=" + id, null) > 0;
    }

    @Override
    public Cursor getCursor() {
        String sql = "SELECT * FROM "+TABLE_NAME+";";
        return db.rawQuery(sql, null);
    }

    @Override
    public ActividadDato getModel(Cursor cursor) {
        return new ActividadDato(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4)
        );
    }
}
