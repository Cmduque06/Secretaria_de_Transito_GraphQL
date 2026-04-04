package co.itagui.secretariadetransitographql.controller;

import co.itagui.secretariadetransitographql.graphql.input.InfraccionInput;
import co.itagui.secretariadetransitographql.graphql.input.PropietarioInput;
import co.itagui.secretariadetransitographql.graphql.input.VehiculoInput;
import co.itagui.secretariadetransitographql.model.Infraccion;
import co.itagui.secretariadetransitographql.model.Propietario;
import co.itagui.secretariadetransitographql.model.Vehiculo;
import co.itagui.secretariadetransitographql.service.InfraccionResolverService;
import co.itagui.secretariadetransitographql.service.PropietarioResolverService;
import co.itagui.secretariadetransitographql.service.VehiculoResolverService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class TransitoMutationController {

    private final PropietarioResolverService propietarioResolverService;
    private final VehiculoResolverService vehiculoResolverService;
    private final InfraccionResolverService infraccionResolverService;

    public TransitoMutationController(PropietarioResolverService propietarioResolverService,
                                      VehiculoResolverService vehiculoResolverService,
                                      InfraccionResolverService infraccionResolverService) {
        this.propietarioResolverService = propietarioResolverService;
        this.vehiculoResolverService = vehiculoResolverService;
        this.infraccionResolverService = infraccionResolverService;
    }

    @MutationMapping
    public Propietario crearPropietario(@Argument PropietarioInput input) {
        return propietarioResolverService.crear(input);
    }

    @MutationMapping
    public Propietario actualizarPropietario(@Argument Long id, @Argument PropietarioInput input) {
        return propietarioResolverService.actualizar(id, input);
    }

    @MutationMapping
    public Boolean eliminarPropietario(@Argument Long id) {
        return propietarioResolverService.eliminar(id);
    }

    @MutationMapping
    public Vehiculo crearVehiculo(@Argument VehiculoInput input) {
        return vehiculoResolverService.crear(input);
    }

    @MutationMapping
    public Vehiculo actualizarVehiculo(@Argument Long id, @Argument VehiculoInput input) {
        return vehiculoResolverService.actualizar(id, input);
    }

    @MutationMapping
    public Boolean eliminarVehiculo(@Argument Long id) {
        return vehiculoResolverService.eliminar(id);
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
