package monopoly.casillas;

import partida.Jugador;

public class Servicios extends Casilla {

    private float factorServicio = 50000; // Según el guión del juego

    // Constructor
    public Servicios(String nombre, int posicion, Jugador duenho) {
        super(nombre, "Servicio", posicion, duenho);
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        // Si la casilla no tiene dueño o es del mismo jugador/banca → no se paga nada
        if (getDuenho() == null || getDuenho() == actual || getDuenho() == banca)
            return true;

        // Cálculo de alquiler: 4 × tirada × factorServicio
        float alquiler = 4 * tirada * factorServicio;

        if (actual.getFortuna() >= alquiler) {
            actual.pagar(alquiler);
            getDuenho().recibir(alquiler);
            System.out.printf("%s paga %.0f a %s por caer en %s.%n",
                    actual.getNombre(), alquiler, getDuenho().getNombre(), getNombre());
            return true;
        } else {
            System.out.printf("%s no puede pagar el alquiler de %.0f en %s.%n",
                    actual.getNombre(), alquiler, getNombre());
            return false;
        }
    }

    @Override
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        if (getDuenho() == banca && solicitante.getFortuna() >= getValor()) {
            solicitante.pagar(getValor());
            setDuenho(solicitante);
            System.out.printf("%s ha comprado el servicio %s por %.0f.%n",
                    solicitante.getNombre(), getNombre(), getValor());
        }
    }

    @Override
    public String infoCasilla() {
        String duenhoStr = (getDuenho() != null) ? getDuenho().getNombre() : "banca";
        return String.format(
                "{%n" +
                        " tipo: Servicio,%n" +
                        " nombre: %s,%n" +
                        " posicion: %d,%n" +
                        " propietario: %s,%n" +
                        " valor: %.0f,%n" +
                        " factor servicio: %.0f%n" +
                        "}",
                getNombre(), getPosicion(), duenhoStr, getValor(), factorServicio
        );
    }

    @Override
    public String casEnVenta() {
        return String.format(
                "{tipo: Servicio, nombre: %s, valor: %.0f}",
                getNombre(), getValor()
        );
    }
}
