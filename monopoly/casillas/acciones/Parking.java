package monopoly.casillas.acciones;

import monopoly.casillas.Impuestos;
import partida.Jugador;
import static monopoly.Juego.consola;

public class Parking extends Accion {

    public Parking (String nombre, int posicion, Jugador duenho) {
        super (nombre, posicion, duenho);
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        int bote = Impuestos.getBote(); // Accedemos al bote estático de Impuestos

        if (bote > 0) {
            actual.recibir(bote);
            actual.acumularPremiosInversionesOBote(bote);
            Impuestos.resetBote();

            consola.imprimir(String.format("%s ha recibido %d del Parking gratuito.", actual.getNombre(), bote));
        } else {
            consola.imprimir(actual.getNombre() + " ha caído en Parking (sin bote).");
        }
        return true;
    }

    @Override
    public String infoCasilla() {
        return String.format("{nombre: %s, tipo: Parking, bote: %d}", getNombre(), Impuestos.getBote());
    }
}
