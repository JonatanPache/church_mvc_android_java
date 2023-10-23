package com.jonatan.church_mvc.Cargo;

import android.view.View;
import android.widget.AdapterView;

public class CargoController {
    private CargoView cargoView;
    private CargoModel cargoModel;
    public CargoController(CargoView cargoView, CargoModel cargoModel){
        this.cargoView = cargoView;
        this.cargoModel = cargoModel;
        initListener();
        this.cargoView.setRowsDataList(cargoModel.rowsList());
    }
    public void initListener() {
        cargoView.btnGuardar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                store();
            }
        });

        cargoView.btnEliminar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                delete();
            }
        });

        cargoView.btnCrear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cargoView.cleanFormData();
            }
        });

        cargoView.cargoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = ((CargoDato) cargoView.cargoListView.getItemAtPosition(i)).id;
                cargoView.setFormData(cargoModel.findRecordInDatabase("id", id));
            }
        });
    }

    private void store() {
        cargoModel.setAttributesData(cargoView.readFormData());
        CargoDato dto = cargoModel.saveInDatabase();
        if (dto == null) return;
        cargoView.setFormData(dto);
        cargoView.setRowsDataList(cargoModel.rowsList());
    }
    private void delete() {
        cargoModel.setAttributesData(cargoView.readFormData());
        cargoModel.deleteRecordInDatabase();
        cargoView.cleanFormData();
        cargoView.setRowsDataList(cargoModel.rowsList());
    }
}
