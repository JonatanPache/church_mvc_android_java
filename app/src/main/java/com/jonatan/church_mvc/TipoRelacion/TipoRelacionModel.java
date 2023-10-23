package com.jonatan.church_mvc.TipoRelacion;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.jonatan.church_mvc.Ingreso.IngresoDato;
import com.jonatan.church_mvc.Utils.Template;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TipoRelacionModel extends Template<TipoRelacionDato> {
    private String id;
    private String nombre;
    private SQLiteDatabase db;
    private static final String TABLE_NAME = "TipoRelacion";
    private static final String KEY_ID = "id";
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    public TipoRelacionModel(SQLiteDatabase db) {
        this.id = "";
        this.nombre = "";
        this.db = db;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public void setAttributesData(TipoRelacionDato data) {
        id = data.id;
        nombre = data.nombre;
    }

    public TipoRelacionDato insert() {
        try {
            calendar = Calendar.getInstance();
            String fecha = dateFormat.format(calendar.getTime());

            ContentValues values = new ContentValues();
            values.put(KEY_NOMBRE, nombre);
            values.put(KEY_CREATED_AT, fecha);
            values.put(KEY_UPDATED_AT, fecha);

            if (!id.isEmpty()) {
                values.put(KEY_ID, id);
            }

            long affectedRows = !id.isEmpty() ?
                    db.update(TABLE_NAME, values, KEY_ID+"=" + id, null) :
                    db.insert(TABLE_NAME, null, values);
            if (affectedRows == 0) return null;
        }catch (SQLException e){
            e.getMessage();
        }
        TipoRelacionDato dto = !id.isEmpty() ?
                findRecordInDatabase(KEY_ID, id) :
                findRecordInDatabase(KEY_NOMBRE, nombre);
        return dto;
    }

    public TipoRelacionDato findRecordInDatabase(String columnName, String value) {
        TipoRelacionDato dto = null;
        String sql = String.format("select * from "+TABLE_NAME+" where %s='%s';", columnName, value);
        Cursor row = db.rawQuery(sql, null);
        if (row.moveToFirst()) {
            dto = new TipoRelacionDato(
                    row.getString(0),
                    row.getString(1)
            );
        }
        return dto;
    }

    public boolean deleteRecordInDatabase() {
        return db.delete(TABLE_NAME, KEY_ID+"=" + id, null) > 0;
    }
    @Override
    public Cursor getCursor() {
        String sql = "SELECT * FROM "+TABLE_NAME+";";
        return db.rawQuery(sql, null);
    }

    @Override
    public TipoRelacionDato getModel(Cursor cursor) {
        return new TipoRelacionDato(
                cursor.getString(0),
                cursor.getString(1)
        );
    }
}
