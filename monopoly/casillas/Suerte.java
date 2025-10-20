package monopoly.casillas;

import partida.Jugador;

public class Suerte extends Casilla {

    // Constructor
    public Suerte(String nombre, int posicion, Jugador duenho) {
        super(nombre, "Suerte", posicion, duenho);
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        System.out.printf("%s ha caído en una casilla de Suerte.%n", actual.getNombre());
        // En futuras versiones se podría implementar aquí la lógica de cartas
        return true;
    }

    @Override
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        // No se puede comprar una casilla de suerte
        System.out.printf("La casilla %s no se puede comprar.%n", getNombre());
    }

    @Override
    public String infoCasilla() {
        return String.format(
                "{%n" +
                        " tipo: Suerte,%n" +
                        " nombre: %s,%n" +
                        " posicion: %d,%n" +
                        " propietario: %s%n" +
                        "}",
                getNombre(),
                getPosicion(),
                getDuenho() != null ? getDuenho().getNombre() : "banca"
        );
    }

    @Override
    public String casEnVenta() {
        // Las casillas de Suerte nunca están en venta
        return null;
    }
}
