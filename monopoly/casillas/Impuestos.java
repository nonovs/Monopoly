package monopoly.casillas;

import partida.*;

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
        return String.format(
                "{tipo: impuesto, nombre: %s, posicion: %d, cantidad: %.0f}",
                getNombre(), getPosicion(), impuesto
        );
    }

    @Override
    public String casEnVenta() {
        return ""; // No está en venta
    }
}