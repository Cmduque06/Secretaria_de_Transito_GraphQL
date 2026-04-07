package co.itagui.secretariadetransitographql.service;

import co.itagui.secretariadetransitographql.graphql.input.VehiculoInput;
import co.itagui.secretariadetransitographql.model.Vehiculo;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class VehiculoResolverService {

    private final DatosMemoriaService datosMemoriaService;
    private final PropietarioResolverService propietarioResolverService;

    public VehiculoResolverService(DatosMemoriaService datosMemoriaService, PropietarioResolverService propietarioResolverService) {
        this.datosMemoriaService = datosMemoriaService;
        this.propietarioResolverService = propietarioResolverService;
    }

    public List<Vehiculo> listar() {
        return datosMemoriaService.getVehiculos().stream()
                .sorted(Comparator.comparing(Vehiculo::getPlaca))
                .toList();
    }

    public Vehiculo obtener(String placa) {
        return datosMemoriaService.getVehiculos().stream()
                .filter(vehiculo -> vehiculo.getPlaca().equalsIgnoreCase(normalizar(placa)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Vehiculo no encontrado"));
    }

    public Vehiculo crear(VehiculoInput input) {
        String placa = normalizar(input.getPlaca()).toUpperCase();
        validarPlacaUnica(placa, null);
        String propietarioIdentificacion = normalizar(input.getPropietarioIdentificacion());
        propietarioResolverService.obtener(propietarioIdentificacion);
        Vehiculo vehiculo = new Vehiculo(placa, input.getMarca(), input.getModelo(), input.getTipo(), propietarioIdentificacion);
        datosMemoriaService.getVehiculos().add(vehiculo);
        datosMemoriaService.recalcularPuntosLicencias();
        return vehiculo;
    }

    public Vehiculo actualizar(String placa, VehiculoInput input) {
        String llave = normalizar(placa).toUpperCase();
        String placaInput = normalizar(input.getPlaca()).toUpperCase();
        if (!placaInput.equals(llave)) {
            throw new IllegalArgumentException("La placa es la llave del vehiculo y no se puede modificar");
        }
        String propietarioIdentificacion = normalizar(input.getPropietarioIdentificacion());
        propietarioResolverService.obtener(propietarioIdentificacion);
        Vehiculo vehiculo = obtener(llave);
        vehiculo.setMarca(input.getMarca());
        vehiculo.setModelo(input.getModelo());
        vehiculo.setTipo(input.getTipo());
        vehiculo.setPropietarioIdentificacion(propietarioIdentificacion);
        datosMemoriaService.recalcularPuntosLicencias();
        return vehiculo;
    }

    public boolean eliminar(String placa) {
        String llave = normalizar(placa).toUpperCase();
        datosMemoriaService.getInfracciones().removeIf(infraccion -> infraccion.getVehiculoPlaca().equalsIgnoreCase(llave));
        boolean eliminado = datosMemoriaService.getVehiculos().removeIf(vehiculo -> vehiculo.getPlaca().equalsIgnoreCase(llave));
        datosMemoriaService.recalcularPuntosLicencias();
        return eliminado;
    }

    private void validarPlacaUnica(String placa, String placaActual) {
        boolean existe = datosMemoriaService.getVehiculos().stream()
                .map(Vehiculo::getPlaca)
                .filter(valor -> placaActual == null || !valor.equalsIgnoreCase(placaActual))
                .anyMatch(valor -> valor.equalsIgnoreCase(placa));
        if (existe) {
            throw new IllegalArgumentException("Ya existe un vehiculo con esa placa");
        }
    }

    private String normalizar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
