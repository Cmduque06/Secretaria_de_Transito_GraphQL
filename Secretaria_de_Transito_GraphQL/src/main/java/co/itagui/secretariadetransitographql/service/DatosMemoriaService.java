package co.itagui.secretariadetransitographql.service;

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

    @PostConstruct
    public void cargarDatos() {
        propietarios.add(new Propietario(1L, "PERSONA", "1020304050", "Laura Gomez", "Calle 10 # 20-30",
                new LicenciaConduccion(1L, "LIC-1001", "B1", 20, 20)));
        propietarios.add(new Propietario(2L, "EMPRESA", "900123456-7", "Transportes Andinos", "Av. Industrial 45-60",
                new LicenciaConduccion(2L, "LIC-1002", "C2", 20, 20)));
        propietarios.add(new Propietario(3L, "PERSONA", "1122334455", "Carlos Ramirez", "Cra. 50 # 80-12",
                new LicenciaConduccion(3L, "LIC-1003", "A2", 20, 20)));

        vehiculos.add(new Vehiculo(1L, "ABC123", "Mazda", "2021", "AUTOMOVIL", 1L));
        vehiculos.add(new Vehiculo(2L, "MOT778", "Yamaha", "2020", "MOTO", 3L));
        vehiculos.add(new Vehiculo(3L, "TRK901", "Chevrolet", "2019", "CAMION", 2L));

        infracciones.add(new Infraccion(1L, "C29", "Exceso de velocidad", 650000, "GRAVE", false, 1L));
        infracciones.add(new Infraccion(2L, "D12", "Estacionar en sitio prohibido", 250000, "LEVE", true, 1L));
        infracciones.add(new Infraccion(3L, "B01", "No respetar semaforo en rojo", 520000, "MEDIA", false, 3L));
        infracciones.add(new Infraccion(4L, "F18", "Circular sin revision tecnico mecanica", 480000, "MEDIA", false, 2L));

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

    public void recalcularPuntosLicencias() {
        for (Propietario propietario : propietarios) {
            int base = propietario.getLicencia().getPuntosBase();
            int descuento = vehiculos.stream()
                    .filter(vehiculo -> vehiculo.getPropietarioId().equals(propietario.getId()))
                    .mapToInt(vehiculo -> infracciones.stream()
                            .filter(infraccion -> infraccion.getVehiculoId().equals(vehiculo.getId()))
                            .mapToInt(infraccion -> puntosPorSeveridad(infraccion.getSeveridad()))
                            .sum())
                    .sum();
            propietario.getLicencia().setPuntosActuales(Math.max(0, base - descuento));
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
