package com.jonatan.church_mvc.Actividad;

public class ActividadDato {
    public final String id;
    public final String descripcion;
    public final String nombre;
    public final String fecha_inicio;
    public final String fecha_final;

    public ActividadDato(String id, String nombre, String descripcion, String fecha_inicio, String fecha_final) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fecha_inicio = fecha_inicio;
        this.fecha_final = fecha_final;
    }

    @Override
    public String toString() {
        return "id: " + id + " Nombre: " + nombre+ " Fecha Inicio: " + fecha_inicio;
    }
}
