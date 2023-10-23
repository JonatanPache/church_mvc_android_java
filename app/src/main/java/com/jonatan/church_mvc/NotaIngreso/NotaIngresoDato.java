package com.jonatan.church_mvc.NotaIngreso;

import com.jonatan.church_mvc.DetalleNotaIngreso.DetalleNotaIngresoDato;

import java.util.List;

public class NotaIngresoDato {
    public final String id;
    public final String actividad_id;
    public final String titulo;
    public final String total;
    public final String fecha;
    public List<DetalleNotaIngresoDato> listDetalleNotaIngreso;
    public NotaIngresoDato(String id, String actividad_id,String titulo, String total, String fecha,
                           List<DetalleNotaIngresoDato> listDetalleNotaIngreso) {
        this.id = id;
        this.titulo = titulo;
        this.actividad_id = actividad_id;
        this.total = total;
        this.fecha = fecha;
        this.listDetalleNotaIngreso = listDetalleNotaIngreso;
    }

    @Override
    public String toString() {
        return "id: " + id + " Titulo: " + titulo + " Actividad ID: " + actividad_id +" Total: " + total;
    }
}
