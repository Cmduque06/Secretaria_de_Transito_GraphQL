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
                .sorted(Comparator.comparing(Propietario::getIdentificacion))
                .toList();
    }

    public Propietario obtener(String identificacion) {
        return datosMemoriaService.getPropietarios().stream()
                .filter(propietario -> propietario.getIdentificacion().equalsIgnoreCase(normalizar(identificacion)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Propietario no encontrado"));
    }

    public Propietario crear(PropietarioInput input) {
        String identificacion = normalizar(input.getIdentificacion());
        validarIdentificacionUnica(identificacion, null);
        Propietario propietario = new Propietario(
                input.getTipo(),
                identificacion,
                input.getNombre(),
                input.getDireccion(),
                new LicenciaConduccion(input.getNumeroLicencia(), input.getCategoriaLicencia(), 20, 20)
        );
        datosMemoriaService.getPropietarios().add(propietario);
        return propietario;
    }

    public Propietario actualizar(String identificacion, PropietarioInput input) {
        String llave = normalizar(identificacion);
        Propietario propietario = obtener(llave);
        if (input.getIdentificacion() != null && !normalizar(input.getIdentificacion()).equalsIgnoreCase(llave)) {
            throw new IllegalArgumentException("La identificacion es la llave del propietario y no se puede modificar");
        }
        propietario.setTipo(input.getTipo());
        propietario.setNombre(input.getNombre());
        propietario.setDireccion(input.getDireccion());
        propietario.getLicencia().setNumero(input.getNumeroLicencia());
        propietario.getLicencia().setCategoria(input.getCategoriaLicencia());
        datosMemoriaService.recalcularPuntosLicencias();
        return propietario;
    }

    public boolean eliminar(String identificacion) {
        String llave = normalizar(identificacion);
        boolean tieneVehiculos = datosMemoriaService.getVehiculos().stream()
                .anyMatch(vehiculo -> vehiculo.getPropietarioIdentificacion().equalsIgnoreCase(llave));
        if (tieneVehiculos) {
            throw new IllegalArgumentException("No se puede eliminar el propietario porque tiene vehiculos asociados");
        }
        return datosMemoriaService.getPropietarios().removeIf(propietario -> propietario.getIdentificacion().equalsIgnoreCase(llave));
    }

    private void validarIdentificacionUnica(String identificacion, String identificacionActual) {
        boolean existe = datosMemoriaService.getPropietarios().stream()
                .map(Propietario::getIdentificacion)
                .filter(valor -> identificacionActual == null || !valor.equalsIgnoreCase(identificacionActual))
                .anyMatch(valor -> valor.equalsIgnoreCase(identificacion));
        if (existe) {
            throw new IllegalArgumentException("Ya existe un propietario con esa identificacion");
        }
    }

    private String normalizar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
