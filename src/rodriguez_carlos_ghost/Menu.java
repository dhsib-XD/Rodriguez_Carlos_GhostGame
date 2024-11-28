package rodriguez_carlos_ghost;

import java.util.Scanner;
import javax.swing.JOptionPane;

public class Menu {

    private String[][] usuariosContraseñas = new String[10][2]; // Arreglo bidimensional para almacenar usuarios y contraseñas
    private int contadorUsuarios = 0; // Contador de usuarios registrados

    public void MainMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            int opciones;

            opciones = Integer.parseInt(JOptionPane.showInputDialog("Bievenido a Ghost Game\n"
                    +"Favor seleccionar una opcion:\n"
                    + "1. Login\n"
                    + "2. Nuevo Jugador\n"
                    + "3. Salir"));

            if (opciones == 3) {
                JOptionPane.showMessageDialog(null, "Saliendo.........Hasta la Proxima vez");
                break;
            }

            switch (opciones) {
                case 1: {
                    String usuario = JOptionPane.showInputDialog("Ingrese su Nuevo Usuario:");
                    String contraseña = JOptionPane.showInputDialog("Ingrese su Nueva contraseña:");
                    if (login(usuario, contraseña)) {
                        JOptionPane.showMessageDialog(null, "Usuario Encontrado");
                    } else {
                        JOptionPane.showMessageDialog(null, "ERROR.........Usuario no encontrado o contraseña incorrecta.");
                    }
                    
                    break;
                }
                case 2: {
                    while (true){
                        
                        String usuario = JOptionPane.showInputDialog("Ingrese un nuevo usuario:");
                    String contraseña = JOptionPane.showInputDialog("Ingrese una nueva contraseña:");
                    if (contraseña.length() >= 8) {
                        
                    nuevoJugador(usuario, contraseña);
                    break;
                    }else{
                    JOptionPane.showMessageDialog(null, "La contraseña tiene que ser mayor o igual que 8 caracters");
                    
                        
                    }
                    
                    }
                    
                 break;   
                }
                default:
                    JOptionPane.showMessageDialog(null, "ERROR....... Opcion No valida favor intente nuevamente");
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
            JOptionPane.showMessageDialog(null, "Llego al limite de Usuarios disponibles ");
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
