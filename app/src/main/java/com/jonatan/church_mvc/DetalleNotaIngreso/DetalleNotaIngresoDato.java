package com.jonatan.church_mvc.DetalleNotaIngreso;

public class DetalleNotaIngresoDato {
    public final String id;
    public String nota_ingreso_id;
    public final String ingreso_id;
    public final String monto;

    public DetalleNotaIngresoDato(String id, String nota_ingreso_id, String ingreso_id, String monto) {
        this.id = id;
        this.ingreso_id = ingreso_id;
        this.nota_ingreso_id = nota_ingreso_id;
        this.monto = monto;
    }

    @Override
    public String toString() {
        return "id: " + id + " Ingreso ID: " + ingreso_id + "Monto: " + monto;
    }
}
