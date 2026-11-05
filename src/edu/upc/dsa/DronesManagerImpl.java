package edu.upc.dsa;

import edu.upc.dsa.models.Dron;
import edu.upc.dsa.models.Piloto;
import edu.upc.dsa.models.PlanVuelo;
import edu.upc.dsa.models.Posicion;
import org.apache.log4j.Logger;

import java.util.*;

public class DronesManagerImpl implements DronesManager {
    private static DronesManagerImpl instance;
    private static final Logger logger = Logger.getLogger(DronesManagerImpl.class);

    private Map<String, Dron> drones;
    private Map<String, Piloto> pilots;
    private Deque<Dron> almacen; // Stack LIFO

    private DronesManagerImpl() {
        this.drones = new HashMap<>();
        this.pilots = new HashMap<>();
        this.almacen = new LinkedList<>();
    }

    public static DronesManagerImpl getInstance() {
        if (instance == null) {
            instance = new DronesManagerImpl();
        }
        return instance;
    }

    @Override
    public Dron addDron(String id, String nombre, String fabricante, String modelo) {
        logger.info("INICIO addDron - Parámetros: id=" + id + ", nombre=" + nombre + ", fabricante=" + fabricante + ", modelo=" + modelo);
        Dron d = new Dron(id, nombre, fabricante, modelo);
        this.drones.put(id, d);
        logger.info("FIN addDron - Resultado: Dron registrado correctamente con ID " + d.getId());
        return d;
    }

    @Override
    public Piloto addPiloto(String id, String nombre, String apellidos) {
        logger.info("INICIO addPiloto - Parámetros: id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos);
        Piloto p = new Piloto(id, nombre, apellidos);
        this.pilots.put(id, p);
        logger.info("FIN addPiloto - Resultado: Piloto registrado correctamente con ID " + p.getId());
        return p;
    }

    @Override
    public List<Dron> getDronesOrdenadosPorHorasVuelo() {
        logger.info("INICIO getDronesOrdenadosPorHorasVuelo - Sin parámetros");
        List<Dron> lista = new ArrayList<>(this.drones.values());
        lista.sort((d1, d2) -> Double.compare(d2.getHorasVuelo(), d1.getHorasVuelo()));
        logger.info("FIN getDronesOrdenadosPorHorasVuelo - Resultado: Devolviendo " + lista.size() + " drones ordenados");
        return lista;
    }

    @Override
    public List<Piloto> getPilotosOrdenadosPorHorasVuelo() {
        logger.info("INICIO getPilotosOrdenadosPorHorasVuelo - Sin parámetros");
        List<Piloto> lista = new ArrayList<>(this.pilots.values());
        lista.sort((p1, p2) -> Double.compare(p2.getHorasVuelo(), p1.getHorasVuelo()));
        logger.info("FIN getPilotosOrdenadosPorHorasVuelo - Resultado: Devolviendo " + lista.size() + " pilotos ordenados");
        return lista;
    }

    @Override
    public Dron guardarDronEnAlmacen(String idDron) throws Exception {
        logger.info("INICIO guardarDronEnAlmacen - Parámetro: idDron=" + idDron);
        Dron d = this.drones.get(idDron);
        if (d == null) {
            logger.error("ERROR guardarDronEnAlmacen - No existe el dron con ID: " + idDron);
            throw new Exception("Dron no encontrado");
        }
        d.setEnAlmacen(true);
        this.almacen.push(d);
        logger.info("FIN guardarDronEnAlmacen - Resultado: Dron " + idDron + " ingresado en almacén LIFO");
        return d;
    }

    @Override
    public Dron repararDronEnAlmacen() throws Exception {
        logger.info("INICIO repararDronEnAlmacen - Sin parámetros");
        if (this.almacen.isEmpty()) {
            logger.fatal("FATAL repararDronEnAlmacen - Intento de desapilar en un almacén completamente vacío");
            throw new Exception("Almacén vacío");
        }
        Dron d = this.almacen.pop();
        d.setEnAlmacen(false);
        logger.info("FIN repararDronEnAlmacen - Resultado: Dron " + d.getId() + " desapilado y reparado");
        return d;
    }

    @Override
    public PlanVuelo crearPlanVuelo(String idDron, String idPiloto, String fecha, double tiempoEstimado, List<Posicion> posiciones) throws Exception {
        logger.info("INICIO crearPlanVuelo - Parámetros: idDron=" + idDron + ", idPiloto=" + idPiloto + ", fecha=" + fecha + ", tiempoEstimado=" + tiempoEstimado);

        Dron d = this.drones.get(idDron);
        Piloto p = this.pilots.get(idPiloto);

        if (d == null || p == null) {
            logger.error("ERROR crearPlanVuelo - Dron o piloto no existen en el sistema (dron=" + idDron + ", piloto=" + idPiloto + ")");
            throw new Exception("Dron o Piloto no encontrado");
        }

        if (d.isEnAlmacen()) {
            logger.error("ERROR crearPlanVuelo - El dron " + idDron + " no está operativo (está en el almacén)");
            throw new Exception("Dron no operativo");
        }

        for (PlanVuelo plan : d.getPlanesDeVuelo()) {
            if (plan.getFecha().equals(fecha)) {
                logger.error("ERROR crearPlanVuelo - Conflicto de horario: El dron " + idDron + " ya vuela en la fecha " + fecha);
                throw new Exception("Conflicto de fecha en el dron");
            }
        }
        for (PlanVuelo plan : p.getPlanesDeVuelo()) {
            if (plan.getFecha().equals(fecha)) {
                logger.error("ERROR crearPlanVuelo - Conflicto de horario: El piloto " + idPiloto + " ya vuela en la fecha " + fecha);
                throw new Exception("Conflicto de fecha en el piloto");
            }
        }

        String planId = "PLAN-" + UUID.randomUUID().toString().substring(0, 5);
        PlanVuelo nuevoPlan = new PlanVuelo(planId, idDron, idPiloto, fecha, tiempoEstimado, posiciones);

        d.addPlanDeVuelo(nuevoPlan);
        d.addHorasVuelo(tiempoEstimado);

        p.addPlanDeVuelo(nuevoPlan);
        p.addHorasVuelo(tiempoEstimado);

        logger.info("FIN crearPlanVuelo - Resultado: Plan de vuelo creado con éxito ID " + planId);
        return nuevoPlan;
    }

    @Override
    public List<PlanVuelo> getPlanesVueloPorPiloto(String idPiloto) throws Exception {
        logger.info("INICIO getPlanesVueloPorPiloto - Parámetro: idPiloto=" + idPiloto);
        Piloto p = this.pilots.get(idPiloto);
        if (p == null) {
            logger.error("ERROR getPlanesVueloPorPiloto - No existe el piloto: " + idPiloto);
            throw new Exception("Piloto no encontrado");
        }
        List<PlanVuelo> lista = p.getPlanesDeVuelo();
        logger.info("FIN getPlanesVueloPorPiloto - Resultado: Devolviendo " + lista.size() + " planes de vuelo");
        return lista;
    }

    @Override
    public List<PlanVuelo> getPlanesVueloPorDron(String idDron) throws Exception {
        logger.info("INICIO getPlanesVueloPorDron - Parámetro: idDron=" + idDron);
        Dron d = this.drones.get(idDron);
        if (d == null) {
            logger.error("ERROR getPlanesVueloPorDron - No existe el dron: " + idDron);
            throw new Exception("Dron no encontrado");
        }
        List<PlanVuelo> lista = d.getPlanesDeVuelo();
        logger.info("FIN getPlanesVueloPorDron - Resultado: Devolviendo " + lista.size() + " planes de vuelo");
        return lista;
    }

    @Override
    public int sizeDrones() {
        logger.info("INICIO sizeDrones - Sin parámetros");
        int size = this.drones.size();
        logger.info("FIN sizeDrones - Resultado: " + size);
        return size;
    }

    @Override
    public int sizePilotos() {
        logger.info("INICIO sizePilotos - Sin parámetros");
        int size = this.pilots.size();
        logger.info("FIN sizePilotos - Resultado: " + size);
        return size;
    }

    @Override
    public int sizeAlmacen() {
        logger.info("INICIO sizeAlmacen - Sin parámetros");
        int size = this.almacen.size();
        logger.info("FIN sizeAlmacen - Resultado: " + size);
        return size;
    }

    @Override
    public void clear() {
        logger.info("INICIO clear - Sin parámetros");
        this.drones.clear();
        this.pilots.clear();
        this.almacen.clear();
        logger.info("FIN clear - Resultado: Estructuras reseteadas");
    }
}