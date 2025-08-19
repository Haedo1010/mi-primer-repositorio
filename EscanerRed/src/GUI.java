import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class GUI extends JFrame {

    private JTextField txtInicio;
    private JTextField txtFin;
    private JButton btnEscanear;
    private JButton btnLimpiar;
    private JButton btnGuardar;
    private JTable tablaResultados;
    private JProgressBar barraProgreso;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> comboTimeout;
    private RedScanner scanner;
    private List<HostInfo> resultadosGlobales;

    public GUI() {
        setTitle("Escáner de Red");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        scanner = new RedScanner();

        // Panel superior con inputs
        JPanel panelSuperior = new JPanel(new GridLayout(3, 4, 5, 5));
        panelSuperior.add(new JLabel("IP Inicio:"));
        txtInicio = new JTextField("10.160.25.1");
        panelSuperior.add(txtInicio);

        panelSuperior.add(new JLabel("IP Fin:"));
        txtFin = new JTextField("10.160.25.5");
        panelSuperior.add(txtFin);

        panelSuperior.add(new JLabel("Timeout (ms):"));
        comboTimeout = new JComboBox<>(new String[]{"1000", "2000", "4000"});
        panelSuperior.add(comboTimeout);

        btnEscanear = new JButton("Escanear");
        btnLimpiar = new JButton("Limpiar");
        btnGuardar = new JButton("Guardar CSV");

        panelSuperior.add(btnEscanear);
        panelSuperior.add(btnLimpiar);
        panelSuperior.add(btnGuardar);

        // Tabla de resultados
        modeloTabla = new DefaultTableModel(new Object[]{"IP", "Nombre", "Estado", "Tiempo (ms)"}, 0);
        tablaResultados = new JTable(modeloTabla);
        tablaResultados.setAutoCreateRowSorter(true); // permite ordenar clickeando

        // Barra de progreso
        barraProgreso = new JProgressBar();
        barraProgreso.setStringPainted(true);

        setLayout(new BorderLayout(5, 5));
        add(panelSuperior, BorderLayout.NORTH);
        add(new JScrollPane(tablaResultados), BorderLayout.CENTER);
        add(barraProgreso, BorderLayout.SOUTH);

        // Acción Escanear
        btnEscanear.addActionListener((ActionEvent e) -> {
            modeloTabla.setRowCount(0);
            String inicio = txtInicio.getText();
            String fin = txtFin.getText();

            if (!Utils.validarIP(inicio) || !Utils.validarIP(fin)) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese IPs válidas.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            btnEscanear.setEnabled(false);   // Desactivar mientras trabaja
            barraProgreso.setValue(0);
            tablaResultados.clearSelection();

            new Thread(() -> {
                int timeout = Integer.parseInt(comboTimeout.getSelectedItem().toString()); // (no usado por ping Windows pero listo para usar si se extiende)

                resultadosGlobales = scanner.escanearRango(inicio, fin);

                barraProgreso.setMaximum(resultadosGlobales.size());
                int progreso = 0;
                int activos = 0;

                for (HostInfo host : resultadosGlobales) {
                    modeloTabla.addRow(new Object[]{
                            host.getIp(),
                            host.getNombre(),
                            host.estaActivo() ? "Activo" : "Inactivo",
                            host.getTiempoRespuestaMs()
                    });
                    if (host.estaActivo()) activos++;
                    progreso++;
                    barraProgreso.setValue(progreso);
                }

                JOptionPane.showMessageDialog(this,
                        "Escaneo completado. Activos: " + activos,
                        "Fin",
                        JOptionPane.INFORMATION_MESSAGE);

                btnEscanear.setEnabled(true);
            }).start();
        });

        // Acción Limpiar
        btnLimpiar.addActionListener((ActionEvent e) -> {
            modeloTabla.setRowCount(0);
            txtInicio.setText("");
            txtFin.setText("");
            barraProgreso.setValue(0);
        });

        // Acción Guardar CSV
        btnGuardar.addActionListener((ActionEvent e) -> {
            if (resultadosGlobales == null || resultadosGlobales.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay resultados para guardar.", "Atención", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JFileChooser chooser = new JFileChooser();
            int opcion = chooser.showSaveDialog(this);
            if (opcion == JFileChooser.APPROVE_OPTION) {
                String ruta = chooser.getSelectedFile().getAbsolutePath();
                if (!ruta.endsWith(".csv")) {
                    ruta += ".csv";
                }
                scanner.guardarResultadosCSV(resultadosGlobales, ruta);
                JOptionPane.showMessageDialog(this, "Archivo CSV guardado en:\n" + ruta);
            }
        });

        // Validación en tiempo real
        DocumentListener docListener = new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { validar(); }
            public void removeUpdate(DocumentEvent e)  { validar(); }
            public void changedUpdate(DocumentEvent e) { validar(); }

            private void validar() {
                boolean v1 = Utils.validarIP(txtInicio.getText());
                boolean v2 = Utils.validarIP(txtFin.getText());
                btnEscanear.setEnabled(v1 && v2);
            }
        };
        txtInicio.getDocument().addDocumentListener(docListener);
        txtFin.getDocument().addDocumentListener(docListener);
    }
}