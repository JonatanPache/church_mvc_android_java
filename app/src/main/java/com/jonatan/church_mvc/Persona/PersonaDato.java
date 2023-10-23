package com.jonatan.church_mvc.Persona;

import com.jonatan.church_mvc.DetalleNotaIngreso.DetalleNotaIngresoDato;
import com.jonatan.church_mvc.RelacionFamiliar.RelacionFamiliarDato;

import java.util.List;

public class PersonaDato {

    public final String ci;
    public final String nombre;
    public final String telefono;
    public final String rol;
    public final String invita_id;

    public List<RelacionFamiliarDato> relacionFamiliarDatoList;

    public PersonaDato(String ci, String nombre, String telefono, String rol, String invita_id,
                       List<RelacionFamiliarDato> relacionFamiliarDatoList) {
        this.ci = ci;
        this.nombre = nombre;
        this.telefono = telefono;
        this.rol = rol;
        this.invita_id = invita_id;
        this.relacionFamiliarDatoList = relacionFamiliarDatoList;
    }

    @Override
    public String toString() {
        return ci != "-1"
                ? "ci: " + ci + " nom: " + nombre+ " rol: " + rol
                : "Selecciona Una Persona";
    }
}
