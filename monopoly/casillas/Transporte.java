package monopoly.casillas;

import partida.Jugador;

public class Transporte extends Casilla {
    int impuesto;
    public Transporte(String nombre, int posicion, float valor, Jugador duenho) {
        super(nombre, "Transporte", posicion, valor, duenho);
        this.impuesto = 250000; // Alquiler fijo para transporte
    }

    @Override
    public String infoCasilla() {
        return "{tipo: Transporte, nombre: " + getNombre() + ", valor: " + valor + ", alquiler: " + impuesto + "}";
    }
}
