package co.itagui.secretariadetransitographql.graphql.input;

public class AgenteInput {

    private String identificacion;
    private String nombre;
    private String placaInstitucional;
    private String rango;
    private String zonaOperativa;
    private Boolean activo;

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPlacaInstitucional() {
        return placaInstitucional;
    }

    public void setPlacaInstitucional(String placaInstitucional) {
        this.placaInstitucional = placaInstitucional;
    }

    public String getRango() {
        return rango;
    }

    public void setRango(String rango) {
        this.rango = rango;
    }

    public String getZonaOperativa() {
        return zonaOperativa;
    }

    public void setZonaOperativa(String zonaOperativa) {
        this.zonaOperativa = zonaOperativa;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
