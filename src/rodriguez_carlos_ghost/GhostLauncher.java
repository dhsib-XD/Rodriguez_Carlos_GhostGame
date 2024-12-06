package rodriguez_carlos_ghost;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import rodriguez_carlos_ghost.Main;

public final class GhostLauncher extends Main {

    public String[][] espacio_juego = new String[6][6];
    public Player jugador1;
    public Player jugador2;
    public Player jugador_actual;
    public JFrame interfaz;
    public JPanel pantalla;
    public JButton[][] casillas = new JButton[6][6];
    public String[][] tablero = new String[6][6];
    public static final int tamaño = 6;
    public String dificultad = "NORMAL";
    public String modo_juego = "ALEATORIO";
    public static Player[] jugadores = new Player[100];
    public static int jugadores_registrados = 0;
    public boolean selección = true;
    public static GhostLauncher juego = new GhostLauncher();

    public GhostLauncher() {
        iniciar_tablero();
    }

    public boolean usuario_existente(String usuario) {
        for (int i = 0; i < jugadores_registrados; i++) {
            if (jugadores[i].getUsername().equals(usuario)) {
                return true;
            }
        }
        return false;
    }

    public Player jugador2(String usuario) {
        for (int i = 0; i < jugadores_registrados; i++) {
            if (jugadores[i].getUsername().equals(usuario) && !usuario.equalsIgnoreCase(jugador1.getUsername())) {
                jugador2 = jugadores[i];
                return jugadores[i];
            } else if (usuario.equalsIgnoreCase(jugador1.getUsername())) {
                JOptionPane.showMessageDialog(null, "No se puede ingresar el nombre del jugador actual.");
            }
        }
        return null;
    }

    public Player asignarJugador1(String usuario) {
        
        for (int i = 0; i < jugadores_registrados; i++) {
            if (jugadores[i].getUsername().equals(usuario)) {
                jugador1 = jugadores[i]; // Asigna el jugador encontrado como jugador1
                JOptionPane.showMessageDialog(null, "Jugador 1 asignado exitosamente: " + usuario);
                return jugador1;
            }
        }
        JOptionPane.showMessageDialog(null, "Jugador no encontrado. Por favor, regístrese.", "Error", JOptionPane.ERROR_MESSAGE);
        return null; // Retorna null si el jugador no existe
    }

    public void iniciar_tablero() {
        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                espacio_juego[i][j] = "";
            }
        }
    }

    public void crear_tablero() {
        interfaz = new JFrame("Juego Ghosts");
        interfaz.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        interfaz.setLocation(450, 100);
        pantalla = new JPanel(new GridLayout(tamaño, tamaño));
        casillas = new JButton[tamaño][tamaño];

        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                casillas[i][j] = new JButton();
                casillas[i][j].setBackground(Color.getHSBColor(32, 81, 38));
                casillas[i][j].setPreferredSize(new Dimension(100, 100));
                pantalla.add(casillas[i][j]);
            }
        }

        interfaz.add(pantalla);
        interfaz.pack();
        interfaz.setVisible(true);
        if (modo_juego.equals("ALEATORIO")) {
            fantasmas_aleatorio();
        } else {
            fantasmas_manual();
        }
    }

    public void iniciar_juego() {
        // Asegúrate de asignar el jugador1 antes de iniciar el juego
        String usuario1 = JOptionPane.showInputDialog("Ingrese el nombre del Jugador 1:");
        jugador1 = asignarJugador1(usuario1);

        if (jugador1 != null) {
            jugador_actual = jugador1; // El jugador 1 comienza el juego
            crear_tablero();
            actualizar_tablero_visibilidad();
            inicializar_fantasmas();
            detectar_fantasmas();
        } else {
            JOptionPane.showMessageDialog(null, "El juego no puede comenzar sin un Jugador 1 válido.");
        }
    }

    public void movimiento() {
        if (jugador_actual == null) { // Verifica si jugador_actual es nulo
            JOptionPane.showMessageDialog(null, "Error: Jugador actual no está inicializado.");
            return;
        }

        int filaInicial, columnaInicial;

        while (!game_over()) {
            while (true) {
                try {
                    filaInicial = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la fila del fantasma que desea mover (0-5):"));
                    columnaInicial = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la columna del fantasma que desea mover (0-5):"));

                    if (!coordenada_valida(filaInicial, columnaInicial)) {
                        JOptionPane.showMessageDialog(null, "Coordenadas fuera de rango. Intente nuevamente.");
                        continue;
                    }

                    String fantasma = espacio_juego[filaInicial][columnaInicial];
                    if (!fantasma.equals("FBueno1") && !fantasma.equals("FMalo1") && !fantasma.equals("FBueno2") && !fantasma.equals("FMalo2")) {
                        JOptionPane.showMessageDialog(null, "No hay un fantasma válido en esa posición. Intente nuevamente.");
                        continue;
                    }

                    if (!esFantasmaDeJugador(filaInicial, columnaInicial, jugador_actual)) {
                        JOptionPane.showMessageDialog(null, "Ese fantasma no te pertenece. Intente nuevamente.");
                        continue;
                    }

                    break;
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Ingrese números válidos para las coordenadas.");
                }
            }
        
    



        int filaDestino, columnaDestino;
        while (true) {
            try {
                filaDestino = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la fila de destino (0-5):"));
                columnaDestino = Integer.parseInt(JOptionPane.showInputDialog("Ingrese la columna de destino (0-5):"));

                if (!coordenada_valida(filaDestino, columnaDestino)) {
                    JOptionPane.showMessageDialog(null, "Coordenadas fuera de rango. Intente nuevamente.");
                    continue;
                }
                
                if (Math.abs(filaDestino - filaInicial) > 1 || Math.abs(columnaDestino - columnaInicial) > 1) {
                    JOptionPane.showMessageDialog(null, "El movimiento solo puede ser de una casilla en horizontal o vertical.");
                    continue;
                }

                String fantasmaEnDestino = espacio_juego[filaDestino][columnaDestino];

                // Verifica que no haya un fantasma en la casilla de destino
                if (!fantasmaEnDestino.equals("")) {
                    if (esFantasmaDeJugador(filaDestino, columnaDestino, jugador_actual)) {
                        JOptionPane.showMessageDialog(null, "No puedes capturar tus propios fantasmas. Intente nuevamente.");
                        continue;
                    }

                    if (fantasmaEnDestino.equals("FBueno1") || fantasmaEnDestino.equals("FMalo1") || fantasmaEnDestino.equals("FBueno2") || fantasmaEnDestino.equals("FMalo2")) {
                        // Captura de fantasmas buenos o malos
                        String atacante = espacio_juego[filaInicial][columnaInicial];
                        if (atacante.equals("FMalo1") && (fantasmaEnDestino.equals("FBueno2") || fantasmaEnDestino.equals("FMalo2"))) {
                            JOptionPane.showMessageDialog(null, "¡Fantasma capturado por FMalo1!");
                            capturarFantasma(filaDestino, columnaDestino);
                        } else if (atacante.equals("FMalo2") && (fantasmaEnDestino.equals("FBueno1") || fantasmaEnDestino.equals("FMalo1"))) {
                            JOptionPane.showMessageDialog(null, "¡Fantasma capturado por FMalo2!");
                            capturarFantasma(filaDestino, columnaDestino);
                        } else {
                            JOptionPane.showMessageDialog(null, "Solo los fantasmas adecuados pueden capturar.");
                            continue;
                        }
                    }
                }

                // Verificar que el movimiento es solo horizontal o vertical
                if (filaInicial != filaDestino && columnaInicial != columnaDestino) {
                    JOptionPane.showMessageDialog(null, "Los fantasmas solo pueden moverse en línea recta (horizontal o vertical).");
                    continue;
                }

                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Ingrese números válidos para las coordenadas.");
            }
        }

        String fantasma = espacio_juego[filaInicial][columnaInicial];
        espacio_juego[filaDestino][columnaDestino] = fantasma;
        espacio_juego[filaInicial][columnaInicial] = "";

        // Actualiza el texto de las casillas con "FBueno" o "FMalo" según corresponda
        if (fantasma.equals("FBueno1") || fantasma.equals("FBueno2")) {
            casillas[filaDestino][columnaDestino].setText("F");
        } else if (fantasma.equals("FMalo1") || fantasma.equals("FMalo2")) {
            casillas[filaDestino][columnaDestino].setText("F");
        }

        casillas[filaDestino][columnaDestino].setBackground(casillas[filaInicial][columnaInicial].getBackground());
        casillas[filaInicial][columnaInicial].setText("");
        casillas[filaInicial][columnaInicial].setBackground(Color.getHSBColor(32, 81, 38));

        JOptionPane.showMessageDialog(null, "Movimiento realizado con éxito.");
        cambio_turno();
    }
}


    public void capturarFantasma(int fila, int columna) {
        // Elimina el fantasma capturado del tablero
        espacio_juego[fila][columna] = "";
        casillas[fila][columna].setText("");
        casillas[fila][columna].setBackground(Color.getHSBColor(32, 81, 38));
    }

    public void fantasmas_aleatorio() {
        int contadorJugador1 = 0;
        int fila = 0;
        int columna = 0;
        while (contadorJugador1 < 8) {
            do {
                fila = (int) (Math.random() * 2);
                columna = (int) (Math.random() * 6);
            } while ((fila == 0 && columna == 0) || (fila == 0 && columna == 5));

            // Verificar si la casilla ya está ocupada
            if (espacio_juego[fila][columna].equals("")) {
                // Colocar el fantasma con nombre FBueno1 o FMalo1 dependiendo de la condición
                tablero[fila][columna] = (contadorJugador1 % 2 == 0) ? "F1" : "F1"; // FBueno1: bueno, FMalo1: malo
                casillas[fila][columna].setText(contadorJugador1 % 2 == 0 ? "F" : "F");
                casillas[fila][columna].setBackground(contadorJugador1 % 2 == 0 ? Color.GREEN : Color.RED);
                espacio_juego[fila][columna] = contadorJugador1 % 2 == 0 ? "FBueno1" : "FMalo1"; // Marcar la casilla como ocupada
                contadorJugador1++;
            }
        }

        int contadorJugador2 = 0;
        while (contadorJugador2 < 8) {
            do {
                fila = 4 + (int) (Math.random() * 2);
                columna = (int) (Math.random() * 6);
            } while ((fila == 5 && columna == 5) || (fila == 5 && columna == 0));

            if (espacio_juego[fila][columna].equals("")) {
                // Colocar el fantasma con nombre FBueno2 o FMalo2 dependiendo de la condición
                tablero[fila][columna] = (contadorJugador2 % 2 == 0) ? "F2" : "F2"; // FBueno2: bueno, FMalo2: malo
                casillas[fila][columna].setText(contadorJugador2 % 2 == 0 ? "F" : "F");
                casillas[fila][columna].setBackground(contadorJugador2 % 2 == 0 ? Color.GREEN : Color.RED);
                espacio_juego[fila][columna] = contadorJugador2 % 2 == 0 ? "FBueno2" : "FMalo2"; // Marcar la casilla como ocupada
                contadorJugador2++;
            }
        }
        actualizar_tablero_visibilidad();
        movimiento();
    }

    public void fantasmas_manual() {
        for (int i = 0; i < 8; i++) {
            int fila = -1, columna = -1;

            while (true) {
                String inputFila = JOptionPane.showInputDialog(null, "Jugador " + jugador1.getUsername() + ", ingresa la fila (0-1) para colocar un fantasma:");
                String inputColumna = JOptionPane.showInputDialog(null, "Jugador " + jugador1.getUsername() + ", ingresa la columna (0-5):");

                try {
                    fila = Integer.parseInt(inputFila);
                    columna = Integer.parseInt(inputColumna);

                    if (fila >= 0 && fila <= 1 && columna >= 0 && columna <= 5 && espacio_juego[fila][columna].equals(" ")) {
                        espacio_juego[fila][columna] = (i % 2 == 0) ? "B" : "M";
                        break;
                    } else {
                        JOptionPane.showMessageDialog(null, "Coordenadas inválidas o el espacio ya está ocupado. Intenta nuevamente.");
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Por favor ingresa números válidos para la fila y columna.");
                }
            }
        }
        JOptionPane.showMessageDialog(null, "¡Todos los fantasmas han sido colocados!");
    }

   public void actualizar_tablero_visibilidad() {
    for (int i = 0; i < tamaño; i++) {
        for (int j = 0; j < tamaño; j++) {
            String contenido = espacio_juego[i][j]; // Obtener contenido de la casilla

            if (contenido == null || contenido.isEmpty()) {
                casillas[i][j].setBackground(Color.getHSBColor(32, 81, 38)); // Casilla vacía
                casillas[i][j].setText("");
            } else if (jugador_actual != null && jugador_actual.equals(jugador1)) {
                // Fantasmas del jugador 2 deben ser visibles solo para jugador 2
                if (contenido.equals("FBueno2") || contenido.equals("FMalo2")) {
                    casillas[i][j].setBackground(Color.WHITE); // Mostrar para el jugador 2
                    casillas[i][j].setText("F");
                } else if (contenido.equals("FBueno1")) {
                    casillas[i][j].setBackground(Color.GREEN);
                    casillas[i][j].setText("F");
                } else if (contenido.equals("FMalo1")) {
                    casillas[i][j].setBackground(Color.RED);
                    casillas[i][j].setText("F");
                }
            } else if (jugador_actual != null && jugador_actual.equals(jugador2)) {
                // Fantasmas del jugador 1 deben ser visibles solo para jugador 1
                if (contenido.equals("FBueno1") || contenido.equals("FMalo1")) {
                    casillas[i][j].setBackground(Color.WHITE); // Mostrar para el jugador 1
                    casillas[i][j].setText("F");
                } else if (contenido.equals("FBueno2")) {
                    casillas[i][j].setBackground(Color.GREEN);
                    casillas[i][j].setText("F");
                } else if (contenido.equals("FMalo2")) {
                    casillas[i][j].setBackground(Color.RED);
                    casillas[i][j].setText("F");
                }
            }
        }
    }
}


    public void inicializar_fantasmas() {
        // Jugador 1: Asignar fantasmas FBueno1 y FMalo1 en su área inicial
        for (int i = 0; i < 4; i++) {
            espacio_juego[5][i] = (i % 2 == 0) ? "FBueno1" : "FMalo1"; // Alternar entre FBueno1 y FMalo1
            jugador_actual.agregarFantasma(5, i); // Registrar posición inicial
        }

        // Jugador 2: Asignar fantasmas FBueno2 y FMalo2 en su área inicial
        for (int i = 0; i < 4; i++) {
            espacio_juego[0][i] = (i % 2 == 0) ? "FBueno2" : "FMalo2"; // Alternar entre FBueno2 y FMalo2
            jugador2.agregarFantasma(0, i); // Registrar posición inicial
        }
    }

    public void mostrar_fantasmas(Player jugador) {
        String mensaje = "Fantasmas de " + jugador.getUsername() + ":\n";
        for (int i = 0; i < jugador.cantidad_fantasmas; i++) {
            mensaje += "Fila: " + jugador.fantasmas[i][0] + ", Columna: " + jugador.fantasmas[i][1] + "\n";
        }
        JOptionPane.showMessageDialog(null, mensaje);
    }

    public void detectar_fantasmas() {
        // Reiniciar los fantasmas de los jugadores
        jugador1.resetFantasmas();
        jugador2.resetFantasmas();

        // Detectar fantasmas en el tablero y asignarlos a los jugadores
        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                String fantasma = espacio_juego[i][j];
                if (fantasma.equals("FBueno1") || fantasma.equals("FMalo1")) {
                    jugador1.agregarFantasma(i, j); // Fantasma del jugador 1
                } else if (fantasma.equals("FBueno2") || fantasma.equals("FMalo2")) {
                    jugador2.agregarFantasma(i, j); // Fantasma del jugador 2
                }
            }
        }
    }

    public boolean esFantasmaDeJugador(int fila, int columna, Player jugador) {
        if (!coordenada_valida(fila, columna)) {
            return false; // Coordenadas fuera de rango
        }

        // Verificar que el fantasma pertenece al jugador actual
        String contenido = espacio_juego[fila][columna];
        if (jugador == jugador1 && (contenido.equals("FBueno1") || contenido.equals("FMalo1"))) {
            return true; // Fantasma del jugador 1
        } else if (jugador == jugador2 && (contenido.equals("FBueno2") || contenido.equals("FMalo2"))) {
            return true; // Fantasma del jugador 2
        }

        return false; // No pertenece al jugador
    }

    public void mostrar_tablero() {
        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                System.out.print(espacio_juego[i][j] + " "); // Mostrar cada casilla
            }
            System.out.println(); // Nueva línea por cada fila
        }
    }

    public void turno_jugador() {
        // Mostrar fantasmas del jugador actual antes de su turno
        mostrar_fantasmas(jugador_actual);
        // Lógica del turno
    }

    public boolean fantasma_valido(int fila, int columna) {
        return !espacio_juego[fila][columna].equals("");
    }

    public boolean coordenada_valida(int fila, int columna) {
        return fila >= 0 && fila < tamaño && columna >= 0 && columna < tamaño;
    }

    public boolean movimiento_valido(int fila, int columna, int filaDestino, int columnaDestino) {
        if (filaDestino < 0 || filaDestino >= 6 || columnaDestino < 0 || columnaDestino >= 6) {
            return false;
        }

        // Verificar que el destino esté vacío
        if (!tablero[filaDestino][columnaDestino].isEmpty()) {
            return false;
        }

        return (Math.abs(fila - filaDestino) == 1 && columna == columnaDestino)
                || (Math.abs(columna - columnaDestino) == 1 && fila == filaDestino);
    }

    public void cambio_turno() {
        if (jugador_actual == jugador1) {
            jugador_actual = jugador2;
        } else {
            jugador_actual = jugador1;
        }
        JOptionPane.showMessageDialog(null, "Es el turno de: " + jugador_actual.getUsername());
        actualizar_tablero_visibilidad();
    }

    public boolean game_over() {
        int fantasmas_buenos_j1 = contar_fantasmas_buenos(jugador2);
        int fantasmas_buenos_j2 = contar_fantasmas_buenos(jugador1);
        if (fantasmas_buenos_j1 == 0) {
            JOptionPane.showMessageDialog(null, jugador1.getUsername() + " gana al capturar todos los fantasmas buenos de " + jugador2.getUsername());
            return true;
        }
        if (fantasmas_buenos_j2 == 0) {
            JOptionPane.showMessageDialog(null, jugador2.getUsername() + " gana al capturar todos los fantasmas buenos de " + jugador1.getUsername());
            return true;
        }

        // Verificar si algún jugador ha perdido todos sus fantasmas malos
        int fantasmas_malos_j1 = contar_fantasmas_malos(jugador1);
        int fantasmas_malos_j2 = contar_fantasmas_malos(jugador2);
        if (fantasmas_malos_j1 == 0) {
            JOptionPane.showMessageDialog(null, jugador2.getUsername() + " gana al eliminar todos los fantasmas malos de " + jugador1.getUsername());
            return true;
        }
        if (fantasmas_malos_j2 == 0) {
            JOptionPane.showMessageDialog(null, jugador1.getUsername() + " gana al eliminar todos los fantasmas malos de " + jugador2.getUsername());
            return true;
        }

        // Verificar si algún jugador ha llevado un fantasma bueno a la salida del castillo del oponente
        for (int i = 0; i < tamaño; i++) {
            if (espacio_juego[0][i].equals("FBueno2")) {
                JOptionPane.showMessageDialog(null, jugador1.getUsername() + " gana al llevar un fantasma bueno a la salida de " + jugador2.getUsername());
                return true;
            }
            if (espacio_juego[5][i].equals("FBueno1")) {
                JOptionPane.showMessageDialog(null, jugador2.getUsername() + " gana al llevar un fantasma bueno a la salida de " + jugador1.getUsername());
                return true;
            }
        }

        return false;
    }

    public int contar_fantasmas_buenos(Player jugador) {
        int count = 0;
        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                if ((jugador == jugador1 && espacio_juego[i][j].equals("FBueno1")) || (jugador == jugador2 && espacio_juego[i][j].equals("FBueno2"))) {
                    count++;
                }
            }
        }
        return count;
    }

    public int contar_fantasmas_malos(Player jugador) {
        int count = 0;
        for (int i = 0; i < tamaño; i++) {
            for (int j = 0; j < tamaño; j++) {
                if ((jugador == jugador1 && espacio_juego[i][j].equals("FMalo1")) || (jugador == jugador2 && espacio_juego[i][j].equals("FMalo2"))) {
                    count++;
                }
            }
        }
        return count;
    }

    public void mostrar_resultado() {
        System.out.println("El juego ha terminado. Resultados:");
    }

    private static class Player {

        private int cantidad_fantasmas;
        private String[][] fantasmas;

        public Player(String usuario, String contraseña) {
            this();
        }

        public Player() {
        }

        private void agregarFantasma(int i, int i0) {
            throw new UnsupportedOperationException("Not supported yet."); 
        }

        private String getUsername() {
            throw new UnsupportedOperationException("Not supported yet."); 
        }

        private void resetFantasmas() {
            throw new UnsupportedOperationException("Not supported yet."); 
        }

        private Object getPassword() {
            throw new UnsupportedOperationException("Not supported yet."); 
        }
    }
}