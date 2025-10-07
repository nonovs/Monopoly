package monopoly.casillas;

import partida.Jugador;

public class CajaComunidad extends Casilla {

    public CajaComunidad(String nombre, int posicion, Jugador duenho) {
        super(nombre, "Comunidad", posicion, duenho);
    }

    @Override
    public String infoCasilla() {
        return "{tipo: Comunidad, nombre: " + getNombre() + "}";
    }
}