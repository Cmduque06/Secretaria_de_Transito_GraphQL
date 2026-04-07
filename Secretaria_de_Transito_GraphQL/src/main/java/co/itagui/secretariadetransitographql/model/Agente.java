package co.itagui.secretariadetransitographql.model;

public class Agente {

    private String identificacion;
    private String nombre;
    private String placaInstitucional;
    private String rango;
    private String zonaOperativa;
    private boolean activo;

    public Agente() {
    }

    public Agente(String identificacion, String nombre, String placaInstitucional, String rango, String zonaOperativa, boolean activo) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.placaInstitucional = placaInstitucional;
        this.rango = rango;
        this.zonaOperativa = zonaOperativa;
        this.activo = activo;
    }

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

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
