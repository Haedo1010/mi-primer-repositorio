public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando escaner de red...");

        RedScanner scanner = new RedScanner();

        // Definimos rango
        String ipInicio = "10.160.25.1";
        String ipFin = "10.160.25.5";

        var resultados = scanner.escanearRango(ipInicio, ipFin);

        System.out.println("\n--- RESULTADOS ---");
        for (HostInfo host : resultados) {
            System.out.println(host.getIp() + " | " +
                               host.getNombre() + " | " +
                               (host.estaActivo() ? "Activo" : "Inactivo") +
                               " | " + host.getTiempoRespuestaMs() + " ms");
        }
    }
}