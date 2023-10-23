package com.jonatan.church_mvc.Cargo;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jonatan.church_mvc.Ingreso.IngresoController;
import com.jonatan.church_mvc.Ingreso.IngresoDato;
import com.jonatan.church_mvc.Ingreso.IngresoModel;
import com.jonatan.church_mvc.R;
import com.jonatan.church_mvc.Utils.DbHelper;

import java.util.List;

public class CargoView extends AppCompatActivity {
    private TextView txtId;
    private EditText txtDescripcion, txtNombre;
    public Button btnCrear, btnGuardar, btnEliminar;
    public ListView cargoListView;
    private ArrayAdapter<CargoDato> cargoDatoArrayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cargo);

        txtId = (TextView) findViewById(R.id.cargo_id);
        txtDescripcion = (EditText) findViewById(R.id.cargo_descripcion);
        txtNombre = (EditText) findViewById(R.id.cargo_nombre);

        btnCrear = (Button) findViewById(R.id.btnCrear);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);

        cargoListView = (ListView) findViewById(R.id.cargo_list_view);
        cargoDatoArrayAdapter = new ArrayAdapter<CargoDato>(this, R.layout.list_item);
        cargoListView.setAdapter(cargoDatoArrayAdapter);

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        CargoModel cargoModel = new CargoModel(db);
        CargoController cargoController = new CargoController(this, cargoModel);

    }

    public CargoDato readFormData() {
        CargoDato dato = new CargoDato(
                txtId.getText().toString(),
                txtNombre.getText().toString(),
                txtDescripcion.getText().toString()
        );
        return dato;
    }
    public void setFormData(CargoDato dto) {
        if (dto == null) return;
        txtId.setText(dto.id);
        txtNombre.setText(dto.nombre);
        txtDescripcion.setText(dto.descripcion);
    }
    public void setRowsDataList(List<CargoDato> list) {
        cargoDatoArrayAdapter.clear();
        for (int i = 0; i < list.size(); i++) {
            cargoDatoArrayAdapter.add(list.get(i));
        }
    }
    public void cleanFormData() {
        txtId.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
    }
}