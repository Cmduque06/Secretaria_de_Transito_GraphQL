package co.itagui.secretariadetransitographql.service;

import co.itagui.secretariadetransitographql.graphql.input.AgenteInput;
import co.itagui.secretariadetransitographql.model.Agente;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AgenteResolverService {

    private final DatosMemoriaService datosMemoriaService;

    public AgenteResolverService(DatosMemoriaService datosMemoriaService) {
        this.datosMemoriaService = datosMemoriaService;
    }

    public List<Agente> listar() {
        return datosMemoriaService.getAgentes().stream()
                .sorted(Comparator.comparing(Agente::getIdentificacion))
                .toList();
    }

    public Agente obtener(String identificacion) {
        String llave = normalizar(identificacion);
        return datosMemoriaService.getAgentes().stream()
                .filter(agente -> agente.getIdentificacion().equalsIgnoreCase(llave))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Agente no encontrado"));
    }

    public Agente crear(AgenteInput input) {
        String identificacion = normalizar(input.getIdentificacion());
        validarIdentificacionUnica(identificacion, null);
        Agente agente = new Agente(
                identificacion,
                input.getNombre(),
                input.getPlacaInstitucional(),
                input.getRango(),
                input.getZonaOperativa(),
                Boolean.TRUE.equals(input.getActivo())
        );
        datosMemoriaService.getAgentes().add(agente);
        return agente;
    }

    public Agente actualizar(String identificacion, AgenteInput input) {
        String llave = normalizar(identificacion);
        Agente agente = obtener(llave);
        if (input.getIdentificacion() != null && !normalizar(input.getIdentificacion()).equalsIgnoreCase(llave)) {
            throw new IllegalArgumentException("La identificacion es la llave del agente y no se puede modificar");
        }
        agente.setNombre(input.getNombre());
        agente.setPlacaInstitucional(input.getPlacaInstitucional());
        agente.setRango(input.getRango());
        agente.setZonaOperativa(input.getZonaOperativa());
        agente.setActivo(Boolean.TRUE.equals(input.getActivo()));
        return agente;
    }

    public boolean eliminar(String identificacion) {
        String llave = normalizar(identificacion);
        boolean tieneInfracciones = datosMemoriaService.getInfracciones().stream()
                .anyMatch(infraccion -> llave.equalsIgnoreCase(infraccion.getAgenteIdentificacion()));
        if (tieneInfracciones) {
            throw new IllegalArgumentException("No se puede eliminar el agente porque tiene infracciones registradas");
        }
        return datosMemoriaService.getAgentes().removeIf(agente -> agente.getIdentificacion().equalsIgnoreCase(llave));
    }

    private void validarIdentificacionUnica(String identificacion, String actual) {
        boolean existe = datosMemoriaService.getAgentes().stream()
                .map(Agente::getIdentificacion)
                .filter(valor -> actual == null || !valor.equalsIgnoreCase(actual))
                .anyMatch(valor -> valor.equalsIgnoreCase(identificacion));
        if (existe) {
            throw new IllegalArgumentException("Ya existe un agente con esa identificacion");
        }
    }

    private String normalizar(String valor) {
        return valor == null ? "" : valor.trim();
    }
}
