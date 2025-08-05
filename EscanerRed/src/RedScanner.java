import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RedScanner {

    public HostInfo escanearIP(String ip) {
        boolean activo = false;
        long tiempoMs = -1;
        String nombre = "";

        try {
            long inicio = System.currentTimeMillis();

            // Ejecutar comando ping
            ProcessBuilder builder = new ProcessBuilder("ping", "-n", "1", ip); // para Windows
            Process proceso = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
            String linea;
            while ((linea = reader.readLine()) != null) {
                System.out.println(linea); // <<< MOSTRAR TODA LA SALIDA DEL PING
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

                // Intentar obtener el nombre del host
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
}