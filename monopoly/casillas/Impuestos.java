package monopoly.casillas;

import monopoly.Menu.*;
import partida.Jugador;

/**
 * Casilla de impuesto.
 */
public class Impuestos extends Casilla {

    private int impuesto;
    private static int bote = 0; // Static para que todas las instancias lo compartan

    // Constructor
    public Impuestos(String nombre, int posicion, int impuesto, Jugador duenho) {
        super(nombre, posicion, impuesto, duenho); 
        this.impuesto = impuesto;
    }

    public Impuestos() {
        super();
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual.getFortuna() >= impuesto) {
            actual.pagar(impuesto);

            // Sumar al bote (entero)
            setBote(impuesto);

            System.out.println("El jugador paga " + impuesto + "€ que se depositan en el Parking.");
            banca.recibir(impuesto);
            return true;

        } else {
            return false;
        }
    }

    // Ahora el bote es int, así que el setter también
    public static int setBote(int cantidad) {
        bote += cantidad;
        return bote;
    }

    public static int getBote() {
        return bote;
    }

    @Override
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        System.out.println("No puedes comprar una casilla de impuestos.");
    }

    @Override
    public String infoCasilla() {
        return String.format("{%n" +
                "tipo: impuesto,%n" +
                "apagar: %d%n" +
                "}", impuesto);
    }

    @Override
    public String casEnVenta() {
        return ""; 
    }
}
