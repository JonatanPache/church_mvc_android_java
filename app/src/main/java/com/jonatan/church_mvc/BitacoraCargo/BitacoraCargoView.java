package com.jonatan.church_mvc.BitacoraCargo;

import androidx.appcompat.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.material.chip.Chip;
import com.jonatan.church_mvc.Cargo.CargoDato;
import com.jonatan.church_mvc.Cargo.CargoModel;
import com.jonatan.church_mvc.Persona.PersonaDato;
import com.jonatan.church_mvc.Persona.PersonaModel;
import com.jonatan.church_mvc.R;
import com.jonatan.church_mvc.Utils.DbHelper;
import java.util.List;

public class BitacoraCargoView extends AppCompatActivity {
    TextView txtFechaInicio, txtFechaFinal, txtId;
    Spinner spinnerPersona, spinnerCargo;
    public Chip chipEstado;
    public Button btnCrear, btnGuardar, btnEliminar;
    public ListView bitacoraCargoListView;
    private ArrayAdapter<BitacoraCargoDato> bitacoraCargoDatoArrayAdapter;
    public ArrayAdapter<PersonaDato> personaDatoArrayAdapter;
    public ArrayAdapter<CargoDato> cargoDatoArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bitacora_cargo);

        txtId = (TextView) findViewById(R.id.bitacora_id);
        txtFechaInicio = findViewById(R.id.txtFechaInicio);
        txtFechaFinal = findViewById(R.id.txtFechaFinal);
        chipEstado = (Chip) findViewById(R.id.chipEstado);

        spinnerPersona = findViewById(R.id.spinnerPersona);
        spinnerCargo = findViewById(R.id.spinnerCargo);

        btnCrear = (Button) findViewById(R.id.btnCrear);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);

        bitacoraCargoListView = (ListView) findViewById(R.id.bitacora_list_view);
        bitacoraCargoDatoArrayAdapter = new ArrayAdapter<BitacoraCargoDato>(this, R.layout.list_item);
        bitacoraCargoListView.setAdapter(bitacoraCargoDatoArrayAdapter);

        personaDatoArrayAdapter = new ArrayAdapter<PersonaDato>(this, android.R.layout.simple_spinner_item);
        personaDatoArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPersona.setAdapter(personaDatoArrayAdapter);  // Configura el adaptador para el Spinner

        cargoDatoArrayAdapter = new ArrayAdapter<CargoDato>(this, android.R.layout.simple_spinner_item);
        cargoDatoArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCargo.setAdapter(cargoDatoArrayAdapter);  // Configura el adaptador para el Spinner

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        BitacoraCargoModel bitacoraCargoModel = new BitacoraCargoModel(db);
        PersonaModel personaModel = new PersonaModel(db);
        CargoModel cargoModel = new CargoModel(db);
        BitacoraCargoController bitacoraCargoController = new BitacoraCargoController(this,
                bitacoraCargoModel, personaModel, cargoModel);
    }

    public BitacoraCargoDato readFormData() {
        BitacoraCargoDato dato = new BitacoraCargoDato(
                txtId.getText().toString(),
                ((PersonaDato) spinnerPersona.getSelectedItem()).ci,
                ((CargoDato) spinnerCargo.getSelectedItem()).id,
                txtFechaInicio.getText().toString(),
                txtFechaFinal.getText().toString(),
                chipEstado.isChecked()?"1":"0"
        );
        return dato;
    }

    public void setFormData(BitacoraCargoDato dto) {
        if (dto == null) return;
        txtId.setText(dto.id);
        txtFechaInicio.setText(dto.fecha_inicio);
        txtFechaFinal.setText(dto.fecha_final);
    }

    public void setRowsDataList(List<BitacoraCargoDato> list) {
        bitacoraCargoDatoArrayAdapter.clear();
        for (int i = 0; i < list.size(); i++) {
            bitacoraCargoDatoArrayAdapter.add(list.get(i));
        }
    }
    public void setDataListSpinnerPersona(List<PersonaDato> list) {
        personaDatoArrayAdapter.clear();
        for (int i = 0; i < list.size(); i++) {
            personaDatoArrayAdapter.add(list.get(i));
        }
    }
    public void setDataListSpinnerCargo(List<CargoDato> list) {
        cargoDatoArrayAdapter.clear();
        for (int i = 0; i < list.size(); i++) {
            cargoDatoArrayAdapter.add(list.get(i));
        }
    }

    public void cleanFormData() {
        txtId.setText("");
        txtFechaInicio.setText("");
        txtFechaFinal.setText("");
//        chipEstado.setCheckedIconEnabledResource().
    }
}