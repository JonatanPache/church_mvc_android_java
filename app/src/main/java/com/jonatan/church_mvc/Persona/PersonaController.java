package com.jonatan.church_mvc.Persona;

import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.jonatan.church_mvc.DetalleNotaIngreso.DetalleNotaIngresoDato;
import com.jonatan.church_mvc.Ingreso.IngresoDato;
import com.jonatan.church_mvc.NotaIngreso.NotaIngresoDato;
import com.jonatan.church_mvc.R;
import com.jonatan.church_mvc.RelacionFamiliar.RelacionFamiliarDato;
import com.jonatan.church_mvc.TipoRelacion.TipoRelacionDato;
import com.jonatan.church_mvc.TipoRelacion.TipoRelacionModel;

import java.util.List;

public class PersonaController {
    private PersonaView personaView;
    private PersonaModel personaModel;
    private TipoRelacionModel tipoRelacionModel;
    public PersonaController(PersonaView personaView, PersonaModel personaModel, TipoRelacionModel tipoRelacionModel){
        this.personaView = personaView;
        this.personaModel = personaModel;
        this.tipoRelacionModel = tipoRelacionModel;
        initListener();
        this.personaView.setRowsDataList(personaModel.rowsList());
        setDataSpinnerInvitado();
        setDataSpinnerPersona();
        setDataSpinnerTipoRelacion();
    }
    public void setDataSpinnerInvitado(){
        List<PersonaDato> list = personaModel.rowsList();
        list.add(0,new PersonaDato("-1", "", "","","",null));
        this.personaView.setDataListSpinnerPersonas(list);   //borra y agrega los nuevos
    }
    public void setDataSpinnerPersona(){
        List<PersonaDato> list2 = personaModel.rowsList();
        list2.add(0,new PersonaDato("-1", "", "","","",null));
        this.personaView.setDataListSpinnerInvitado(list2);   //borra y agrega los nuevos
    }
    public void setDataSpinnerTipoRelacion(){
        List<TipoRelacionDato> list3 = tipoRelacionModel.rowsList();
        list3.add(0,new TipoRelacionDato("-1"," "));
        this.personaView.setDataListSpinnerTipoRelacion(list3);
    }
    public void initListener() {
        personaView.chipRol.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    personaView.txtTelefono.setVisibility(View.VISIBLE);
                    personaView.txtTelefono.setEnabled(true);
                }else{
                    personaView.txtTelefono.setVisibility(View.INVISIBLE);
                    personaView.txtTelefono.setEnabled(false);
                }
            }
        });

        personaView.btnGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                store();
            }
        });

        personaView.btnUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                up();
            }
        });

        personaView.btnEliminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                delete();
            }
        });

        personaView.btnCrear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clearTable();
                personaView.cleanFormData();
            }
        });

        personaView.personaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String ci = ((PersonaDato) personaView.personaListView.getItemAtPosition(i)).ci;
                PersonaDato selected = personaModel.findRecordInDatabase("ci", ci);
                personaView.setFormData(selected);
                clearTable();
                setRelacionFamiliarDato(selected.relacionFamiliarDatoList);
            }
        });

        personaView.btnAgregarFila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addRowToTable();
            }
        });
    }

    private void clearTable(){
        personaView.tableLayout.removeViews(1,personaView.tableLayout.getChildCount()-1);
    }
    private void store() {
        personaModel.setAttributesData(personaView.readFormData());
        PersonaDato dto = personaModel.insert();
        if (dto == null) return;
        personaView.setFormData(dto);
        personaView.setRowsDataList(personaModel.rowsList());
        setDataSpinnerPersona();
        setDataSpinnerInvitado();
    }
    private void up() {
        personaModel.setAttributesData(personaView.readFormData());
        PersonaDato dto = personaModel.update();
        if (dto == null) return;
        personaView.setFormData(dto);
        personaView.setRowsDataList(personaModel.rowsList());
        setDataSpinnerPersona();
        setDataSpinnerInvitado();
    }
    private void delete() {
        personaModel.setAttributesData(personaView.readFormData());
        personaModel.deleteRecordInDatabase();
        personaView.cleanFormData();
        clearTable();
        personaView.setRowsDataList(personaModel.rowsList());
        setDataSpinnerPersona();
        setDataSpinnerInvitado();
    }
    private void addRowToTable() {
        deleteRowPrueba();

        TableRow nuevaFila = new TableRow(personaView);   // Crea una nueva fila para agregar datos
        TextView textViewId = new TextView(personaView);   // Crea un TextView para el ID
        textViewId.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        textViewId.setText(nextId()); // Puedes establecer el ID dinámicamente
        textViewId.setGravity(Gravity.CENTER);

        TipoRelacionDato tipoRelacionDato = ((TipoRelacionDato) personaView.spinnerTipoRelacion.getSelectedItem());
        TextView textViewTipoRelacion = new TextView(personaView);
        textViewTipoRelacion.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
        textViewTipoRelacion.setText(tipoRelacionDato.nombre); // Puedes establecer el nombre dinámicamente
        textViewTipoRelacion.setTag(tipoRelacionDato.id);
        textViewTipoRelacion.setGravity(Gravity.CENTER);

        PersonaDato personaDato = ((PersonaDato) personaView.spinnerPersona.getSelectedItem());
        TextView textViewPersona = new TextView(personaView);
        textViewPersona.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        textViewPersona.setText(personaDato.nombre); // Establece el hint como prefieras
        textViewPersona.setTag(personaDato.ci);
        textViewPersona.setInputType(InputType.TYPE_CLASS_TEXT);
        textViewPersona.setGravity(Gravity.CENTER);

        ImageButton eliminarButton = new ImageButton(personaView);
        eliminarButton.setImageResource(R.drawable.baseline_remove_circle_24);
        eliminarButton.setBackgroundColor(Color.TRANSPARENT);
        eliminarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarFila(nuevaFila);
            }
        });

        // Agrega los elementos a la nueva fila
        nuevaFila.addView(textViewId);
        nuevaFila.addView(textViewTipoRelacion);
        nuevaFila.addView(textViewPersona);
        nuevaFila.addView(eliminarButton);

        // Agrega la nueva fila a la tabla
        personaView.tableLayout.addView(nuevaFila);
    }
    private void addRowToTable(String id, String tipo_relacion_id, String persona_id) {
        TableRow nuevaFila = new TableRow(personaView);   // Crea una nueva fila para agregar datos
        TextView textViewId = new TextView(personaView);   // Crea un TextView para el ID
        textViewId.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        textViewId.setText(id); // Puedes establecer el ID dinámicamente
        textViewId.setGravity(Gravity.CENTER);

//        IngresoDato ingresoDato = (IngresoDato) notaIngresoView.spinnerIngreso. (ingreso_id).getTag();
        TipoRelacionDato tipoRelacionDato = tipoRelacionModel.findRecordInDatabase("id",tipo_relacion_id);
        TextView textViewNombreIngreso = new TextView(personaView);
        textViewNombreIngreso.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
        textViewNombreIngreso.setText(tipoRelacionDato.nombre); // Puedes establecer el nombre dinámicamente
        textViewNombreIngreso.setTag(tipoRelacionDato.id);
        textViewNombreIngreso.setGravity(Gravity.CENTER);

        PersonaDato personaDato = personaModel.findRecordInDatabase("ci",persona_id);
        TextView textViewPersona = new TextView(personaView);
        textViewPersona.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
        textViewPersona.setText(personaDato.nombre); // Establece el hint como prefieras
        textViewPersona.setTag(personaDato.ci);
        textViewPersona.setInputType(InputType.TYPE_CLASS_TEXT);
        textViewPersona.setGravity(Gravity.CENTER);

        ImageButton eliminarButton = new ImageButton(personaView);
        eliminarButton.setImageResource(R.drawable.baseline_remove_circle_24);
        eliminarButton.setBackgroundColor(Color.TRANSPARENT);
        eliminarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarFila(nuevaFila);
            }
        });

        // Agrega los elementos a la nueva fila
        nuevaFila.addView(textViewId);
        nuevaFila.addView(textViewNombreIngreso);
        nuevaFila.addView(textViewPersona);
        nuevaFila.addView(eliminarButton);

        // Agrega la nueva fila a la tabla
        personaView.tableLayout.addView(nuevaFila);
    }

    private void eliminarFila(View view) {
        personaView.tableLayout.removeView((TableRow) view);
    }
    private String nextId(){
        int ultimoID = 0;  // Valor predeterminado si no hay filas aún
        int rowCount = personaView.tableLayout.getChildCount();
        if (rowCount > 1) { // title of column not count, so start with 2
            View ultimaFila = personaView.tableLayout.getChildAt(rowCount-1);
            if (ultimaFila != null) {
                TableRow fila = (TableRow) ultimaFila;
                TextView primeraColumna = (TextView) fila.getChildAt(0);
                String idColumn = primeraColumna.getText().toString();
                ultimoID+=Integer.parseInt(idColumn);
            }
        }
        return  String.valueOf(ultimoID+1);
    }
    public void setRelacionFamiliarDato(List<RelacionFamiliarDato> relacionFamiliarDatoList){
        for (RelacionFamiliarDato item : relacionFamiliarDatoList){
            addRowToTable(item.persona_id, item.relacion_id, item.pariente_id);
        }
    }

    private void deleteRowPrueba(){
        TableRow filaDePrueba = (TableRow) personaView.tableLayout.findViewWithTag("row_prueba");
        if (filaDePrueba != null) personaView.tableLayout.removeView(filaDePrueba);
    }

}
