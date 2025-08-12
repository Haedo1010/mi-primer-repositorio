import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class RedScanner {

    public HostInfo escanearIP(String ip) {
        boolean activo = false;
        long tiempoMs = -1;
        String nombre = "";

        try {
            long inicio = System.currentTimeMillis();

            ProcessBuilder builder = new ProcessBuilder("ping", "-n", "1", ip); // Windows
            Process proceso = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
            String linea;
            while ((linea = reader.readLine()) != null) {
                String lineaLower = linea.toLowerCase();
                if ((lineaLower.contains("ttl") || lineaLower.contains("tiempo") ||
                    (lineaLower.contains("respuesta desde") && !lineaLower.contains("inaccesible")))) {
                    activo = true;
                }
            }

            proceso.waitFor();
            long fin = System.currentTimeMillis();

            if (activo) {
                tiempoMs = fin - inicio;
                try {
                    java.net.InetAddress addr = java.net.InetAddress.getByName(ip);
                    nombre = addr.getHostName();
                } catch (Exception e) {
                    nombre = "No resuelto";
                }
            }

        } catch (Exception e) {
            System.out.println("Error al escanear IP: " + ip);
        }

        return new HostInfo(ip, nombre, activo, tiempoMs);
    }

    public List<HostInfo> escanearRango(String ipInicio, String ipFin) {
        List<HostInfo> resultados = new ArrayList<>();

        try {
            String[] partesInicio = ipInicio.split("\\.");
            String[] partesFin = ipFin.split("\\.");

            // Solo soportamos que cambie el último octeto para simplificar
            if (partesInicio[0].equals(partesFin[0]) &&
                partesInicio[1].equals(partesFin[1]) &&
                partesInicio[2].equals(partesFin[2])) {

                int start = Integer.parseInt(partesInicio[3]);
                int end = Integer.parseInt(partesFin[3]);

                for (int i = start; i <= end; i++) {
                    String ip = partesInicio[0] + "." + partesInicio[1] + "." +
                                partesInicio[2] + "." + i;

                    System.out.println("Escaneando: " + ip);
                    HostInfo info = escanearIP(ip);
                    resultados.add(info);
                }
            } else {
                System.out.println("Por ahora solo se puede escanear dentro del mismo rango de red.");
            }

        } catch (Exception e) {
            System.out.println("Error en escaneo de rango: " + e.getMessage());
        }

        return resultados;
    }
}
