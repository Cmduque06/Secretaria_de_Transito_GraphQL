package co.itagui.secretariadetransitographql.model.report;

public class VehiculoInfraccionesReporte {

    private String placa;
    private String propietarioIdentificacion;
    private String propietarioNombre;
    private int totalInfracciones;

    public VehiculoInfraccionesReporte() {
    }

    public VehiculoInfraccionesReporte(String placa, String propietarioIdentificacion, String propietarioNombre, int totalInfracciones) {
        this.placa = placa;
        this.propietarioIdentificacion = propietarioIdentificacion;
        this.propietarioNombre = propietarioNombre;
        this.totalInfracciones = totalInfracciones;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getPropietarioIdentificacion() {
        return propietarioIdentificacion;
    }

    public void setPropietarioIdentificacion(String propietarioIdentificacion) {
        this.propietarioIdentificacion = propietarioIdentificacion;
    }

    public String getPropietarioNombre() {
        return propietarioNombre;
    }

    public void setPropietarioNombre(String propietarioNombre) {
        this.propietarioNombre = propietarioNombre;
    }

    public int getTotalInfracciones() {
        return totalInfracciones;
    }

    public void setTotalInfracciones(int totalInfracciones) {
        this.totalInfracciones = totalInfracciones;
    }
}
