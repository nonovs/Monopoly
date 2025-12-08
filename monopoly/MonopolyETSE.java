package monopoly;

// import excepciones.Excepcion;  // <- Ya no es necesario, puedes borrarlo si quieres

/// Para ejecutar desde terminal ///
/// javac -d bin -sourcepath . monopoly/*.java monopoly/casillas/*.java monopoly/Construccion/*.java partida/*.java
/// java -cp bin monopoly.MonopolyETSE fichero.txt
public class MonopolyETSE {

    public static void main(String[] args) {

        Menu menu = new Menu();
        if (args.length >= 1) {
            String fichero = args[0];

            //System.out.println("Procesando fichero de comandos: " + fichero);
            boolean stop = menu.procesarFichero(fichero);
            if (stop) {
                //System.out.println("Ejecución detenida por 'salir' en el fichero.");
                return;
            }
        }
        menu.iniciarPartida();
    }

}
