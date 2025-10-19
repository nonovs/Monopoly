package monopoly; 

import partida.Jugador;
import monopoly.Menu;
import java.util.jar.JarEntry;

public class MonopolyETSE {

    public static void main(String[] args) {
        Menu menu = new Menu();
        if (args.length >= 1) {
            String fichero = args[0];
            System.out.println("Procesando fichero de comandos: " + fichero);
            boolean stop = menu.procesarFichero(fichero);
            if (stop) {
                System.out.println("Ejecución detenida por 'salir' en el fichero.");
                return;
            }
        }
        new Menu().iniciarPartida();
    }

}