package co.itagui.secretariadetransitographql.service;

import co.itagui.secretariadetransitographql.graphql.input.InfraccionInput;
import co.itagui.secretariadetransitographql.model.Infraccion;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class InfraccionResolverService {

    private final DatosMemoriaService datosMemoriaService;
    private final VehiculoResolverService vehiculoResolverService;

    public InfraccionResolverService(DatosMemoriaService datosMemoriaService, VehiculoResolverService vehiculoResolverService) {
        this.datosMemoriaService = datosMemoriaService;
        this.vehiculoResolverService = vehiculoResolverService;
    }

    public List<Infraccion> listar() {
        return datosMemoriaService.getInfracciones().stream()
                .sorted(Comparator.comparing(Infraccion::getId))
                .toList();
    }

    public Infraccion obtener(Long id) {
        return datosMemoriaService.getInfracciones().stream()
                .filter(infraccion -> infraccion.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Infraccion no encontrada"));
    }

    public Infraccion crear(InfraccionInput input) {
        vehiculoResolverService.obtener(input.getVehiculoId());
        Infraccion infraccion = new Infraccion(siguienteId(), input.getCodigo(), input.getDescripcion(), input.getValor(),
                input.getSeveridad(), input.getPagada(), input.getVehiculoId());
        datosMemoriaService.getInfracciones().add(infraccion);
        datosMemoriaService.recalcularPuntosLicencias();
        return infraccion;
    }

    public Infraccion actualizar(Long id, InfraccionInput input) {
        vehiculoResolverService.obtener(input.getVehiculoId());
        Infraccion infraccion = obtener(id);
        infraccion.setCodigo(input.getCodigo());
        infraccion.setDescripcion(input.getDescripcion());
        infraccion.setValor(input.getValor());
        infraccion.setSeveridad(input.getSeveridad());
        infraccion.setPagada(input.getPagada());
        infraccion.setVehiculoId(input.getVehiculoId());
        datosMemoriaService.recalcularPuntosLicencias();
        return infraccion;
    }

    public boolean eliminar(Long id) {
        boolean eliminado = datosMemoriaService.getInfracciones().removeIf(infraccion -> infraccion.getId().equals(id));
        datosMemoriaService.recalcularPuntosLicencias();
        return eliminado;
    }

    private Long siguienteId() {
        return datosMemoriaService.getInfracciones().stream()
                .map(Infraccion::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }
}
