package monopoly.casillas;

import monopoly.Tablero;
// import monopoly.Valor;       // Ya no es necesario aqui
// import partida.Avatar;       // Ya no es necesario aqui
import monopoly.cartas.CartaSuerte;
import partida.Jugador;

import java.util.List;

import static monopoly.Juego.consola;


public class Suerte extends Casilla {

    // Índice global para las cartas de Suerte (1..7 en bucle)
    private static int indiceCarta = 0;
    private static final int NUM_CARTAS = 7;

    // Constructor
    public Suerte(String nombre, int posicion, Jugador duenho) {
        super(nombre, "Suerte", posicion, duenho);
    }

    private static int siguienteCarta() {
        // 0 -> 1, 1 -> 2, ..., 7 -> 1
        indiceCarta = (indiceCarta % NUM_CARTAS) + 1;
        return indiceCarta;
    }

    /**
     * Roba una carta de Suerte y delega la accion en la jerarquia de cartas.
     */
    public boolean aplicarCarta(Tablero tablero, Jugador actual, Jugador banca,
                                List<Jugador> jugadores, int tirada) {

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
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        // No se puede comprar una casilla de suerte
       consola.imprimir(String.format("La casilla %s no se puede comprar.%n", getNombre()));
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

    @Override
    public String casEnVenta() {
        return ""; //No esta en venta
    }
}
