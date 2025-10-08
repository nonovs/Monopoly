package monopoly.casillas;

import partida.*;
import java.util.ArrayList;

public class Transporte extends Casilla {

    private float alquiler;
    private float valor;

    // Constructor para transporte
    public Transporte(String nombre, int posicion, float valor, float alquiler, Jugador duenho) {
        super(nombre, "Transporte", posicion, valor, duenho);
        this.valor = valor;
        this.alquiler = alquiler;
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (getDuenho() == null || getDuenho() == actual || getDuenho() == banca) return true;

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
                "{tipo: transporte, nombre: %s, posicion: %d, propietario: %s, valor: %.0f, alquiler: %.0f}",
                getNombre(), getPosicion(), getDuenho() != null ? getDuenho().getNombre() : "banca", valor, alquiler
        );
    }

    @Override
    public String casEnVenta() {
        return String.format("{tipo: transporte, nombre: %s, valor: %.0f}", getNombre(), valor);
    }
}
