package monopoly.casillas;

import monopoly.Grupo;
import monopoly.casillas.Casilla;
import partida.*;
import java.util.ArrayList;

public class Solar extends Casilla {

    private float hipoteca;
    private float alquilerBase;
    private int casas;
    private boolean hotel;
    private boolean piscina;
    private boolean pistaDeporte;
    private Grupo grupo;

    // Constructor
    public Solar(String nombre, int posicion, float valor, float hipoteca, float alquilerBase, Jugador duenho, Grupo grupo) {
        super(nombre, "Solar", posicion, valor, duenho);
        this.hipoteca = hipoteca;
        this.alquilerBase = alquilerBase;
        this.grupo = grupo;
        this.casas = 0;
        this.hotel = false;
        this.piscina = false;
        this.pistaDeporte = false;
        this.setGrupo(grupo); // Para que Casilla también lo tenga
        this.setHipoteca(hipoteca); // Para que Casilla también lo tenga
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (getDuenho() == null || getDuenho() == actual || getDuenho() == banca) return true;
        if (actual.tieneHipoteca(this)) return true;

        float alquiler = calcularAlquiler();
        if (actual.getFortuna() >= alquiler) {
            actual.pagar(alquiler);
            getDuenho().recibir(alquiler);
            return true;
        } else {
            // Aquí podrías invocar lógica de hipoteca o bancarrota
            return false;
        }
    }

    @Override
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        if (getDuenho() == banca && solicitante.getFortuna() >= getValor()) {
            solicitante.pagar(getValor());
            setDuenho(solicitante);
        }
    }

    @Override
    public String infoCasilla() {
        return String.format(
                "{tipo: solar, grupo: %s, propietario: %s, valor: %.0f, hipoteca: %.0f, alquiler base: %.0f, casas: %d, hotel: %b, piscina: %b, pista: %b}",
                grupo.getColor(), getDuenho() != null ? getDuenho().getNombre() : "banca", getValor(), hipoteca, alquilerBase,
                casas, hotel, piscina, pistaDeporte
        );
    }

    @Override
    public String casEnVenta() {
        return String.format("{tipo: solar, grupo: %s, valor: %.0f}", grupo.getColor(), getValor());
    }

    // Lógica de alquiler
    public float calcularAlquiler() {
        float total = alquilerBase;

        if (casas > 0) total += casas * grupo.getAlquilerCasa();
        if (hotel) total += grupo.getAlquilerHotel();
        if (piscina) total += grupo.getAlquilerPiscina();
        if (pistaDeporte) total += grupo.getAlquilerPista();

        // Doble alquiler si el grupo está completo y no hay edificios
        if (grupo.esDuenhoGrupo(getDuenho()) && casas == 0 && !hotel && !piscina && !pistaDeporte) {
            total *= 2;
        }

        return total;
    }

    // Métodos para construir edificios
    public boolean construirCasa() {
        if (casas < 4 && !hotel && grupo.esDuenhoGrupo(getDuenho())) {
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
