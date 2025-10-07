package monopoly.casillas;

import partida.Jugador;

public class Servicios extends Casilla {
    public Servicios(String nombre, int posicion, float valor, Jugador duenho) {
        super(nombre, "Servicio", posicion, valor, duenho);
        // El alquiler depende de la tirada, así que no lo fijamos aquí
    }

    @Override
    public String infoCasilla() {
        return "{tipo: Servicio, nombre: " + getNombre() +"}";
    }
}
