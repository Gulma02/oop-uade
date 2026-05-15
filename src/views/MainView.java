package views;

import controllers.JuegoController;
import dto.CaballoDTO;
import dto.CarreraEstadoDTO;
import dto.JugadorDTO;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainView extends JFrame {
    private JuegoController controller;
    private JTextField nombreField;
    private JTextField emailField;
    private JComboBox<CaballoDTO> caballosCombo;
    private JButton crearJugadorButton;
    private JButton iniciarCarreraButton;
    private JLabel puntajeLabel;
    private JLabel estadoLabel;
    private PistaPanel pistaPanel;
    private Timer timer;

    public MainView() {
        controller = new JuegoController();
        configurarVentana();
        crearComponentes();
        cargarCaballos();
        setVisible(true);
    }

    private void configurarVentana() {
        setTitle("Carrera de Caballos - POO UADE");
        setSize(920, 560);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void crearComponentes() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBorder(BorderFactory.createTitledBorder("Jugador"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        nombreField = new JTextField(16);
        emailField = new JTextField(16);
        caballosCombo = new JComboBox<>();
        crearJugadorButton = new JButton("Crear jugador");
        iniciarCarreraButton = new JButton("Iniciar carrera");
        iniciarCarreraButton.setEnabled(false);

        agregarCampo(formulario, gbc, 0, "Nombre", nombreField);
        agregarCampo(formulario, gbc, 1, "Mail", emailField);
        agregarCampo(formulario, gbc, 2, "Caballo", caballosCombo);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formulario.add(crearJugadorButton, gbc);

        gbc.gridy = 4;
        formulario.add(iniciarCarreraButton, gbc);

        puntajeLabel = new JLabel("Puntaje: 0");
        estadoLabel = new JLabel("Crea un jugador para comenzar.");
        pistaPanel = new PistaPanel();

        JPanel estadoPanel = new JPanel(new BorderLayout());
        estadoPanel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        estadoPanel.add(estadoLabel, BorderLayout.CENTER);
        estadoPanel.add(puntajeLabel, BorderLayout.EAST);

        root.add(formulario, BorderLayout.WEST);
        root.add(pistaPanel, BorderLayout.CENTER);
        root.add(estadoPanel, BorderLayout.SOUTH);
        add(root);

        crearJugadorButton.addActionListener(event -> crearJugador());
        iniciarCarreraButton.addActionListener(event -> iniciarCarrera());
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String texto, JComponent componente) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 1;
        panel.add(new JLabel(texto), gbc);

        gbc.gridx = 1;
        panel.add(componente, gbc);
    }

    private void cargarCaballos() {
        try {
            for (CaballoDTO caballo : controller.listarCaballos()) {
                caballosCombo.addItem(caballo);
            }
        } catch (RuntimeException e) {
            mostrarError("No se pudo conectar a la base de datos. Revisa que Docker/MySQL este levantado.");
        }
    }

    private void crearJugador() {
        try {
            JugadorDTO jugador = controller.crearJugador(
                    nombreField.getText(),
                    emailField.getText(),
                    (CaballoDTO) caballosCombo.getSelectedItem()
            );
            puntajeLabel.setText("Puntaje: " + jugador.getPuntajeAcumulado());
            estadoLabel.setText("Jugador creado: " + jugador.getNombre() + ". Ya podes iniciar la carrera.");
            iniciarCarreraButton.setEnabled(true);
            crearJugadorButton.setEnabled(false);
            nombreField.setEnabled(false);
            emailField.setEnabled(false);
            caballosCombo.setEnabled(false);
        } catch (RuntimeException e) {
            mostrarError(e.getMessage());
        }
    }

    private void iniciarCarrera() {
        try {
            CarreraEstadoDTO estado = controller.iniciarCarrera();
            pistaPanel.actualizar(estado);
            estadoLabel.setText("Carrera en curso...");
            iniciarCarreraButton.setEnabled(false);

            timer = new Timer(180, event -> avanzarCarrera());
            timer.start();
        } catch (RuntimeException e) {
            mostrarError(e.getMessage());
        }
    }

    private void avanzarCarrera() {
        CarreraEstadoDTO estado = controller.avanzarCarrera();
        pistaPanel.actualizar(estado);

        if (estado.isFinalizada()) {
            timer.stop();
            puntajeLabel.setText("Puntaje: " + estado.getPuntajeAcumulado());
            estadoLabel.setText("Gano " + estado.getGanador() + ". Tu posicion: " + estado.getPosicionJugador() + ". Puntos obtenidos: " + estado.getPuntosObtenidos());
            iniciarCarreraButton.setEnabled(true);
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Atencion", JOptionPane.WARNING_MESSAGE);
    }

    private static class PistaPanel extends JPanel {
        private CarreraEstadoDTO estado;
        private Map<Integer, Color> colores = new LinkedHashMap<>();

        public PistaPanel() {
            setBackground(new Color(38, 120, 73));
            setBorder(BorderFactory.createTitledBorder("Pista"));
            colores.put(1, new Color(244, 90, 90));
            colores.put(2, new Color(248, 196, 72));
            colores.put(3, new Color(70, 150, 240));
            colores.put(4, new Color(180, 110, 230));
            colores.put(5, new Color(245, 245, 245));
        }

        public void actualizar(CarreraEstadoDTO estado) {
            this.estado = estado;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int anchoPista = getWidth() - 120;
            int metaX = getWidth() - 58;
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(3));
            g.drawLine(metaX, 42, metaX, getHeight() - 36);
            g.drawString("META", metaX - 12, 28);

            if (estado == null) {
                g.setFont(new Font("Arial", Font.BOLD, 18));
                g.drawString("Esperando carrera...", 28, 70);
                return;
            }

            int y = 66;
            int indice = 1;
            for (CaballoDTO caballo : estado.getCaballos()) {
                double proporcion = caballo.getDistanciaRecorrida() / estado.getDistanciaTotal();
                int x = 28 + (int) (Math.min(1.0, proporcion) * anchoPista);

                g.setColor(new Color(255, 255, 255, 120));
                g.drawLine(28, y + 17, metaX, y + 17);

                g.setColor(colores.getOrDefault(indice, Color.LIGHT_GRAY));
                g.fillOval(x, y, 34, 24);
                g.setColor(Color.BLACK);
                g.drawOval(x, y, 34, 24);
                g.drawString(caballo.getNombre() + " - Energia " + (int) caballo.getEnergiaActual() + "%", 28, y - 6);
                y += 70;
                indice++;
            }
        }
    }
}
