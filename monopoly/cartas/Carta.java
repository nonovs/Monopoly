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


    public abstract boolean accion(Tablero tablero, Jugador actual, Jugador banca, List<Jugador> jugadores, int tirada);
}
