package monopoly.casillas;

import monopoly.Tablero;
import monopoly.Valor;
import partida.Avatar;
import partida.Jugador;
import java.util.List;
import static monopoly.Juego.consola;

public class Especial extends Casilla {
    private Tablero tablero;

    // Constructor que recibe la referencia al tablero
    public Especial(String nombre, int posicion, Jugador duenho, Tablero tablero) {
        super(nombre, posicion, duenho);
        this.tablero = tablero;
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        int pos = getPosicion();
        switch (pos) {
            case 0: // Salida
                consola.imprimir(actual.getNombre() + " ha pasado por la salida.");
                return true;

            case 10: // Cárcel (visita)
                consola.imprimir(actual.getNombre() + " está en la casilla de la cárcel (visita).");
                return true;

            case 30: // Ir a la cárcel
                Casilla carcel = tablero.getCasilla(10);
                if (carcel != null) {
                    actual.enviarACarcel(carcel);
                    consola.imprimir(actual.getNombre() + " ha sido enviado a la cárcel.");
                } else {
                    consola.imprimir("No se pudo encontrar la casilla cárcel en el tablero.");
                }
                return true;

            default:
                consola.imprimir(actual.getNombre() + " ha caído en una casilla especial: " + getNombre());
                return true;
        }
    }

    @Override
    public String infoCasilla() {
        if (getPosicion() == 10) return "{tipo: Carcel, salir: " + Valor.SALIR_CARCEL + "}";
        return super.toString();
    }
}