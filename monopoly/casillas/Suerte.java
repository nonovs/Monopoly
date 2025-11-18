package monopoly.casillas;

import monopoly.Tablero;
import monopoly.Valor;
import partida.Avatar;
import partida.Jugador;

import java.util.List;


public class Suerte extends Casilla {

    // Índice global para las cartas de Suerte (1..7 en bucle)
    private static int indiceCarta = 0;
    private static final int NUM_CARTAS = 7;

    // Constructor
    public Suerte(String nombre, int posicion, Jugador duenho) {
        super(nombre, "Suerte", posicion, duenho);
    }


    private static int siguienteCarta() {
        indiceCarta = (indiceCarta % NUM_CARTAS) + 1; // 0->1, 1->2, ..., 7->1
        return indiceCarta;
    }

    private void moverPorCarta(Tablero tablero, Jugador jugador, Jugador banca, int nuevaPos, boolean considerarSalida, int tirada, boolean evaluarDestino) {

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
            System.out.printf("%s pasa por Salida y cobra %.0f€.%n",
                    jugador.getNombre(), Valor.SUMA_VUELTA);
        }

        System.out.printf("%s se mueve a %s (pos %d) debido a la carta de Suerte.%n",
                jugador.getNombre(),
                destino != null ? destino.getNombre() : "desconocida",
                posFin);

        if (evaluarDestino && destino != null && !destino.esIrACarcel()) {
            boolean ok = destino.evaluarCasilla(jugador, banca, tirada);
            if (!ok) {
                System.out.println("No has podido pagar tras la acción de la carta de Suerte.");
            }
        }
    }

    public boolean aplicarCarta(Tablero tablero, Jugador actual, Jugador banca, List<Jugador> jugadores, int tirada) {

        int carta = siguienteCarta();
        System.out.printf("%s roba carta de Suerte nº %d.%n", actual.getNombre(), carta);

        int posicion = actual.getPosicion();

        switch (carta) {
            case 1:
                // 1. Viaje placer -> ir a Solar19 (pos 32). Si pasas por Salida, cobras 2M.
                System.out.println("Carta Suerte 1: Decides hacer un viaje de placer. Avanza hasta Solar19.");
                moverPorCarta(tablero, actual, banca, 32, true, tirada, true);
                return true;

            case 2:
                // 2. Acreedores -> Ir a la cárcel sin pasar por Salida ni cobrar
                System.out.println("Carta Suerte 2: Los acreedores te persiguen. Vas directamente a la Cárcel.");
                Casilla carcel = tablero.getCasilla(10);
                if (carcel != null) {
                    actual.enviarACarcel(carcel);
                    System.out.printf("%s ha sido enviado a la cárcel.%n", actual.getNombre());
                } else {
                    System.out.println("No se encontró la casilla Cárcel en el tablero.");
                }
                return true;

            case 3:
                // 3. Ganas la lotería: +1.000.000€
                System.out.println("Carta Suerte 3: ¡Has ganado el bote de la lotería! Recibes 1.000.000€.");
                actual.sumarFortuna(1_000_000f);
                //añadido para estadisticas
                actual.acumularPremiosInversionesOBote(1_000_000f);
                return true;

            case 4:
                // 4. Presidente: paga 250.000€ a cada jugador
                System.out.println("Carta Suerte 4: Has sido elegido presidente. Pagas 250.000€ a cada jugador.");
                int numOtros = 0;
                for (Jugador j : jugadores) {
                    if (j != null && j != actual) numOtros++;
                }
                float total = 250_000f * numOtros;

                if (total > actual.getFortuna()) {
                    System.out.printf("%s no puede pagar los %.0f€ requeridos. Debe hipotecar o declararse en bancarrota.%n",
                            actual.getNombre(), total);
                    return false;
                }

                for (Jugador j : jugadores) {
                    if (j != null && j != actual) {
                        actual.pagar(250_000f);
                        j.recibir(250_000f);
                        //añadido para estadisticas
                        actual.acumularPagoTasasEImpuestos(250_000f); 
                        j.acumularPremiosInversionesOBote(250_000f);  
                    }
                }
                return true;

            case 5:
                // 5. Hora punta: retrocede 3 casillas (sin cobrar aunque pases por salida)
                System.out.println("Carta Suerte 5: ¡Hora punta de tráfico! Retrocedes tres casillas.");
                int nuevaPos = (posicion - 3 + 40) % 40;
                moverPorCarta(tablero, actual, banca, nuevaPos, false, tirada, true);
                return true;

            case 6:
                // 6. Multa móvil: paga 150.000€
                System.out.println("Carta Suerte 6: Te multan por usar el móvil mientras conduces. Pagas 150.000€.");
                if (actual.getFortuna() >= 150_000f) {
                    actual.pagar(150_000f);
                    banca.recibir(150_000f);
                    //añadido para estadisticas
                    actual.acumularPagoTasasEImpuestos(150_000f);
                    return true;
                } else {
                    System.out.printf("%s no tiene dinero suficiente para pagar la multa de 150.000€.%n",
                            actual.getNombre());
                    return false;
                }

            case 7:
                // 7. Ir al transporte más cercano. Si no tiene dueño, puedes comprarla.
                //    Si tiene dueño, pagar el doble del alquiler.
                System.out.println("Carta Suerte 7: Avanza hasta el transporte más cercano.");

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

                // Nos movemos SIN considerar Salida (la carta no dice que cobres por  pasar))
                moverPorCarta(tablero, actual, banca, mejorPos, false, tirada, false);

                Casilla dest = tablero.getCasilla(mejorPos);
                if (dest instanceof Transporte) {
                    Transporte tr = (Transporte) dest;
                    Jugador du = tr.getDuenho();

                    if (du == null || du == banca) {
                        System.out.printf("El transporte %s no tiene dueño. Puedes comprarlo con el comando 'comprar %s'.%n",
                                tr.getNombre(), tr.getNombre());
                        return true;
                    }

                    if (du == actual) {
                        System.out.println("El transporte pertenece al propio jugador. No se paga alquiler.");
                        return true;
                    }

                    float alquilerDoble = tr.getAlquiler() * 2;
                    if (actual.getFortuna() >= alquilerDoble) {
                        actual.pagar(alquilerDoble);
                        du.recibir(alquilerDoble);

                        //añadido para estadisticas
                        actual.acumularPagoDeAlquileres(alquilerDoble);
                        du.acumularCobroDeAlquileres(alquilerDoble);
                        System.out.printf("%s paga %.0f€ a %s (doble alquiler) por el transporte %s.%n",
                                actual.getNombre(), alquilerDoble, du.getNombre(), tr.getNombre());
                        return true;
                    } else {
                        System.out.printf("%s no puede pagar el doble alquiler (%.0f€) del transporte %s.%n",
                                actual.getNombre(), alquilerDoble, tr.getNombre());
                        return false;
                    }
                } else {
                    System.out.println("Error: la casilla destino no es un transporte.");
                    return true;
                }

            default:
                return true;
        }
    }


    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        System.out.printf("%s ha caído en una casilla de Suerte.%n", actual.getNombre());
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
        return ""; //No esta en venta
    }
}
