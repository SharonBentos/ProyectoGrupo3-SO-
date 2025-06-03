import java.io.*;
import java.util.*;

public class Configuracion {
    private final List<String> lineas = new ArrayList<>();

    public static Configuracion leerConfiguracion(String archivo) {
        Configuracion config = new Configuracion();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                config.lineas.add(linea);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    public Map<Integer, List<Paciente>> generarAgenda() {
        Map<Integer, List<Paciente>> agenda = new HashMap<>();
        for (String l : lineas) {
            if (l.isEmpty() || l.startsWith("#")) continue;
            String[] partes = l.split(";");
            int hora = Integer.parseInt(partes[0]);
            int minuto = Integer.parseInt(partes[1]);
            Paciente.Tipo tipo = Paciente.Tipo.valueOf(partes[2]);
            int cantidad = Integer.parseInt(partes[3]);
            int tiempoAtencion = Integer.parseInt(partes[4]);

            int tiempoEnMinutos = hora * 60 + minuto;
            agenda.putIfAbsent(tiempoEnMinutos, new ArrayList<>());
            for (int i = 0; i < cantidad; i++) {
                agenda.get(tiempoEnMinutos).add(new Paciente(tipo, new Tiempo(hora, minuto), tiempoAtencion));
            }
        }
        return agenda;
    }
}