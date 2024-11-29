package rodriguez_carlos_ghost;

import java.util.Scanner;
import javax.swing.JOptionPane;

public class Menu {

    private String[][] usuariosContraseñas = new String[10][2]; // Arreglo bidimensional para almacenar usuarios y contraseñas
    private int contadorUsuarios = 0; // Contador de usuarios registrados

    public void MainMenu() {
        Scanner scanner = new Scanner(System.in);
        Main MainM = new Main(); // Instancia del Main
        
        while (true) {
            int opciones;

            opciones = Integer.parseInt(JOptionPane.showInputDialog("Bienvenido a Ghost Game\n"
                    + "Favor seleccionar una opción:\n"
                    + "1. Login\n"
                    + "2. Nuevo Jugador\n"
                    + "3. Salir"));

            if (opciones == 3) {
                JOptionPane.showMessageDialog(null, "Saliendo.........Hasta la próxima vez");
                break;
            }

            switch (opciones) {
                case 1: {
                    String usuario = JOptionPane.showInputDialog("Ingrese su Usuario:");
                    String contraseña = JOptionPane.showInputDialog("Ingrese su Contraseña:");
                    if (login(usuario, contraseña)) {
                        JOptionPane.showMessageDialog(null, "Usuario Encontrado");
                        MainM.MenuM();
                        
                    } else {
                        JOptionPane.showMessageDialog(null, "ERROR.........Usuario no encontrado o contraseña incorrecta.");
                    }
                    break;
                }
                case 2: {
                    while (true) {
                        String usuario = JOptionPane.showInputDialog("Ingrese un nuevo usuario:");
                        String contraseña = JOptionPane.showInputDialog("Ingrese una nueva contraseña:");
                        if (contraseña.length() >= 8) {
                            nuevoJugador(usuario, contraseña);
                            break;
                        } else {
                            JOptionPane.showMessageDialog(null, "La contraseña tiene que ser mayor o igual que 8 caracteres.");
                        }
                    }
                    break;   
                }
                default:
                    JOptionPane.showMessageDialog(null, "ERROR....... Opción No válida, favor intente nuevamente.");
            }
        }
    }

    public void nuevoJugador(String usuario, String contraseña) {
        if (contadorUsuarios < usuariosContraseñas.length) {
            usuariosContraseñas[contadorUsuarios][0] = usuario;
            usuariosContraseñas[contadorUsuarios][1] = contraseña;
            contadorUsuarios++;
            JOptionPane.showMessageDialog(null, "Usuario Creado!");
        } else {
            JOptionPane.showMessageDialog(null, "Llegó al límite de Usuarios disponibles.");
        }
    }

    public boolean login(String usuario, String contraseña) {
        for (int i = 0; i < contadorUsuarios; i++) {
            if (usuariosContraseñas[i][0].equals(usuario) && usuariosContraseñas[i][1].equals(contraseña)) {
                return true;
            }
        }
        return false;
    }
}
