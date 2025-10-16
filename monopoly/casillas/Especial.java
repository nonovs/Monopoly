package monopoly.casillas;
import monopoly.Tablero;
import partida.*;

public class Especial extends Casilla {
    public Tablero tablero;
    // Constructor
    public Especial(String nombre, int posicion, Jugador duenho) {
        super(nombre, "Especial", posicion, duenho);
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        switch (getNombre()) {
            case "Ir a la cárcel":
                actual.enviarACarcel(tablero.getCasilla(10)); // Método que deberías tener en Jugador
                System.out.println(actual.getNombre() + " ha sido enviado a la cárcel.");
                return true;

            case "Parking gratuito":
                float bote = getValor(); // Usa el valor acumulado en la casilla
                actual.recibir(bote);
                setValor(0); // Se vacía el bote
                System.out.println(actual.getNombre() + " ha recibido " + bote + "€ del Parking.");
                return true;

            case "Salida":
                System.out.println(actual.getNombre() + " ha pasado por la salida.");
                return true;

            default:
                System.out.println(actual.getNombre() + " ha caído en una casilla especial: " + getNombre());
                return true;
        }
    }

    @Override
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        // No se puede comprar una casilla especial
    }

    @Override
    public String infoCasilla() {
        return String.format(
                "{tipo: especial, nombre: %s, posicion: %d}",
                getNombre(), getPosicion()
        );
    }

    @Override
    public String casEnVenta() {
        return ""; // No está en venta
    }
}