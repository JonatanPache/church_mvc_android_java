package com.jonatan.church_mvc.NotaIngreso;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.jonatan.church_mvc.Actividad.ActividadDato;
import com.jonatan.church_mvc.Actividad.ActividadModel;
import com.jonatan.church_mvc.DetalleNotaIngreso.DetalleNotaIngresoDato;
import com.jonatan.church_mvc.Ingreso.IngresoController;
import com.jonatan.church_mvc.Ingreso.IngresoDato;
import com.jonatan.church_mvc.Ingreso.IngresoModel;
import com.jonatan.church_mvc.R;
import com.jonatan.church_mvc.Utils.DbHelper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotaIngresoView extends AppCompatActivity {
    EditText txtTitulo, txtMontoTotal;
    TextView txtFecha, txtId;
    Spinner spinnerActividad, spinnerIngreso;
    ImageButton btnAgregarFila;
    public Button btnCrear, btnGuardar, btnEliminar;
    TableLayout tableLayout;
    public TableRow rowTemplate;
    public ListView notaIngresoListView;
    private ArrayAdapter<NotaIngresoDato> notaIngresoDatoArrayAdapter;
    public ArrayAdapter<ActividadDato> adapter_actividad;
    private ArrayAdapter<IngresoDato> adapter_ingreso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota_ingreso);

        txtId = (TextView) findViewById(R.id.nota_ingreso_id);
        txtTitulo = findViewById(R.id.txtTitulo);
        txtFecha = findViewById(R.id.txtFecha);
        txtMontoTotal = findViewById(R.id.txtMontoTotal);

        spinnerActividad = findViewById(R.id.spinnerActividad);
        spinnerIngreso = findViewById(R.id.spinnerIngreso);

        tableLayout = findViewById(R.id.tablaIngresos);
        rowTemplate = findViewById(R.id.row_template);


        btnCrear = (Button) findViewById(R.id.btnCrear);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
        btnAgregarFila = findViewById(R.id.btnAgregarFila);

        notaIngresoListView = (ListView) findViewById(R.id.nota_ingreso_list_view);
        notaIngresoDatoArrayAdapter = new ArrayAdapter<NotaIngresoDato>(this, R.layout.list_item);
        notaIngresoListView.setAdapter(notaIngresoDatoArrayAdapter);

        adapter_actividad = new ArrayAdapter<ActividadDato>(this, android.R.layout.simple_spinner_item);
        adapter_actividad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerActividad.setAdapter(adapter_actividad);  // Configura el adaptador para el Spinner

        adapter_ingreso = new ArrayAdapter<IngresoDato>(this, android.R.layout.simple_spinner_item);
        adapter_ingreso.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIngreso.setAdapter(adapter_ingreso);  // Configura el adaptador para el Spinner

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        NotaIngresoModel notaIngresoModel = new NotaIngresoModel(db);
        ActividadModel actividadModel = new ActividadModel(db);
        IngresoModel ingresoModel = new IngresoModel(db);
        NotaIngresoController notaIngresoController = new NotaIngresoController(this,
                notaIngresoModel, actividadModel, ingresoModel);
    }

    public NotaIngresoDato readFormData() {
        NotaIngresoDato dato = new NotaIngresoDato(
                txtId.getText().toString(),
                ((ActividadDato) spinnerActividad.getSelectedItem()).id,
                txtTitulo.getText().toString(),
                txtMontoTotal.getText().toString(),
                txtFecha.getText().toString(),
                getDetalleNotaIngresoDato()
        );
        return dato;
    }

    public List<DetalleNotaIngresoDato> getDetalleNotaIngresoDato(){
        List<DetalleNotaIngresoDato> detalleNotaIngresoDatoList = new ArrayList<>();
        int rowCount = tableLayout.getChildCount();
        for (int i = 1; i < rowCount; i++) {
            View rowView = tableLayout.getChildAt(i);
            if (rowView instanceof TableRow) {
                TableRow row = (TableRow) rowView;
                TextView ingresoTextView = (TextView) row.getChildAt(1);
                EditText montoEditText = (EditText) row.getChildAt(2);
                String ingreso_id = ingresoTextView.getTag().toString();
                String monto = montoEditText.getText().toString();
                DetalleNotaIngresoDato detalleNotaIngresoDato = new DetalleNotaIngresoDato("",null ,ingreso_id,monto);
                detalleNotaIngresoDatoList.add(detalleNotaIngresoDato);
            }
        }
        return detalleNotaIngresoDatoList;
    }

    public void setFormData(NotaIngresoDato dto) {
        if (dto == null) return;
        txtId.setText(dto.id);
        txtTitulo.setText(dto.titulo);
        txtMontoTotal.setText(dto.total);
        txtFecha.setText(dto.fecha);
    }

    public void setRowsDataList(List<NotaIngresoDato> list) {
        notaIngresoDatoArrayAdapter.clear();
        for (int i = 0; i < list.size(); i++) {
            notaIngresoDatoArrayAdapter.add(list.get(i));
        }
    }
    public void setDataListSpinnerActividad(List<ActividadDato> list) {
        adapter_actividad.clear();
        for (int i = 0; i < list.size(); i++) {
            adapter_actividad.add(list.get(i));
        }
    }
    public void setDataListSpinnerIngreso(List<IngresoDato> list) {
        adapter_ingreso.clear();
        for (int i = 0; i < list.size(); i++) {
            adapter_ingreso.add(list.get(i));
        }
    }

    public void cleanFormData() {
        txtId.setText("");
        txtTitulo.setText("");
        txtMontoTotal.setText("");
    }

}