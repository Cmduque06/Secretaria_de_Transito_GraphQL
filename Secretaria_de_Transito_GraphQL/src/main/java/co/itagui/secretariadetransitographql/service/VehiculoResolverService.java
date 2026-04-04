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
                .sorted(Comparator.comparing(Vehiculo::getId))
                .toList();
    }

    public Vehiculo obtener(Long id) {
        return datosMemoriaService.getVehiculos().stream()
                .filter(vehiculo -> vehiculo.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Vehiculo no encontrado"));
    }

    public Vehiculo crear(VehiculoInput input) {
        propietarioResolverService.obtener(input.getPropietarioId());
        Vehiculo vehiculo = new Vehiculo(siguienteId(), input.getPlaca(), input.getMarca(), input.getModelo(), input.getTipo(), input.getPropietarioId());
        datosMemoriaService.getVehiculos().add(vehiculo);
        return vehiculo;
    }

    public Vehiculo actualizar(Long id, VehiculoInput input) {
        propietarioResolverService.obtener(input.getPropietarioId());
        Vehiculo vehiculo = obtener(id);
        vehiculo.setPlaca(input.getPlaca());
        vehiculo.setMarca(input.getMarca());
        vehiculo.setModelo(input.getModelo());
        vehiculo.setTipo(input.getTipo());
        vehiculo.setPropietarioId(input.getPropietarioId());
        datosMemoriaService.recalcularPuntosLicencias();
        return vehiculo;
    }

    public boolean eliminar(Long id) {
        datosMemoriaService.getInfracciones().removeIf(infraccion -> infraccion.getVehiculoId().equals(id));
        boolean eliminado = datosMemoriaService.getVehiculos().removeIf(vehiculo -> vehiculo.getId().equals(id));
        datosMemoriaService.recalcularPuntosLicencias();
        return eliminado;
    }

    private Long siguienteId() {
        return datosMemoriaService.getVehiculos().stream()
                .map(Vehiculo::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }
}
