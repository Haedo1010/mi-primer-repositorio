import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Utils {
    public static boolean validarIP(String ip) {
        return ip.matches("^((25[0-5]|2[0-4][0-9]|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?\\d\\d?)$");
    }

    public static String netstatConexiones() {
        StringBuilder resultado = new StringBuilder();
        try {
            ProcessBuilder builder = new ProcessBuilder("netstat", "-ano");
            Process proceso = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
            String linea;
            while ((linea = reader.readLine()) != null) {
                resultado.append(linea).append("\n");
            }
            proceso.waitFor();
        } catch (Exception e) {
            resultado.append("Error ejecutando netstat: ").append(e.getMessage());
        }
        return resultado.toString();
    }

    public static String netstatEstadisticas() {
        StringBuilder resultado = new StringBuilder();
        try {
            ProcessBuilder builder = new ProcessBuilder("netstat", "-e");
            Process proceso = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
            String linea;
            while ((linea = reader.readLine()) != null) {
                resultado.append(linea).append("\n");
            }
            proceso.waitFor();
        } catch (Exception e) {
            resultado.append("Error ejecutando netstat -e: ").append(e.getMessage());
        }
        return resultado.toString();
    }
}