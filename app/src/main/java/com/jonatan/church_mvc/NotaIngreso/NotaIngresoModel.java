package com.jonatan.church_mvc.NotaIngreso;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.jonatan.church_mvc.Actividad.ActividadDato;
import com.jonatan.church_mvc.DetalleNotaIngreso.DetalleNotaIngresoDato;
import com.jonatan.church_mvc.DetalleNotaIngreso.DetalleNotaIngresoModel;
import com.jonatan.church_mvc.Utils.Template;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotaIngresoModel extends Template<NotaIngresoDato> {
    private String id;
    private String actividad_id, titulo, total;
    private List<DetalleNotaIngresoDato> detalleNotaIngresoModelList;
    private DetalleNotaIngresoModel detalleNotaIngresoModel;
    private SQLiteDatabase db;
    private static final String TABLE_NAME = "NotaIngreso";
    private static final String KEY_ID = "id";
    private static final String KEY_NOTA_INGRESO_ID = "nota_ingreso_id";
    private static final String KEY_ACTIVIDAD_ID = "actividad_id";
    private static final String KEY_TITULO = "titulo";
    private static final String KEY_TOTAL = "total";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_UPDATED_AT = "updated_at";
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    public NotaIngresoModel(SQLiteDatabase db) {
        this.id = "";
        this.actividad_id = "";
        this.titulo = "";
        this.total = "";
        this.db = db;
        detalleNotaIngresoModel = new DetalleNotaIngresoModel(db);
        detalleNotaIngresoModelList = new ArrayList<>();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }
    public void setAttributesData(NotaIngresoDato data) {
        id = data.id;
        actividad_id = data.actividad_id;
        titulo = data.titulo;
        total = data.total;
        detalleNotaIngresoModelList = data.listDetalleNotaIngreso;
    }
    public NotaIngresoDato insert() {
        NotaIngresoDato dto = null;
        try {
            calendar = Calendar.getInstance();
            String fecha = dateFormat.format(calendar.getTime());

            ContentValues values = new ContentValues();
            values.put(KEY_ACTIVIDAD_ID, actividad_id);
            values.put(KEY_TITULO, titulo);
            values.put(KEY_TOTAL, total);
            values.put(KEY_CREATED_AT, fecha);
            values.put(KEY_UPDATED_AT, fecha);

            if (!id.isEmpty()) {
                values.put(KEY_ID, id);
            }

            long affectedRows = !id.isEmpty()
                    ? update(values)
                :db.insert(TABLE_NAME, null, values);

            if (affectedRows == 0) return null;

            for (DetalleNotaIngresoDato detalleNotaIngresoDato: detalleNotaIngresoModelList){
                detalleNotaIngresoDato.nota_ingreso_id = String.valueOf(affectedRows);
                detalleNotaIngresoModel.setAttributesData(detalleNotaIngresoDato);
                detalleNotaIngresoModel.insert();
            }
            dto = findRecordInDatabase(KEY_ID, String.valueOf(affectedRows));
        }catch (SQLException e){
            e.getMessage();
        }
        return dto;
    }
    public long update(ContentValues values){
        detalleNotaIngresoModel.deleteRecordInDatabaseNI(id);
        return db.update(TABLE_NAME, values, "id=" + id, null);
    }
    public NotaIngresoDato findRecordInDatabase(String columnName, String value) {
        NotaIngresoDato dto = null;
        String sql = String.format("select * from "+TABLE_NAME+" where %s='%s';", columnName, value);
        Cursor row = db.rawQuery(sql, null);
        if (row.moveToFirst()) {
            dto = new NotaIngresoDato(
                    row.getString(0),
                    row.getString(1),
                    row.getString(2),
                    row.getString(3),
                    row.getString(4),
                    detalleNotaIngresoModel.findRecordsInDatabase(KEY_NOTA_INGRESO_ID, value
                    )
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
    public NotaIngresoDato getModel(Cursor cursor) {
        return new NotaIngresoDato(
                cursor.getString(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                detalleNotaIngresoModel.findRecordsInDatabase("nota_ingreso_id", cursor.getString(0)
                )
        );
    }
}
