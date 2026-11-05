package edu.upc.dsa.services;

import edu.upc.dsa.DronesManager;
import edu.upc.dsa.DronesManagerImpl;
import edu.upc.dsa.models.Dron;
import edu.upc.dsa.models.Piloto;
import edu.upc.dsa.models.PlanVuelo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Api(value = "/drones", description = "Servicio API REST para la gestión de flota de drones")
@Path("/drones")
public class DronesService {
    private DronesManager tm;

    public DronesService() {
        this.tm = DronesManagerImpl.getInstance();
        if (tm.sizeDrones() == 0) {
            tm.addDron("D1", "Mavic 3", "DJI", "Pro");
            tm.addPiloto("P1", "Carlos", "Sáez");
        }
    }

    @POST
    @Path("/addDron")
    @ApiOperation(value = "Añadir un nuevo dron")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Dron creado exitosamente", response = Dron.class),
            @ApiResponse(code = 400, message = "Datos de petición incorrectos")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addDron(Dron dron) {
        if (dron.getId() == null || dron.getNombre() == null) {
            return Response.status(400).entity("ID y Nombre son obligatorios").build();
        }
        Dron d = this.tm.addDron(dron.getId(), dron.getNombre(), dron.getFabricante(), dron.getModelo());
        return Response.status(201).entity(d).build();
    }

    @POST
    @Path("/addPiloto")
    @ApiOperation(value = "Añadir un nuevo piloto")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Piloto creado exitosamente", response = Piloto.class),
            @ApiResponse(code = 400, message = "Datos de petición incorrectos")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPiloto(Piloto piloto) {
        if (piloto.getId() == null || piloto.getNombre() == null) {
            return Response.status(400).entity("ID y Nombre son obligatorios").build();
        }
        Piloto p = this.tm.addPiloto(piloto.getId(), piloto.getNombre(), piloto.getApellidos());
        return Response.status(201).entity(p).build();
    }

    @POST
    @Path("/almacen/{idDron}")
    @ApiOperation(value = "Guardar un dron en el almacén de mantenimiento")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Dron ingresado en almacén", response = Dron.class),
            @ApiResponse(code = 404, message = "Dron no encontrado")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response guardarEnAlmacen(@PathParam("idDron") String idDron) {
        try {
            Dron d = this.tm.guardarDronEnAlmacen(idDron);
            return Response.status(200).entity(d).build();
        } catch (Exception e) {
            return Response.status(404).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/almacen/reparar")
    @ApiOperation(value = "Reparar y desapilar el dron más cercano a la puerta (LIFO)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Dron reparado con éxito", response = Dron.class),
            @ApiResponse(code = 404, message = "Almacén vacío")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response repararDron() {
        try {
            Dron d = this.tm.repararDronEnAlmacen();
            return Response.status(200).entity(d).build();
        } catch (Exception e) {
            return Response.status(404).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/planVuelo")
    @ApiOperation(value = "Crear un nuevo plan de vuelo")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Plan de vuelo creado exitosamente", response = PlanVuelo.class),
            @ApiResponse(code = 400, message = "Dron/Piloto no encontrado, conflicto de fechas o dron en almacén")
    })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearPlanVuelo(PlanVuelo plan) {
        try {
            PlanVuelo p = this.tm.crearPlanVuelo(plan.getIdDron(), plan.getIdPiloto(), plan.getFecha(), plan.getTiempoEstimado(), plan.getPosiciones());
            return Response.status(201).entity(p).build();
        } catch (Exception e) {
            return Response.status(400).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/drones/horas")
    @ApiOperation(value = "Obtener lista de drones ordenados por horas de vuelo descendentemente")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista de drones obtenida exitosamente")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDronesOrdenados() {
        List<Dron> list = this.tm.getDronesOrdenadosPorHorasVuelo();
        GenericEntity<List<Dron>> entity = new GenericEntity<List<Dron>>(list) {};
        return Response.status(200).entity(entity).build();
    }

    @GET
    @Path("/pilotos/horas")
    @ApiOperation(value = "Obtener lista de pilotos ordenados por horas de vuelo descendentemente")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Lista de pilotos obtenida exitosamente")
    })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPilotosOrdenados() {
        List<Piloto> list = this.tm.getPilotosOrdenadosPorHorasVuelo();
        GenericEntity<List<Piloto>> entity = new GenericEntity<List<Piloto>>(list) {};
        return Response.status(200).entity(entity).build();
    }
}