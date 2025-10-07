package monopoly.casillas;

import partida.Jugador;

public class Especial extends Casilla {

    public Especial(String nombre, int posicion, Jugador duenho) {
        super(nombre, "Especial", posicion, duenho);
    }

    @Override
    public String infoCasilla() {
        return "{tipo: Especial, nombre: " + getNombre() + "}";
    }
}