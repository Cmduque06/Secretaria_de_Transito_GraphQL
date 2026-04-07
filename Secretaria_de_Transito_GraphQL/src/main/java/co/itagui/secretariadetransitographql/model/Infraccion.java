package co.itagui.secretariadetransitographql.model;

public class Infraccion {

    private Long id;
    private String codigo;
    private String descripcion;
    private double valor;
    private String severidad;
    private boolean pagada;
    private String vehiculoPlaca;
    private String origenRegistro;
    private String agenteIdentificacion;
    private String camaraCodigo;
    private String fechaRegistro;

    public Infraccion() {
    }

    public Infraccion(Long id, String codigo, String descripcion, double valor, String severidad, boolean pagada,
                      String vehiculoPlaca, String origenRegistro, String agenteIdentificacion, String camaraCodigo, String fechaRegistro) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.valor = valor;
        this.severidad = severidad;
        this.pagada = pagada;
        this.vehiculoPlaca = vehiculoPlaca;
        this.origenRegistro = origenRegistro;
        this.agenteIdentificacion = agenteIdentificacion;
        this.camaraCodigo = camaraCodigo;
        this.fechaRegistro = fechaRegistro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getSeveridad() {
        return severidad;
    }

    public void setSeveridad(String severidad) {
        this.severidad = severidad;
    }

    public boolean isPagada() {
        return pagada;
    }

    public void setPagada(boolean pagada) {
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
