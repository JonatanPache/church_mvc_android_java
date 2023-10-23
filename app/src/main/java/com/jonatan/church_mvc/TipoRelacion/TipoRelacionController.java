package com.jonatan.church_mvc.TipoRelacion;

import android.view.View;
import android.widget.AdapterView;

import com.jonatan.church_mvc.Ingreso.IngresoDato;
import com.jonatan.church_mvc.Ingreso.IngresoModel;
import com.jonatan.church_mvc.Ingreso.IngresoView;

public class TipoRelacionController {
    private TipoRelacionView tipoRelacionView;
    private TipoRelacionModel tipoRelacionModel;
    public TipoRelacionController(TipoRelacionView tipoRelacionView, TipoRelacionModel tipoRelacionModel){
        this.tipoRelacionView = tipoRelacionView;
        this.tipoRelacionModel = tipoRelacionModel;
        initListener();
        this.tipoRelacionView.setRowsDataList(tipoRelacionModel.rowsList());
    }
    public void initListener() {
        tipoRelacionView.btnGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                store();
            }
        });

        tipoRelacionView.btnEliminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                delete();
            }
        });

        tipoRelacionView.btnCrear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tipoRelacionView.cleanFormData();
            }
        });

        tipoRelacionView.tipoRelacionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = ((IngresoDato) tipoRelacionView.tipoRelacionListView.getItemAtPosition(i)).id;
                tipoRelacionView.setFormData(tipoRelacionModel.findRecordInDatabase("id", id));
            }
        });
    }

    private void store() {
        tipoRelacionModel.setAttributesData(tipoRelacionView.readFormData());
        TipoRelacionDato dto = tipoRelacionModel.insert();
        if (dto == null) return;
        tipoRelacionView.setFormData(dto);
        tipoRelacionView.setRowsDataList(tipoRelacionModel.rowsList());
    }
    private void delete() {
        tipoRelacionModel.setAttributesData(tipoRelacionView.readFormData());
        tipoRelacionModel.deleteRecordInDatabase();
        tipoRelacionView.cleanFormData();
        tipoRelacionView.setRowsDataList(tipoRelacionModel.rowsList());
    }
}
