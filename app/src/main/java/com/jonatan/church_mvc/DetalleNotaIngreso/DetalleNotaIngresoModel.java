package com.jonatan.church_mvc.DetalleNotaIngreso;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jonatan.church_mvc.Actividad.ActividadDato;
import com.jonatan.church_mvc.Utils.Template;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DetalleNotaIngresoModel extends Template<DetalleNotaIngresoDato> {
    private String id;
    private String nota_ingreso_id, ingreso_id, monto;
    private SQLiteDatabase db;
    private static final String TABLE_NAME = "DetalleIngreso";
    private static final String KEY_ID = "id";
    private static final String KEY_NOTA_INGRESO_ID = "nota_ingreso_id";
    private static final String KEY_INGRESO_ID = "ingreso_id";
    private static final String KEY_MONTO = "monto";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    public DetalleNotaIngresoModel(SQLiteDatabase db) {
        this.id = "";
        this.nota_ingreso_id = "";
        this.ingreso_id = "";
        this.monto = "";
        this.db = db;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    public void setAttributesData(DetalleNotaIngresoDato data) {
        id = data.id;
        nota_ingreso_id = data.nota_ingreso_id;
        ingreso_id = data.ingreso_id;
        monto = data.monto;
    }
    public DetalleNotaIngresoDato insert() {
        try {
            calendar = Calendar.getInstance();
            String fecha = dateFormat.format(calendar.getTime());

            ContentValues values = new ContentValues();
            values.put(KEY_NOTA_INGRESO_ID, nota_ingreso_id);
            values.put(KEY_INGRESO_ID, ingreso_id);
            values.put(KEY_MONTO, monto);
            values.put(KEY_CREATED_AT, fecha);
            values.put(KEY_UPDATED_AT, fecha);
            if (!id.isEmpty()) {
                values.put(KEY_ID, id);
            }

            long affectedRows = !id.isEmpty() ?
                    db.update(TABLE_NAME, values, "id=" + id, null) :
                    db.insert(TABLE_NAME, null, values);
            if (affectedRows == 0)
                return null;
        }catch (SQLException e){
            e.getMessage();
        }
        DetalleNotaIngresoDato dto = !id.isEmpty() ?
                findRecordInDatabase("id", id) :
                findRecordInDatabase("nota_ingreso_id", nota_ingreso_id);
        return dto;
    }
    public DetalleNotaIngresoDato findRecordInDatabase(String columnName, String value) {
        DetalleNotaIngresoDato dto = null;
        String sql = String.format("select * from "+TABLE_NAME+" where %s='%s';", columnName, value);
        Cursor row = db.rawQuery(sql, null);
        if (row.moveToFirst()) {
            dto = new DetalleNotaIngresoDato(
                    row.getString(0),
                    row.getString(1),
                    row.getString(2),
                    row.getString(3)
            );
        }
        return dto;
    }
    public List<DetalleNotaIngresoDato> findRecordsInDatabase(String columnName, String value) {
        List<DetalleNotaIngresoDato> list = new ArrayList<>();
        String sql = String.format("select * from "+TABLE_NAME+" where %s='%s';", columnName, value);
        Cursor row = db.rawQuery(sql, null);
        while (row.moveToNext()) {
            DetalleNotaIngresoDato dto = new DetalleNotaIngresoDato(
                    row.getString(0),
                    row.getString(1),
                    row.getString(2),
                    row.getString(3)
            );
            list.add(dto);
        }
        return list;
    }

    public boolean deleteRecordInDatabase() {
        return db.delete(TABLE_NAME, "id=" + id, null) > 0;
    }

    public boolean deleteRecordInDatabaseNI(String nota_ingreso_id) {
        return db.delete(TABLE_NAME, "nota_ingreso_id=" + nota_ingreso_id, null) > 0;
    }
    @Override
    public Cursor getCursor() {
        String sql = "SELECT * FROM "+TABLE_NAME+";";
        return db.rawQuery(sql, null);
    }

    @Override
    public DetalleNotaIngresoDato getModel(Cursor cursor) {
        return new DetalleNotaIngresoDato(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3)
        );
    }
}
