package com.jonatan.church_mvc.RelacionFamiliar;

public class RelacionFamiliarDato {
    public final String persona_id;
    public String pariente_id;
    public final String relacion_id;

    public RelacionFamiliarDato(String persona_id, String pariente_id, String relacion_id) {
        this.persona_id = persona_id;
        this.pariente_id = pariente_id;
        this.relacion_id = relacion_id;
    }

    @Override
    public String toString() {
        return "persona_id: " + persona_id + " pariente_id: " + pariente_id + "relacion_id: " + relacion_id;
    }
}
