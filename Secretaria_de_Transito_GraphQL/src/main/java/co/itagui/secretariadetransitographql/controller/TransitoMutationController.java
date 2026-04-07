package co.itagui.secretariadetransitographql.controller;

import co.itagui.secretariadetransitographql.graphql.input.AgenteInput;
import co.itagui.secretariadetransitographql.model.Agente;
import co.itagui.secretariadetransitographql.graphql.input.InfraccionInput;
import co.itagui.secretariadetransitographql.graphql.input.PropietarioInput;
import co.itagui.secretariadetransitographql.graphql.input.VehiculoInput;
import co.itagui.secretariadetransitographql.model.Infraccion;
import co.itagui.secretariadetransitographql.model.Propietario;
import co.itagui.secretariadetransitographql.model.Vehiculo;
import co.itagui.secretariadetransitographql.service.AgenteResolverService;
import co.itagui.secretariadetransitographql.service.InfraccionResolverService;
import co.itagui.secretariadetransitographql.service.PropietarioResolverService;
import co.itagui.secretariadetransitographql.service.VehiculoResolverService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class TransitoMutationController {

    private final AgenteResolverService agenteResolverService;
    private final PropietarioResolverService propietarioResolverService;
    private final VehiculoResolverService vehiculoResolverService;
    private final InfraccionResolverService infraccionResolverService;

    public TransitoMutationController(AgenteResolverService agenteResolverService,
                                      PropietarioResolverService propietarioResolverService,
                                      VehiculoResolverService vehiculoResolverService,
                                      InfraccionResolverService infraccionResolverService) {
        this.agenteResolverService = agenteResolverService;
        this.propietarioResolverService = propietarioResolverService;
        this.vehiculoResolverService = vehiculoResolverService;
        this.infraccionResolverService = infraccionResolverService;
    }

    @MutationMapping
    public Agente crearAgente(@Argument AgenteInput input) {
        return agenteResolverService.crear(input);
    }

    @MutationMapping
    public Agente actualizarAgente(@Argument String identificacion, @Argument AgenteInput input) {
        return agenteResolverService.actualizar(identificacion, input);
    }

    @MutationMapping
    public Boolean eliminarAgente(@Argument String identificacion) {
        return agenteResolverService.eliminar(identificacion);
    }

    @MutationMapping
    public Propietario crearPropietario(@Argument PropietarioInput input) {
        return propietarioResolverService.crear(input);
    }

    @MutationMapping
    public Propietario actualizarPropietario(@Argument String identificacion, @Argument PropietarioInput input) {
        return propietarioResolverService.actualizar(identificacion, input);
    }

    @MutationMapping
    public Boolean eliminarPropietario(@Argument String identificacion) {
        return propietarioResolverService.eliminar(identificacion);
    }

    @MutationMapping
    public Vehiculo crearVehiculo(@Argument VehiculoInput input) {
        return vehiculoResolverService.crear(input);
    }

    @MutationMapping
    public Vehiculo actualizarVehiculo(@Argument String placa, @Argument VehiculoInput input) {
        return vehiculoResolverService.actualizar(placa, input);
    }

    @MutationMapping
    public Boolean eliminarVehiculo(@Argument String placa) {
        return vehiculoResolverService.eliminar(placa);
    }

    @MutationMapping
    public Infraccion crearInfraccion(@Argument InfraccionInput input) {
        return infraccionResolverService.crear(input);
    }

    @MutationMapping
    public Infraccion actualizarInfraccion(@Argument Long id, @Argument InfraccionInput input) {
        return infraccionResolverService.actualizar(id, input);
    }

    @MutationMapping
    public Boolean eliminarInfraccion(@Argument Long id) {
        return infraccionResolverService.eliminar(id);
    }
}
