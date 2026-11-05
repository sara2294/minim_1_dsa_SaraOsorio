package edu.upc.dsa.models;

import java.util.LinkedList;
import java.util.List;

public class PlanVuelo {
    private String id;
    private String idDron;
    private String idPiloto;
    private String fecha;
    private double tiempoEstimado;
    private List<Posicion> posiciones = new LinkedList<>();

    public PlanVuelo() {}

    public PlanVuelo(String id, String idDron, String idPiloto, String fecha, double tiempoEstimado, List<Posicion> posiciones) {
        this.id = id;
        this.idDron = idDron;
        this.idPiloto = idPiloto;
        this.fecha = fecha;
        this.tiempoEstimado = tiempoEstimado;
        if (posiciones != null) {
            this.posiciones = posiciones;
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getIdDron() { return idDron; }
    public void setIdDron(String idDron) { this.idDron = idDron; }

    public String getIdPiloto() { return idPiloto; }
    public void setIdPiloto(String idPiloto) { this.idPiloto = idPiloto; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public double getTiempoEstimado() { return tiempoEstimado; }
    public void setTiempoEstimado(double tiempoEstimado) { this.tiempoEstimado = tiempoEstimado; }

    public List<Posicion> getPosiciones() { return posiciones; }
    public void setPosiciones(List<Posicion> posiciones) { this.posiciones = posiciones; }
}