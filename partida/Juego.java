package monopoly;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Importaciones necesarias
import partida.*;
import monopoly.casillas.Casilla;
import monopoly.casillas.Solar;
import monopoly.Construccion.Edificio;
import monopoly.casillas.Suerte;
import monopoly.casillas.CajaComunidad;

import static partida.GestorEdificaciones.eliminarEdificio;

public class Juego {

    // --- ATRIBUTOS DE ESTADO DEL JUEGO ---
    private ArrayList<Jugador> jugadores;
    private ArrayList<Avatar> avatares;
    private Tablero tablero;
    private Dado dado;
    private Jugador banca;

    // Variables de control de flujo
    private int turno = 0;
    private int lanzamientos = 0;
    private boolean tirado = false;
    private boolean solvente = true;
    private boolean puedeRepetirLanzamiento = false;

    // --- CONSTRUCTOR ---
    public Juego() {
        this.banca = new Jugador();
        this.tablero = new Tablero(banca);
        this.dado = new Dado();
        this.jugadores = new ArrayList<>();
        this.avatares = new ArrayList<>();
    }

    // --- GETTERS ---
    public Jugador getJugadorActual() {
        if (jugadores.isEmpty()) return null;
        return jugadores.get(turno);
    }

    public ArrayList<Jugador> getJugadores() { return jugadores; }
    public Tablero getTablero() { return tablero; }

    // ================================================================
    //                 MÉTODOS DE GESTIÓN DE JUGADORES
    // ================================================================

    public void crearJugador(String nombre, String tipoAvatar) {
        for (Jugador j : jugadores) {
            if (j.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println("Ya existe un jugador con ese nombre.");
                return;
            }
        }
        if (jugadores.size() >= 6) { // Ajusta el límite según tus reglas (4 o 6)
            System.out.println("No se pueden crear más jugadores.");
            return;
        }
        if (!tipoAvatar.equalsIgnoreCase("Coche") && !tipoAvatar.equalsIgnoreCase("Esfinge") &&
                !tipoAvatar.equalsIgnoreCase("Sombrero") && !tipoAvatar.equalsIgnoreCase("Pelota")) {
            System.out.println("Tipo de avatar no válido.");
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

        // Evitar duplicar avatar en la lista general si ya existe
        boolean existe = false;
        for(Avatar a : avatares) if(a.getId().equals(avatar.getId())) existe = true;
        if(!existe) avatares.add(avatar);

        // Añadir a casilla Salida
        if (!salida.getAvatares().contains(avatar)) {
            salida.anhadirAvatar(avatar);
            avatar.setLugar(salida);
        }
        nuevo.setPosicion(salida.getPosicion());

        System.out.printf("Jugador creado: {nombre: %s, avatar: %s}%n", nombre, avatar.getId());
        mostrarTablero();
    }

    public void mostrarJugadorEnTurno() {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores.");
            return;
        }
        Jugador actual = jugadores.get(turno);
        String avatarId = (actual.getAvatar() != null) ? actual.getAvatar().getId() : "-";
        System.out.printf("Turno de: %s (avatar %s) - posicion %d%n", actual.getNombre(), avatarId, actual.getPosicion());

        if (actual.isEnCarcel()) {
            System.out.println("ESTÁ EN LA CÁRCEL. Opciones: 'salir carcel' (pagando) o sacar dobles.");
        }
    }

    // ================================================================
    //                 MÉTODOS PRINCIPALES (Mecánicas)
    // ================================================================

    public void mostrarTablero() {
        tablero.mostrarTablero();
    }

    public void lanzarDados() {
        if (!validarTurno()) return;
        if (lanzamientos == 0) dado.iniciarTurno();
        int suma = dado.tirar();
        procesarTirada(suma);
    }

    public void lanzarDadosForzada(int a, int b) {
        if (!validarTurno()) return;
        if (a < 1 || a > 6 || b < 1 || b > 6) {
            System.out.println("Dados deben ser 1-6.");
            return;
        }
        if (lanzamientos == 0) dado.iniciarTurno();
        int suma = dado.tirarForzado(a, b);
        procesarTirada(suma);
    }

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

        System.out.printf("%s tira: %d + %d = %d%n", actual.getNombre(), d1, d2, suma);
        moverYEvaluar(actual, suma);

        if (dado.esDoble()) {
            if (lanzamientos == 3) {
                System.out.println("¡Tres dobles! A la cárcel.");
                irACarcel(actual);
                acabarTurno();
            } else {
                System.out.println("Dobles. Puedes tirar de nuevo.");
                puedeRepetirLanzamiento = true;
                tirado = false;
            }
        } else {
            puedeRepetirLanzamiento = false;
            System.out.println("Fin de tirada. Usa 'acabar turno'.");
        }
    }

    private void moverYEvaluar(Jugador j, int pasos) {
        int posIni = j.getPosicion();
        int posFin = (posIni + pasos) % 40;

        if (posIni + pasos >= 40) {
            j.sumarFortuna((float) Valor.SUMA_VUELTA);
            j.acumularPasarPorSalida((float) Valor.SUMA_VUELTA);
            j.setVueltas();
            System.out.println("Pasas por Salida. Cobras " + Valor.SUMA_VUELTA);
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
            System.out.println("Casilla Ir a Cárcel.");
            irACarcel(j);
            mostrarTablero();
            return;
        }

        System.out.printf("Avanzas a %s (pos %d)%n", destino != null ? destino.getNombre() : "?", posFin);

        if (destino != null) {
            boolean ok;
            if (destino instanceof Suerte) ok = ((Suerte) destino).aplicarCarta(tablero, j, banca, jugadores, pasos);
            else if (destino instanceof CajaComunidad) ok = ((CajaComunidad) destino).aplicarCarta(tablero, j, banca, jugadores, pasos);
            else ok = destino.evaluarCasilla(j, banca, pasos);

            solvente = ok;
            if (!ok) System.out.println("No tienes fondos suficientes.");

            destino.incrementarVisitas();
        }
        mostrarTablero();
    }

    private void irACarcel(Jugador j) {
        Casilla carcel = tablero.getCasilla(10);
        j.enviarACarcel(carcel);
    }

    public void salirCarcel() {
        Jugador j = getJugadorActual();
        if (j == null || !j.isEnCarcel()) {
            System.out.println("No estás en la cárcel.");
            return;
        }
        if (j.getFortuna() >= 500000) {
            j.pagar(500000);
            j.acumularPagoTasasEImpuestos(500000);
            j.salirDeCarcel();
            System.out.println("Fianza pagada. Estás libre.");
        } else {
            System.out.println("No tienes dinero suficiente.");
        }
    }

    private void procesarCarcel(Jugador j, int d1, int d2, int suma) {
        System.out.printf("Tirada en cárcel: %d-%d%n", d1, d2);
        if (d1 == d2) {
            System.out.println("¡Dobles! Sales libre.");
            j.salirDeCarcel();
            moverYEvaluar(j, suma);
            puedeRepetirLanzamiento = true;
            tirado = false;
        } else {
            j.setTiradasCarcel(j.getTiradasCarcel() + 1);
            if (j.getTiradasCarcel() >= 3) {
                System.out.println("3 intentos fallidos. Pagas 500,000 y sales.");
                if (j.getFortuna() >= 500000) {
                    j.pagar(500000);
                    j.salirDeCarcel();
                    moverYEvaluar(j, suma);
                } else {
                    System.out.println("No puedes pagar. ¡Bancarrota!"); // Implementar lógica bancarrota si necesario
                }
            } else {
                System.out.println("Sigues preso. Intentos: " + j.getTiradasCarcel() + "/3");
            }
        }
    }

    // --- ACCIONES ECONÓMICAS ---

    public void comprar(String nombre) {
        if (jugadores.isEmpty()) return;
        Casilla c = tablero.encontrar_casilla(nombre);
        if (c != null) c.comprarCasilla(getJugadorActual(), banca);
        else System.out.println("Casilla no existe.");
    }

    public void edificar(String tipo) {
        if (jugadores.isEmpty()) return;
        GestorEdificaciones.edificar(getJugadorActual(), tipo);
    }

    public void hipotecar(String nombre) {
        Casilla c = tablero.encontrar_casilla(nombre);
        if (c != null) getJugadorActual().hipotecarPropiedad(c);
        else System.out.println("Casilla no existe.");
    }

    public void deshipotecar(String nombre) {
        Casilla c = tablero.encontrar_casilla(nombre);
        if (c != null) getJugadorActual().deshipotecarPropiedad(c);
        else System.out.println("Casilla no existe.");
    }

    public void venderEdificio(String tipo, String solarNombre, int cantidad) {
        if (jugadores.isEmpty()) return;
        Jugador actual = getJugadorActual();
        Casilla c = tablero.encontrar_casilla(solarNombre);

        if (!(c instanceof Solar)) {
            System.out.println("No es un solar.");
            return;
        }
        Solar s = (Solar) c;
        if (s.getDuenho() != actual) {
            System.out.println("No eres el dueño.");
            return;
        }

        // Lógica simplificada de venta delegando en Gestor o interna
        // Aquí adapto tu lógica original para que funcione dentro de Juego:
        String tipoNorm = tipo.toLowerCase();
        if(tipoNorm.startsWith("casa")) tipoNorm = "casa";
        else if(tipoNorm.startsWith("hotel")) tipoNorm = "hotel";
        else if(tipoNorm.startsWith("piscina")) tipoNorm = "piscina";
        else if(tipoNorm.startsWith("pista")) tipoNorm = "pista";
        else { System.out.println("Tipo inválido."); return; }

        if(!tipoNorm.equals("casa") && cantidad > 1) cantidad = 1;

        List<Edificio> aVender = new ArrayList<>();
        for(Edificio e : s.getEdificaciones()) {
            String t = e.getTipo().toLowerCase();
            if(t.contains(tipoNorm)) aVender.add(e);
        }

        if(aVender.size() < cantidad) {
            System.out.println("No hay suficientes edificios.");
            return;
        }

        float total = 0;
        for(int i=0; i<cantidad; i++) {
            Edificio e = aVender.get(i);
            if(eliminarEdificio(e)) total += e.getPrecio() / 2; // Asumiendo venta a mitad de precio
        }
        actual.sumarFortuna(total);
        System.out.printf("Vendidos %d %s. Recibes %.0f.%n", cantidad, tipoNorm, total);
    }

    public void acabarTurno() {
        if (!tirado) {
            System.out.println("Debes tirar dados primero.");
            return;
        }
        turno = (turno + 1) % jugadores.size();
        tirado = false;
        lanzamientos = 0;
        puedeRepetirLanzamiento = false;
        solvente = true;
        System.out.println("Turno de: " + getJugadorActual().getNombre());
    }

    // ================================================================
    //                 MÉTODOS DE ESTADÍSTICAS Y LISTADOS
    // ================================================================

    public void listarJugadores() {
        if (jugadores.isEmpty()) { System.out.println("Sin jugadores."); return; }
        for (Jugador j : jugadores) descJugadorInterno(j);
    }

    public void descJugador(String nombre) {
        for (Jugador j : jugadores) {
            if (j.getNombre().equalsIgnoreCase(nombre)) {
                descJugadorInterno(j);
                return;
            }
        }
        System.out.println("Jugador no encontrado.");
    }

    private void descJugadorInterno(Jugador j) {
        String av = (j.getAvatar() != null) ? j.getAvatar().getId() : "-";
        List<String> props = new ArrayList<>();
        for (Casilla c : j.getPropiedades()) props.add(c.getNombre());

        System.out.printf("{nombre: %s, avatar: %s, fortuna: %.0f, propiedades: %s}%n",
                j.getNombre(), av, j.getFortuna(), props);
    }

    public void descAvatar(String id) {
        for (Avatar a : avatares) if (a.getId().equalsIgnoreCase(id)) { System.out.println(a); return; }
        System.out.println("Avatar no encontrado.");
    }

    public void descCasilla(String nombre) {
        Casilla c = tablero.encontrar_casilla(nombre);
        if (c != null) System.out.println(c.infoCasilla());
        else System.out.println("Casilla no encontrada.");
    }

    public void listarVenta() {
        for (Casilla c : tablero.getCasillas()) {
            if (c.getDuenho() == banca && !c.casEnVenta().isEmpty()) {
                System.out.println(c.casEnVenta());
            }
        }
    }

    public void listarCasillasGrupo(String grupo) {
        for (Casilla c : tablero.getCasillas()) {
            if (c.getGrupo() != null && c.getGrupo().getColor().equalsIgnoreCase(grupo) && c.getDuenho() == banca) {
                System.out.println(c.casEnVenta());
            }
        }
    }

    public void listarEdificios() {
        boolean hay = false;
        for (Casilla c : tablero.getCasillas()) {
            if (c instanceof Solar) {
                Solar s = (Solar) c;
                for (Edificio e : s.getEdificaciones()) {
                    hay = true;
                    System.out.printf("{id: %s, solar: %s, coste: %.0f}%n", e.getId(), s.getNombre(), e.getPrecio());
                }
            }
        }
        if(!hay) System.out.println("No hay edificios.");
    }

    public void listarEdificiosGrupo(String colorGrupo) {
        // Lógica simplificada de listado por grupo
        boolean hay = false;
        for (Casilla c : tablero.getCasillas()) {
            if (c instanceof Solar) {
                Solar s = (Solar) c;
                if(s.getGrupo() != null && s.getGrupo().getColor().equalsIgnoreCase(colorGrupo) && !s.getEdificaciones().isEmpty()){
                    hay = true;
                    System.out.println(s.getNombre() + ": " + s.getEdificaciones().size() + " edificios.");
                }
            }
        }
        if(!hay) System.out.println("No hay edificios en el grupo " + colorGrupo);
    }

    public void listarAvatares() {
        for(Avatar a : avatares) System.out.println(a);
    }

    // --- ESTADÍSTICAS INDIVIDUALES ---
    public void mostrarEstadisticas(String nombre) {
        Jugador j = null;
        for (Jugador aux : jugadores) if (aux.getNombre().equalsIgnoreCase(nombre)) j = aux;

        if (j == null) {
            System.out.println("Jugador no existe.");
            return;
        }
        System.out.printf("Estadísticas de %s:%n", j.getNombre());
        System.out.printf(" Dinero Invertido: %.0f%n", j.getDineroInvertido());
        System.out.printf(" Pago Tasas: %.0f%n", j.getPagoTasasEImpuestos());
        System.out.printf(" Cobro Alquileres: %.0f%n", j.getCobroDeAlquileres());
        System.out.printf(" Pago Alquileres: %.0f%n", j.getPagoDeAlquileres());
        System.out.printf(" Veces en Cárcel: %d%n", j.getVecesEnLaCarcel());
        System.out.printf(" Pasos por Salida: %.0f%n", j.getPasarPorCasillaDeSalida());
    }

    // --- ESTADÍSTICAS GLOBALES (Trasladadas desde Menu) ---
    public void mostrarEstadisticasJuego() {
        System.out.println("\n--- ESTADÍSTICAS DEL JUEGO ---");

        // 1. Casilla más rentable
        Casilla masRentable = null;
        float maxAlq = 0;
        for (Casilla c : tablero.getCasillas()) {
            if (c.getAlquileresGenerados() > maxAlq) {
                maxAlq = c.getAlquileresGenerados();
                masRentable = c;
            }
        }
        System.out.println("Casilla más rentable: " + (masRentable != null ? masRentable.getNombre() + " (" + maxAlq + ")" : "Ninguna"));

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
        System.out.println("Grupo más rentable: " + (mejorGrupo != null ? mejorGrupo + " (" + maxG + ")" : "Ninguno"));

        // 3. Casilla más frecuentada
        Casilla masFrec = null;
        int maxVis = 0;
        for (Casilla c : tablero.getCasillas()) {
            if (c.getVecesVisitada() > maxVis) {
                maxVis = c.getVecesVisitada();
                masFrec = c;
            }
        }
        System.out.println("Casilla más frecuentada: " + (masFrec != null ? masFrec.getNombre() + " (" + maxVis + ")" : "Ninguna"));

        // 4. Jugador más vueltas
        Jugador masVueltas = null;
        int maxV = 0;
        for (Jugador j : jugadores) {
            if (j.getVueltas() > maxV) {
                maxV = j.getVueltas();
                masVueltas = j;
            }
        }
        System.out.println("Jugador más vueltas: " + (masVueltas != null ? masVueltas.getNombre() + " (" + maxV + ")" : "Ninguno"));

        // 5. Jugador más rico (Fortuna + Propiedades + Edificios)
        Jugador masRico = null;
        float maxF = -1;
        for (Jugador j : jugadores) {
            float total = j.getFortuna();
            for (Casilla c : j.getPropiedades()) {
                total += c.getValor();
                if (c instanceof Solar) {
                    for (Edificio e : ((Solar) c).getEdificaciones()) total += e.getPrecio();
                }
            }
            if (total > maxF) {
                maxF = total;
                masRico = j;
            }
        }
        System.out.println("Jugador más rico: " + (masRico != null ? masRico.getNombre() + " (" + maxF + ")" : "Ninguno"));
    }

    // --- CHEAT / TESTING ---
    public void moverChetada(String nombreCasilla) {
        if (jugadores.isEmpty()) return;
        Casilla destino = tablero.encontrar_casilla(nombreCasilla);

        if (destino == null) {
            System.out.println("Casilla no encontrada: " + nombreCasilla);
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

        System.out.println("Teletransportado a " + destino.getNombre());
        tirado = true;
        mostrarTablero();
    }

    private boolean validarTurno() {
        if (jugadores.isEmpty()) { System.out.println("No hay jugadores."); return false; }
        if (tirado && !puedeRepetirLanzamiento) { System.out.println("Ya has tirado."); return false; }
        if (lanzamientos >= 3) { System.out.println("Límite lanzamientos."); return false; }
        return true;
    }
}