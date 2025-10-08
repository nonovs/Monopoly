package monopoly.casillas;

import partida.*;

public class CajaComunidad extends Casilla {

    // Constructor
    public CajaComunidad(String nombre, int posicion, Jugador duenho) {
        super(nombre, "CajaComunidad", posicion, duenho);
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        // En esta entrega, no hay lógica de cartas, solo se notifica
        System.out.println(actual.getNombre() + " ha caído en una casilla de Caja de Comunidad.");
        return true;
    }

    @Override
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        // No se puede comprar una casilla de comunidad
    }

    @Override
    public String infoCasilla() {
        return String.format(
                "{tipo: caja comunidad, nombre: %s, posicion: %d}",
                getNombre(), getPosicion()
        );
    }

    @Override
    public String casEnVenta() {
        return ""; // No está en venta
    }
}