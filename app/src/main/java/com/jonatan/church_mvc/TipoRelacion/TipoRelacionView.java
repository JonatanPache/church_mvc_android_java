package com.jonatan.church_mvc.TipoRelacion;

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

public class TipoRelacionView extends AppCompatActivity {
    private TextView txtId;
    private EditText txtNombre;
    public Button btnCrear, btnGuardar, btnEliminar;
    public ListView tipoRelacionListView;
    private ArrayAdapter<TipoRelacionDato> tipoRelacionDatoArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_relacion);

        txtId = (TextView) findViewById(R.id.tipo_relacion_id);
        txtNombre = (EditText) findViewById(R.id.tipo_relacion_nombre);

        btnCrear = (Button) findViewById(R.id.btnCrear);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);

        tipoRelacionListView = (ListView) findViewById(R.id.tipo_relacion_list_view);
        tipoRelacionDatoArrayAdapter = new ArrayAdapter<TipoRelacionDato>(this, R.layout.list_item);
        tipoRelacionListView.setAdapter(tipoRelacionDatoArrayAdapter);

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        TipoRelacionModel tipoRelacionModel = new TipoRelacionModel(db);
        TipoRelacionController tipoRelacionController = new TipoRelacionController(this, tipoRelacionModel);
    }


    public TipoRelacionDato readFormData() {
        TipoRelacionDato dato = new TipoRelacionDato(
                txtId.getText().toString(),
                txtNombre.getText().toString()
        );
        return dato;
    }

    public void setFormData(TipoRelacionDato dto) {
        if (dto == null) return;
        txtId.setText(dto.id);
        txtNombre.setText(dto.nombre);
    }

    public void setRowsDataList(List<TipoRelacionDato> list) {
        tipoRelacionDatoArrayAdapter.clear();
        Log.d("rows", String.valueOf(list.size()));
        for (int i = 0; i < list.size(); i++) {
            tipoRelacionDatoArrayAdapter.add(list.get(i));
        }
    }

    public void cleanFormData() {
        txtId.setText("");
        txtNombre.setText("");
    }
}