package com.example;

public class Paciente {
    public enum Tipo { GENERAL, EMERGENCIA, CARNE_SALUD }
    public String name;
    private Tipo tipo;
    private Tiempo llegada;
    private int tiempoAtencion;

    public Paciente(Tipo tipo, Tiempo llegada, int tiempoAtencion, String name) {
        this.tipo = tipo;
        this.llegada = llegada;
        this.tiempoAtencion = tiempoAtencion;
        this.name = name;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public Tiempo getLlegada() {
        return llegada;
    }

    public int getTiempoAtencion() {
        return tiempoAtencion;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Paciente{Nombre=" + name + ", tipo=" + tipo + ", llegada=" + llegada + ", atencion=" + tiempoAtencion + "min}";
    }
}