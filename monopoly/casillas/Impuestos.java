package monopoly.casillas;

import partida.*;

/**
 * Casilla de impuesto.
 */
public class Impuestos extends Casilla {

    private float impuesto;

    // Constructor
    public Impuestos(String nombre, int posicion, float impuesto, Jugador duenho) {
        super(nombre, posicion, impuesto, duenho); // usa el constructor específico de impuestos
        this.impuesto = impuesto;
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

    @Override
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        // No se puede comprar una casilla de impuesto
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
}