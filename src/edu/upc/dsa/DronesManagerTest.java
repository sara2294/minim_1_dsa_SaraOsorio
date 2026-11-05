package edu.upc.dsa;

import edu.upc.dsa.models.Dron;
import edu.upc.dsa.models.Piloto;
import edu.upc.dsa.models.PlanVuelo;
import edu.upc.dsa.models.Posicion;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DronesManagerTest {
    private DronesManager manager;

    @Before
    public void setUp() {
        manager = DronesManagerImpl.getInstance();
        manager.addDron("D1", "Dron Alpha", "DJI", "Mavic");
        manager.addPiloto("P1", "Juan", "Pérez");
    }

    @After
    public void tearDown() {
        manager.clear();
    }

    @Test
    public void testAddDronYPiloto() {
        Assert.assertEquals(1, manager.sizeDrones());
        Assert.assertEquals(1, manager.sizePilotos());
    }

    @Test
    public void testGuardarYRepararAlmacen() throws Exception {
        manager.guardarDronEnAlmacen("D1");
        Assert.assertEquals(1, manager.sizeAlmacen());

        Dron reparado = manager.repararDronEnAlmacen();
        Assert.assertEquals("D1", reparado.getId());
        Assert.assertEquals(0, manager.sizeAlmacen());
    }

    @Test
    public void testCrearPlanVuelo() throws Exception {
        List<Posicion> pos = new ArrayList<>();
        pos.add(new Posicion(41.27, 1.98));

        PlanVuelo plan = manager.crearPlanVuelo("D1", "P1", "2026-11-05T10:00", 2.0, pos);
        Assert.assertNotNull(plan);
        Assert.assertEquals(1, manager.getPlanesVueloPorDron("D1").size());
    }

    @Test(expected = Exception.class)
    public void testCrearPlanVueloDronEnAlmacen() throws Exception {
        manager.guardarDronEnAlmacen("D1");
        List<Posicion> pos = new ArrayList<>();
        manager.crearPlanVuelo("D1", "P1", "2026-11-05T12:00", 1.5, pos);
    }
}