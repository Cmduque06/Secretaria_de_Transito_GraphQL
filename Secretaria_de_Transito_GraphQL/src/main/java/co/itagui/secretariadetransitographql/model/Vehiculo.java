package co.itagui.secretariadetransitographql.model;

public class Vehiculo {

    private String placa;
    private String marca;
    private String modelo;
    private String tipo;
    private String propietarioIdentificacion;

    public Vehiculo() {
    }

    public Vehiculo(String placa, String marca, String modelo, String tipo, String propietarioIdentificacion) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.tipo = tipo;
        this.propietarioIdentificacion = propietarioIdentificacion;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPropietarioIdentificacion() {
        return propietarioIdentificacion;
    }

    public void setPropietarioIdentificacion(String propietarioIdentificacion) {
        this.propietarioIdentificacion = propietarioIdentificacion;
    }
}
