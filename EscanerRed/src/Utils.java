import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Utils {

    // Valida si una IP tiene formato correcto
    public static boolean validarIP(String ip) {
        return ip.matches(
            "^((25[0-5]|2[0-4][0-9]|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?\\d\\d?)$"
        );
    }

     public static void mostrarNetstat() {
        try {
            ProcessBuilder builder = new ProcessBuilder("netstat", "-ano");
            Process proceso = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
            String linea;
            System.out.println("=== NETSTAT ===");
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea);
            }
            proceso.waitFor();
        } catch (Exception e) {
            System.out.println("Error al ejecutar netstat: " + e.getMessage());
        }
    }
}