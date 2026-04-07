package co.itagui.secretariadetransitographql.model;

public class Infraccion {

    private Long id;
    private String codigo;
    private String descripcion;
    private double valor;
    private String severidad;
    private boolean pagada;
    private String vehiculoPlaca;

    public Infraccion() {
    }

    public Infraccion(Long id, String codigo, String descripcion, double valor, String severidad, boolean pagada, String vehiculoPlaca) {
        this.id = id;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.valor = valor;
        this.severidad = severidad;
        this.pagada = pagada;
        this.vehiculoPlaca = vehiculoPlaca;
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
}
