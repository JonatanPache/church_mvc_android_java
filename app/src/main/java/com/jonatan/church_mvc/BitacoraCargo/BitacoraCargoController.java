package com.jonatan.church_mvc.BitacoraCargo;

import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.jonatan.church_mvc.Actividad.ActividadDato;
import com.jonatan.church_mvc.Cargo.CargoDato;
import com.jonatan.church_mvc.Cargo.CargoModel;
import com.jonatan.church_mvc.DetalleNotaIngreso.DetalleNotaIngresoDato;
import com.jonatan.church_mvc.Ingreso.IngresoDato;
import com.jonatan.church_mvc.NotaIngreso.NotaIngresoDato;
import com.jonatan.church_mvc.Persona.PersonaDato;
import com.jonatan.church_mvc.Persona.PersonaModel;
import com.jonatan.church_mvc.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class BitacoraCargoController {

    private BitacoraCargoView bitacoraCargoView;
    private BitacoraCargoModel bitacoraCargoModel;
    private PersonaModel personaModel;
    private CargoModel cargoModel;
    public BitacoraCargoController(BitacoraCargoView bitacoraCargoView, BitacoraCargoModel bitacoraCargoModel,
                                   PersonaModel personaModel, CargoModel cargoModel){
        this.bitacoraCargoView = bitacoraCargoView;
        this.bitacoraCargoModel = bitacoraCargoModel;
        this.personaModel = personaModel;
        this.cargoModel = cargoModel;
        initListener();
        this.bitacoraCargoView.setRowsDataList(bitacoraCargoModel.rowsList());
        this.bitacoraCargoView.setDataListSpinnerPersona(personaModel.findRecordsInDatabase("rol","MIEMBRO"));
        this.bitacoraCargoView.setDataListSpinnerCargo(cargoModel.rowsList());
    }
    public void initListener() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//HH:mm:ss
//        bitacoraCargoView.txtFecha.setText(dateFormat.format(Calendar.getInstance().getTime()));

        bitacoraCargoView.chipEstado.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    bitacoraCargoView.chipEstado.setText("Estado: Habilitado");
                    bitacoraCargoView.chipEstado.setBackgroundColor(Color.parseColor("#63EF69"));
                }else{
                    bitacoraCargoView.chipEstado.setText("Estado: Deshabilitado");
                    bitacoraCargoView.chipEstado.setBackgroundColor(Color.parseColor("#E81010"));
                }
            }
        });

        bitacoraCargoView.btnGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    store();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        bitacoraCargoView.btnEliminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    delete();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        bitacoraCargoView.btnCrear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bitacoraCargoView.cleanFormData();
            }
        });

        bitacoraCargoView.bitacoraCargoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = ((BitacoraCargoDato) bitacoraCargoView.bitacoraCargoListView.getItemAtPosition(i)).id;
                BitacoraCargoDato selected = bitacoraCargoModel.findRecordInDatabase("id", id);
                bitacoraCargoView.setFormData(selected);
                PersonaDato item = personaModel.findRecordInDatabase("ci",selected.persona_ci);
                if (item!=null) {
                    int index = obtenerIndexPersona(item,bitacoraCargoView.personaDatoArrayAdapter);
                    bitacoraCargoView.spinnerPersona.setSelection(index);
                }
                CargoDato item2 = cargoModel.findRecordInDatabase("id",selected.cargo_id);
                if (item2!=null) {
                    int index = obtenerIndexCargo(item2,bitacoraCargoView.cargoDatoArrayAdapter);
                    bitacoraCargoView.spinnerCargo.setSelection(index);
                }
            }
        });
    }

    private int obtenerIndexPersona(PersonaDato personaDato, ArrayAdapter<PersonaDato> arrayAdapter){
        for (int i=0; i<arrayAdapter.getCount();i++){
            if (personaDato.ci.compareTo(arrayAdapter.getItem(i).ci) == 0 ){
                return i;
            }
        }
        return -1;
    }
    private int obtenerIndexCargo(CargoDato cargoDato, ArrayAdapter<CargoDato> arrayAdapter){
        for (int i=0; i<arrayAdapter.getCount();i++){
            if (cargoDato.id.compareTo(arrayAdapter.getItem(i).id) == 0 ){
                return i;
            }
        }
        return -1;
    }
    private void store() throws ParseException {
        bitacoraCargoModel.setAttributesData(bitacoraCargoView.readFormData());
        BitacoraCargoDato dto = bitacoraCargoModel.saveInDatabase();
        if (dto == null) return;
        bitacoraCargoView.setFormData(dto);
//        ActividadDato item = personaModel.findRecordInDatabase("id",dto.actividad_id);
//        if (item!=null) notaIngresoView.spinnerActividad.setSelection(notaIngresoView.adapter_actividad.getPosition(item));
        bitacoraCargoView.setRowsDataList(bitacoraCargoModel.rowsList());
    }
    private void delete() throws ParseException {
        bitacoraCargoModel.setAttributesData(bitacoraCargoView.readFormData());
        bitacoraCargoModel.deleteRecordInDatabase();
        bitacoraCargoView.cleanFormData();
        bitacoraCargoView.setRowsDataList(bitacoraCargoModel.rowsList());
    }
}
