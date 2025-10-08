package monopoly.casillas;

import partida.*;
import java.util.ArrayList;

public class Servicios extends Casilla {

    private final float valor;
    private final float factorServicio = 50000; // Valor fijo según el guión

    // Constructor
    public Servicios(String nombre, int posicion, float valor, Jugador duenho) {
        super(nombre, "Servicios", posicion, valor, duenho);
        this.valor = valor;
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (getDuenho() == null || getDuenho() == actual || getDuenho() == banca) return true;

        // En esta entrega, se paga siempre 4 × tirada × factorServicio
        float alquiler = 4 * tirada * factorServicio;

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
        if (getDuenho() == banca && solicitante.getFortuna() >= valor) {
            solicitante.pagar(valor);
            setDuenho(solicitante);
        }
    }

    @Override
    public String infoCasilla() {
        return String.format(
                "{tipo: servicio, nombre: %s, posicion: %d, propietario: %s, valor: %.0f, factor: %.0f}",
                getNombre(), getPosicion(), getDuenho() != null ? getDuenho().getNombre() : "banca", valor, factorServicio
        );
    }

    @Override
    public String casEnVenta() {
        return String.format("{tipo: servicio, nombre: %s, valor: %.0f}", getNombre(), valor);
    }
}
