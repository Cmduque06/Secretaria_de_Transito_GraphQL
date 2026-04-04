package co.itagui.secretariadetransitographql.service;

import co.itagui.secretariadetransitographql.model.Propietario;
import co.itagui.secretariadetransitographql.model.Vehiculo;
import co.itagui.secretariadetransitographql.model.report.ConductorPuntosReporte;
import co.itagui.secretariadetransitographql.model.report.VehiculoInfraccionesReporte;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ReporteResolverService {

    private final DatosMemoriaService datosMemoriaService;

    public ReporteResolverService(DatosMemoriaService datosMemoriaService) {
        this.datosMemoriaService = datosMemoriaService;
    }

    public List<VehiculoInfraccionesReporte> vehiculosConMasInfracciones() {
        return datosMemoriaService.getVehiculos().stream()
                .map(this::mapVehiculoReporte)
                .sorted(Comparator.comparingInt(VehiculoInfraccionesReporte::getTotalInfracciones).reversed())
                .toList();
    }

    public List<VehiculoInfraccionesReporte> totalInfraccionesPorVehiculo() {
        return vehiculosConMasInfracciones();
    }

    public List<ConductorPuntosReporte> conductoresConMenosPuntos() {
        return datosMemoriaService.getPropietarios().stream()
                .map(propietario -> new ConductorPuntosReporte(
                        propietario.getId(),
                        propietario.getNombre(),
                        propietario.getLicencia().getNumero(),
                        propietario.getLicencia().getPuntosActuales()
                ))
                .sorted(Comparator.comparingInt(ConductorPuntosReporte::getPuntosActuales))
                .toList();
    }

    public double totalDineroRecaudadoPorMultas() {
        return datosMemoriaService.getInfracciones().stream()
                .filter(infraccion -> infraccion.isPagada())
                .mapToDouble(infraccion -> infraccion.getValor())
                .sum();
    }

    private VehiculoInfraccionesReporte mapVehiculoReporte(Vehiculo vehiculo) {
        int total = (int) datosMemoriaService.getInfracciones().stream()
                .filter(infraccion -> infraccion.getVehiculoId().equals(vehiculo.getId()))
                .count();

        Propietario propietario = datosMemoriaService.getPropietarios().stream()
                .filter(item -> item.getId().equals(vehiculo.getPropietarioId()))
                .findFirst()
                .orElse(null);

        return new VehiculoInfraccionesReporte(
                vehiculo.getId(),
                vehiculo.getPlaca(),
                propietario != null ? propietario.getNombre() : "Sin propietario",
                total
        );
    }
}
