package monopoly.cartas;

import monopoly.Tablero;
import partida.Jugador;

import java.util.List;

/**
 * Clase abstracta base para todas las cartas del juego.
 * Define los atributos y el método común que deben implementar
 * las cartas de Suerte y de Caja de Comunidad.
 */
public abstract class Carta {

    // Identificador de la carta (1..N dentro de su baraja)
    protected final int id;

    protected final String descripcion;

    /**
     * Constructor de la carta.
     *
     * @param id          
     * @param descripcion 
     */
    public Carta(int id, String descripcion) {
        this.id = id;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Ejecuta la acción asociada a la carta.
     *
     * @param tablero   Tablero de juego.
     * @param actual    Jugador al que se aplica la carta.
     * @param banca     Jugador que representa a la banca.
     * @param jugadores Lista de jugadores de la partida (para cartas que afectan a varios).
     * @param tirada    Tirada de dados que ha llevado al jugador a la casilla.
     *
     * @return true si el jugador sigue siendo solvente tras aplicar la carta,
     *         false si no puede hacer frente a los pagos (deuda/bancarrota).
     */
    public abstract boolean accion(Tablero tablero,
                                   Jugador actual,
                                   Jugador banca,
                                   List<Jugador> jugadores,
                                   int tirada);
}
