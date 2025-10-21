package monopoly.casillas;

import monopoly.Grupo;
import partida.Jugador;

public class Solar extends Casilla {

    private float alquilerBase;
    private int casas;
    private boolean hotel;
    private boolean piscina;
    private boolean pistaDeporte;


    // Constructor
    public Solar(String nombre, int posicion, float valor, float hipoteca, float alquilerBase, Jugador duenho, Grupo grupo) {
        super(nombre, "Solar", posicion, valor, duenho);
        this.setHipoteca(hipoteca); // guardamos en la clase padre
        this.alquilerBase = alquilerBase;
        this.setGrupo(grupo);       // también lo guardamos en la clase padre
        this.casas = 0;
        this.hotel = false;
        this.piscina = false;
        this.pistaDeporte = false;
    }

    // --- POLIMORFISMO: comportamiento diferente al de Casilla ---
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (getDuenho() == null || getDuenho() == actual || getDuenho() == banca)
            return true;

        float alquiler = calcularAlquiler();
        if (actual.getFortuna() >= alquiler) {
            actual.pagar(alquiler);
            getDuenho().recibir(alquiler);
            return true;
        } else {
            // Lógica en caso de insolvencia
            System.out.println(actual.getNombre() + " no puede pagar el alquiler de " + alquiler);
            return false;
        }
    }

    @Override
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        if (getDuenho() != banca) {
            System.out.println("Este solar ya tiene dueño.");
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
                "El jugador %s compra la casilla %s por %.0f€. Su fortuna actual es %.0f€.\n",
                solicitante.getNombre(), getNombre(), getValor(), solicitante.getFortuna()
        );
    }

    // --- POLIMORFISMO: esta versión sustituye la infoCasilla() de Casilla ---
    @Override
    public String infoCasilla() {

        Grupo g = getGrupo();
        String color = (g != null && g.getColor() != null) ? g.getColor() : "N/A";
        String duenhoStr = (getDuenho() != null) ? getDuenho().getNombre() : "banca";

        // Valores de grupo (pueden ser null)
        float vCasa = g != null && g.getValorCasa() != null ? g.getValorCasa() : 0f;
        float vHotel = g != null && g.getValorHotel() != null ? g.getValorHotel() : 0f;
        float vPiscina = g != null && g.getValorPiscina() != null ? g.getValorPiscina() : 0f;
        float vPista = g != null && g.getValorPista() != null ? g.getValorPista() : 0f;

        float aCasa = g != null ? g.getAlquilerCasa() : 0f;
        float aHotel = g != null ? g.getAlquilerHotel() : 0f;
        float aPiscina = g != null ? g.getAlquilerPiscina() : 0f;
        float aPista = g != null ? g.getAlquilerPista() : 0f;

        // Formato informativo
        return String.format(
                "{%n" +
                        " tipo: Solar,%n" +
                        " grupo: %s,%n" +
                        " propietario: %s,%n" +
                        " valor: %.0f,%n" +
                        " alquiler : %.0f,%n" +
                        "valor casa: %.0f,%n" +
                        "valor hotel: %.0f,%n " +
                        "valor piscina: %.0f,%n " +
                        "valor pista: %.0f,%n" +
                        " alquiler casa: %.0f,%n " +
                        "alquiler hotel: %.0f,%n " +
                        "alquiler piscina: %.0f,%n " +
                        "alquiler pista dporte: %.0f%n" +
                        "}",
                color, duenhoStr,
                getValor(), alquilerBase,
                vCasa, vHotel, vPiscina, vPista,
                aCasa, aHotel, aPiscina, aPista
        );
    }

    @Override
    public String casEnVenta() {
        if (getDuenho() != null && !"Banca".equalsIgnoreCase(getDuenho().getNombre())) {
            return ""; // no está en venta
        }

        return String.format(
                "{\n    tipo: solar,\n  grupo: %s,\n    valor: %.0f\n}",
                getGrupo(), getValor()
        );
    }

    // --- Cálculo de alquiler ---
    public float calcularAlquiler() {
        float total = alquilerBase;
        Grupo g = getGrupo();

        if (g == null) return total;

        if (casas > 0) total += casas * g.getAlquilerCasa();
        if (hotel) total += g.getAlquilerHotel();
        if (piscina) total += g.getAlquilerPiscina();
        if (pistaDeporte) total += g.getAlquilerPista();

        // Doble si posee todo el grupo y sin edificios
        if (g.esDuenhoGrupo(getDuenho()) && casas == 0 && !hotel && !piscina && !pistaDeporte)
            total *= 2;

        return total;
    }

    // --- Métodos de construcción ---
    public boolean construirCasa() {
        Grupo g = getGrupo();
        if (g != null && casas < 4 && !hotel && g.esDuenhoGrupo(getDuenho())) {
            casas++;
            return true;
        }
        return false;
    }

    public boolean construirHotel() {
        if (casas == 4 && !hotel) {
            casas = 0;
            hotel = true;
            return true;
        }
        return false;
    }

    public boolean construirPiscina() {
        if (hotel && !piscina) {
            piscina = true;
            return true;
        }
        return false;
    }

    public boolean construirPista() {
        if (hotel && piscina && !pistaDeporte) {
            pistaDeporte = true;
            return true;
        }
        return false;
    }
}


