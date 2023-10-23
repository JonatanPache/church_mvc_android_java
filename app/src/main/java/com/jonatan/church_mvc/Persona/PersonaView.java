package com.jonatan.church_mvc.Persona;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.material.chip.Chip;
import com.jonatan.church_mvc.Actividad.ActividadDato;
import com.jonatan.church_mvc.Actividad.ActividadModel;
import com.jonatan.church_mvc.DetalleNotaIngreso.DetalleNotaIngresoDato;
import com.jonatan.church_mvc.Ingreso.IngresoDato;
import com.jonatan.church_mvc.Ingreso.IngresoModel;
import com.jonatan.church_mvc.NotaIngreso.NotaIngresoController;
import com.jonatan.church_mvc.NotaIngreso.NotaIngresoDato;
import com.jonatan.church_mvc.NotaIngreso.NotaIngresoModel;
import com.jonatan.church_mvc.R;
import com.jonatan.church_mvc.RelacionFamiliar.RelacionFamiliarDato;
import com.jonatan.church_mvc.TipoRelacion.TipoRelacionDato;
import com.jonatan.church_mvc.TipoRelacion.TipoRelacionModel;
import com.jonatan.church_mvc.Utils.DbHelper;

import java.util.ArrayList;
import java.util.List;

public class PersonaView extends AppCompatActivity {
    EditText txtNombre, txtTelefono, txtCi;
    Spinner spinnerInvitado, spinnerTipoRelacion, spinnerPersona;
    public Button btnCrear, btnGuardar, btnEliminar, btnAgregarFila, btnUp;
    TableLayout tableLayout;
    public TableRow rowTemplate;
    public ListView personaListView;
    private ArrayAdapter<PersonaDato> personaDatoArrayAdapter;
    private ArrayAdapter<PersonaDato> adapter_invitado;
    private ArrayAdapter<PersonaDato> adapter_persona;
    private ArrayAdapter<TipoRelacionDato> adapter_tipo_relacion;
    public Chip chipRol;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_persona);

        txtCi = findViewById(R.id.persona_ci);
        txtNombre = findViewById(R.id.persona_nombre);
        txtTelefono = findViewById(R.id.persona_telefono);

        spinnerPersona = findViewById(R.id.spinnerPersona);
        spinnerInvitado = findViewById(R.id.spinnerInvitado);
        spinnerTipoRelacion = findViewById(R.id.spinnerTipoRelacion);
        chipRol = (Chip) findViewById(R.id.rol);

        tableLayout = findViewById(R.id.tablaRelacionFamiliar);
        rowTemplate = findViewById(R.id.row_template);


        btnCrear = (Button) findViewById(R.id.btnCrear);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        btnEliminar = (Button) findViewById(R.id.btnEliminar);
        btnAgregarFila = findViewById(R.id.btnAgregarFila);
        btnUp = findViewById(R.id.btnUp);

        personaListView = (ListView) findViewById(R.id.personas_list_view);
        personaDatoArrayAdapter = new ArrayAdapter<PersonaDato>(this, R.layout.list_item);
        personaListView.setAdapter(personaDatoArrayAdapter);

        adapter_invitado = new ArrayAdapter<PersonaDato>(this, android.R.layout.simple_spinner_item);
        adapter_invitado.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerInvitado.setPrompt("Quien te invito?");
        spinnerInvitado.setAdapter(adapter_invitado);  // Configura el adaptador para el Spinner

        adapter_persona = new ArrayAdapter<PersonaDato>(this, android.R.layout.simple_spinner_item);
        adapter_persona.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPersona.setPrompt("Selecciona Pariente");
        spinnerPersona.setAdapter(adapter_persona);  // Configura el adaptador para el Spinner

        adapter_tipo_relacion = new ArrayAdapter<TipoRelacionDato>(this, android.R.layout.simple_spinner_item);
        adapter_tipo_relacion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipoRelacion.setPrompt("Seleccion Tipo Relacion");
        spinnerTipoRelacion.setAdapter(adapter_tipo_relacion);  // Configura el adaptador para el Spinner

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        PersonaModel personaModel = new PersonaModel(db);
        TipoRelacionModel tipoRelacionModel = new TipoRelacionModel(db);
        PersonaController personaController = new PersonaController(this,
                personaModel, tipoRelacionModel);
    }

    public PersonaDato readFormData() {
        PersonaDato dato = new PersonaDato(
                txtCi.getText().toString(),
                txtNombre.getText().toString(),
                txtTelefono.getText().toString(),
                chipRol.isChecked() ? "MIEMBRO" :"VISITANTE",
                spinnerInvitado.getSelectedItem() !=null
                        ?((PersonaDato) spinnerInvitado.getSelectedItem()).ci!="-1"
                            ? ((PersonaDato) spinnerInvitado.getSelectedItem()).ci
                            :null
                        :null,
                getRelacionFamiliar()
        );
        return dato;
    }

    public List<RelacionFamiliarDato> getRelacionFamiliar(){
        List<RelacionFamiliarDato> relacionFamiliarDatoList = new ArrayList<>();
        int rowCount = tableLayout.getChildCount();
        for (int i = 1; i < rowCount; i++) {
            View rowView = tableLayout.getChildAt(i);
            if (rowView instanceof TableRow) {
                TableRow row = (TableRow) rowView;
                TextView tipoRelactionText = (TextView) row.getChildAt(1);
                TextView parienteText = (TextView) row.getChildAt(2);
                String tipo_relacion_id = tipoRelactionText.getTag().toString();
                String pariente_id = parienteText.getTag().toString();
                RelacionFamiliarDato relacionFamiliarDato = new RelacionFamiliarDato(txtCi.getText().toString(),pariente_id.toString(),
                        tipo_relacion_id.toString());
                relacionFamiliarDatoList.add(relacionFamiliarDato);
            }
        }
        return relacionFamiliarDatoList;
    }

    public void cleanFormData() {
        txtCi.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
    }

    public void setRowsDataList(List<PersonaDato> list) {
        personaDatoArrayAdapter.clear();
        for (int i = 0; i < list.size(); i++) {
            personaDatoArrayAdapter.add(list.get(i));
        }
    }

    public void setFormData(PersonaDato dto) {
        if (dto == null) return;
        txtCi.setText(dto.ci);
        txtNombre.setText(dto.nombre);
        txtTelefono.setText(dto.telefono);
        if (dto.rol.compareTo("MIEMBRO") == 0) {
            txtTelefono.setVisibility(View.VISIBLE);
        } else {
            txtTelefono.setVisibility(View.INVISIBLE);
        }
    }

    public void setDataListSpinnerPersonas(List<PersonaDato> list) {
        adapter_persona.clear();
        for (int i = 0; i < list.size(); i++) {
            adapter_persona.add(list.get(i));
        }
    }
    public void setDataListSpinnerTipoRelacion(List<TipoRelacionDato> list) {
        adapter_tipo_relacion.clear();
        for (int i = 0; i < list.size(); i++) {
            adapter_tipo_relacion.add(list.get(i));
        }
    }

    public void setDataListSpinnerInvitado(List<PersonaDato> list) {
        adapter_invitado.clear();
        for (int i = 0; i < list.size(); i++) {
            adapter_invitado.add(list.get(i));
        }
    }
}