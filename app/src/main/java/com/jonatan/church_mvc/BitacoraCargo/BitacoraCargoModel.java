package com.jonatan.church_mvc.BitacoraCargo;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.jonatan.church_mvc.Ingreso.IngresoDato;
import com.jonatan.church_mvc.Utils.Template;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BitacoraCargoModel extends Template<BitacoraCargoDato> {
    private String id;
    private String persona_ci, cargo_id, fecha_inicio, fecha_final, estado;
    private SQLiteDatabase db;
    private static final String TABLE_NAME = "BitacoraCargo";
    private static final String KEY_ID = "id";
    private static final String KEY_PERSONA_CI = "persona_ci";
    private static final String KEY_CARGO_ID = "cargo_id";
    private static final String KEY_FECHA_INICIO = "fecha_inicio";
    private static final String KEY_FECHA_FINAL = "fecha_final";
    private static final String KEY_ESTADO = "estado";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    public BitacoraCargoModel(SQLiteDatabase db) {
        this.id = "";
        this.persona_ci = "";
        this.cargo_id = "";
        this.fecha_inicio = "";
        this.fecha_final = "";
        this.estado = "";
        this.db = db;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public void setAttributesData(BitacoraCargoDato data) {
        id = data.id;
        persona_ci = data.persona_ci;
        cargo_id = data.cargo_id;
        fecha_inicio = data.fecha_inicio;
        fecha_final = data.fecha_final;
        estado = data.estado;
    }

    public BitacoraCargoDato saveInDatabase() {
        try {
            calendar = Calendar.getInstance();
            String fecha = dateFormat.format(calendar.getTime());

            ContentValues values = new ContentValues();
            values.put(KEY_PERSONA_CI, persona_ci);
            values.put(KEY_CARGO_ID, cargo_id);
            values.put(KEY_FECHA_INICIO, fecha_inicio);
            values.put(KEY_FECHA_FINAL, fecha_final);
            values.put(KEY_ESTADO, estado);
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
        BitacoraCargoDato dto = !id.isEmpty() ?
                findRecordInDatabase(KEY_ID, id) :
                findRecordInDatabase(KEY_PERSONA_CI, persona_ci);
        return dto;
    }

    public BitacoraCargoDato findRecordInDatabase(String columnName, String value) {
        BitacoraCargoDato dto = null;
        String sql = String.format("select * from "+TABLE_NAME+" where %s='%s';", columnName, value);
        Cursor row = db.rawQuery(sql, null);
        if (row.moveToFirst()) {
            dto = new BitacoraCargoDato(
                    row.getString(0),
                    row.getString(1),
                    row.getString(2),
                    row.getString(3),
                    row.getString(4),
                    row.getString(5)
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
    public BitacoraCargoDato getModel(Cursor cursor) {
        return new BitacoraCargoDato(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5)
        );
    }
}
