package co.itagui.secretariadetransitographql.service;

import co.itagui.secretariadetransitographql.graphql.input.PropietarioInput;
import co.itagui.secretariadetransitographql.model.LicenciaConduccion;
import co.itagui.secretariadetransitographql.model.Propietario;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PropietarioResolverService {

    private final DatosMemoriaService datosMemoriaService;

    public PropietarioResolverService(DatosMemoriaService datosMemoriaService) {
        this.datosMemoriaService = datosMemoriaService;
    }

    public List<Propietario> listar() {
        return datosMemoriaService.getPropietarios().stream()
                .sorted(Comparator.comparing(Propietario::getId))
                .toList();
    }

    public Propietario obtener(Long id) {
        return datosMemoriaService.getPropietarios().stream()
                .filter(propietario -> propietario.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Propietario no encontrado"));
    }

    public Propietario crear(PropietarioInput input) {
        Long nuevoId = siguienteId();
        int puntosBase = input.getPuntosBase() == null ? 20 : input.getPuntosBase();
        Propietario propietario = new Propietario(
                nuevoId,
                input.getTipo(),
                input.getIdentificacion(),
                input.getNombre(),
                input.getDireccion(),
                new LicenciaConduccion(nuevoId, input.getNumeroLicencia(), input.getCategoriaLicencia(), puntosBase, puntosBase)
        );
        datosMemoriaService.getPropietarios().add(propietario);
        return propietario;
    }

    public Propietario actualizar(Long id, PropietarioInput input) {
        Propietario propietario = obtener(id);
        propietario.setTipo(input.getTipo());
        propietario.setIdentificacion(input.getIdentificacion());
        propietario.setNombre(input.getNombre());
        propietario.setDireccion(input.getDireccion());
        propietario.getLicencia().setNumero(input.getNumeroLicencia());
        propietario.getLicencia().setCategoria(input.getCategoriaLicencia());
        if (input.getPuntosBase() != null) {
            propietario.getLicencia().setPuntosBase(input.getPuntosBase());
        }
        datosMemoriaService.recalcularPuntosLicencias();
        return propietario;
    }

    public boolean eliminar(Long id) {
        boolean tieneVehiculos = datosMemoriaService.getVehiculos().stream()
                .anyMatch(vehiculo -> vehiculo.getPropietarioId().equals(id));
        if (tieneVehiculos) {
            throw new IllegalArgumentException("No se puede eliminar el propietario porque tiene vehiculos asociados");
        }
        return datosMemoriaService.getPropietarios().removeIf(propietario -> propietario.getId().equals(id));
    }

    private Long siguienteId() {
        return datosMemoriaService.getPropietarios().stream()
                .map(Propietario::getId)
                .max(Long::compareTo)
                .orElse(0L) + 1;
    }
}
