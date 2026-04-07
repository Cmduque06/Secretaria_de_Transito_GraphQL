package co.itagui.secretariadetransitographql.model;

public class LicenciaConduccion {

    private String numero;
    private String categoria;
    private int puntosBase;
    private int puntosActuales;
    private boolean suspendida;
    private String motivoSuspension;

    public LicenciaConduccion() {
    }

    public LicenciaConduccion(String numero, String categoria, int puntosBase, int puntosActuales, boolean suspendida, String motivoSuspension) {
        this.numero = numero;
        this.categoria = categoria;
        this.puntosBase = puntosBase;
        this.puntosActuales = puntosActuales;
        this.suspendida = suspendida;
        this.motivoSuspension = motivoSuspension;
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

    public boolean isSuspendida() {
        return suspendida;
    }

    public void setSuspendida(boolean suspendida) {
        this.suspendida = suspendida;
    }

    public String getMotivoSuspension() {
        return motivoSuspension;
    }

    public void setMotivoSuspension(String motivoSuspension) {
        this.motivoSuspension = motivoSuspension;
    }
}
