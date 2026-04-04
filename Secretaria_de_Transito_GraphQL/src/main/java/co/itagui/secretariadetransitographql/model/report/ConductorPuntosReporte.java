package co.itagui.secretariadetransitographql.model.report;

public class ConductorPuntosReporte {

    private Long propietarioId;
    private String nombre;
    private String numeroLicencia;
    private int puntosActuales;

    public ConductorPuntosReporte() {
    }

    public ConductorPuntosReporte(Long propietarioId, String nombre, String numeroLicencia, int puntosActuales) {
        this.propietarioId = propietarioId;
        this.nombre = nombre;
        this.numeroLicencia = numeroLicencia;
        this.puntosActuales = puntosActuales;
    }

    public Long getPropietarioId() {
        return propietarioId;
    }

    public void setPropietarioId(Long propietarioId) {
        this.propietarioId = propietarioId;
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
}
