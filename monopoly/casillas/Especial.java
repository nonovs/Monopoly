package monopoly.casillas;

import monopoly.Tablero;
import monopoly.Valor;
import partida.Avatar;
import partida.Jugador;

import java.util.List;

/**
 * Casilla especial: Salida, Carcel, Parking, IrCarcel, ...
 */
public class Especial extends Casilla {
    private Tablero tablero;

    // Constructor que recibe la referencia al tablero
    public Especial(String nombre, int posicion, Jugador duenho, Tablero tablero) {
        super(nombre, "Especial", posicion, duenho);
        this.tablero = tablero;
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {

        int pos = getPosicion();

        switch (pos) {

            case 0: // Salida
                System.out.println(actual.getNombre() + " ha pasado por la salida.");
                return true;

            case 10: // Cárcel (visita)
                System.out.println(actual.getNombre() + " está en la casilla de la cárcel (visita).");
                return true;

            case 20: // Parking con bote (entero)
                int bote = Impuestos.getBote();

                if (bote > 0) {
                    actual.recibir(bote);
                    actual.acumularPremiosInversionesOBote(bote);
                    Impuestos.setBote(0);

                    System.out.printf("%s ha recibido %d del Parking gratuito.%n",
                            actual.getNombre(), bote);

                } else {
                    System.out.println(actual.getNombre() + " ha caído en Parking (sin bote).");
                }

                return true;

            case 30: // Ir a la cárcel
                Casilla carcel = tablero.getCasilla(10);

                if (carcel != null) {
                    actual.enviarACarcel(carcel);
                    System.out.println(actual.getNombre() + " ha sido enviado a la cárcel.");
                } else {
                    System.out.println("No se pudo encontrar la casilla cárcel en el tablero.");
                }
                return true;

            default:
                System.out.println(actual.getNombre() + " ha caído en una casilla especial: " + getNombre());
                return true;
        }
    }

    @Override
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        System.out.println("No puedes comprar una casilla de especial.");
    }

    @Override
    public String infoCasilla() {
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");

        switch (getPosicion()) {

            case 10: // Cárcel
                sb.append("salir: ").append(Valor.SALIR_CARCEL).append(", ");
                break;

            case 20: // Parking
                sb.append("bote: ").append(Impuestos.getBote()).append(", ");
                break;

            default:
                break;
        }

        sb.append("jugadores: [");

        List<Avatar> avs = getAvatares();
        for (int i = 0; i < avs.size(); i++) {
            Avatar a = avs.get(i);
            sb.append(a.getId());

            if (getPosicion() == 10) {
                sb.append(",").append(a.getJugador().getTurnosEnCarcel());
            }
            if (i < avs.size() - 1) sb.append("] [");
        }

        sb.append("] }");

        return sb.toString();
    }

    @Override
    public String casEnVenta() {
        return "";
    }
}
