import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class RedScanner {

    // Método que escanea una sola IP
    public HostInfo escanearIP(String ip) {
        boolean activo = false;
        long tiempoMs = -1;
        String nombre = "";

        try {
            long inicio = System.currentTimeMillis();

            ProcessBuilder builder = new ProcessBuilder("ping", "-n", "1", ip);  // Windows
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
            System.out.println("Error al escanear IP: " + ip + " - " + e.getMessage());
        }

        return new HostInfo(ip, nombre, activo, tiempoMs);
    }

    // Método que recorre un rango de IPs
    public List<HostInfo> escanearRango(String ipInicio, String ipFin) {
        List<HostInfo> resultados = new ArrayList<>();

        try {
            String[] partesInicio = ipInicio.split("\\.");
            String[] partesFin = ipFin.split("\\.");

            // Solo permitimos cambiar el último octeto
            if (partesInicio[0].equals(partesFin[0]) &&
                partesInicio[1].equals(partesFin[1]) &&
                partesInicio[2].equals(partesFin[2])) {

                int start = Integer.parseInt(partesInicio[3]);
                int end   = Integer.parseInt(partesFin[3]);

                for (int i = start; i <= end; i++) {
                    String ipActual = partesInicio[0] + "." + partesInicio[1] + "." +
                                      partesInicio[2] + "." + i;

                    System.out.println("Escaneando: " + ipActual);
                    HostInfo info = escanearIP(ipActual);
                    resultados.add(info);
                }
            } 
            else {
                System.out.println("Solo se permite escanear dentro del mismo rango (mismo 1er, 2do y 3er octeto).");
            }

        } catch (Exception e) {
            System.out.println("Error en escaneo de rango: " + e.getMessage());
        }

        return resultados;
    }

    // Método que guarda resultados en CSV
    public void guardarResultadosCSV(List<HostInfo> resultados, String nombreArchivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nombreArchivo))) {

            writer.println("IP,Nombre,Activo,TiempoRespuesta(ms)");

            for (HostInfo host : resultados) {
                writer.println(
                    host.getIp() + "," +
                    host.getNombre() + "," +
                    (host.estaActivo() ? "Activo" : "Inactivo") + "," +
                    host.getTiempoRespuestaMs()
                );
            }

            System.out.println("Resultados guardados en: " + nombreArchivo);

        } catch (Exception e) {
            System.out.println("Error al guardar CSV: " + e.getMessage());
        }
    }
}