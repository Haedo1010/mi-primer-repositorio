public class HostInfo {
    private String ip;
    private String nombre;
    private boolean activo;
    private long tiempoRespuestaMs;

    public HostInfo(String ip, String nombre, boolean activo, long tiempoRespuestaMs) {
        this.ip = ip;
        this.nombre = nombre;
        this.activo = activo;
        this.tiempoRespuestaMs = tiempoRespuestaMs;
    }

    // Getters
    public String getIp() { return ip; }
    public String getNombre() { return nombre; }
    public boolean estaActivo() { return activo; }
    public long getTiempoRespuestaMs() { return tiempoRespuestaMs; }
}