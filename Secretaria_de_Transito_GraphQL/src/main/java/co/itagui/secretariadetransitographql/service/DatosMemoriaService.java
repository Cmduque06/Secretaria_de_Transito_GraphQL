package co.itagui.secretariadetransitographql.service;

import co.itagui.secretariadetransitographql.model.Agente;
import co.itagui.secretariadetransitographql.model.Infraccion;
import co.itagui.secretariadetransitographql.model.LicenciaConduccion;
import co.itagui.secretariadetransitographql.model.Propietario;
import co.itagui.secretariadetransitographql.model.Vehiculo;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DatosMemoriaService {

    private final List<Propietario> propietarios = new ArrayList<>();
    private final List<Vehiculo> vehiculos = new ArrayList<>();
    private final List<Infraccion> infracciones = new ArrayList<>();
    private final List<Agente> agentes = new ArrayList<>();

    @PostConstruct
    public void cargarDatos() {
        propietarios.add(new Propietario("PERSONA", "1020304050", "Laura Gomez", "Calle 10 # 20-30",
                new LicenciaConduccion("LIC-1001", "B1", 20, 20, false, "Sin suspension")));
        propietarios.add(new Propietario("EMPRESA", "900123456-7", "Transportes Andinos", "Av. Industrial 45-60",
                new LicenciaConduccion("LIC-1002", "C2", 20, 20, false, "Sin suspension")));
        propietarios.add(new Propietario("PERSONA", "1122334455", "Carlos Ramirez", "Cra. 50 # 80-12",
                new LicenciaConduccion("LIC-1003", "A2", 20, 20, false, "Sin suspension")));

        agentes.add(new Agente("7001001", "Sandra Ospina", "AG-021", "Subintendente", "Centro", true));
        agentes.add(new Agente("7001002", "Mario Restrepo", "AG-145", "Patrullero", "Autopista", true));

        vehiculos.add(new Vehiculo("ABC123", "Mazda", "2021", "AUTOMOVIL", "1020304050"));
        vehiculos.add(new Vehiculo("MOT778", "Yamaha", "2020", "MOTO", "1122334455"));
        vehiculos.add(new Vehiculo("TRK901", "Chevrolet", "2019", "CAMION", "900123456-7"));

        infracciones.add(new Infraccion(1L, "C29", "Exceso de velocidad", 650000, "GRAVE", false,
                "ABC123", "CAMARA", null, "CAM-AV-101", "2026-03-01"));
        infracciones.add(new Infraccion(2L, "D12", "Estacionar en sitio prohibido", 250000, "LEVE", true,
                "ABC123", "AGENTE", "7001001", null, "2026-03-08"));
        infracciones.add(new Infraccion(3L, "B01", "No respetar semaforo en rojo", 520000, "MEDIA", false,
                "TRK901", "CAMARA", null, "CAM-SUR-22", "2026-03-10"));
        infracciones.add(new Infraccion(4L, "F18", "Circular sin revision tecnico mecanica", 480000, "MEDIA", false,
                "MOT778", "AGENTE", "7001002", null, "2026-03-12"));

        recalcularPuntosLicencias();
    }

    public List<Propietario> getPropietarios() {
        return propietarios;
    }

    public List<Vehiculo> getVehiculos() {
        return vehiculos;
    }

    public List<Infraccion> getInfracciones() {
        return infracciones;
    }

    public List<Agente> getAgentes() {
        return agentes;
    }

    public void recalcularPuntosLicencias() {
        for (Propietario propietario : propietarios) {
            int base = propietario.getLicencia().getPuntosBase();
            int descuento = vehiculos.stream()
                    .filter(vehiculo -> vehiculo.getPropietarioIdentificacion().equals(propietario.getIdentificacion()))
                    .mapToInt(vehiculo -> infracciones.stream()
                            .filter(infraccion -> infraccion.getVehiculoPlaca().equals(vehiculo.getPlaca()))
                            .mapToInt(infraccion -> puntosPorSeveridad(infraccion.getSeveridad()))
                            .sum())
                    .sum();
            int puntosActuales = Math.max(0, base - descuento);
            propietario.getLicencia().setPuntosActuales(puntosActuales);
            boolean suspendida = puntosActuales == 0;
            propietario.getLicencia().setSuspendida(suspendida);
            propietario.getLicencia().setMotivoSuspension(suspendida
                    ? "Licencia suspendida por perdida total de puntos"
                    : "Sin suspension");
        }
    }

    private int puntosPorSeveridad(String severidad) {
        return switch (severidad.toUpperCase()) {
            case "LEVE" -> 2;
            case "MEDIA" -> 4;
            case "GRAVE" -> 6;
            default -> 1;
        };
    }
}
