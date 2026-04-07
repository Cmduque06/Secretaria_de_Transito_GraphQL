package co.itagui.secretariadetransitographql.controller;

import co.itagui.secretariadetransitographql.model.Infraccion;
import co.itagui.secretariadetransitographql.model.Propietario;
import co.itagui.secretariadetransitographql.model.Vehiculo;
import co.itagui.secretariadetransitographql.model.report.ConductorPuntosReporte;
import co.itagui.secretariadetransitographql.model.report.VehiculoInfraccionesReporte;
import co.itagui.secretariadetransitographql.service.InfraccionResolverService;
import co.itagui.secretariadetransitographql.service.PropietarioResolverService;
import co.itagui.secretariadetransitographql.service.ReporteResolverService;
import co.itagui.secretariadetransitographql.service.VehiculoResolverService;
import java.util.List;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class TransitoQueryController {

    private final PropietarioResolverService propietarioResolverService;
    private final VehiculoResolverService vehiculoResolverService;
    private final InfraccionResolverService infraccionResolverService;
    private final ReporteResolverService reporteResolverService;

    public TransitoQueryController(PropietarioResolverService propietarioResolverService,
                                   VehiculoResolverService vehiculoResolverService,
                                   InfraccionResolverService infraccionResolverService,
                                   ReporteResolverService reporteResolverService) {
        this.propietarioResolverService = propietarioResolverService;
        this.vehiculoResolverService = vehiculoResolverService;
        this.infraccionResolverService = infraccionResolverService;
        this.reporteResolverService = reporteResolverService;
    }

    @QueryMapping
    public List<Propietario> propietarios() {
        return propietarioResolverService.listar();
    }

    @QueryMapping
    public Propietario propietario(@Argument String identificacion) {
        return propietarioResolverService.obtener(identificacion);
    }

    @QueryMapping
    public List<Vehiculo> vehiculos() {
        return vehiculoResolverService.listar();
    }

    @QueryMapping
    public Vehiculo vehiculo(@Argument String placa) {
        return vehiculoResolverService.obtener(placa);
    }

    @QueryMapping
    public List<Infraccion> infracciones() {
        return infraccionResolverService.listar();
    }

    @QueryMapping
    public Infraccion infraccion(@Argument Long id) {
        return infraccionResolverService.obtener(id);
    }

    @QueryMapping
    public List<VehiculoInfraccionesReporte> vehiculosConMasInfracciones() {
        return reporteResolverService.vehiculosConMasInfracciones();
    }

    @QueryMapping
    public List<VehiculoInfraccionesReporte> totalInfraccionesPorVehiculo() {
        return reporteResolverService.totalInfraccionesPorVehiculo();
    }

    @QueryMapping
    public List<ConductorPuntosReporte> conductoresConMenosPuntos() {
        return reporteResolverService.conductoresConMenosPuntos();
    }

    @QueryMapping
    public Double totalDineroRecaudadoPorMultas() {
        return reporteResolverService.totalDineroRecaudadoPorMultas();
    }
}
