package com.jonatan.church_mvc.Cargo;

import com.jonatan.church_mvc.RelacionFamiliar.RelacionFamiliarDato;

import java.util.List;

public class CargoDato {

    public final String id;
    public final String nombre;
    public final String descripcion;

    public CargoDato(String id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "ID: " + id + " nom: " + nombre;
    }
}
