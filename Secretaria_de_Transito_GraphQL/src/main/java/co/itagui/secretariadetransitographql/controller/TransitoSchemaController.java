package co.itagui.secretariadetransitographql.controller;

import co.itagui.secretariadetransitographql.model.Infraccion;
import co.itagui.secretariadetransitographql.model.Propietario;
import co.itagui.secretariadetransitographql.model.Vehiculo;
import co.itagui.secretariadetransitographql.service.DatosMemoriaService;
import java.util.List;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class TransitoSchemaController {

    private final DatosMemoriaService datosMemoriaService;

    public TransitoSchemaController(DatosMemoriaService datosMemoriaService) {
        this.datosMemoriaService = datosMemoriaService;
    }

    @SchemaMapping(typeName = "Propietario", field = "vehiculos")
    public List<Vehiculo> vehiculos(Propietario propietario) {
        return datosMemoriaService.getVehiculos().stream()
                .filter(vehiculo -> vehiculo.getPropietarioId().equals(propietario.getId()))
                .toList();
    }

    @SchemaMapping(typeName = "Vehiculo", field = "propietario")
    public Propietario propietario(Vehiculo vehiculo) {
        return datosMemoriaService.getPropietarios().stream()
                .filter(item -> item.getId().equals(vehiculo.getPropietarioId()))
                .findFirst()
                .orElse(null);
    }

    @SchemaMapping(typeName = "Vehiculo", field = "infracciones")
    public List<Infraccion> infracciones(Vehiculo vehiculo) {
        return datosMemoriaService.getInfracciones().stream()
                .filter(infraccion -> infraccion.getVehiculoId().equals(vehiculo.getId()))
                .toList();
    }

    @SchemaMapping(typeName = "Infraccion", field = "vehiculo")
    public Vehiculo vehiculo(Infraccion infraccion) {
        return datosMemoriaService.getVehiculos().stream()
                .filter(item -> item.getId().equals(infraccion.getVehiculoId()))
                .findFirst()
                .orElse(null);
    }
}
