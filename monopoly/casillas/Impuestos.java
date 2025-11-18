package monopoly.casillas;

import monopoly.Menu.*;
import partida.Jugador;

/**
 * Casilla de impuesto.
 */
public class Impuestos extends Casilla {

    private float impuesto;
    private static int bote=0;//Static para que todas as instancias o compartan
    // Constructor
    public Impuestos(String nombre, int posicion, float impuesto, Jugador duenho) {
        super(nombre, posicion, impuesto, duenho); // usa el constructor específico de impuestos
        this.impuesto = impuesto;
    }

    public Impuestos() {
        super();
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual.getFortuna() >= impuesto) {

            actual.acumularPagoTasasEImpuestos(impuesto); //añadido para estadisticas

            actual.pagar(impuesto);
            setBote(impuesto);
            System.out.println("El jugador paga "+ impuesto +"€ que se depositan en el Parking.");
            banca.recibir(impuesto);
            return true;
        } else {

            return false;
        }
    }
    public static float setBote(float impuesto){
        return bote+=impuesto;
    }
    public static float resetBote(){
        return bote=0;
    }
    @Override
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        System.out.println("No puedes comprar una casilla de impuestos.");
    }

    @Override
    public String infoCasilla() {

        return String.format("{%n" +
                "tipo: impuesto,%n" +
                "apagar: %.0f%n" +
                "}", impuesto);
    }

    @Override
    public String casEnVenta() {
        return ""; // No  está en venta
    }

    public static int getBote() {
        return bote;
}



}