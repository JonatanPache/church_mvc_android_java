package com.jonatan.church_mvc.TipoRelacion;

public class TipoRelacionDato {

    public final String id;
    public final String nombre;

    public TipoRelacionDato(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return id != "-1"
                ? "id: " + id + " nom: " + nombre
                : "Selecciona un tipo";
    }
}
