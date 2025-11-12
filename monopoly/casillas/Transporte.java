package monopoly.casillas;

import partida.Jugador;

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
            System.out.printf("%s paga %.0f a %s por el transporte %s.%n",
                    actual.getNombre(), alquilerTotal, getDuenho().getNombre(), getNombre());
            return true;
        } else {
            System.out.printf("%s no puede pagar el alquiler de %.0f por %s.%n",
                    actual.getNombre(), alquilerTotal, getNombre());

            return false;
        }
    }

    @Override
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        if (getDuenho() != banca) {
            System.out.println("Este transporte ya tiene dueño.");
            return;
        }

        if (solicitante.getPosicion() != this.getPosicion()) {
            System.out.println("Solo puedes comprar la casilla en la que estás situado.");
            return;
        }

        if (solicitante.getFortuna() < getValor()) {
            System.out.println(solicitante.getNombre() + " no tiene suficiente dinero para comprar " + getNombre());
            return;
        }

        solicitante.pagar(getValor());
        setDuenho(solicitante);
        solicitante.anhadirPropiedad(this);

        System.out.printf(
                "El jugador %s compra el transporte %s por %.0f€. Su fortuna actual es %.0f€.\n",
                solicitante.getNombre(), getNombre(), getValor(), solicitante.getFortuna()
        );
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
