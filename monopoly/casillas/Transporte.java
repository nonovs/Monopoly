package monopoly.casillas;

import partida.Jugador;

import static monopoly.Juego.consola;

public class Transporte extends Casilla {

    private final float valor;
    private final float alquiler;

    // Constructor
    public Transporte(String nombre, int posicion, int valor, float alquiler, Jugador duenho) {
        super(nombre, "Transporte", posicion, valor, duenho);
        this.valor = valor;
        this.alquiler = alquiler;
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (getDuenho() == null || getDuenho() == actual || getDuenho() == banca)
            return true;
        int numTransportes = 0;
        for (Casilla c : getDuenho().getPropiedades()) {
            if (c instanceof Transporte) {
                numTransportes++;
            }
        }
        float alquilerTotal = alquiler * numTransportes;
        if (actual.getFortuna() >= alquilerTotal) {
            actual.pagar(alquilerTotal);
            getDuenho().recibir(alquilerTotal);

            //Como siempre guardamos datos para estadísticas  del juego
            actual.acumularPagoDeAlquileres(alquilerTotal);
            getDuenho().acumularCobroDeAlquileres(alquilerTotal);
            this.sumarAlquilerGenerado(alquiler);
            consola.imprimir(String.format("%s paga %.0f a %s por el transporte %s.",
                    actual.getNombre(), alquilerTotal, getDuenho().getNombre(), getNombre()));
            return true;
        } else {
            consola.imprimir(String.format("%s no puede pagar el alquiler de %.0f por %s.",
                    actual.getNombre(), alquilerTotal, getNombre()));
            return false;
        }
    }

    @Override
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        if (getDuenho() != banca) {
            consola.imprimir("Este transporte ya tiene dueño.");
            return;
        }

        if (solicitante.getPosicion() != this.getPosicion()) {
            consola.imprimir("Solo puedes comprar la casilla en la que estás situado.");
            return;
        }

        if (solicitante.getFortuna() < getValor()) {
            consola.imprimir(solicitante.getNombre() + " no tiene suficiente dinero para comprar " + getNombre());
            return;
        }

        solicitante.pagar(getValor());
        solicitante.acumularDineroInvertido(getValor());
        setDuenho(solicitante);
        solicitante.anhadirPropiedad(this);

        consola.imprimir(String.format(
                "El jugador %s compra el transporte %s por %.0f€. Su fortuna actual es %.0f€.",
                solicitante.getNombre(), getNombre(), getValor(), solicitante.getFortuna()
        ));
    }

    public float getAlquiler() {
        return alquiler;
    }

    @Override
    public String infoCasilla() {
        return String.format(
                "{%n" +
                        " tipo: transporte,%n" +
                        " nombre: %s,%n" +
                        " posicion: %d,%n" +
                        " propietario: %s,%n" +
                        " valor: %.0f,%n" +
                        " alquiler: %.0f%n" +
                        "}",
                getNombre(),
                getPosicion(),
                getDuenho() != null ? getDuenho().getNombre() : "banca",
                valor,
                alquiler
        );
    }

    @Override
    public String casEnVenta() {
        if (getDuenho() != null && !"Banca".equalsIgnoreCase(getDuenho().getNombre())) {
            return "";
        }

        return String.format(
                "{\n    tipo: transporte,\n valor: %.0f\n}",
                getValor()
        );
    }
}