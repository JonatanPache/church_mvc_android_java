package com.jonatan.church_mvc.Ingreso;

import android.view.View;
import android.widget.AdapterView;

public class IngresoController {
    private IngresoView ingresoView;
    private IngresoModel ingresoModel;
    public IngresoController(IngresoView ingresoView, IngresoModel ingresoModel){
        this.ingresoView = ingresoView;
        this.ingresoModel = ingresoModel;
        initListener();
        this.ingresoView.setRowsDataList(ingresoModel.rowsList());
    }
    public void initListener() {
        ingresoView.btnGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                store();
            }
        });

        ingresoView.btnEliminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                delete();
            }
        });

        ingresoView.btnCrear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ingresoView.cleanFormData();
            }
        });

        ingresoView.ingresoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = ((IngresoDato) ingresoView.ingresoListView.getItemAtPosition(i)).id;
                ingresoView.setFormData(ingresoModel.findRecordInDatabase("id", id));
            }
        });
    }

    private void store() {
        ingresoModel.setAttributesData(ingresoView.readFormData());
        IngresoDato dto = ingresoModel.saveInDatabase();
        if (dto == null) return;
        ingresoView.setFormData(dto);
        ingresoView.setRowsDataList(ingresoModel.rowsList());
    }
    private void delete() {
        ingresoModel.setAttributesData(ingresoView.readFormData());
        ingresoModel.deleteRecordInDatabase();
        ingresoView.cleanFormData();
        ingresoView.setRowsDataList(ingresoModel.rowsList());
    }
}
