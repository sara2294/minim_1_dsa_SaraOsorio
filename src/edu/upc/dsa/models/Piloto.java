package edu.upc.dsa.models;

import java.util.LinkedList;
import java.util.List;

public class Piloto {
    private String id;
    private String nombre;
    private String apellidos;
    private double horasVuelo;
    private List<PlanVuelo> planesDeVuelo = new LinkedList<>();

    public Piloto() {}

    public Piloto(String id, String nombre, String apellidos) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.horasVuelo = 0.0;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public double getHorasVuelo() { return horasVuelo; }
    public void addHorasVuelo(double horas) { this.horasVuelo += horas; }

    public List<PlanVuelo> getPlanesDeVuelo() { return planesDeVuelo; }
    public void addPlanDeVuelo(PlanVuelo plan) { this.planesDeVuelo.add(plan); }
}