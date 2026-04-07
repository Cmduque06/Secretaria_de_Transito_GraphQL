package co.itagui.secretariadetransitographql.model.report;

public class ConductorPuntosReporte {

    private String identificacion;
    private String nombre;
    private String numeroLicencia;
    private int puntosActuales;
    private boolean suspendida;

    public ConductorPuntosReporte() {
    }

    public ConductorPuntosReporte(String identificacion, String nombre, String numeroLicencia, int puntosActuales, boolean suspendida) {
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.numeroLicencia = numeroLicencia;
        this.puntosActuales = puntosActuales;
        this.suspendida = suspendida;
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

    public String getNumeroLicencia() {
        return numeroLicencia;
    }

    public void setNumeroLicencia(String numeroLicencia) {
        this.numeroLicencia = numeroLicencia;
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
}
