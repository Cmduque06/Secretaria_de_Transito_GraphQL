package co.itagui.secretariadetransitographql.model.report;

public class VehiculoInfraccionesReporte {

    private Long vehiculoId;
    private String placa;
    private String propietarioNombre;
    private int totalInfracciones;

    public VehiculoInfraccionesReporte() {
    }

    public VehiculoInfraccionesReporte(Long vehiculoId, String placa, String propietarioNombre, int totalInfracciones) {
        this.vehiculoId = vehiculoId;
        this.placa = placa;
        this.propietarioNombre = propietarioNombre;
        this.totalInfracciones = totalInfracciones;
    }

    public Long getVehiculoId() {
        return vehiculoId;
    }

    public void setVehiculoId(Long vehiculoId) {
        this.vehiculoId = vehiculoId;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
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
