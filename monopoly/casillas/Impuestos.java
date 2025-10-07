package monopoly.casillas;

import partida.Jugador;

public class Impuestos extends Casilla {

    public Impuestos(String nombre, int posicion, float cantidad, Jugador duenho) {
        super(nombre, posicion, cantidad, duenho);
    }

    @Override
    public String infoCasilla() {
        return "{tipo: Impuesto, nombre: " + getNombre()  + "}";
    }
}