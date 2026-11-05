package edu.upc.dsa;

import edu.upc.dsa.models.Dron;
import edu.upc.dsa.models.Piloto;
import edu.upc.dsa.models.PlanVuelo;
import edu.upc.dsa.models.Posicion;

import java.util.List;

public interface DronesManager {
    Dron addDron(String id, String nombre, String fabricante, String modelo);
    Piloto addPiloto(String id, String nombre, String apellidos);

    List<Dron> getDronesOrdenadosPorHorasVuelo();
    List<Piloto> getPilotosOrdenadosPorHorasVuelo();

    Dron guardarDronEnAlmacen(String idDron) throws Exception;
    Dron repararDronEnAlmacen() throws Exception;

    PlanVuelo crearPlanVuelo(String idDron, String idPiloto, String fecha, double tiempoEstimado, List<Posicion> posiciones) throws Exception;

    List<PlanVuelo> getPlanesVueloPorPiloto(String idPiloto) throws Exception;
    List<PlanVuelo> getPlanesVueloPorDron(String idDron) throws Exception;

    int sizeDrones();
    int sizePilotos();
    int sizeAlmacen();
    void clear();
}