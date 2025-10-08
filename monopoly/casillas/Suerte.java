package monopoly.casillas;

import partida.*;

public class Suerte extends Casilla {

    // Constructor
    public Suerte(String nombre, int posicion, Jugador duenho) {
        super(nombre, "Suerte", posicion, duenho);
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        // En esta entrega, no hay lógica de cartas, solo se notifica
        System.out.println(actual.getNombre() + " ha caído en una casilla de Suerte.");
        return true;
    }

    @Override
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        // No se puede comprar una casilla de suerte
    }

    @Override
    public String infoCasilla() {
        return String.format(
                "{tipo: suerte, nombre: %s, posicion: %d}",
                getNombre(), getPosicion()
        );
    }

    @Override
    public String casEnVenta() {
        return ""; // No está en venta
    }
}