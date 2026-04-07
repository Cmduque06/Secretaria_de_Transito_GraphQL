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
    private final AgenteResolverService agenteResolverService;

    public InfraccionResolverService(DatosMemoriaService datosMemoriaService,
                                     VehiculoResolverService vehiculoResolverService,
                                     AgenteResolverService agenteResolverService) {
        this.datosMemoriaService = datosMemoriaService;
        this.vehiculoResolverService = vehiculoResolverService;
        this.agenteResolverService = agenteResolverService;
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
        String vehiculoPlaca = normalizar(input.getVehiculoPlaca()).toUpperCase();
        vehiculoResolverService.obtener(vehiculoPlaca);
        DatosRegistro datosRegistro = resolverDatosRegistro(input);
        Infraccion infraccion = new Infraccion(siguienteId(), input.getCodigo(), input.getDescripcion(), input.getValor(),
                input.getSeveridad(), input.getPagada(), vehiculoPlaca,
                datosRegistro.origenRegistro(), datosRegistro.agenteIdentificacion(),
                datosRegistro.camaraCodigo(), normalizar(input.getFechaRegistro()));
        datosMemoriaService.getInfracciones().add(infraccion);
        datosMemoriaService.recalcularPuntosLicencias();
        return infraccion;
    }

    public Infraccion actualizar(Long id, InfraccionInput input) {
        String vehiculoPlaca = normalizar(input.getVehiculoPlaca()).toUpperCase();
        vehiculoResolverService.obtener(vehiculoPlaca);
        DatosRegistro datosRegistro = resolverDatosRegistro(input);
        Infraccion infraccion = obtener(id);
        infraccion.setCodigo(input.getCodigo());
        infraccion.setDescripcion(input.getDescripcion());
        infraccion.setValor(input.getValor());
        infraccion.setSeveridad(input.getSeveridad());
        infraccion.setPagada(input.getPagada());
        infraccion.setVehiculoPlaca(vehiculoPlaca);
        infraccion.setOrigenRegistro(datosRegistro.origenRegistro());
        infraccion.setAgenteIdentificacion(datosRegistro.agenteIdentificacion());
        infraccion.setCamaraCodigo(datosRegistro.camaraCodigo());
        infraccion.setFechaRegistro(normalizar(input.getFechaRegistro()));
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

    private String normalizar(String valor) {
        return valor == null ? "" : valor.trim();
    }

    private DatosRegistro resolverDatosRegistro(InfraccionInput input) {
        String origenRegistro = normalizar(input.getOrigenRegistro()).toUpperCase();
        if (!origenRegistro.equals("AGENTE") && !origenRegistro.equals("CAMARA")) {
            throw new IllegalArgumentException("El origen de la infraccion debe ser AGENTE o CAMARA");
        }

        if (origenRegistro.equals("AGENTE")) {
            String agenteIdentificacion = normalizar(input.getAgenteIdentificacion());
            if (agenteIdentificacion.isBlank()) {
                throw new IllegalArgumentException("Debes seleccionar un agente para registrar la infraccion");
            }
            agenteResolverService.obtener(agenteIdentificacion);
            return new DatosRegistro(origenRegistro, agenteIdentificacion, null);
        }

        String camaraCodigo = normalizar(input.getCamaraCodigo()).toUpperCase();
        if (camaraCodigo.isBlank()) {
            throw new IllegalArgumentException("Debes indicar el codigo de la camara que registro la infraccion");
        }
        return new DatosRegistro(origenRegistro, null, camaraCodigo);
    }

    private record DatosRegistro(String origenRegistro, String agenteIdentificacion, String camaraCodigo) {
    }
}
