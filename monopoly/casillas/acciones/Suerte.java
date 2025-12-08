package monopoly.casillas.acciones;

import monopoly.Tablero;
import monopoly.cartas.CartaSuerte;
import partida.Jugador;
import java.util.List;
import static monopoly.Juego.consola;


public class Suerte extends Accion {

    // Índice global para las cartas de Suerte (1..7 en bucle)
    private static int indiceCarta = 0;
    private static final int NUM_CARTAS = 7;

    public Suerte(String nombre, int posicion, Jugador duenho) {
        super(nombre, posicion, duenho);
    }

    private static int siguienteCarta() {
        // 0 -> 1, 1 -> 2, ..., 7 -> 1
        indiceCarta = (indiceCarta % NUM_CARTAS) + 1;
        return indiceCarta;
    }

    /**
     * Roba una carta de Suerte y delega la accion en la jerarquia de cartas.
     */
    public boolean aplicarCarta(Tablero tablero, Jugador actual, Jugador banca, List<Jugador> jugadores, int tirada) {
        int carta = siguienteCarta();
        consola.imprimir(String.format("%s roba carta de Suerte nº %d.%n", actual.getNombre(), carta));
        // Delegamos la logica en la carta correspondiente
        CartaSuerte cartaSuerte = new CartaSuerte(carta);
        return cartaSuerte.accion(tablero, actual, banca, jugadores, tirada);
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        consola.imprimir(String.format("%s ha caído en una casilla de Suerte.%n", actual.getNombre()));
        return true;
    }

    @Override
    public String infoCasilla() {
        return String.format(
                "{%n" +
                        " tipo: Suerte,%n" +
                        " nombre: %s,%n" +
                        " posicion: %d,%n" +
                        " propietario: %s%n" +
                        "}",
                getNombre(),
                getPosicion(),
                getDuenho() != null ? getDuenho().getNombre() : "banca"
        );
    }
}
