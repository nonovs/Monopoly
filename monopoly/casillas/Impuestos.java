package monopoly.casillas;

import monopoly.Menu.*;
import partida.Jugador;

/**
 * Casilla de impuesto.
 */
public class Impuestos extends Casilla {

    private float impuesto;
    private float bote=0;
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
            actual.pagar(impuesto);
            banca.recibir(impuesto);
            return true;
        } else {
            // Aquí podrías invocar lógica de bancarrota
            return false;
        }
    }
    public float setBote(float impuesto){
        return bote+=impuesto;
    }
    @Override
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        System.out.println("No puedes comprar una casilla de impuestos.");
    }

    @Override
    public String infoCasilla() {
        // Devuelve solo el bloque solicitado:
        // {
        // tipo: impuesto,
        // apagar: <cantidad>
        // }
        return String.format("{%n" +
                "tipo: impuesto,%n" +
                "apagar: %.0f%n" +
                "}", impuesto);
    }

    @Override
    public String casEnVenta() {
        return ""; // No está en venta
    }

    public float getBote() {
        return bote;
}


}