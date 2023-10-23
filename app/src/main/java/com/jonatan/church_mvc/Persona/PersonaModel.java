package com.jonatan.church_mvc.Persona;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jonatan.church_mvc.DetalleNotaIngreso.DetalleNotaIngresoDato;
import com.jonatan.church_mvc.RelacionFamiliar.RelacionFamiliarDato;
import com.jonatan.church_mvc.RelacionFamiliar.RelacionFamiliarModel;
import com.jonatan.church_mvc.Utils.Template;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class PersonaModel extends Template<PersonaDato> {
    private String ci;
    private String nombre , telefono, rol, invita_id;
    private List<RelacionFamiliarDato> relacionFamiliarDatoList;
    private RelacionFamiliarModel relacionFamiliarModel;
    private SQLiteDatabase db;
    private static final String TABLE_NAME = "Persona";
    private static final String KEY_CI = "ci";
    private static final String KEY_NOMBRE = "nombre";
    private static final String KEY_TELEFONO = "telefono";
    private static final String KEY_ROL = "rol";
    private static final String KEY_INVITA_ID = "invita_id";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    public PersonaModel(SQLiteDatabase db) {
        this.ci = "";
        this.nombre = "";
        this.telefono = "";
        this.rol = "";
        this.invita_id = "";
        this.db = db;
        relacionFamiliarModel = new RelacionFamiliarModel(db);
        relacionFamiliarDatoList = new ArrayList<>();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public void setAttributesData(PersonaDato data) {
        ci = data.ci;
        nombre = data.nombre;
        telefono = data.telefono;
        rol = data.rol;
        invita_id = data.invita_id;
        relacionFamiliarDatoList = data.relacionFamiliarDatoList;
    }

    public PersonaDato insert() {
        try {
            calendar = Calendar.getInstance();
            String fecha = dateFormat.format(calendar.getTime());

            ContentValues values = new ContentValues();
            values.put(KEY_CI, ci);
            values.put(KEY_NOMBRE, nombre);
            values.put(KEY_TELEFONO, telefono);
            values.put(KEY_ROL, rol);
            if (invita_id!=null) values.put(KEY_INVITA_ID, invita_id);  //no necesario
            values.put(KEY_CREATED_AT, fecha);
            values.put(KEY_UPDATED_AT, fecha);

            if (!ci.isEmpty()) {
                values.put(KEY_CI, ci);
            }

            long affectedRows = db.insert(TABLE_NAME, null, values);
            Log.d("asd",String.valueOf(affectedRows));
            if (affectedRows == 0) return null;
            if(relacionFamiliarDatoList!=null && !relacionFamiliarDatoList.isEmpty()){
                for (RelacionFamiliarDato relacionFamiliarDato: relacionFamiliarDatoList){
                    relacionFamiliarModel.setAttributesData(relacionFamiliarDato);
                    relacionFamiliarModel.insert();
                }
            }
        }catch (SQLException e){
            e.getMessage();
        }
        PersonaDato dto = !ci.isEmpty() ?
                findRecordInDatabase(KEY_CI, ci) :
                findRecordInDatabase(KEY_NOMBRE, nombre);
        return dto;
    }

    public PersonaDato update() {
        try {
            calendar = Calendar.getInstance();
            String fecha = dateFormat.format(calendar.getTime());

            ContentValues values = new ContentValues();
            values.put(KEY_CI, ci);
            values.put(KEY_NOMBRE, nombre);
            values.put(KEY_TELEFONO, telefono);
            values.put(KEY_ROL, rol);
            if (invita_id!=null) values.put(KEY_INVITA_ID, invita_id);
            values.put(KEY_UPDATED_AT, fecha);
            int code = db.update(TABLE_NAME, values, KEY_CI+"=" + ci, null);
            if (code == 0) return null;
            relacionFamiliarModel.deleteRecordInDatabaseByPersona(ci);
            if(relacionFamiliarDatoList!=null && !relacionFamiliarDatoList.isEmpty()){
                for (RelacionFamiliarDato relacionFamiliarDato: relacionFamiliarDatoList){
                    relacionFamiliarModel.setAttributesData(relacionFamiliarDato);
                    relacionFamiliarModel.insert();
                }
            }
        }catch (SQLException e){
            e.getMessage();
        }
        PersonaDato dto = !ci.isEmpty() ?
                findRecordInDatabase(KEY_CI, ci) :
                findRecordInDatabase(KEY_NOMBRE, nombre);
        return dto;
    }

    public PersonaDato findRecordInDatabase(String columnName, String value) {
        PersonaDato dto = null;
        String sql = String.format("select * from "+TABLE_NAME+" where %s='%s';", columnName, value);
        Cursor row = db.rawQuery(sql, null);
        if (row.moveToFirst()) {
            dto = new PersonaDato(
                    row.getString(0),
                    row.getString(1),
                    row.getString(2),
                    row.getString(3),
                    row.getString(4),
                    relacionFamiliarModel.findRecordsInDatabase("persona_id",value)
            );
        }
        return dto;
    }
    public List<PersonaDato> findRecordsInDatabase(String columnName, String value) {
        List<PersonaDato> listdto = new ArrayList<>();
        String sql = String.format("select * from "+TABLE_NAME+" where %s='%s';", columnName, value);
        Cursor row = db.rawQuery(sql, null);
        while (row.moveToNext()) {
            PersonaDato dto = new PersonaDato(
                    row.getString(0),
                    row.getString(1),
                    row.getString(2),
                    row.getString(3),
                    row.getString(4),
                    relacionFamiliarModel.findRecordsInDatabase("persona_id",row.getString(0))
            );
            listdto.add(dto);
        }
        return listdto;
    }

    public boolean deleteRecordInDatabase() {
        return db.delete(TABLE_NAME, KEY_CI+"=" + ci, null) > 0
                ? relacionFamiliarModel.deleteRecordInDatabase()
                : false;
    }
    @Override
    public Cursor getCursor() {
        String sql = "SELECT * FROM "+TABLE_NAME+";";
        return db.rawQuery(sql, null);
    }

    @Override
    public PersonaDato getModel(Cursor cursor) {
        return new PersonaDato(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                relacionFamiliarModel.findRecordsInDatabase("persona_id", cursor.getString(0))
        );
    }
}
