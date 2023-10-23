package com.jonatan.church_mvc.Actividad;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.text.ParseException;

public class ActividadController {

    private ActividadView actividadView;
    private ActividadModel actividadModel;
    public ActividadController(ActividadView actividadView, ActividadModel actividadModel){
        this.actividadView = actividadView;
        this.actividadModel = actividadModel;
        initListener();
        this.actividadView.setRowsDataList(actividadModel.rowsList());
    }
    public void initListener() {
        actividadView.btnGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    store();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        actividadView.btnEliminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    delete();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        actividadView.btnCrear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                actividadView.cleanFormData();
            }
        });

        actividadView.actividadListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = ((ActividadDato) actividadView.actividadListView.getItemAtPosition(i)).id;
                actividadView.setFormData(actividadModel.findRecordInDatabase("id", id));
            }
        });
    }

    private void store() throws ParseException {
        actividadModel.setAttributesData(actividadView.readFormData());
        ActividadDato dto = actividadModel.insert();
        if (dto == null) return;
        actividadView.setFormData(dto);
        actividadView.setRowsDataList(actividadModel.rowsList());
    }
    private void delete() throws ParseException {
        actividadModel.setAttributesData(actividadView.readFormData());
        actividadModel.deleteRecordInDatabase();
        actividadView.cleanFormData();
        actividadView.setRowsDataList(actividadModel.rowsList());
    }
}
