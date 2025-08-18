import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class GUI extends JFrame {

    private JTextField txtInicio;
    private JTextField txtFin;
    private JButton btnEscanear;
    private JButton btnLimpiar;
    private JTable tablaResultados;
    private JProgressBar barraProgreso;
    private DefaultTableModel modeloTabla;

    private RedScanner scanner;

    public GUI() {
        setTitle("Escáner de Red");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        scanner = new RedScanner();

        // Panel superior con inputs
        JPanel panelSuperior = new JPanel(new GridLayout(2, 3, 5, 5));
        panelSuperior.add(new JLabel("IP Inicio:"));
        txtInicio = new JTextField("10.160.25.1");
        panelSuperior.add(txtInicio);

        panelSuperior.add(new JLabel("IP Fin:"));
        txtFin = new JTextField("10.160.25.5");
        panelSuperior.add(txtFin);

        btnEscanear = new JButton("Escanear");
        btnLimpiar = new JButton("Limpiar");
        panelSuperior.add(btnEscanear);
        panelSuperior.add(btnLimpiar);

        // Tabla de resultados
        modeloTabla = new DefaultTableModel(new Object[]{"IP", "Nombre", "Activo", "Tiempo (ms)"}, 0);
        tablaResultados = new JTable(modeloTabla);

        // Barra de progreso
        barraProgreso = new JProgressBar();
        barraProgreso.setStringPainted(true);

        // Layout principal
        setLayout(new BorderLayout(5, 5));
        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(tablaResultados), BorderLayout.CENTER);
        add(barraProgreso, BorderLayout.SOUTH);

        // Acción Botón Escanear
        btnEscanear.addActionListener((ActionEvent e) -> {
            modeloTabla.setRowCount(0); // limpiar tabla
            String inicio = txtInicio.getText();
            String fin = txtFin.getText();

            new Thread(() -> {
                List<HostInfo> resultados = scanner.escanearRango(inicio, fin);

                barraProgreso.setMaximum(resultados.size());
                int progreso = 0;

                for (HostInfo host : resultados) {
                    modeloTabla.addRow(new Object[]{
                        host.getIp(),
                        host.getNombre(),
                        host.estaActivo() ? "Activo" : "Inactivo",
                        host.getTiempoRespuestaMs()
                    });
                    progreso++;
                    barraProgreso.setValue(progreso);
                }
            }).start();
        });

        // Acción Botón Limpiar
        btnLimpiar.addActionListener((ActionEvent e) -> {
            modeloTabla.setRowCount(0);
            txtInicio.setText("");
            txtFin.setText("");
            barraProgreso.setValue(0);
        });
    }
}