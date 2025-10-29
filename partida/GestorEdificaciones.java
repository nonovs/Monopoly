package partida;

import monopoly.casillas.Casilla;
import monopoly.casillas.Solar;
import monopoly.Construccion.Casa;
import monopoly.Construccion.Hotel;
import monopoly.Construccion.Piscina;
import monopoly.Construccion.PistaDeporte;
import monopoly.Construccion.Edificio;

/**
 * Clase utilitaria para centralizar a lóxica de edificar.
 */
public class GestorEdificaciones {

    /**
     * Tenta edificar no solar onde está o avatar do xogador.
     * tipo: "casa", "hotel", "piscina", "pista" ou "pista_deporte"
     */
    public static void edificar(Jugador jugador, String tipo) {
        if (jugador == null || jugador.getAvatar() == null || jugador.getAvatar().getLugar() == null) {
            System.out.println("No se ha localizado la casilla actual del jugador.");
            return;
        }

        Casilla lugar = jugador.getAvatar().getLugar();
        if (!(lugar instanceof Solar)) {
            System.out.println("No se puede edificar en esta casilla (no es un solar).");
            return;
        }

        Solar solar = (Solar) lugar;
        String nombreSolar = solar.getNombre();
        String jugadorNombre = jugador.getNombre();
        tipo = tipo == null ? "" : tipo.trim().toLowerCase();

        switch (tipo) {
            case "casa": {
                float precio = solar.getPrecioCasa();
                if (jugador.getFortuna() < precio) {
                    System.out.printf("La fortuna de %s no es suficiente para edificar una casa en la casilla %s.%n", jugadorNombre, nombreSolar);
                    return;
                }
                Casa casa = new Casa(solar, precio);
                if (!casa.esEdificable()) {
                    System.out.println("No se puede edificar ningún edificio más en esta casilla ni en el grupo al que la casilla pertenece.");
                    return;
                }
                boolean ok = solar.construirCasa();
                if (!ok) {
                    System.out.println("No se ha podido edificar la casa (condiciones no cumplidas).");
                    return;
                }
                jugador.pagar(precio);
                solar.anhadirEdificacion((Edificio) casa);
                System.out.printf("Se ha edificado una casa (%s) en %s. La fortuna de %s se reduce en %.0f€, la fortuna restante es de %.0f€.%n", casa.getId(), nombreSolar, jugadorNombre, precio,jugador.getFortuna());
                return;
            }
            case "hotel": {
                float precio = solar.getPrecioHotel();
                if (jugador.getFortuna() < precio) {
                    System.out.printf("La fortuna de %s no es suficiente para edificar un hotel en la casilla %s.%n", jugadorNombre, nombreSolar);
                    return;
                }
                Hotel hotel = new Hotel(solar, precio);
                if (!hotel.esEdificable()) {
                    System.out.println("No se puede edificar un hotel: se requieren 4 casas y ser dueño del grupo o ya existe un hotel.");
                    return;
                }
                boolean ok = solar.construirHotel();
                if (!ok) {
                    System.out.println("No se pudo edificar el hotel.");
                    return;
                }
                jugador.pagar(precio);
                solar.anhadirEdificacion((Edificio) hotel);
                System.out.printf("Se ha edificado un hotel (%s) en %s. La fortuna de %s se reduce en %.0f€.%n", hotel.getId(), nombreSolar, jugadorNombre, precio);
                return;
            }
            case "piscina": {
                float precio = solar.getPrecioPiscina();
                if (jugador.getFortuna() < precio) {
                    System.out.printf("La fortuna de %s no es suficiente para edificar una piscina en la casilla %s.%n", jugadorNombre, nombreSolar);
                    return;
                }
                Piscina piscina = new Piscina(solar, precio);
                if (!piscina.esEdificable()) {
                    System.out.println("No se puede edificar una piscina, ya que no se dispone de un hotel.");
                    return;
                }
                boolean ok = solar.construirPiscina();
                if (!ok) {
                    System.out.println("No se pudo edificar la piscina.");
                    return;
                }
                jugador.pagar(precio);
                solar.anhadirEdificacion((Edificio) piscina);
                System.out.printf("Se ha edificado una piscina (%s) en %s. La fortuna de %s se reduce en %.0f€.%n", piscina.getId(), nombreSolar, jugadorNombre, precio);
                return;
            }
            case "pista":
            case "pista_deporte": {
                float precio = solar.getPrecioPista();
                if (jugador.getFortuna() < precio) {
                    System.out.printf("La fortuna de %s no es suficiente para edificar una pista de deporte en la casilla %s.%n", jugadorNombre, nombreSolar);
                    return;
                }
                PistaDeporte pista = new PistaDeporte(solar, precio);
                if (!pista.esEdificable()) {
                    System.out.println("No se puede edificar una pista, ya que falta hotel o piscina.");
                    return;
                }
                boolean ok = solar.construirPista();
                if (!ok) {
                    System.out.println("No se pudo edificar la pista.");
                    return;
                }
                jugador.pagar(precio);
                solar.anhadirEdificacion((Edificio) pista);
                System.out.printf("Se ha edificado una pista de deporte (%s) en %s. La fortuna de %s se reduce en %.0f€.%n", pista.getId(), nombreSolar, jugadorNombre, precio);
                return;
            }
            default:
                System.out.println("Tipo de edificación no reconocido. Usa: casa, hotel, piscina o pista_deporte.");
        }
    }
}