package com.example;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SimuladorCentroMedico {
    public static void main(String[] args) {
        try (PrintWriter logWriter = new PrintWriter(new FileWriter("src/main/java/com/example/resultado_simulacion.txt"))) {
            Configuracion config = Configuracion.leerConfiguracion("src/main/java/com/example/configuracion.txt");
            CentroMedico centro = new CentroMedico(config, logWriter);
            centro.iniciarSimulacion();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}