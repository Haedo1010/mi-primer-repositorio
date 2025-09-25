
public class Main {
    public static void main(String[] args) {
        // Abrir GUI normalmente
        javax.swing.SwingUtilities.invokeLater(() -> {
            GUI ventana = new GUI();
            ventana.setVisible(true);
        });

        // (Opcional) Probar netstat en consola
        // Utils.mostrarNetstat();
    }
}