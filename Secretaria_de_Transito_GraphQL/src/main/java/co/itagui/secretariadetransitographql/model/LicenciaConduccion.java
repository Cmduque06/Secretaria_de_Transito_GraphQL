package co.itagui.secretariadetransitographql.model;

public class LicenciaConduccion {

    private String numero;
    private String categoria;
    private int puntosBase;
    private int puntosActuales;

    public LicenciaConduccion() {
    }

    public LicenciaConduccion(String numero, String categoria, int puntosBase, int puntosActuales) {
        this.numero = numero;
        this.categoria = categoria;
        this.puntosBase = puntosBase;
        this.puntosActuales = puntosActuales;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getPuntosBase() {
        return puntosBase;
    }

    public void setPuntosBase(int puntosBase) {
        this.puntosBase = puntosBase;
    }

    public int getPuntosActuales() {
        return puntosActuales;
    }

    public void setPuntosActuales(int puntosActuales) {
        this.puntosActuales = puntosActuales;
    }
}
