package monopoly;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import monopoly.casillas.*;      // Importa todas las casillas (Solar, Suerte, etc.)
import monopoly.Construccion.*;  // Importa todos los edificios (Edificio, Casa, etc.)
import partida.*;                // Importa Jugador, Avatar, Dado, etc.

import static partida.GestorEdificaciones.eliminarEdificio;


public class Juego implements Comando {
//carallo
    public static ConsolaNormal consola = new ConsolaNormal();

    private ArrayList<Jugador> jugadores;
    private ArrayList<Avatar> avatares;
    private Tablero tablero;
    private Dado dado;
    private Jugador banca;

    private int turno = 0;
    private int lanzamientos = 0;
    private boolean tirado = false;
    private boolean solvente = true;
    private boolean puedeRepetirLanzamiento = false;

    public Juego() {
        this.banca = new Jugador();
        this.tablero = new Tablero(banca);
        this.dado = new Dado();
        this.jugadores = new ArrayList<>();
        this.avatares = new ArrayList<>();
    }

    public Jugador getJugadorActual() {
        if (jugadores.isEmpty()) return null;
        return jugadores.get(turno);
    }

    public ArrayList<Jugador> getJugadores() { return jugadores; }
    public Tablero getTablero() { return tablero; }

    // ================================================================
    //                 MÉTODOS DE LA INTERFAZ COMANDO (PÚBLICOS)
    // ================================================================

    @Override
    public void crearJugador(String nombre, String tipoAvatar) {
        for (Jugador j : jugadores) {
            if (j.getNombre().equalsIgnoreCase(nombre)) {
                consola.imprimir("Ya existe un jugador con ese nombre.");
                return;
            }
        }
        if (jugadores.size() >= 6) {
            consola.imprimir("No se pueden crear más jugadores.");
            return;
        }
        if (!tipoAvatar.equalsIgnoreCase("Coche") && !tipoAvatar.equalsIgnoreCase("Esfinge") &&
                !tipoAvatar.equalsIgnoreCase("Sombrero") && !tipoAvatar.equalsIgnoreCase("Pelota")) {
            consola.imprimir("Tipo de avatar no válido.");
            return;
        }

        Casilla salida = tablero.encontrar_casilla("Salida");
        if (salida == null) return;

        Jugador nuevo = new Jugador(nombre, tipoAvatar, salida, avatares);
        Avatar avatar = nuevo.getAvatar();

        if (avatar == null) {
            avatar = new Avatar(tipoAvatar, nuevo, salida, avatares);
            nuevo.setAvatar(avatar);
        }

        jugadores.add(nuevo);

        boolean existe = false;
        for(Avatar a : avatares) if(a.getId().equals(avatar.getId())) existe = true;
        if(!existe) avatares.add(avatar);

        if (!salida.getAvatares().contains(avatar)) {
            salida.anhadirAvatar(avatar);
            avatar.setLugar(salida);
        }
        nuevo.setPosicion(salida.getPosicion());

        consola.imprimir(String.format("Jugador creado: {nombre: %s, avatar: %s}", nombre, avatar.getId()));
        mostrarTablero();
    }

    @Override
    public void mostrarJugadorEnTurno() {
        if (jugadores.isEmpty()) {
            consola.imprimir("No hay jugadores.");
            return;
        }
        Jugador actual = jugadores.get(turno);
        String avatarId = (actual.getAvatar() != null) ? actual.getAvatar().getId() : "-";
        consola.imprimir(String.format("Turno de: %s (avatar %s) - posicion %d", actual.getNombre(), avatarId, actual.getPosicion()));

        if (actual.isEnCarcel()) {
            consola.imprimir("ESTÁ EN LA CÁRCEL. Opciones: 'salir carcel' (pagando) o sacar dobles.");
        }
    }

    @Override
    public void mostrarTablero() {
        tablero.mostrarTablero();
    }

    @Override
    public void lanzarDados() {
        if (!validarTurno()) return;
        if (lanzamientos == 0) dado.iniciarTurno();
        int suma = dado.tirar();
        procesarTirada(suma);
    }

    @Override
    public void lanzarDadosForzada(int a, int b) {
        if (!validarTurno()) return;
        if (a < 1 || a > 6 || b < 1 || b > 6) {
            consola.imprimir("Dados deben ser 1-6.");
            return;
        }
        if (lanzamientos == 0) dado.iniciarTurno();
        int suma = dado.tirarForzado(a, b);
        procesarTirada(suma);
    }

    @Override
    public void salirCarcel() {
        Jugador j = getJugadorActual();
        if (j == null || !j.isEnCarcel()) {
            consola.imprimir("No estás en la cárcel.");
            return;
        }
        if (j.getFortuna() >= 500000) {
            j.pagar(500000);
            j.acumularPagoTasasEImpuestos(500000);
            j.salirDeCarcel();
            consola.imprimir("Fianza pagada. Estás libre.");
        } else {
            consola.imprimir("No tienes dinero suficiente.");
        }
    }

    @Override
    public void comprar(String nombre) {
        if (jugadores.isEmpty()) return;
        Casilla c = tablero.encontrar_casilla(nombre);
        if (c != null) c.comprarCasilla(getJugadorActual(), banca);
        else consola.imprimir("Casilla no existe.");
    }

    @Override
    public void edificar(String tipo) {
        if (jugadores.isEmpty()) return;
        GestorEdificaciones.edificar(getJugadorActual(), tipo);
    }

    @Override
    public void hipotecar(String nombre) {
        Casilla c = tablero.encontrar_casilla(nombre);
        if (c != null) getJugadorActual().hipotecarPropiedad(c);
        else consola.imprimir("Casilla no existe.");
    }

    @Override
    public void deshipotecar(String nombre) {
        Casilla c = tablero.encontrar_casilla(nombre);
        if (c != null) getJugadorActual().deshipotecarPropiedad(c);
        else consola.imprimir("Casilla no existe.");
    }

    @Override
    public void venderEdificio(String tipo, String solarNombre, int cantidad) {
        if (jugadores.isEmpty()) return;
        Jugador actual = getJugadorActual();
        Casilla c = tablero.encontrar_casilla(solarNombre);

        if (!(c instanceof Solar)) {
            consola.imprimir("No es un solar.");
            return;
        }
        Solar s = (Solar) c;
        if (s.getDuenho() != actual) {
            consola.imprimir("No eres el dueño.");
            return;
        }

        String tipoNorm = tipo.toLowerCase();
        if(tipoNorm.startsWith("casa")) tipoNorm = "casa";
        else if(tipoNorm.startsWith("hotel")) tipoNorm = "hotel";
        else if(tipoNorm.startsWith("piscina")) tipoNorm = "piscina";
        else if(tipoNorm.startsWith("pista")) tipoNorm = "pista";
        else { consola.imprimir("Tipo inválido."); return; }

        if(!tipoNorm.equals("casa") && cantidad > 1) cantidad = 1;

        List<Edificio> aVender = new ArrayList<>();
        for(Edificio e : s.getEdificaciones()) {
            String t = e.getTipo().toLowerCase();
            if(t.contains(tipoNorm)) aVender.add(e);
        }

        if(aVender.size() < cantidad) {
            consola.imprimir("No hay suficientes edificios.");
            return;
        }

        float total = 0;
        for(int i=0; i<cantidad; i++) {
            Edificio e = aVender.get(i);
            if(eliminarEdificio(e)) total += e.getPrecio() / 2;
        }
        actual.sumarFortuna(total);
        consola.imprimir(String.format("Vendidos %d %s. Recibes %.0f.", cantidad, tipoNorm, total));
    }

    @Override
    public void acabarTurno() {
        if (!tirado) {
            consola.imprimir("Debes tirar dados primero.");
            return;
        }
        turno = (turno + 1) % jugadores.size();
        tirado = false;
        lanzamientos = 0;
        puedeRepetirLanzamiento = false;
        solvente = true;
        consola.imprimir("Turno de: " + getJugadorActual().getNombre());
    }

    @Override
    public void listarJugadores() {
        if (jugadores.isEmpty()) { consola.imprimir("Sin jugadores."); return; }
        for (Jugador j : jugadores) descJugadorInterno(j);
    }

    @Override
    public void descJugador(String nombre) {
        for (Jugador j : jugadores) {
            if (j.getNombre().equalsIgnoreCase(nombre)) {
                descJugadorInterno(j);
                return;
            }
        }
        consola.imprimir("Jugador no encontrado.");
    }

    @Override
    public void descAvatar(String id) {
        for (Avatar a : avatares) if (a.getId().equalsIgnoreCase(id)) { consola.imprimir(a.toString()); return; }
        consola.imprimir("Avatar no encontrado.");
    }

    @Override
    public void descCasilla(String nombre) {
        Casilla c = tablero.encontrar_casilla(nombre);
        if (c != null) consola.imprimir(c.infoCasilla());
        else consola.imprimir("Casilla no encontrada.");
    }

    @Override
    public void listarVenta() {
        for (Casilla c : tablero.getCasillas()) {
            if (c.getDuenho() == banca && !c.casEnVenta().isEmpty()) {
                consola.imprimir(c.casEnVenta());
            }
        }
    }

    @Override
    public void listarCasillasGrupo(String grupo) {
        for (Casilla c : tablero.getCasillas()) {
            if (c.getGrupo() != null && c.getGrupo().getColor().equalsIgnoreCase(grupo) && c.getDuenho() == banca) {
                consola.imprimir(c.casEnVenta());
            }
        }
    }

    @Override
    public void listarEdificios() {
        boolean hay = false;
        for (Casilla c : tablero.getCasillas()) {
            if (c instanceof Solar) {
                Solar s = (Solar) c;
                for (Edificio e : s.getEdificaciones()) {
                    hay = true;
                    consola.imprimir(String.format("{id: %s, solar: %s, coste: %.0f}", e.getId(), s.getNombre(), e.getPrecio()));
                }
            }
        }
        if(!hay) consola.imprimir("No hay edificios.");
    }

    @Override
    public void listarEdificiosGrupo(String colorGrupo) {
        boolean hay = false;
        for (Casilla c : tablero.getCasillas()) {
            if (c instanceof Solar) {
                Solar s = (Solar) c;
                if(s.getGrupo() != null && s.getGrupo().getColor().equalsIgnoreCase(colorGrupo) && !s.getEdificaciones().isEmpty()){
                    hay = true;
                    consola.imprimir(s.getNombre() + ": " + s.getEdificaciones().size() + " edificios.");
                }
            }
        }
        if(!hay) consola.imprimir("No hay edificios en el grupo " + colorGrupo);
    }

    @Override
    public void listarAvatares() {
        for(Avatar a : avatares) consola.imprimir(a.toString());
    }

    @Override
    public void mostrarEstadisticas(String nombre) {
        Jugador j = null;
        for (Jugador aux : jugadores) if (aux.getNombre().equalsIgnoreCase(nombre)) j = aux;

        if (j == null) {
            consola.imprimir("Jugador no existe.");
            return;
        }
        consola.imprimir(String.format("Estadísticas de %s:", j.getNombre()));
        consola.imprimir(String.format(" Dinero Invertido: %.0f", j.getDineroInvertido()));
        consola.imprimir(String.format(" Pago Tasas: %.0f", j.getPagoTasasEImpuestos()));
        consola.imprimir(String.format(" Cobro Alquileres: %.0f", j.getCobroDeAlquileres()));
        consola.imprimir(String.format(" Pago Alquileres: %.0f", j.getPagoDeAlquileres()));
        consola.imprimir(String.format(" Veces en Cárcel: %d", j.getVecesEnLaCarcel()));
        consola.imprimir(String.format(" Pasos por Salida: %.0f", j.getPasarPorCasillaDeSalida()));
    }

    @Override
    public void mostrarEstadisticasJuego() {
        consola.imprimir("\n--- ESTADÍSTICAS DEL JUEGO ---");

        // 1. Casilla más rentable
        Casilla masRentable = null;
        float maxAlq = 0;
        for (Casilla c : tablero.getCasillas()) {
            if (c.getAlquileresGenerados() > maxAlq) {
                maxAlq = c.getAlquileresGenerados();
                masRentable = c;
            }
        }
        String rentMsg = (masRentable != null)
                ? String.format("%s (%.0f€)", masRentable.getNombre(), maxAlq)
                : "Ninguna";
        consola.imprimir("Casilla más rentable: " + rentMsg);

        // 2. Grupo más rentable
        Map<String, Float> grupoRent = new HashMap<>();
        for (Casilla c : tablero.getCasillas()) {
            if (c instanceof Solar && c.getGrupo() != null) {
                String col = c.getGrupo().getColor();
                grupoRent.put(col, grupoRent.getOrDefault(col, 0f) + c.getAlquileresGenerados());
            }
        }
        String mejorGrupo = null;
        float maxG = 0;
        for (Map.Entry<String, Float> e : grupoRent.entrySet()) {
            if (e.getValue() > maxG) {
                maxG = e.getValue();
                mejorGrupo = e.getKey();
            }
        }
        String grupMsg = (mejorGrupo != null)
                ? String.format("%s (%.0f€)", mejorGrupo, maxG)
                : "Ninguno";
        consola.imprimir("Grupo más rentable: " + grupMsg);

        // 3. Casilla más frecuentada
        Casilla masFrec = null;
        int maxVis = 0;
        for (Casilla c : tablero.getCasillas()) {
            if (c.getVecesVisitada() > maxVis) {
                maxVis = c.getVecesVisitada();
                masFrec = c;
            }
        }
        String frecMsg = (masFrec != null)
                ? String.format("%s (%d visitas)", masFrec.getNombre(), maxVis)
                : "Ninguna";
        consola.imprimir("Casilla más frecuentada: " + frecMsg);

        // 4. Jugador más vueltas
        Jugador masVueltas = null;
        int maxV = -1;
        for (Jugador j : jugadores) {
            if (j.getVueltas() > maxV) {
                maxV = j.getVueltas();
                masVueltas = j;
            }
        }
        String vueltasMsg = (masVueltas != null)
                ? String.format("%s (%d vueltas)", masVueltas.getNombre(), maxV)
                : "Ninguno";
        consola.imprimir("Jugador más vueltas: " + vueltasMsg);

        // 5. Jugador más rico
        Jugador masRico = null;
        float maxF = -1;
        for (Jugador j : jugadores) {
            float total = j.getFortuna();
            for (Casilla c : j.getPropiedades()) {
                total += c.getValor();
                if (c instanceof Solar) {
                    for (Edificio e : ((Solar) c).getEdificaciones()) {
                        total += e.getPrecio();
                    }
                }
            }
            if (total > maxF) {
                maxF = total;
                masRico = j;
            }
        }
        String ricoMsg = (masRico != null)
                ? String.format("%s (%.0f€)", masRico.getNombre(), maxF)
                : "Ninguno";
        consola.imprimir("Jugador más rico: " + ricoMsg);
    }

    @Override
    public void moverChetada(String nombreCasilla) {
        if (jugadores.isEmpty()) return;
        Casilla destino = tablero.encontrar_casilla(nombreCasilla);

        if (destino == null) {
            consola.imprimir("Casilla no encontrada: " + nombreCasilla);
            return;
        }

        Jugador j = getJugadorActual();
        Casilla origen = tablero.getCasilla(j.getPosicion());

        if (origen != null && j.getAvatar() != null) origen.eliminarAvatar(j.getAvatar());
        if (j.getAvatar() != null) {
            destino.anhadirAvatar(j.getAvatar());
            j.getAvatar().setLugar(destino);
        }

        j.setPosicion(destino.getPosicion());
        destino.incrementarVisitas();

        consola.imprimir("Teletransportado a " + destino.getNombre());
        tirado = true;
        mostrarTablero();
    }

    // ================================================================
    //                 MÉTODOS PRIVADOS (NO SON DEL COMANDO)
    // ================================================================

    private void procesarTirada(int suma) {
        Jugador actual = jugadores.get(turno);
        int d1 = dado.getD1();
        int d2 = dado.getD2();
        lanzamientos++;
        tirado = true;

        if (actual.isEnCarcel()) {
            procesarCarcel(actual, d1, d2, suma);
            return;
        }

        consola.imprimir(String.format("%s tira: %d + %d = %d", actual.getNombre(), d1, d2, suma));
        moverYEvaluar(actual, suma);

        if (dado.esDoble()) {
            if (lanzamientos == 3) {
                consola.imprimir("¡Tres dobles! A la cárcel.");
                irACarcel(actual);
                acabarTurno();
            } else {
                consola.imprimir("Dobles. Puedes tirar de nuevo.");
                puedeRepetirLanzamiento = true;
                tirado = false;
            }
        } else {
            puedeRepetirLanzamiento = false;
            consola.imprimir("Fin de tirada. Usa 'acabar turno'.");
        }
    }

    private void moverYEvaluar(Jugador j, int pasos) {
        int posIni = j.getPosicion();
        int posFin = (posIni + pasos) % 40;

        if (posIni + pasos >= 40) {

            // Solo sumar si no cae en salida (0), pues Salida ya suma su propia estadística
            if (posFin != 0) {
                j.sumarFortuna((float) Valor.SUMA_VUELTA);
                j.acumularPasarPorSalida((float) Valor.SUMA_VUELTA);
                j.setVueltas();
            }
            consola.imprimir("Pasas por Salida. Cobras " + Valor.SUMA_VUELTA);
        }

        Casilla origen = tablero.getCasilla(posIni);
        Casilla destino = tablero.getCasilla(posFin);

        if (origen != null && j.getAvatar() != null) origen.eliminarAvatar(j.getAvatar());
        if (destino != null && j.getAvatar() != null) {
            destino.anhadirAvatar(j.getAvatar());
            j.getAvatar().setLugar(destino);
        }

        j.setPosicion(posFin);

        if (destino != null && destino.esIrACarcel()) {
            consola.imprimir("Casilla Ir a Cárcel.");
            irACarcel(j);
            mostrarTablero();
            return;
        }

        consola.imprimir(String.format("Avanzas a %s (pos %d)", destino != null ? destino.getNombre() : "?", posFin));

        if (destino != null) {
            boolean ok;
            if (destino instanceof Suerte) ok = ((Suerte) destino).aplicarCarta(tablero, j, banca, jugadores, pasos);
            else if (destino instanceof CajaComunidad) ok = ((CajaComunidad) destino).aplicarCarta(tablero, j, banca, jugadores, pasos);
            else ok = destino.evaluarCasilla(j, banca, pasos);

            solvente = ok;
            if (!ok) consola.imprimir("No tienes fondos suficientes.");

            destino.incrementarVisitas();
        }
        mostrarTablero();
    }

    private void irACarcel(Jugador j) {
        Casilla carcel = tablero.getCasilla(10);
        j.enviarACarcel(carcel);
    }

    private void procesarCarcel(Jugador j, int d1, int d2, int suma) {
        consola.imprimir(String.format("Tirada en cárcel: %d-%d", d1, d2));
        if (d1 == d2) {
            consola.imprimir("¡Dobles! Sales libre.");
            j.salirDeCarcel();
            moverYEvaluar(j, suma);
            puedeRepetirLanzamiento = true;
            tirado = false;
        } else {
            j.setTiradasCarcel(j.getTiradasCarcel() + 1);
            if (j.getTiradasCarcel() >= 3) {
                consola.imprimir("3 intentos fallidos. Pagas 500,000 y sales.");
                if (j.getFortuna() >= 500000) {
                    j.pagar(500000);
                    j.salirDeCarcel();
                    moverYEvaluar(j, suma);
                } else {
                    consola.imprimir("No puedes pagar. ¡Bancarrota!");
                }
            } else {
                consola.imprimir("Sigues preso. Intentos: " + j.getTiradasCarcel() + "/3");
            }
        }
    }

    private void descJugadorInterno(Jugador j) {
        String av = (j.getAvatar() != null) ? j.getAvatar().getId() : "-";
        List<String> props = new ArrayList<>();
        for (Casilla c : j.getPropiedades()) props.add(c.getNombre());

        consola.imprimir(String.format("{nombre: %s, avatar: %s, fortuna: %.0f, propiedades: %s}",
                j.getNombre(), av, j.getFortuna(), props));
    }

    private boolean validarTurno() {
        if (jugadores.isEmpty()) { consola.imprimir("No hay jugadores."); return false; }
        if (tirado && !puedeRepetirLanzamiento) { consola.imprimir("Ya has tirado."); return false; }
        if (lanzamientos >= 3) { consola.imprimir("Límite lanzamientos."); return false; }
        return true;
    }
}