package monopoly.casillas;

import monopoly.Menu.*;
import partida.Jugador;
import static monopoly.Juego.consola;

public class Impuestos extends Casilla {

    private float impuesto;
    private static int bote=0;//Static para que todas as instancias o compartan

    public Impuestos(String nombre, int posicion, float impuesto, Jugador duenho) {
        super(nombre, posicion, duenho); // usa el constructor específico de impuestos
        this.impuesto = impuesto;
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (actual.getFortuna() >= impuesto) {
            actual.acumularPagoTasasEImpuestos(impuesto); //añadido para estadisticas
            actual.pagar(impuesto);
            setBote(impuesto);
            consola.imprimir(String.format("El jugador paga "+ (int)impuesto +" € que se depositan en el Parking."));//O cast solo o fixen pa que imprima bonito todo en int's
            banca.recibir(impuesto);
            return true;
        } else {
            consola.imprimir(actual.getNombre() + "no tiene suficiente dinero para pagar el impuesto.");
            return false;
        }
    }

    //  Métodos estaticos del Bote (usados para Parking)
    public static void setBote(float impuesto){ bote+=impuesto; }
    public static int getBote() { return bote; }
    public static void resetBote(){ bote=0;}

    @Override
    public String infoCasilla() {
        return String.format("{%n" +
                "tipo: impuesto,%n" +
                "apagar: %.0f%n" +
                "}", impuesto);
    }
}