package com.jonatan.church_mvc.BitacoraCargo;

public class BitacoraCargoDato {

    public final String id;
    public final String persona_ci;
    public final String cargo_id;
    public final String fecha_inicio;
    public final String fecha_final;
    public final String estado;

    public BitacoraCargoDato(String id, String persona_ci, String cargo_id, String fecha_inicio,
                             String fecha_final, String estado) {
        this.id = id;
        this.persona_ci = persona_ci;
        this.cargo_id = cargo_id;
        this.fecha_inicio = fecha_inicio;
        this.fecha_final = fecha_final;
        this.estado = estado;
    }

    @Override
    public String toString() {
        String estado_1 = (estado.compareTo("0") == 0)?"Inhabilitado":"Habilitado";
        return "ID: " + id
                + " CI: " + persona_ci
                + " IDCargo: " + cargo_id
                + " Estado: " + estado_1 ;
    }
}
