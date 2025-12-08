package monopoly.cartas;

import monopoly.Tablero;
import monopoly.Valor;
import monopoly.casillas.Casilla;
import partida.Avatar;
import partida.Jugador;

import java.util.List;

import static monopoly.Juego.consola;

/**
 * Representa una carta de Caja de Comunidad.
 * Contiene la logica que antes estaba en CajaComunidad.aplicarCarta(...)
 */
public class CartaCajaComunidad extends Carta {

    public CartaCajaComunidad(int id) {
        super(id, "Carta de Caja de Comunidad " + id);
    }

    @Override
    public boolean accion(Tablero tablero,
                          Jugador actual,
                          Jugador banca,
                          List<Jugador> jugadores,
                          int tirada) {

        int posicion = actual.getPosicion();

        switch (id) {

            case 1:
                // 1. Paga 500.000€ por balneario
                consola.imprimir("Carta Comunidad 1: Fin de semana en balneario de 5 estrellas. Pagas 500.000€.");
                if (actual.getFortuna() >= 500_000f) {
                    actual.pagar(500_000f);
                    banca.recibir(500_000f);
                    actual.acumularPagoTasasEImpuestos(500_000f);
                    return true;
                } else {
                    consola.imprimir(String.format("%s no puede pagar los 500.000€ del balneario.", actual.getNombre()));
                    return false;
                }

            case 2:
                // 2. A la cárcel sin pasar por salida ni cobrar
                consola.imprimir("Carta Comunidad 2: Te investigan por fraude de identidad. Vas a la Carcel.");
                Casilla carcel = tablero.getCasilla(10);
                if (carcel != null) {
                    actual.enviarACarcel(carcel);
                    consola.imprimir(String.format("%s ha sido enviado a la carcel.%n", actual.getNombre()));
                } else {
                    System.out.println("No se encontro la casilla Carcel en el tablero.");
                }
                return true;

            case 3:
                // 3. Colócate en Salida y cobra 2.000.000€
                consola.imprimir("Carta Comunidad 3: Te colocas en la casilla de Salida y cobras 2.000.000€.");
                moverPorCarta(tablero, actual, banca, 0, false, tirada, false);
                actual.sumarFortuna((float) Valor.SUMA_VUELTA); // 2.000.000€
                actual.acumularPasarPorSalida((float) Valor.SUMA_VUELTA);
                consola.imprimir(String.format("%s cobra %.0f€ por situarse en Salida.%n",
                        actual.getNombre(), Valor.SUMA_VUELTA));
                return true;

            case 4:
                // 4. Devolución de Hacienda: cobras 500.000€
                consola.imprimir(String.format("Carta Comunidad 4: Devolucion de Hacienda. Cobras 500.000€."));
                actual.sumarFortuna(500_000f);
                actual.acumularPremiosInversionesOBote(500_000f);
                return true;

            case 5:
                // 5. Retrocede hasta Solar1 (pos 1)
                consola.imprimir("Carta Comunidad 5: Retrocedes hasta Solar1 para comprar antiguedades.");
                moverPorCarta(tablero, actual, banca, 1, false, tirada, true);
                return true;

            case 6:
                // 6. Ve a Solar20 (pos 34). Si pasas por Salida, cobras 2M.
                consola.imprimir("Carta Comunidad 6: Vas a Solar20 para disfrutar de San Fermin.");
                moverPorCarta(tablero, actual, banca, 34, true, tirada, true);
                return true;

            default:
                return true;
        }
    }

    /**
     * Logica comun para mover al jugador debido a una carta de Caja de Comunidad.
     * Es la misma que tenias en CajaComunidad.moverPorCarta(...)
     */
    private void moverPorCarta(Tablero tablero,
                               Jugador jugador,
                               Jugador banca,
                               int nuevaPos,
                               boolean considerarSalida,
                               int tirada,
                               boolean evaluarDestino) {

        int posIni = jugador.getPosicion();
        int posFin = ((nuevaPos % 40) + 40) % 40; // Normaliza la posicion para que sea entre 0 y 39

        Casilla origen = tablero.getCasilla(posIni);
        Casilla destino = tablero.getCasilla(posFin);

        Avatar av = jugador.getAvatar();
        if (origen != null && av != null) {
            origen.eliminarAvatar(av);
        }
        if (destino != null && av != null) {
            destino.anhadirAvatar(av);
            av.setLugar(destino);
        }

        jugador.setPosicion(posFin);

        if (considerarSalida && posFin < posIni) {
            jugador.sumarFortuna((float) Valor.SUMA_VUELTA);
            jugador.acumularPasarPorSalida((float) Valor.SUMA_VUELTA);
            consola.imprimir(String.format("%s pasa por Salida y cobra %.0f€.%n",
                    jugador.getNombre(), Valor.SUMA_VUELTA));
        }

        consola.imprimir(String.format("%s se mueve a %s (pos %d) debido a la carta de Caja de Comunidad.%n",
                jugador.getNombre(),
                destino != null ? destino.getNombre() : "desconocida",
                posFin);

        if (evaluarDestino && destino != null && !destino.esIrACarcel()) {
            boolean ok = destino.evaluarCasilla(jugador, banca, tirada);
            if (!ok) {
                consola.imprimir("No has podido pagar tras la accion de la carta de Caja de Comunidad.");
            }
        }
    }
}
