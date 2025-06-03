import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SimuladorCentroMedico {
    public static void main(String[] args) {
        try (PrintWriter logWriter = new PrintWriter(new FileWriter("resultado_simulacion.txt"))) {
            Configuracion config = Configuracion.leerConfiguracion("configuracion.txt");
            CentroMedico centro = new CentroMedico(config, logWriter);
            centro.iniciarSimulacion();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}