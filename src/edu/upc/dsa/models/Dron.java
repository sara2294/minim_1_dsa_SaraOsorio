package edu.upc.dsa.models;

import java.util.LinkedList;
import java.util.List;

public class Dron {
    private String id;
    private String nombre;
    private String fabricante;
    private String modelo;
    private double horasVuelo;
    private boolean enAlmacen;
    private List<PlanVuelo> planesDeVuelo = new LinkedList<>();

    public Dron() {}

    public Dron(String id, String nombre, String fabricante, String modelo) {
        this.id = id;
        this.nombre = nombre;
        this.fabricante = fabricante;
        this.modelo = modelo;
        this.horasVuelo = 0.0;
        this.enAlmacen = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getFabricante() { return fabricante; }
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public double getHorasVuelo() { return horasVuelo; }
    public void addHorasVuelo(double horas) { this.horasVuelo += horas; }

    public boolean isEnAlmacen() { return enAlmacen; }
    public void setEnAlmacen(boolean enAlmacen) { this.enAlmacen = enAlmacen; }

    public List<PlanVuelo> getPlanesDeVuelo() { return planesDeVuelo; }
    public void addPlanDeVuelo(PlanVuelo plan) { this.planesDeVuelo.add(plan); }
}