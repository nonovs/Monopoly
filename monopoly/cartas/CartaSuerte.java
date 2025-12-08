package monopoly.cartas;

import monopoly.Tablero;
import monopoly.Valor;
import monopoly.casillas.Casilla;
import monopoly.casillas.Transporte;
import partida.Avatar;
import partida.Jugador;

import java.util.List;

import static monopoly.Juego.consola;

/**
 * Representa una carta de Suerte.
 * Contiene la logica que antes estaba en la casilla Suerte.aplicarCarta(...)
 */
public class CartaSuerte extends Carta {

    public CartaSuerte(int id) {
      
        super(id, "Carta de Suerte " + id);
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
                // 1. Viaje placer -> ir a Solar19 (pos 32). Si pasas por Salida, cobras 2M.
                consola.imprimir("Carta Suerte 1: Decides hacer un viaje de placer. Avanza hasta Solar19.");
                moverPorCarta(tablero, actual, banca, 32, true, tirada, true);
                return true;

            case 2:
                // 2. Acreedores -> Ir a la cárcel sin pasar por Salida ni cobrar
                consola.imprimir("Carta Suerte 2: Los acreedores te persiguen. Vas directamente a la Carcel.");
                Casilla carcel = tablero.getCasilla(10);
                if (carcel != null) {
                    actual.enviarACarcel(carcel);
                    consola.imprimir(String.format("%s ha sido enviado a la carcel.%n", actual.getNombre()));
                } else {
                    System.out.println("No se encontro la casilla Carcel en el tablero.");
                }
                return true;

            case 3:
                // 3. Ganas la loteria: +1.000.000€
                consola.imprimir("Carta Suerte 3: ¡Has ganado el bote de la loteria! Recibes 1.000.000€.");
                actual.sumarFortuna(1_000_000f);
                // estadisticas
                actual.acumularPremiosInversionesOBote(1_000_000f);
                return true;

            case 4:
                // 4. Presidente: paga 250.000€ a cada jugador
                consola.imprimir("Carta Suerte 4: Has sido elegido presidente. Pagas 250.000€ a cada jugador.");
                int numOtros = 0;
                for (Jugador j : jugadores) {
                    if (j != null && j != actual) numOtros++;
                }
                float total = 250_000f * numOtros;

                if (total > actual.getFortuna()) {
                    consola.imprimir(String.format(
                            "%s no puede pagar los %.0f€ requeridos. Debe hipotecar o declararse en bancarrota.%n",
                            actual.getNombre(), total));
                    return false;
                }

                for (Jugador j : jugadores) {
                    if (j != null && j != actual) {
                        actual.pagar(250_000f);
                        j.recibir(250_000f);
                        // estadisticas
                        actual.acumularPagoTasasEImpuestos(250_000f);
                        j.acumularPremiosInversionesOBote(250_000f);
                    }
                }
                return true;

            case 5:
                // 5. Hora punta: retrocede 3 casillas (sin cobrar aunque pases por salida)
                consola.imprimir("Carta Suerte 5: ¡Hora punta de trafico! Retrocedes tres casillas.");
                int nuevaPos = (posicion - 3 + 40) % 40;
                moverPorCarta(tablero, actual, banca, nuevaPos, false, tirada, true);
                return true;

            case 6:
                // 6. Multa movil: paga 150.000€
                consola.imprimir("Carta Suerte 6: Te multan por usar el movil mientras conduces. Pagas 150.000€.");
                if (actual.getFortuna() >= 150_000f) {
                    actual.pagar(150_000f);
                    banca.recibir(150_000f);
                    // estadisticas
                    actual.acumularPagoTasasEImpuestos(150_000f);
                    return true;
                } else {
                    consola.imprimir(String.format(
                            "%s no tiene dinero suficiente para pagar la multa de 150.000€.%n",
                            actual.getNombre()));
                    return false;
                }

            case 7:
                // 7. Ir al transporte mas cercano. Si no tiene dueño, puedes comprarla.
                //    Si tiene dueño, pagar el doble del alquiler.
                consola.imprimir("Carta Suerte 7: Avanza hasta el transporte mas cercano.");

                int[] transportes = {5, 15, 25, 35};
                int mejorPos = transportes[0];
                int mejorDist = 40;

                for (int t : transportes) {
                    int dist = (t - posicion + 40) % 40;
                    if (dist > 0 && dist < mejorDist) {
                        mejorDist = dist;
                        mejorPos = t;
                    }
                }

                // Nos movemos SIN considerar Salida
                moverPorCarta(tablero, actual, banca, mejorPos, false, tirada, false);

                Casilla dest = tablero.getCasilla(mejorPos);
                if (dest instanceof Transporte) {
                    Transporte tr = (Transporte) dest;
                    Jugador du = tr.getDuenho();

                    if (du == null || du == banca) {
                        consola.imprimir(String.format(
                                "El transporte %s no tiene dueño. Puedes comprarlo con el comando 'comprar %s'.%n",
                                tr.getNombre(), tr.getNombre()));
                        return true;
                    }

                    if (du == actual) {
                        consola.imprimir("El transporte pertenece al propio jugador. No se paga alquiler.");
                        return true;
                    }

                    float alquilerDoble = tr.getAlquiler() * 2;
                    if (actual.getFortuna() >= alquilerDoble) {
                        actual.pagar(alquilerDoble);
                        du.recibir(alquilerDoble);

                        // estadisticas
                        actual.acumularPagoDeAlquileres(alquilerDoble);
                        du.acumularCobroDeAlquileres(alquilerDoble);

                        consola.imprimir(String.format(
                                "%s paga %.0f€ a %s (doble alquiler) por el transporte %s.%n",
                                actual.getNombre(), alquilerDoble, du.getNombre(), tr.getNombre()));
                        return true;
                    } else {
                        consola.imprimir(String.format(
                                "%s no puede pagar el doble alquiler (%.0f€) del transporte %s.%n",
                                actual.getNombre(), alquilerDoble, tr.getNombre()));
                        return false;
                    }
                } else {
                    consola.imprimir("Error: la casilla destino no es un transporte.");
                    return true;
                }

            default:
                return true;
        }
    }

    /**
     * Logica comun para mover al jugador debido a una carta de Suerte.
     * Es la misma que tenias en la casilla Suerte.moverPorCarta(...)
     */
    private void moverPorCarta(Tablero tablero,
                               Jugador jugador,
                               Jugador banca,
                               int nuevaPos,
                               boolean considerarSalida,
                               int tirada,
                               boolean evaluarDestino) {

        int posIni = jugador.getPosicion();
        int posFin = ((nuevaPos % 40) + 40) % 40; // normalizar por si acaso

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

        // Cobrar por pasar por Salida si la carta lo indica
        if (considerarSalida && posFin < posIni) {
            jugador.sumarFortuna((float) Valor.SUMA_VUELTA);
            jugador.acumularPasarPorSalida((float) Valor.SUMA_VUELTA);
            consola.imprimir(String.format("%s pasa por Salida y cobra %.0f€.%n",
                    jugador.getNombre(), Valor.SUMA_VUELTA));
        }

        consola.imprimir(String.format("%s se mueve a %s (pos %d) debido a la carta de Suerte.%n",
                jugador.getNombre(),
                destino != null ? destino.getNombre() : "desconocida",
                posFin));

        if (evaluarDestino && destino != null && !destino.esIrACarcel()) {
            boolean ok = destino.evaluarCasilla(jugador, banca, tirada);
            if (!ok) {
                consola.imprimir(String.format("No has podido pagar tras la accion de la carta de Suerte."));
            }
        }
    }
}
