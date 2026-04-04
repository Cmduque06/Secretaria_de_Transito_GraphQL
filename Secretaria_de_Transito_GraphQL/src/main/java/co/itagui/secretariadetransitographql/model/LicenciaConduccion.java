package co.itagui.secretariadetransitographql.model;

public class LicenciaConduccion {

    private Long id;
    private String numero;
    private String categoria;
    private int puntosBase;
    private int puntosActuales;

    public LicenciaConduccion() {
    }

    public LicenciaConduccion(Long id, String numero, String categoria, int puntosBase, int puntosActuales) {
        this.id = id;
        this.numero = numero;
        this.categoria = categoria;
        this.puntosBase = puntosBase;
        this.puntosActuales = puntosActuales;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
