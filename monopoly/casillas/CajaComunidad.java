package monopoly.casillas;

import monopoly.Tablero;
import monopoly.cartas.CartaCajaComunidad;
import partida.Jugador;

import java.util.List;

import static monopoly.Juego.consola;

//hola
public class CajaComunidad extends Casilla {

    private static int indiceCarta = 0;       
    private static final int NUM_CARTAS = 6;  

    // Constructor
    public CajaComunidad(String nombre, int posicion, Jugador duenho) {
        super(nombre, "CajaComunidad", posicion, duenho);
    }

    private static int siguienteCarta() {
        // 0 -> 1, 1 -> 2, ..., 6 -> 1
        indiceCarta = (indiceCarta % NUM_CARTAS) + 1;
        return indiceCarta;
    }

    /**
     * Roba una carta de Caja de Comunidad y delega la accion en la jerarquia de cartas.
     */
    public boolean aplicarCarta(Tablero tablero, Jugador actual, Jugador banca,
                                List<Jugador> jugadores, int tirada) {

        //Esta funcion lo que hace es robar una carta de Caja de Comunidad y realizar la accion correspondiente
        int carta = siguienteCarta();
        consola.imprimir(String.format("%s roba carta de Caja de Comunidad nº %d.%n", actual.getNombre(), carta));

        CartaCajaComunidad cartaComunidad = new CartaCajaComunidad(carta);
        return cartaComunidad.accion(tablero, actual, banca, jugadores, tirada);
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        consola.imprimir(String.format(actual.getNombre() + " ha caído en una casilla de Caja de Comunidad."));
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
