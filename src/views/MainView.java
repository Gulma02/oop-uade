package views;

import controllers.JuegoController;
import dto.CaballoDTO;
import dto.CarreraEstadoDTO;
import dto.JugadorDTO;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;


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
    // Panel y controles para seleccionar el caballo antes de iniciar la carrera
    private JPanel seleccionCaballoPanel;
    private JButton comenzarCarreraButton;
    private JPanel rootPanel;
    private CardLayout cardLayout;
    private static final String SCREEN_LOGIN = "LOGIN";
    private static final String SCREEN_MENU = "MENU";
    private static final String SCREEN_CARRERA = "CARRERA";
    private static final String SCREEN_HISTORIAL = "HISTORIAL";

    public MainView() {
        controller = new JuegoController();
        configurarVentana();
        crearComponentes();
        cargarCaballos();
        setVisible(true);
    }

    private void configurarVentana() {
        setTitle("Carrera de Caballos - POO UADE");
        setSize(1080, 640);
        setMinimumSize(new Dimension(980, 580));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void crearComponentes() {
        cardLayout = new CardLayout();
        rootPanel = new JPanel(cardLayout);
        rootPanel.setBackground(new Color(243, 246, 250));

        rootPanel.add(crearLoginPanel(), SCREEN_LOGIN);
        rootPanel.add(crearMenuPanel(), SCREEN_MENU);
        rootPanel.add(crearCarreraPanel(), SCREEN_CARRERA);
        rootPanel.add(crearHistorialPanel(), SCREEN_HISTORIAL);

        add(rootPanel);
        cardLayout.show(rootPanel, SCREEN_LOGIN);
    }

    private JPanel crearBaseScreen() {
        JPanel panel = new JPanel(new BorderLayout(18, 18));
        panel.setBackground(new Color(243, 246, 250));
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));
        return panel;
    }

    private JPanel crearHeader(String tituloTexto, String subtituloTexto, boolean mostrarPuntaje) {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JLabel titulo = new JLabel(tituloTexto);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        titulo.setForeground(new Color(25, 42, 62));

        JLabel subtitulo = new JLabel(subtituloTexto);
        subtitulo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        subtitulo.setForeground(new Color(94, 108, 132));

        JPanel titleStack = new JPanel(new GridLayout(2, 1, 0, 2));
        titleStack.setOpaque(false);
        titleStack.add(titulo);
        titleStack.add(subtitulo);
        header.add(titleStack, BorderLayout.WEST);

        if (mostrarPuntaje) {
            if (puntajeLabel == null) {
                puntajeLabel = new JLabel("Puntaje: 0");
            }
            puntajeLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
            puntajeLabel.setForeground(new Color(30, 76, 120));
            puntajeLabel.setBorder(new EmptyBorder(10, 16, 10, 16));
            puntajeLabel.setOpaque(true);
            puntajeLabel.setBackground(new Color(221, 235, 250));
            header.add(puntajeLabel, BorderLayout.EAST);
        }

        return header;
    }

    private JPanel crearLoginPanel() {
        JPanel screen = crearBaseScreen();
        screen.add(crearHeader("Carrera de Caballos", "Creá tu jugador para comenzar", false), BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel formulario = crearCardPanel();
        formulario.setPreferredSize(new Dimension(390, 430));
        formulario.setLayout(new GridBagLayout());

        JLabel formTitle = new JLabel("Login / Registro");
        formTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        formTitle.setForeground(new Color(25, 42, 62));

        JLabel formHint = new JLabel("Ingresá tus datos.");
        formHint.setFont(new Font("SansSerif", Font.PLAIN, 13));
        formHint.setForeground(new Color(94, 108, 132));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 8, 0);
        formulario.add(formTitle, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 24, 0);
        formulario.add(formHint, gbc);

        nombreField = new JTextField(16);
        emailField = new JTextField(16);
        caballosCombo = new JComboBox<>();
        // Estilizar únicamente los campos de texto en la pantalla de login.  El combo de caballos se utilizará
        // más adelante para seleccionar el caballo al iniciar la carrera.
        estilizarInput(nombreField);
        estilizarInput(emailField);

        crearJugadorButton = new JButton("Crear jugador");
        estilizarBotonPrimario(crearJugadorButton);

        // Campos: solo nombre y mail en la pantalla de registro
        agregarCampo(formulario, gbc, 2, "Nombre", nombreField);
        agregarCampo(formulario, gbc, 4, "Mail", emailField);

        // Botón para crear jugador
        gbc.gridy = 8;
        gbc.insets = new Insets(14, 0, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formulario.add(crearJugadorButton, gbc);

        // Etiqueta de estado debajo del botón
        estadoLabel = new JLabel("Crea un jugador para comenzar.");
        estadoLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        estadoLabel.setForeground(new Color(94, 108, 132));

        gbc.gridy = 9;
        gbc.insets = new Insets(14, 0, 0, 0);
        formulario.add(estadoLabel, gbc);

        centerWrapper.add(formulario);
        screen.add(centerWrapper, BorderLayout.CENTER);

        crearJugadorButton.addActionListener(event -> crearJugador());
        return screen;
    }

    private JPanel crearMenuPanel() {
        JPanel screen = crearBaseScreen();
        screen.add(crearHeader("Menú principal", "Elegí qué querés hacer", true), BorderLayout.NORTH);

        JPanel menu = crearCardPanel();
        menu.setPreferredSize(new Dimension(420, 330));
        menu.setLayout(new GridBagLayout());

        JLabel title = new JLabel("Opciones");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(new Color(25, 42, 62));

        iniciarCarreraButton = new JButton("Iniciar carrera");
        JButton historialButton = new JButton("Ver historial");
        JButton salirButton = new JButton("Salir");
        estilizarBotonPrimario(iniciarCarreraButton);
        estilizarBotonSecundario(historialButton);
        estilizarBotonSecundario(salirButton);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 22, 0);
        menu.add(title, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 12, 0);
        menu.add(iniciarCarreraButton, gbc);

        gbc.gridy = 2;
        menu.add(historialButton, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        menu.add(salirButton, gbc);

        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        wrapper.add(menu);
        screen.add(wrapper, BorderLayout.CENTER);

        iniciarCarreraButton.addActionListener(event -> {
            // Cambiar a la pantalla de carrera y mostrar la selección de caballo antes de iniciar
            cardLayout.show(rootPanel, SCREEN_CARRERA);
            mostrarSeleccionCaballo();
        });
        historialButton.addActionListener(event -> cardLayout.show(rootPanel, SCREEN_HISTORIAL));
        salirButton.addActionListener(event -> cerrarSesion());

        return screen;
    }

    private JPanel crearCarreraPanel() {
        JPanel screen = crearBaseScreen();
        screen.add(crearHeader("Carrera", "Seguí el avance de los caballos", true), BorderLayout.NORTH);

        // Contenedor central con selector de caballo y pista
        JPanel centro = new JPanel(new BorderLayout());
        centro.setOpaque(false);

        // Panel de selección de caballo: tarjeta con título, combo y botón
        seleccionCaballoPanel = crearCardPanel();
        seleccionCaballoPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbcSel = new GridBagConstraints();
        gbcSel.gridx = 0;
        gbcSel.weightx = 1;
        gbcSel.fill = GridBagConstraints.HORIZONTAL;

        JLabel selTitulo = new JLabel("Seleccioná tu caballo");
        selTitulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        selTitulo.setForeground(new Color(25, 42, 62));
        gbcSel.gridy = 0;
        gbcSel.insets = new Insets(0, 0, 12, 0);
        seleccionCaballoPanel.add(selTitulo, gbcSel);

        // Asegurarse de que el combo esté estilizado
        estilizarCombo(caballosCombo);
        gbcSel.gridy = 1;
        gbcSel.insets = new Insets(0, 0, 16, 0);
        seleccionCaballoPanel.add(caballosCombo, gbcSel);

        comenzarCarreraButton = new JButton("Comenzar carrera");
        estilizarBotonPrimario(comenzarCarreraButton);
        gbcSel.gridy = 2;
        gbcSel.insets = new Insets(0, 0, 0, 0);
        seleccionCaballoPanel.add(comenzarCarreraButton, gbcSel);

        // Acción al presionar comenzar: iniciar la carrera con el caballo elegido
        comenzarCarreraButton.addActionListener(event -> iniciarCarreraSeleccionada());

        // Crear la pista y ocultarla inicialmente hasta que se inicie la carrera
        pistaPanel = new PistaPanel();
        pistaPanel.setVisible(false);

        centro.add(seleccionCaballoPanel, BorderLayout.NORTH);
        centro.add(pistaPanel, BorderLayout.CENTER);
        screen.add(centro, BorderLayout.CENTER);

        // Footer con botón para volver
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        JButton volverButton = new JButton("Volver al menú");
        estilizarBotonSecundario(volverButton);
        volverButton.addActionListener(event -> {
            // Si una carrera estaba en curso, detener el temporizador
            if (timer != null && timer.isRunning()) {
                timer.stop();
            }
            // Volver al menú principal
            cardLayout.show(rootPanel, SCREEN_MENU);
        });
        footer.add(volverButton, BorderLayout.EAST);
        screen.add(footer, BorderLayout.SOUTH);

        return screen;
    }

    private JPanel crearHistorialPanel() {
        JPanel screen = crearBaseScreen();
        screen.add(crearHeader("Historial", "Resumen de carreras jugadas", true), BorderLayout.NORTH);

        JPanel card = crearCardPanel();
        card.setLayout(new BorderLayout(12, 12));

        JLabel title = new JLabel("Historial de partidas");
        title.setFont(new Font("SansSerif", Font.BOLD, 20));
        title.setForeground(new Color(25, 42, 62));

        JTextArea historialArea = new JTextArea("Todavía no hay historial cargado.\n\nAcá podemos listar carreras finalizadas, ganador, posición y puntos obtenidos.");
        historialArea.setEditable(false);
        historialArea.setLineWrap(true);
        historialArea.setWrapStyleWord(true);
        historialArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        historialArea.setForeground(new Color(55, 65, 81));
        historialArea.setBackground(new Color(249, 251, 253));
        historialArea.setBorder(new EmptyBorder(16, 16, 16, 16));

        JButton volverButton = new JButton("Volver al menú");
        estilizarBotonSecundario(volverButton);
        volverButton.addActionListener(event -> cardLayout.show(rootPanel, SCREEN_MENU));

        card.add(title, BorderLayout.NORTH);
        card.add(new JScrollPane(historialArea), BorderLayout.CENTER);
        card.add(volverButton, BorderLayout.SOUTH);

        screen.add(card, BorderLayout.CENTER);
        return screen;
    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int fila, String texto, JComponent componente) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("SansSerif", Font.BOLD, 13));
        label.setForeground(new Color(55, 65, 81));

        gbc.gridx = 0;
        gbc.gridy = fila * 2;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 0, 6, 0);
        panel.add(label, gbc);

        gbc.gridy = fila * 2 + 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 14, 0);
        panel.add(componente, gbc);
    }

    private JPanel crearCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(new Color(220, 228, 238), 18),
                new EmptyBorder(22, 22, 22, 22)
        ));
        return panel;
    }

    private void estilizarInput(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setForeground(new Color(25, 42, 62));
        field.setBackground(new Color(249, 251, 253));
        field.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(new Color(211, 219, 230), 12),
                new EmptyBorder(9, 12, 9, 12)
        ));
    }

    private void estilizarCombo(JComboBox<CaballoDTO> combo) {
        combo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        combo.setForeground(new Color(25, 42, 62));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(new Color(211, 219, 230), 12),
                new EmptyBorder(6, 8, 6, 8)
        ));
    }

    private void estilizarBotonPrimario(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(43, 111, 181));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(12, 16, 12, 16));
    }

    private void estilizarBotonSecundario(JButton button) {
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(new Color(43, 111, 181));
        button.setBackground(new Color(225, 237, 250));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(12, 16, 12, 16));
    }

    private static class RoundedBorder extends AbstractBorder {
        private final Color color;
        private final int radius;

        private RoundedBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(1, 1, 1, 1);
        }
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
            // Crear el jugador sin asignar caballo; el caballo se seleccionará antes de iniciar la carrera
            JugadorDTO jugador = controller.crearJugador(
                    nombreField.getText(),
                    emailField.getText()
            );
            puntajeLabel.setText("Puntaje: " + jugador.getPuntajeAcumulado());
            estadoLabel.setText("Jugador creado: " + jugador.getNombre() + ".");
            // Pasar al menú principal una vez creado el usuario
            cardLayout.show(rootPanel, SCREEN_MENU);
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
            cardLayout.show(rootPanel, SCREEN_MENU);
        }
    }

    private void cerrarSesion() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }

        nombreField.setText("");
        emailField.setText("");
        caballosCombo.setSelectedIndex(caballosCombo.getItemCount() > 0 ? 0 : -1);
        puntajeLabel.setText("Puntaje: 0");
        estadoLabel.setText("Crea un jugador para comenzar.");
        pistaPanel.actualizar(null);
        cardLayout.show(rootPanel, SCREEN_LOGIN);
    }

    /**
     * Muestra el panel de selección de caballo y oculta la pista.  Refresca la lista de caballos disponibles.
     */
    private void mostrarSeleccionCaballo() {
        // refrescar listado de caballos
        caballosCombo.removeAllItems();
        try {
            for (CaballoDTO caballo : controller.listarCaballos()) {
                caballosCombo.addItem(caballo);
            }
        } catch (RuntimeException e) {
            mostrarError("No se pudo cargar la lista de caballos");
        }
        // mostrar panel de selección y ocultar la pista
        if (seleccionCaballoPanel != null) {
            seleccionCaballoPanel.setVisible(true);
        }
        if (pistaPanel != null) {
            pistaPanel.setVisible(false);
            pistaPanel.actualizar(null);
        }
    }

    /**
     * Inicia la carrera con el caballo seleccionado en el combo.  Oculta el panel de selección y muestra la pista.
     */
    private void iniciarCarreraSeleccionada() {
        try {
            CaballoDTO caballo = (CaballoDTO) caballosCombo.getSelectedItem();
            if (caballo == null) {
                mostrarError("Debe seleccionar un caballo");
                return;
            }
            CarreraEstadoDTO estado = controller.iniciarCarrera(caballo);
            // actualizar la pista con el estado inicial
            pistaPanel.actualizar(estado);
            // ocultar panel de selección y mostrar la pista
            seleccionCaballoPanel.setVisible(false);
            pistaPanel.setVisible(true);
            // detener temporizador anterior si existía
            if (timer != null && timer.isRunning()) {
                timer.stop();
            }
            // iniciar temporizador para avanzar la carrera
            timer = new Timer(180, event -> avanzarCarrera());
            timer.start();
        } catch (RuntimeException e) {
            mostrarError(e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Atencion", JOptionPane.WARNING_MESSAGE);
    }

    private static class PistaPanel extends JPanel {
        private CarreraEstadoDTO estado;
        private final Map<Integer, Color> colores = new LinkedHashMap<>();
        private final Map<Integer, java.awt.image.BufferedImage> imagenesCaballos = new HashMap<>();
        private java.awt.image.BufferedImage imgMeta;

        public PistaPanel() {
            setBackground(new Color(40, 116, 67));
            setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(new Color(220, 228, 238), 18),
                    new EmptyBorder(18, 18, 18, 18)
            ));

            colores.put(1, new Color(232, 84, 84));
            colores.put(2, new Color(242, 179, 64));
            colores.put(3, new Color(67, 143, 215));
            colores.put(4, new Color(146, 101, 216));
            colores.put(5, new Color(240, 240, 240));

            for (int i = 1; i <= 5; i++) {
                try {
                    java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/resources/caballo_" + i + ".PNG"));
                    imagenesCaballos.put(i, img);
                } catch (Exception e) {
                    System.out.println("Aviso: No se pudo cargar /resources/caballo_" + i + ".png");
                }
            }

            try {
                imgMeta = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/resources/meta.png"));
            } catch (Exception e) {
                System.out.println("Aviso: No se pudo cargar /resources/meta.png");
            }
        }

        public void actualizar(CarreraEstadoDTO estado) {
            this.estado = estado;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int padding = 28;
            int pistaX = padding;
            int pistaY = 72;
            int pistaW = getWidth() - padding * 2;
            int pistaH = getHeight() - pistaY - 34;
            int metaX = pistaX + pistaW - 44;

            g.setColor(new Color(255, 255, 255, 245));
            g.setFont(new Font("SansSerif", Font.BOLD, 22));
            g.drawString("Pista", padding, 38);

            g.setFont(new Font("SansSerif", Font.PLAIN, 13));
            g.setColor(new Color(218, 238, 222));
            g.drawString("La carrera se actualiza en tiempo real mientras avanzan los caballos.", padding, 58);

            g.setColor(new Color(35, 97, 56));
            g.fillRoundRect(pistaX, pistaY, pistaW, pistaH, 22, 22);

            g.setColor(new Color(45, 128, 74));
            g.fillRoundRect(pistaX + 10, pistaY + 10, pistaW - 20, pistaH - 20, 18, 18);

            if (imgMeta != null) {
                g.drawImage(imgMeta, metaX - 15, pistaY + 10, 44, pistaH - 20, null);
            } else {
                dibujarMeta(g, metaX, pistaY + 12, pistaH - 24);
            }

            if (estado == null) {
                g.setFont(new Font("SansSerif", Font.BOLD, 20));
                g.setColor(Color.WHITE);
                g.drawString("Esperando carrera...", pistaX + 28, pistaY + 58);

                g.setFont(new Font("SansSerif", Font.PLAIN, 14));
                g.setColor(new Color(220, 240, 225));
                g.drawString("Creá un jugador y seleccioná un caballo para comenzar.", pistaX + 28, pistaY + 84);
                g.dispose();
                return;
            }

            int cantidadCaballos = Math.max(1, estado.getCaballos().size());
            int carrilH = pistaH / cantidadCaballos;
            int indice = 1;

            for (CaballoDTO caballo : estado.getCaballos()) {
                int carrilY = pistaY + ((indice - 1) * carrilH);
                int centroY = carrilY + carrilH / 2;

                g.setColor(new Color(255, 255, 255, 45));
                g.drawLine(pistaX + 18, carrilY + carrilH, pistaX + pistaW - 18, carrilY + carrilH);

                double proporcion = caballo.getDistanciaRecorrida() / estado.getDistanciaTotal();
                int recorridoMax = metaX - pistaX - 86;
                int x = pistaX + 26 + (int) (Math.min(1.0, proporcion) * recorridoMax);

                g.setColor(new Color(255, 255, 255, 230));
                g.setFont(new Font("SansSerif", Font.BOLD, 13));
                g.drawString(caballo.getNombre(), pistaX + 24, centroY - 18);

                g.setFont(new Font("SansSerif", Font.PLAIN, 12));
                g.setColor(new Color(220, 240, 225));
                g.drawString("Energía " + (int) caballo.getEnergiaActual() + "%", pistaX + 24, centroY + 1);

                g.setColor(new Color(255, 255, 255, 70));
                g.fillRoundRect(pistaX + 126, centroY - 8, metaX - pistaX - 166, 8, 8, 8);

                g.setColor(colores.getOrDefault(indice, Color.LIGHT_GRAY));
                g.fillRoundRect(pistaX + 126, centroY - 8, Math.max(12, x - (pistaX + 126)), 8, 8, 8);

                java.awt.image.BufferedImage imgActual = imagenesCaballos.get(indice);
                if (imgActual != null) {
                    g.drawImage(imgActual, x, centroY - 26, 54, 54, null);
                } else {
                    dibujarCaballoFallback(g, x, centroY, colores.getOrDefault(indice, Color.LIGHT_GRAY), indice);
                }

                indice++;
            }

            g.dispose();
        }

        private void dibujarMeta(Graphics2D g, int x, int y, int alto) {
            int tile = 10;
            for (int fila = 0; fila < alto / tile; fila++) {
                for (int col = 0; col < 2; col++) {
                    boolean blanco = (fila + col) % 2 == 0;
                    g.setColor(blanco ? Color.WHITE : new Color(30, 30, 30));
                    g.fillRect(x + col * tile, y + fila * tile, tile, tile);
                }
            }
            g.setColor(Color.WHITE);
            g.setFont(new Font("SansSerif", Font.BOLD, 12));
            g.drawString("META", x - 7, y - 8);
        }

        private void dibujarCaballoFallback(Graphics2D g, int x, int centroY, Color color, int numero) {
            g.setColor(new Color(0, 0, 0, 35));
            g.fillOval(x + 2, centroY + 14, 48, 10);

            g.setColor(color);
            g.fillRoundRect(x, centroY - 15, 42, 26, 18, 18);
            g.fillOval(x + 31, centroY - 22, 20, 20);

            g.setColor(new Color(25, 42, 62));
            g.setStroke(new BasicStroke(3));
            g.drawLine(x + 9, centroY + 9, x + 5, centroY + 22);
            g.drawLine(x + 30, centroY + 9, x + 35, centroY + 22);
            g.drawLine(x + 6, centroY - 1, x - 6, centroY - 9);

            g.setFont(new Font("SansSerif", Font.BOLD, 11));
            g.setColor(Color.WHITE);
            g.drawString(String.valueOf(numero), x + 17, centroY + 2);
        }
    }
}
