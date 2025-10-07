package monopoly.casillas;

import monopoly.Grupo;
import partida.Jugador;

public class Solar extends Casilla{

    public Solar(String nombre, int posicion, Jugador duenho) {
        super(nombre,"Solar",posicion,duenho);

    }
    @Override
    public String infoCasilla(){
        return "{tipo: Solar, nombre : "+getNombre()+"}";
    }
}
