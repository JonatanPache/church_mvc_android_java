package com.jonatan.church_mvc.Ingreso;

public class IngresoDato {

    public final String id;
    public final String descripcion;
    public final String nombre;

    public IngresoDato(String id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "id: " + id + " nom: " + nombre;
    }
}
