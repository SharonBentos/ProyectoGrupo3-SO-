public class Paciente {
    public enum Tipo { GENERAL, EMERGENCIA, CARNE_SALUD }
    private Tipo tipo;
    private Tiempo llegada;
    private int tiempoAtencion;

    public Paciente(Tipo tipo, Tiempo llegada, int tiempoAtencion) {
        this.tipo = tipo;
        this.llegada = llegada;
        this.tiempoAtencion = tiempoAtencion;
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

    @Override
    public String toString() {
        return "Paciente{" + "tipo=" + tipo + ", llegada=" + llegada + ", atencion=" + tiempoAtencion + "min}";
    }
}