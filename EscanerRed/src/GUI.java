import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
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
    private JButton btnNetstatConex;
    private JButton btnNetstatStats;
    private JButton btnNetstatRutas;
    private JTable tablaResultados;
    private JProgressBar barraProgreso;
    private DefaultTableModel modeloTabla;
    private JComboBox<String> comboTimeout;
    private RedScanner scanner;
    private List<HostInfo> resultadosGlobales;

    public GUI() {
        setTitle("Escaner de Red");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        scanner = new RedScanner();

        // Panel superior con inputs
        JPanel panelSuperior = new JPanel(new GridLayout(3, 7, 5, 5));
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
        btnNetstatConex = new JButton("Netstat Conexiones");
        btnNetstatStats = new JButton("Netstat Estadisticas");
        btnNetstatRutas = new JButton("Netstat Rutas");

        panelSuperior.add(btnEscanear);
        panelSuperior.add(btnLimpiar);
        panelSuperior.add(btnGuardar);
        panelSuperior.add(btnNetstatConex);
        panelSuperior.add(btnNetstatStats);
        panelSuperior.add(btnNetstatRutas);

        // Tabla de resultados
        modeloTabla = new DefaultTableModel(new Object[]{"IP", "Nombre", "Estado", "Tiempo (ms)"}, 0);
        tablaResultados = new JTable(modeloTabla);
        tablaResultados.setAutoCreateRowSorter(true);

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
                JOptionPane.showMessageDialog(this, "Por favor ingrese IPs validas.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            btnEscanear.setEnabled(false);
            barraProgreso.setValue(0);
            tablaResultados.clearSelection();

            new Thread(() -> {
                int timeout = Integer.parseInt(comboTimeout.getSelectedItem().toString()); // reservado para mejoras futuras
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
                JOptionPane.showMessageDialog(this, "No hay resultados para guardar.", "Atencion", JOptionPane.WARNING_MESSAGE);
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

        // Acción Netstat Conexiones
        btnNetstatConex.addActionListener((ActionEvent e) -> {
            ejecutarNetstat(new String[]{"netstat", "-ano"}, "Conexiones activas (netstat -ano)", 700, 400);
        });

        // Acción Netstat Estadísticas
        btnNetstatStats.addActionListener((ActionEvent e) -> {
            ejecutarNetstat(new String[]{"netstat", "-e"}, "Estadisticas de red (netstat -e)", 500, 300);
        });

        // Acción Netstat Rutas
        btnNetstatRutas.addActionListener((ActionEvent e) -> {
            ejecutarNetstat(new String[]{"netstat", "-r"}, "Tabla de enrutamiento (netstat -r)", 700, 400);
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

    // Método auxiliar para ejecutar cualquier comando netstat
    private void ejecutarNetstat(String[] comando, String titulo, int ancho, int alto) {
        new Thread(() -> {
            try {
                ProcessBuilder builder = new ProcessBuilder(comando);
                Process proceso = builder.start();

                BufferedReader reader = new BufferedReader(new InputStreamReader(proceso.getInputStream()));
                StringBuilder salida = new StringBuilder();
                String linea;
                while ((linea = reader.readLine()) != null) {
                    salida.append(linea).append("\n");
                }
                proceso.waitFor();

                JTextArea area = new JTextArea(salida.toString());
                area.setEditable(false);
                JScrollPane scroll = new JScrollPane(area);
                scroll.setPreferredSize(new Dimension(ancho, alto));

                JOptionPane.showMessageDialog(this, scroll, titulo, JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error ejecutando netstat: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }
}