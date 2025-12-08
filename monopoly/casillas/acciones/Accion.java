package monopoly.casillas.acciones;

import monopoly.casillas.Casilla;
import partida.Jugador;

public abstract class Accion extends Casilla {

    public Accion (String nombre, int posicion, Jugador duenho) {
        super(nombre, posicion, duenho);
    }

    //Dejamos el evaluarCasilla abstracto para que cada accion lo implemente
    @Override
    public abstract boolean evaluarCasilla (Jugador actual, Jugador banca, int tirada);

    @Override
    public String casEnVenta() {
        return "";  // Las acciones nunca están en venta
    }
}
