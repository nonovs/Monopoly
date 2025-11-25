package monopoly.casillas;

import monopoly.Tablero;
import monopoly.Valor;
import partida.Avatar;
import partida.Jugador;

import java.util.List;


public class CajaComunidad extends Casilla {

    private static int indiceCarta = 0; // Índice global para las cartas de Caja de Comunidad (1..6 en bucle)
    private static final int NUM_CARTAS = 6; // Número total de cartas de Caja de Comunidad

    // Constructor
    public CajaComunidad(String nombre, int posicion, Jugador duenho) {
        super(nombre, "CajaComunidad", posicion, duenho);
    }

    private static int siguienteCarta() {
        indiceCarta = (indiceCarta % NUM_CARTAS) + 1;//Sirve para calcular el indice de la siguiente carta
        return indiceCarta;
    }
    
    private void moverPorCarta(Tablero tablero, Jugador jugador, Jugador banca, int nuevaPos, boolean considerarSalida, int tirada, boolean evaluarDestino) {

        int posIni = jugador.getPosicion();
        int posFin = ((nuevaPos % 40) + 40) % 40;// Normaliza la posicion para que sea entre 0 y 39

        Casilla origen = tablero.getCasilla(posIni);//tomamos datos de la casilla actual
        Casilla destino = tablero.getCasilla(posFin);//tomamos datos de la casilla destino

        Avatar av = jugador.getAvatar();//tomamos datos del avatar actual
        if (origen != null && av != null) {//Sacamos el avatar de la casilla actual
            origen.eliminarAvatar(av);
        }
        if (destino != null && av != null) {//Pasamos el avatar a la casilla destino
            destino.anhadirAvatar(av);
            av.setLugar(destino);
        }

        jugador.setPosicion(posFin);//Actualizamos la posicion del jugador

        if (considerarSalida && posFin < posIni) {//En caso de que el jugador haya pasado por salida, sumamos
            jugador.sumarFortuna((float) Valor.SUMA_VUELTA);//Le damos dinero
            jugador.acumularPasarPorSalida((float) Valor.SUMA_VUELTA);//Actualizamos estadisticas
            System.out.printf("%s pasa por Salida y cobra %.0f€.%n",
                    jugador.getNombre(), Valor.SUMA_VUELTA);
        }

        System.out.printf("%s se mueve a %s (pos %d) debido a la carta de Caja de Comunidad.%n",
                jugador.getNombre(),
                destino != null ? destino.getNombre() : "desconocida",
                posFin);//Informamos del movimiento

        if (evaluarDestino && destino != null && !destino.esIrACarcel()) {
            boolean ok = destino.evaluarCasilla(jugador, banca, tirada);
            if (!ok) {
                System.out.println("No has podido pagar tras la acción de la carta de Caja de Comunidad.");
            }
        }
    }

    public boolean aplicarCarta(Tablero tablero, Jugador actual, Jugador banca, List<Jugador> jugadores, int tirada) {
    //Esta funcion lo que hace es robar una carta de Caja de Comunidad y realizar la accion correspondiente
        int carta = siguienteCarta();
        System.out.printf("%s roba carta de Caja de Comunidad nº %d.%n", actual.getNombre(), carta);

        int posicion = actual.getPosicion();

        switch (carta) {
            case 1:
                // 1. Paga 500.000€ por balneario
                System.out.println("Carta Comunidad 1: Fin de semana en balneario de 5 estrellas. Pagas 500.000€.");
                if (actual.getFortuna() >= 500_000f) {
                    actual.pagar(500_000f);
                    banca.recibir(500_000f);
                    actual.acumularPagoTasasEImpuestos(500_000f);
                    return true;
                } else {
                    System.out.printf("%s no puede pagar los 500.000€ del balneario.%n", actual.getNombre());
                    return false;
                }

            case 2:
                // 2. A la cárcel sin pasar por salida ni cobrar
                System.out.println("Carta Comunidad 2: Te investigan por fraude de identidad. Vas a la Cárcel.");
                Casilla carcel = tablero.getCasilla(10);
                if (carcel != null) {
                    actual.enviarACarcel(carcel);
                    System.out.printf("%s ha sido enviado a la cárcel.%n", actual.getNombre());
                } else {
                    System.out.println("No se encontró la casilla Cárcel en el tablero.");
                }
                return true;

            case 3:
                // 3. Colócate en Salida y cobra 2.000.000€
                System.out.println("Carta Comunidad 3: Te colocas en la casilla de Salida y cobras 2.000.000€.");
                moverPorCarta(tablero, actual, banca, 0, false, tirada, false);
                actual.sumarFortuna((float) Valor.SUMA_VUELTA); // 2.000.000€
                actual.acumularPasarPorSalida((float) Valor.SUMA_VUELTA); //añadido para estadisticas
                System.out.printf("%s cobra %.0f€ por situarse en Salida.%n", actual.getNombre(), Valor.SUMA_VUELTA);
                return true;

            case 4:
                // 4. Devolución de Hacienda: cobras 500.000€
                System.out.println("Carta Comunidad 4: Devolución de Hacienda. Cobras 500.000€.");
                actual.sumarFortuna(500_000f);
                actual.acumularPremiosInversionesOBote(500_000f); //añadido para estadisticas
                return true;

            case 5:
                // 5. Retrocede hasta Solar1 (pos 1)
                System.out.println("Carta Comunidad 5: Retrocedes hasta Solar1 para comprar antigüedades.");
                moverPorCarta(tablero, actual, banca, 1, false, tirada, true);
                return true;

            case 6:
                // 6. Ve a Solar20 (pos 34). Si pasas por Salida, cobras 2M.
                System.out.println("Carta Comunidad 6: Vas a Solar20 para disfrutar de San Fermín.");
                moverPorCarta(tablero, actual, banca, 34, true, tirada, true);
                return true;

            default:
                return true;
        }

    }


    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        System.out.println(actual.getNombre() + " ha caído en una casilla de Caja de Comunidad.");
        return true;
    }

    @Override
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        // No se puede comprar una casilla de comunidad
    }

    @Override
    public String infoCasilla() {
        return String.format(
                "{tipo: caja comunidad, nombre: %s, posicion: %d}",
                getNombre(), getPosicion()
        );
    }

    @Override
    public String casEnVenta() {
        return ""; // No está en venta
    }
}