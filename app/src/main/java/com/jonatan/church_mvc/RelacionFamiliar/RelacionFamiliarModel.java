package com.jonatan.church_mvc.RelacionFamiliar;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.jonatan.church_mvc.DetalleNotaIngreso.DetalleNotaIngresoDato;
import com.jonatan.church_mvc.Utils.Template;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class RelacionFamiliarModel extends Template<RelacionFamiliarDato> {
    private String persona_id;
    private String pariente_id, relacion_id;
    private SQLiteDatabase db;
    private static final String TABLE_NAME = "RelacionFamiliar";
    private static final String KEY_PERSONA_ID = "persona_id";
    private static final String KEY_PARIENTE_ID = "pariente_id";
    private static final String KEY_RELACION_ID = "relacion_id";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    public RelacionFamiliarModel(SQLiteDatabase db) {
        this.persona_id = "";
        this.pariente_id = "";
        this.relacion_id = "";
        this.db = db;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    public void setAttributesData(RelacionFamiliarDato data) {
        persona_id = data.persona_id;
        pariente_id = data.pariente_id;
        relacion_id = data.relacion_id;
    }
    public RelacionFamiliarDato insert() {
        RelacionFamiliarDato dto = null;
        try {
            calendar = Calendar.getInstance();
            String fecha = dateFormat.format(calendar.getTime());

            ContentValues values = new ContentValues();
            values.put(KEY_PERSONA_ID, persona_id);
            values.put(KEY_PARIENTE_ID, pariente_id);
            values.put(KEY_RELACION_ID, relacion_id);
            values.put(KEY_CREATED_AT, fecha);
            values.put(KEY_UPDATED_AT, fecha);
//            if (!id.isEmpty()) {
//                values.put(KEY_PERSONA_ID, id);
//            }

            long affectedRows = db.insert(TABLE_NAME, null, values);
            if (affectedRows == 0) return null;
            dto = findRecordInDatabase(KEY_PERSONA_ID, String.valueOf(affectedRows));
        }catch (SQLException e){
            e.getMessage();
        }
        return dto;
    }

    public RelacionFamiliarDato findRecordInDatabase(String columnName, String value) {
        RelacionFamiliarDato dto = null;
        String sql = String.format("select * from "+TABLE_NAME+" where %s='%s';", columnName, value);
        Cursor row = db.rawQuery(sql, null);
        if (row.moveToFirst()) {
            dto = new RelacionFamiliarDato(
                    row.getString(0),
                    row.getString(1),
                    row.getString(2)
            );
        }
        return dto;
    }
    public List<RelacionFamiliarDato> findRecordsInDatabase(String columnName, String value) {
        List<RelacionFamiliarDato> list = new ArrayList<>();
        String sql = String.format("select * from "+TABLE_NAME+" where %s='%s';", columnName, value);
        Cursor row = db.rawQuery(sql, null);
        while (row.moveToNext()) {
            RelacionFamiliarDato dto = new RelacionFamiliarDato(
                    row.getString(0),
                    row.getString(1),
                    row.getString(2)
            );
            list.add(dto);
        }
        return list;
    }

    public boolean deleteRecordInDatabase() {
        return db.delete(TABLE_NAME, "persona_id=" + persona_id, null) > 0;
    }

    public boolean deleteRecordInDatabaseByPersona(String persona_id) {
        return db.delete(TABLE_NAME, "persona_id=" + persona_id, null) > 0;
    }

    @Override
    public Cursor getCursor() {
        String sql = "SELECT * FROM "+TABLE_NAME+";";
        return db.rawQuery(sql, null);
    }

    @Override
    public RelacionFamiliarDato getModel(Cursor cursor) {
        return new RelacionFamiliarDato(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2)
        );
    }
}
