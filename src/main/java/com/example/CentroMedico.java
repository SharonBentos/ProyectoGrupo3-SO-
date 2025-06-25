package com.example;

import java.util.*;
import java.io.PrintWriter;

public class CentroMedico {
    private final Paciente[] pacientes = new Paciente[10];
    private Tiempo tiempoActual = new Tiempo(8, 0);
    private final Tiempo tiempoFin = new Tiempo(20, 0);
    private final Configuracion config;
    private boolean finSimulacion = false;
    private final Map<Integer, List<Paciente>> agendaLlegadas;
    private final PrintWriter logWriter;

    public CentroMedico(Configuracion config, PrintWriter logWriter) {
        this.config = config;
        this.agendaLlegadas = config.generarAgenda();
        this.logWriter = logWriter;
    }

    public void iniciarSimulacion() {
        Thread reloj = new Thread(new SimuladorReloj(this));
        Thread recepcionista = new Thread(new Recepcionista(this));
        Thread planificador = new Thread(new Planificador(this));

        reloj.start();
        recepcionista.start();
        planificador.start();

        try {
            reloj.join();
            recepcionista.join();
            planificador.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized Tiempo getTiempoActual() {
        return new Tiempo(tiempoActual.hora, tiempoActual.minuto);
    }

    public synchronized void avanzarMinuto() {
        tiempoActual.avanzarMinuto();
        for (Paciente p : pacientes) {
            if (p != null) p.aumentarPrioridad();
        }
        log("[Reloj] Tiempo actual: " + tiempoActual);
        if (tiempoActual.compareTo(tiempoFin) >= 0) {
            finSimulacion = true;
        }
    }

    public synchronized boolean isFinSimulacion() {
        return finSimulacion;
    }

    public synchronized void agregarPaciente(Paciente p) {
        for (int i = 0; i < pacientes.length; i++) {
            if (pacientes[i] == null) {
                pacientes[i] = p;
                log("[Recepcion] Llega: " + p);
                return;
            }
        }
        log("[Recepcion] No hay lugar para: " + p);
    }

    public synchronized void removerPaciente(Paciente paciente) {
        for (int i = 0; i < pacientes.length; i++) {
            if (pacientes[i] == paciente) {
                pacientes[i] = null;
                return;
            }
        }
    }

    public synchronized Paciente obtenerSiguientePaciente() {
        List<Paciente> lista = new ArrayList<>();
        for (Paciente p : pacientes) {
            if (p != null) lista.add(p);
        }
        if (lista.isEmpty()) return null;
        Paciente siguiente = Collections.max(lista, Comparator.comparingInt(Paciente::getPrioridad));
        removerPaciente(siguiente);
        return siguiente;
    }

    public synchronized Paciente[] getPacientes() {
        return pacientes;
    }

    public List<Paciente> obtenerLlegadasActuales() {
        int minutoActual = tiempoActual.minutosTotales();
        return agendaLlegadas.getOrDefault(minutoActual, new ArrayList<>());
    }

    public synchronized void log(String mensaje) {
        logWriter.println(mensaje);
        logWriter.flush();
    }
}
