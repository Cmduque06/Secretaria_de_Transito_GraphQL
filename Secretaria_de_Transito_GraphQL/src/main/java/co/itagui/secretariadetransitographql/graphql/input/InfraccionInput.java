package co.itagui.secretariadetransitographql.graphql.input;

public class InfraccionInput {

    private String codigo;
    private String descripcion;
    private Double valor;
    private String severidad;
    private Boolean pagada;
    private String vehiculoPlaca;
    private String origenRegistro;
    private String agenteIdentificacion;
    private String camaraCodigo;
    private String fechaRegistro;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getSeveridad() {
        return severidad;
    }

    public void setSeveridad(String severidad) {
        this.severidad = severidad;
    }

    public Boolean getPagada() {
        return pagada;
    }

    public void setPagada(Boolean pagada) {
        this.pagada = pagada;
    }

    public String getVehiculoPlaca() {
        return vehiculoPlaca;
    }

    public void setVehiculoPlaca(String vehiculoPlaca) {
        this.vehiculoPlaca = vehiculoPlaca;
    }

    public String getOrigenRegistro() {
        return origenRegistro;
    }

    public void setOrigenRegistro(String origenRegistro) {
        this.origenRegistro = origenRegistro;
    }

    public String getAgenteIdentificacion() {
        return agenteIdentificacion;
    }

    public void setAgenteIdentificacion(String agenteIdentificacion) {
        this.agenteIdentificacion = agenteIdentificacion;
    }

    public String getCamaraCodigo() {
        return camaraCodigo;
    }

    public void setCamaraCodigo(String camaraCodigo) {
        this.camaraCodigo = camaraCodigo;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
