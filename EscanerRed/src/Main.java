import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUI ventana = new GUI();
            ventana.setVisible(true);
        });
    }
}