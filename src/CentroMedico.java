import java.util.concurrent.*;
import java.util.*;
import java.io.PrintWriter;

public class CentroMedico {
    private final BlockingQueue<Paciente> colaGenerales = new LinkedBlockingQueue<>();
    private final BlockingQueue<Paciente> colaEmergencias = new LinkedBlockingQueue<>();
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
        log("[Reloj] Tiempo actual: " + tiempoActual);
        if (tiempoActual.compareTo(tiempoFin) >= 0) {
            finSimulacion = true;
        }
    }

    public synchronized boolean isFinSimulacion() {
        return finSimulacion;
    }

    public void agregarPaciente(Paciente p) {
        if (p.getTipo() == Paciente.Tipo.EMERGENCIA) {
            colaEmergencias.add(p);
            log("[Recepcion] Llega EMERGENCIA: " + p);
        } else {
            colaGenerales.add(p);
            log("[Recepcion] Llega GENERAL: " + p);
        }
    }

    public Paciente obtenerSiguientePaciente() throws InterruptedException {
        while (true) {
            if (!colaEmergencias.isEmpty()) {
                return colaEmergencias.poll();
            }
            if (!colaGenerales.isEmpty()) {
                return colaGenerales.poll();
            }
            Thread.sleep(50);
        }
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