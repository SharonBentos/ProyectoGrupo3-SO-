package com.example;

public class Planificador implements Runnable {
    private final CentroMedico centro;
    private Paciente pacienteActual = null;
    private Tiempo finAtencion = null;

    public Planificador(CentroMedico centro) {
        this.centro = centro;
    }

    public void run() {
        while (!centro.isFinSimulacion() || pacienteActual != null) {
            try {
                Thread.sleep(100); // Simula el paso del tiempo, pero el reloj real es el que manda

                synchronized (centro) {
                    Tiempo ahora = centro.getTiempoActual();

                    // Si hay un paciente en atención, verificar si ya terminó
                    if (pacienteActual != null && ahora.compareTo(finAtencion) >= 0) {
                        centro.log("[Planificador] Terminó atención de: " + pacienteActual);
                        centro.removerPaciente(pacienteActual);
                        pacienteActual = null;
                        finAtencion = null;
                    }

                    // Si no hay paciente en atención, buscar uno
                    if (pacienteActual == null) {
                        pacienteActual = centro.obtenerSiguientePaciente();
                        if (pacienteActual != null) {
                            centro.log("[Planificador] Atendiendo a: " + pacienteActual);
                            finAtencion = ahora.sumarMinutos(pacienteActual.getTiempoAtencion());
                        }
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
