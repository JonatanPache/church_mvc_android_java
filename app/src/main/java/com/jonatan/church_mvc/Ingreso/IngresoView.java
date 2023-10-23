package com.jonatan.church_mvc.Ingreso;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jonatan.church_mvc.R;
import com.jonatan.church_mvc.Utils.DbHelper;

import java.util.List;

public class IngresoView extends AppCompatActivity {
    private TextView txtId;
    private EditText txtDescripcion, txtNombre;
    public Button btnCrear, btnGuardar, btnEliminar;
    public ListView ingresoListView;
    private ArrayAdapter<IngresoDato> ingresoModelArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso);

        txtId = (TextView) findViewById(R.id.ingreso_id);
        txtDescripcion = (EditText) findViewById(R.id.ingreso_descripcion);
        txtNombre = (EditText) findViewById(R.id.ingreso_nombre);

        btnCrear = (Button) findViewById(R.id.btnCrear);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);

        ingresoListView = (ListView) findViewById(R.id.ingreso_list_view);
        ingresoModelArrayAdapter = new ArrayAdapter<IngresoDato>(this, R.layout.list_item);
        ingresoListView.setAdapter(ingresoModelArrayAdapter);

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        IngresoModel ingresoModel = new IngresoModel(db);
        IngresoController ingresoController = new IngresoController(this, ingresoModel);
    }
    public IngresoDato readFormData() {
        IngresoDato dato = new IngresoDato(
                txtId.getText().toString(),
                txtNombre.getText().toString(),
                txtDescripcion.getText().toString()
        );
        return dato;
    }
    public void setFormData(IngresoDato dto) {
        if (dto == null) return;
        txtId.setText(dto.id);
        txtNombre.setText(dto.nombre);
        txtDescripcion.setText(dto.descripcion);
    }
    public void setRowsDataList(List<IngresoDato> list) {
        ingresoModelArrayAdapter.clear();
        for (int i = 0; i < list.size(); i++) {
            ingresoModelArrayAdapter.add(list.get(i));
        }
    }
    public void cleanFormData() {
        txtId.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
    }
}