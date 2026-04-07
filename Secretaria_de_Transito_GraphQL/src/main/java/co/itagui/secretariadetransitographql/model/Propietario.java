package co.itagui.secretariadetransitographql.model;

public class Propietario {

    private String tipo;
    private String identificacion;
    private String nombre;
    private String direccion;
    private LicenciaConduccion licencia;

    public Propietario() {
    }

    public Propietario(String tipo, String identificacion, String nombre, String direccion, LicenciaConduccion licencia) {
        this.tipo = tipo;
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.direccion = direccion;
        this.licencia = licencia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LicenciaConduccion getLicencia() {
        return licencia;
    }

    public void setLicencia(LicenciaConduccion licencia) {
        this.licencia = licencia;
    }
}
