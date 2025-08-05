public class Main {
    public static void main(String[] args) {
        System.out.println("Iniciando escaner de red...");

        RedScanner scanner = new RedScanner();
        String ip = "10.160.25.9"; // Cambiá por una IP real de tu red local

        HostInfo resultado = scanner.escanearIP(ip);

        System.out.println("Resultado:");
        System.out.println("IP: " + resultado.getIp());
        System.out.println("Nombre: " + resultado.getNombre());
        System.out.println("Activo: " + resultado.estaActivo());
        System.out.println("Tiempo de respuesta: " + resultado.getTiempoRespuestaMs() + " ms");
    }
}