package partida;
import monopoly.Juego;
import monopoly.casillas.Casilla;
import monopoly.casillas.Solar;
import monopoly.Construccion.Casa;
import monopoly.Construccion.Hotel;
import monopoly.Construccion.Piscina;
import monopoly.Construccion.PistaDeporte;
import monopoly.Construccion.Edificio;
import monopoly.Grupo;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase utilitaria para centralizar la lógica de edificar.
 *
 * Corrección: antes de permitir edificar en un Solar se comprueba que el jugador
 * sea dueño de TODO el grupo (esDuenhoGrupo) cuando la regla del juego lo exige
 * (p. ej. para casas/hoteles). Además se dan mensajes claros si falta la propiedad
 * del grupo.
 */
public class GestorEdificaciones {

    /**
     * Intenta edificar en el solar donde está el avatar del jugador.
     * tipo: "casa", "hotel", "piscina", "pista" o "pista_deporte"
     */
    public static void edificar(Jugador jugador, String tipo) {
        if (jugador == null || jugador.getAvatar() == null || jugador.getAvatar().getLugar() == null) {
            Juego.consola.imprimir("No se ha localizado la casilla actual del jugador.");
            return;
        }

        Casilla lugar = jugador.getAvatar().getLugar();
        if (!(lugar instanceof Solar)) {
            Juego.consola.imprimir("No se puede edificar en esta casilla (no es un solar).");
            return;
        }

        Solar solar = (Solar) lugar;
        String nombreSolar = solar.getNombre();
        String jugadorNombre = jugador.getNombre();
        tipo = tipo == null ? "" : tipo.trim().toLowerCase();

        // Comprueba que el jugador sea dueño del solar y del grupo de solares
        if (solar.getDuenho() == null || solar.getDuenho() != jugador) {
            Juego.consola.imprimir(String.format("%s no es el propietario de %s.%n", jugadorNombre, nombreSolar));
            return;
        }

        // Recuperar el grupo y comprobar la propiedad total del grupo cuando aplique
        Grupo grupo = solar.getGrupo();

        switch (tipo) {
            case "casa": {
                // Regla para construir casas en un grupo se requiere ser dueño de todo el grupo
                if (grupo != null && !grupo.esDuenhoGrupo(jugador)) {
                    Juego.consola.imprimir(String.format("No se puede edificar una casa en %s: %s no es dueño de todas las casillas del grupo %s.%n",nombreSolar, jugadorNombre, grupo.getColor()));
                    return;
                }
                float precio = solar.getPrecioCasa();
                if (jugador.getFortuna() < precio) {
                    Juego.consola.imprimir(String.format("La fortuna de %s no es suficiente para edificar una casa en la casilla %s.%n", jugadorNombre, nombreSolar));
                    return;
                }
                Casa casa = new Casa(solar, precio);
                if (!casa.esEdificable()) {
                    Juego.consola.imprimir("No se puede edificar ningún edificio más en esta casilla ni en el grupo al que la casilla pertenece.");
                    return;
                }
                boolean okCasa = solar.construirCasa();
                if (!okCasa) {
                    Juego.consola.imprimir("No se ha podido edificar la casa (condiciones no cumplidas).");
                    return;
                }
                jugador.pagar(precio);
                jugador.acumularDineroInvertido(precio);
                solar.anhadirEdificacion((Edificio) casa);
                Juego.consola.imprimir(String.format("Se ha edificado una casa (%s) en %s. La fortuna de %s se reduce en %.0f€.%n",
                        casa.getId(), nombreSolar, jugadorNombre, precio));
                return;
            }

            case "hotel": {
                //Compruebo que es duanho del grupo y que tiene 4 casas
                if (grupo != null && !grupo.esDuenhoGrupo(jugador)) {
                    Juego.consola.imprimir(String.format("No se puede edificar un hotel en %s: %s no es dueño de todas las casillas del grupo %s.%n",
                            nombreSolar, jugadorNombre, grupo.getColor()));
                    return;
                }
                float precio = solar.getPrecioHotel();
                if (jugador.getFortuna() < precio) {
                    Juego.consola.imprimir(String.format("La fortuna de %s no es suficiente para edificar un hotel en la casilla %s.%n", jugadorNombre, nombreSolar));
                    return;
                }
                Hotel hotel = new Hotel(solar, precio);
                if (!hotel.esEdificable()) {
                    Juego.consola.imprimir("No se puede edificar un hotel: se requieren 4 casas y ser dueño del grupo o ya existe un hotel.");
                    return;
                }
                List<Edificio> casasAEliminar = new ArrayList<>();
                for (Edificio e : solar.getEdificaciones()) {
                    if (e instanceof Casa) {
                        casasAEliminar.add(e);
                    }
                }
                int eliminadas = 0;
                for (Edificio casa : casasAEliminar) {
                    if (eliminadas < 4) {
                        solar.eliminarEdificacion(casa);
                        eliminadas++;
                    }
                }
                boolean ok = solar.construirHotel();
                if (!ok) {
                    Juego.consola.imprimir("No se pudo edificar el hotel.");
                    return;
                }
                jugador.pagar(precio);
                jugador.acumularDineroInvertido(precio);
                solar.anhadirEdificacion((Edificio) hotel);
                Juego.consola.imprimir(String.format("Se ha edificado un hotel (%s) en %s. La fortuna de %s se reduce en %.0f€.%n",
                        hotel.getId(), nombreSolar, jugadorNombre, precio));
                Juego.consola.imprimir(String.format("Las 4 casas en %s han sido reemplazadas por el hotel.%n", nombreSolar));
                return;
            }

            case "piscina": {
                // Piscina  requerir hotel en la misma casilla; la propiedad del grupo ya no tengo que comprobarla la compruebo en hotel

                float precio = solar.getPrecioPiscina();
                if (jugador.getFortuna() < precio) {
                    Juego.consola.imprimir(String.format("La fortuna de %s no es suficiente para edificar una piscina en la casilla %s.%n", jugadorNombre, nombreSolar));
                    return;
                }
                Piscina piscina = new Piscina(solar, precio);
                if (!piscina.esEdificable()) {
                    Juego.consola.imprimir("No se puede edificar una piscina, ya que no se dispone de un hotel.");
                    return;
                }
                boolean okPisc = solar.construirPiscina();
                if (!okPisc) {
                    Juego.consola.imprimir("No se pudo edificar la piscina.");
                    return;
                }
                jugador.pagar(precio);
                jugador.acumularDineroInvertido(precio);
                solar.anhadirEdificacion((Edificio) piscina);
                Juego.consola.imprimir(String.format("Se ha edificado una piscina (%s) en %s. La fortuna de %s se reduce en %.0f€.%n",
                        piscina.getId(), nombreSolar, jugadorNombre, precio));
                return;
            }

            case "pista":
            case "pista_deporte": {

                float precio = solar.getPrecioPista();
                if (jugador.getFortuna() < precio) {
                    Juego.consola.imprimir(String.format("La fortuna de %s no es suficiente para edificar una pista de deporte en la casilla %s.%n", jugadorNombre, nombreSolar));
                    return;
                }
                PistaDeporte pista = new PistaDeporte(solar, precio);
                if (!pista.esEdificable()) {
                    Juego.consola.imprimir("No se puede edificar una pista, ya que falta hotel o piscina.");
                    return;
                }
                boolean okPista = solar.construirPista();
                if (!okPista) {
                    Juego.consola.imprimir("No se pudo edificar la pista.");
                    return;
                }
                jugador.pagar(precio);
                jugador.acumularDineroInvertido(precio);
                solar.anhadirEdificacion((Edificio) pista);
                Juego.consola.imprimir(String.format("Se ha edificado una pista de deporte (%s) en %s. La fortuna de %s se reduce en %.0f€.%n",
                        pista.getId(), nombreSolar, jugadorNombre, precio));
                return;
            }

            default:
                Juego.consola.imprimir("Tipo de edificación no reconocido. Usa: casa, hotel, piscina o pista_deporte.");
        }
    }


    public static boolean eliminarEdificio(Edificio edificio) {
        if (edificio == null) {
            Juego.consola.imprimir("No se puede eliminar un edificio nulo.");
            return false;
        }

        Solar solar = edificio.getSolar();
        if (solar == null) {
            Juego.consola.imprimir("El edificio no está asociado a ningún solar.");
            return false;
        }

        Jugador duenho = solar.getDuenho();
        if (duenho == null) {
            Juego.consola.imprimir("El solar no tiene dueño asignado.");
            return false;
        }

        String tipoEdificio = "";

        boolean resultado = false;

        // Identificar el tipo de edificio y ejecutar la demolición correspondiente
        if (edificio instanceof Casa) {
            tipoEdificio = "casa";

            resultado = solar.romperCasa((Casa) edificio);

        } else if (edificio instanceof Hotel) {
            tipoEdificio = "hotel";
            resultado = solar.romperHotel((Hotel) edificio);

        } else if (edificio instanceof Piscina) {
            tipoEdificio = "piscina";
            resultado = solar.romperPiscina((Piscina) edificio);

        } else if (edificio instanceof PistaDeporte) {
            tipoEdificio = "pista de deporte";
            resultado = solar.romperPista((PistaDeporte) edificio);

        } else {
            Juego.consola.imprimir("Tipo de edificio no reconocido.");
            return false;
        }

        if (!resultado) {
            Juego.consola.imprimir(String.format("No se pudo demoler el %s (%s) en %s.%n",
                    tipoEdificio, edificio.getId(), solar.getNombre()));
            return false;
        }


        solar.eliminarEdificacion(edificio);

        return true;
    }
}