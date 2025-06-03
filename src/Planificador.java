public class Planificador implements Runnable {
    private final CentroMedico centro;

    public Planificador(CentroMedico centro) {
        this.centro = centro;
    }

    public void run() {
        while (!centro.isFinSimulacion() || true) {
            try {
                Paciente paciente = centro.obtenerSiguientePaciente();
                if (paciente != null) {
                    centro.log("[Planificador] Atendiendo a: " + paciente);
                    Thread.sleep(paciente.getTiempoAtencion() * 100);
                    centro.log("[Planificador] Terminó atención de: " + paciente);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}