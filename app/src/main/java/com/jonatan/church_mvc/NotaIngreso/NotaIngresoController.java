package com.jonatan.church_mvc.NotaIngreso;

import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.jonatan.church_mvc.Actividad.ActividadDato;
import com.jonatan.church_mvc.Actividad.ActividadModel;
import com.jonatan.church_mvc.Actividad.ActividadView;
import com.jonatan.church_mvc.DetalleNotaIngreso.DetalleNotaIngresoDato;
import com.jonatan.church_mvc.DetalleNotaIngreso.DetalleNotaIngresoModel;
import com.jonatan.church_mvc.Ingreso.IngresoDato;
import com.jonatan.church_mvc.Ingreso.IngresoModel;
import com.jonatan.church_mvc.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NotaIngresoController {

    private NotaIngresoView notaIngresoView;
    private NotaIngresoModel notaIngresoModel;
    private ActividadModel actividadModel;
    private IngresoModel ingresoModel;
    public NotaIngresoController(NotaIngresoView notaIngresoView, NotaIngresoModel notaIngresoModel, ActividadModel actividadModel, IngresoModel ingresoModel){
        this.notaIngresoView = notaIngresoView;
        this.notaIngresoModel = notaIngresoModel;
        this.actividadModel = actividadModel;
        this.ingresoModel = ingresoModel;
        initListener();
        this.notaIngresoView.setRowsDataList(notaIngresoModel.rowsList());
        this.notaIngresoView.setDataListSpinnerActividad(actividadModel.rowsList());
        this.notaIngresoView.setDataListSpinnerIngreso(ingresoModel.rowsList());
    }
    public void initListener() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//HH:mm:ss
        notaIngresoView.txtFecha.setText(dateFormat.format(Calendar.getInstance().getTime()));

        notaIngresoView.btnGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    store();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        notaIngresoView.btnEliminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    delete();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        notaIngresoView.btnCrear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                notaIngresoView.cleanFormData();
                clearTable();
            }
        });

        notaIngresoView.notaIngresoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = ((NotaIngresoDato) notaIngresoView.notaIngresoListView.getItemAtPosition(i)).id;
                NotaIngresoDato selected = notaIngresoModel.findRecordInDatabase("id", id);
                notaIngresoView.setFormData(selected);
                clearTable();
                ActividadDato item = actividadModel.findRecordInDatabase("id",selected.actividad_id);
                Log.d("asd",item.id);
                if (item!=null) {
                    int index = obtenerIndex(item,notaIngresoView.adapter_actividad);
                    Log.d("asd",String.valueOf(index));
                    notaIngresoView.spinnerActividad.setSelection(index);
                }
                setDetalleNotaIngresoDato(selected.listDetalleNotaIngreso);

            }
        });

        notaIngresoView.btnAgregarFila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRowToTable();
            }
        });
    }

    private int obtenerIndex(ActividadDato actividadDato, ArrayAdapter<ActividadDato> arrayAdapter){
        for (int i=0; i<arrayAdapter.getCount();i++){
            Log.d("asf",arrayAdapter.getItem(i).toString());
            Log.d("asf",String.valueOf(i));
            if (actividadDato.id.compareTo(arrayAdapter.getItem(i).id) == 0 ){
                return i;
            }
        }
        return -1;
    }
    private void store() throws ParseException {
        notaIngresoModel.setAttributesData(notaIngresoView.readFormData());
        NotaIngresoDato dto = notaIngresoModel.insert();
        if (dto == null) return;
        notaIngresoView.setFormData(dto);
        ActividadDato item = actividadModel.findRecordInDatabase("id",dto.actividad_id);
        if (item!=null) notaIngresoView.spinnerActividad.setSelection(notaIngresoView.adapter_actividad.getPosition(item));
        notaIngresoView.setRowsDataList(notaIngresoModel.rowsList());
    }
    private void delete() throws ParseException {
        notaIngresoModel.setAttributesData(notaIngresoView.readFormData());
        notaIngresoModel.deleteRecordInDatabase();
        notaIngresoView.cleanFormData();
        clearTable();
        notaIngresoView.setRowsDataList(notaIngresoModel.rowsList());
    }
    private void deleteRowPrueba(){
        TableRow filaDePrueba = (TableRow) notaIngresoView.tableLayout.findViewWithTag("row_prueba");
        if (filaDePrueba != null) notaIngresoView.tableLayout.removeView(filaDePrueba);
    }
    private void addRowToTable() {
        deleteRowPrueba();

        TableRow nuevaFila = new TableRow(notaIngresoView);   // Crea una nueva fila para agregar datos
        TextView textViewId = new TextView(notaIngresoView);   // Crea un TextView para el ID
        textViewId.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        textViewId.setText(nextId()); // Puedes establecer el ID dinámicamente
        textViewId.setGravity(Gravity.CENTER);

        IngresoDato ingresoDato = ((IngresoDato) notaIngresoView.spinnerIngreso.getSelectedItem());
        TextView textViewNombreIngreso = new TextView(notaIngresoView);
        textViewNombreIngreso.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
        textViewNombreIngreso.setText(ingresoDato.nombre); // Puedes establecer el nombre dinámicamente
        textViewNombreIngreso.setTag(ingresoDato.id);
        textViewNombreIngreso.setGravity(Gravity.CENTER);

        EditText editTextMonto = new EditText(notaIngresoView);
        editTextMonto.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
        editTextMonto.setText("0"); // Establece el hint como prefieras
        editTextMonto.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextMonto.setGravity(Gravity.CENTER);
        editTextMonto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ( !s.toString().isEmpty() ) {
                    notaIngresoView.txtMontoTotal.setText(String.valueOf(getMontoDetalleNotaIngresoDato()));
                }
            }
        });

        ImageButton eliminarButton = new ImageButton(notaIngresoView);
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
        nuevaFila.addView(editTextMonto);
        nuevaFila.addView(eliminarButton);

        // Agrega la nueva fila a la tabla
        notaIngresoView.tableLayout.addView(nuevaFila);
    }
    private void clearTable(){
        notaIngresoView.tableLayout.removeViews(1,notaIngresoView.tableLayout.getChildCount()-1);
    }
    private void addRowToTable(String id, String ingreso_id, String monto) {
        TableRow nuevaFila = new TableRow(notaIngresoView);   // Crea una nueva fila para agregar datos
        TextView textViewId = new TextView(notaIngresoView);   // Crea un TextView para el ID
        textViewId.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
        textViewId.setText(id); // Puedes establecer el ID dinámicamente
        textViewId.setGravity(Gravity.CENTER);

//        IngresoDato ingresoDato = (IngresoDato) notaIngresoView.spinnerIngreso. (ingreso_id).getTag();
        IngresoDato ingresoDato = ingresoModel.findRecordInDatabase("id",ingreso_id);
        TextView textViewNombreIngreso = new TextView(notaIngresoView);
        textViewNombreIngreso.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
        textViewNombreIngreso.setText(ingresoDato.nombre); // Puedes establecer el nombre dinámicamente
        textViewNombreIngreso.setTag(ingresoDato.id);
        textViewNombreIngreso.setGravity(Gravity.CENTER);

        EditText editTextMonto = new EditText(notaIngresoView);
        editTextMonto.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 2f));
        editTextMonto.setText(monto); // Establece el hint como prefieras
        editTextMonto.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editTextMonto.setGravity(Gravity.CENTER);

        editTextMonto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if ( !s.toString().isEmpty() ) {
                    notaIngresoView.txtMontoTotal.setText(String.valueOf(getMontoDetalleNotaIngresoDato()));
                }
            }
        });

        ImageButton eliminarButton = new ImageButton(notaIngresoView);
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
        nuevaFila.addView(editTextMonto);
        nuevaFila.addView(eliminarButton);

        // Agrega la nueva fila a la tabla
        notaIngresoView.tableLayout.addView(nuevaFila);
    }
    private void eliminarFila(View view) {
        notaIngresoView.tableLayout.removeView((TableRow) view);
    }
    private String nextId(){
        int ultimoID = 0;  // Valor predeterminado si no hay filas aún
        int rowCount = notaIngresoView.tableLayout.getChildCount();
        if (rowCount > 1) { // title of column not count, so start with 2
            View ultimaFila = notaIngresoView.tableLayout.getChildAt(rowCount-1);
            if (ultimaFila != null) {
                TableRow fila = (TableRow) ultimaFila;
                TextView primeraColumna = (TextView) fila.getChildAt(0);
                String idColumn = primeraColumna.getText().toString();
                ultimoID+=Integer.parseInt(idColumn);
            }
        }
        return  String.valueOf(ultimoID+1);
    }
    public void setDetalleNotaIngresoDato(List<DetalleNotaIngresoDato> detalleNotaIngresoDatoList){
        for (DetalleNotaIngresoDato item : detalleNotaIngresoDatoList){
            addRowToTable(item.id,item.ingreso_id,item.monto);
        }
    }
    public int getMontoDetalleNotaIngresoDato(){
        int monto_total = 0;
        int rowCount = notaIngresoView.tableLayout.getChildCount();
        for (int i = 1; i < rowCount; i++) {
            View rowView = notaIngresoView.tableLayout.getChildAt(i);
            if (rowView instanceof TableRow) {
                TableRow row = (TableRow) rowView;
                EditText montoEditText = (EditText) row.getChildAt(2);
                int monto = Integer.parseInt(montoEditText.getText().toString());
                monto_total += monto;
            }
        }
        return monto_total;
    }
}
