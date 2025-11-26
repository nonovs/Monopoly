package monopoly;

import monopoly.Juego;
import monopoly.casillas.*;
import partida.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tablero {

    private final ArrayList<ArrayList<Casilla>> posiciones;
    private final HashMap<String, Grupo> grupos;
    private final Jugador banca;

    // ANCHO FIJO de 14 caracteres para que quepa en pantalla y se alinee bien.
    private static final int ANCHO_CELDA = 14;

    public Tablero(Jugador banca) {
        this.banca = banca;
        this.grupos = new HashMap<>();
        this.posiciones = new ArrayList<>();
        this.generarCasillas();
    }

    private void generarCasillas() {
        insertarLadoSur();
        insertarLadoOeste();
        insertarLadoNorte();
        insertarLadoEste();
    }

    // ================================================================
    //           MÉTODOS DE IMPRESIÓN (ALINEACIÓN PERFECTA)
    // ================================================================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // CÁLCULO DE ANCHOS:
        // El ancho total es: BarraInicial + 11 * (Contenido + BarraFinal)
        // Total = 1 + 11 * (14 + 1) = 1 + 11 * 15 = 166 caracteres.
        final int ANCHO_TOTAL = 166;

        // CÁLCULO DEL HUECO CENTRAL:
        // Una fila central es: |CeldaIzq| + HUECO + |CeldaDer|
        // Ancho ocupado por celdas = (1+14+1) + (1+14+1) = 16 + 16 = 32.
        // Hueco = Total - Ocupado = 166 - 32 = 134 espacios.
        final int ANCHO_HUECO = 134;

        String separadorHorizontal = "|" + "-".repeat(ANCHO_TOTAL - 2) + "|\n";

        // 1. TECHO SÓLIDO
        sb.append(separadorHorizontal);

        // 2. LADO NORTE (Indices 20 al 30)
        sb.append("|"); // Barra inicial absoluta
        for (Casilla c : posiciones.get(2)) {
            sb.append(formatearCelda(c)).append("|"); // Contenido + Barra derecha
        }
        sb.append("\n");

        // Separador Norte-Centro
        sb.append(separadorHorizontal);

        // 3. CENTRO (El sándwich)
        ArrayList<Casilla> oeste = posiciones.get(1);
        ArrayList<Casilla> este = posiciones.get(3);
        String espacioHueco = " ".repeat(ANCHO_HUECO);
        String separadorCelda = "-".repeat(ANCHO_CELDA);

        for (int i = 0; i < 9; i++) {
            Casilla cOeste = oeste.get(i);
            Casilla cEste = este.get(i);

            // Fila con contenido: |OESTE| + HUECO + |ESTE|
            sb.append("|").append(formatearCelda(cOeste)).append("|");
            sb.append(espacioHueco);
            sb.append("|").append(formatearCelda(cEste)).append("|\n");

            // Línea separadora horizontal interna (solo entre celdas, no al final)
            if (i < 8) {
                // |---| + HUECO + |---|
                sb.append("|").append(separadorCelda).append("|");
                sb.append(espacioHueco);
                sb.append("|").append(separadorCelda).append("|\n");
            }
        }

        // Separador Centro-Sur
        sb.append(separadorHorizontal);

        // 4. LADO SUR (Indices 10 al 0)
        sb.append("|"); // Barra inicial absoluta
        for (Casilla c : posiciones.get(0)) {
            sb.append(formatearCelda(c)).append("|"); // Contenido + Barra derecha
        }
        sb.append("\n");

        // Suelo final (opcional, queda bien cerrado)
        sb.append(separadorHorizontal);

        return sb.toString();
    }

    /**
     * Formatea el contenido para que mida EXACTAMENTE 'ANCHO_CELDA'.
     * Corta si sobra, rellena con espacios si falta. Ignora colores ANSI al medir.
     */
    private String formatearCelda(Casilla c) {
        String nombreColor = c.getNombre();
        String avatares = jugadoresTablero(c);

        // 1. Calcular longitud visible real (sin códigos de color)
        String textoVisible = sinAnsi(nombreColor);
        if (!avatares.isEmpty()) textoVisible += " " + sinAnsi(avatares);

        int longitudReal = textoVisible.length();
        int padding = ANCHO_CELDA - longitudReal;

        // 2. Construir la celda
        StringBuilder celda = new StringBuilder();

        if (padding >= 0) {
            // CASO NORMAL: Rellenar con espacios
            celda.append(nombreColor);
            if (!avatares.isEmpty()) celda.append(" ").append(avatares);
            celda.append(" ".repeat(padding));
        } else {
            // CASO EXTREMO: El texto es demasiado largo. Hay que cortar.
            // Cortamos el nombre visualmente para que todo encaje en ANCHO_CELDA.
            // (Se pierden los avatares si no caben, para priorizar la estructura del tablero)
            String nombreSinAnsi = sinAnsi(nombreColor);
            String recorte = nombreSinAnsi.substring(0, ANCHO_CELDA);
            // NOTA: Al cortar perdemos el color original en esta versión simple.
            // Es un sacrificio necesario para mantener la alineación perfecta si el nombre es larguísimo.
            celda.append(recorte);
        }

        return celda.toString();
    }

    // Elimina códigos ANSI para medir la longitud real visible
    private String sinAnsi(String texto) {
        if (texto == null) return "";
        return texto.replaceAll("\u001B\\[[;?0-9]*[a-zA-Z]", "");
    }

    public String jugadoresTablero(Casilla casilla) {
        if (casilla == null) return "";
        List<Avatar> avs = casilla.getAvatares();
        if (avs == null || avs.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        sb.append("&");
        for (Avatar a : avs) {
            if (a != null && a.getId() != null) sb.append(a.getId());
        }
        // Color brillante y negrita para que los avatares destaquen
        return Valor.BOLD_STRING + sb.toString() + Valor.RESET;
    }

    public void mostrarTablero() {
        Juego.consola.imprimir(this.toString());
    }

    // ================================================================
    //           DATOS DEL TABLERO (LOS MISMOS QUE TENÍAS)
    // ================================================================

    private void insertarLadoSur() {
        ArrayList<Casilla> ladoSur = new ArrayList<>(11);
        // NOTA: He acortado ligeramente algunos nombres largos para asegurar que caben bien con avatares
        ladoSur.add(new Especial(Valor.WHITE + "Carcel" + Valor.RESET, 10, banca, this));
        ladoSur.add(new Solar(Valor.CYAN + "Solar5" + Valor.RESET, 9, 1200000, 600000, 80000, 500000, 500000, 100000, 200000, 1250000, 6000000, 1200000, 1200000, banca, null));
        ladoSur.add(new Solar(Valor.CYAN + "Solar4" + Valor.RESET, 8, 1000000, 500000, 60000, 500000, 500000, 100000, 200000, 1000000, 5500000, 1100000, 1100000, banca, null));
        ladoSur.add(new Suerte(Valor.WHITE + "Suerte" + Valor.RESET, 7, banca));
        ladoSur.add(new Solar(Valor.CYAN + "Solar3" + Valor.RESET, 6, 1000000, 500000, 60000, 500000, 500000, 100000, 200000, 1000000, 5500000, 1100000, 1100000, banca, null));
        ladoSur.add(new Transporte(Valor.WHITE + "Trans1" + Valor.RESET, 5, 500000, 250000, banca));
        ladoSur.add(new Impuestos(Valor.WHITE + "Imp1" + Valor.RESET, 4, 2000000, banca));
        ladoSur.add(new Solar(Valor.BLACK + "Solar2" + Valor.RESET, 3, 600000, 300000, 40000, 500000, 500000, 100000, 200000, 800000, 4500000, 900000, 900000, banca, null));
        ladoSur.add(new CajaComunidad(Valor.WHITE + "Caja" + Valor.RESET, 2, banca));
        ladoSur.add(new Solar(Valor.BLACK + "Solar1" + Valor.RESET, 1, 600000, 300000, 20000, 500000, 500000, 100000, 200000, 400000, 2500000, 500000, 500000, banca, null));
        ladoSur.add(new Especial("Salida", 0, banca, this));

        for (int i=0; i<ladoSur.size(); i++) if(i!=0 && i!=10) banca.anhadirPropiedad(ladoSur.get(i));

        Grupo grupoCyan = new Grupo(ladoSur.get(1), ladoSur.get(2), ladoSur.get(4), "cian");
        ladoSur.get(1).setGrupo(grupoCyan); ladoSur.get(2).setGrupo(grupoCyan); ladoSur.get(4).setGrupo(grupoCyan);
        grupos.put("cian", grupoCyan);

        Grupo grupoBlack = new Grupo(ladoSur.get(7), ladoSur.get(9), "negro");
        ladoSur.get(7).setGrupo(grupoBlack); ladoSur.get(9).setGrupo(grupoBlack);
        grupos.put("negro", grupoBlack);

        posiciones.add(ladoSur);
    }

    private void insertarLadoOeste() {
        ArrayList<Casilla> ladoOeste = new ArrayList<>(9);
        ladoOeste.add(new Solar(Valor.ORANGE + "Solar11" + Valor.RESET, 19, 2200000, 1000000, 160000, 1000000, 1000000, 200000, 400000, 2000000, 10000000, 2000000, 2000000, banca, null));
        ladoOeste.add(new Solar(Valor.ORANGE + "Solar10" + Valor.RESET, 18, 1800000, 900000, 140000, 1000000, 1000000, 200000, 400000, 1850000, 9500000, 1900000, 1900000, banca, null));
        ladoOeste.add(new CajaComunidad(Valor.WHITE + "Caja" + Valor.RESET, 17, banca));
        ladoOeste.add(new Solar(Valor.ORANGE + "Solar9" + Valor.RESET, 16, 1800000, 900000, 140000, 1000000, 1000000, 200000, 400000, 1850000, 9500000, 1900000, 1900000, banca, null));
        ladoOeste.add(new Transporte(Valor.WHITE + "Trans2" + Valor.RESET, 15, 500000, 250000, banca));
        ladoOeste.add(new Solar(Valor.PURPLE + "Solar8" + Valor.RESET, 14, 1600000, 800000, 120000, 1000000, 1000000, 200000, 400000, 1750000, 9000000, 1800000, 1800000, banca, null));
        ladoOeste.add(new Solar(Valor.PURPLE + "Solar7" + Valor.RESET, 13, 1400000, 700000, 100000, 1000000, 1000000, 200000, 400000, 1500000, 7500000, 1500000, 1500000, banca, null));
        ladoOeste.add(new Servicios(Valor.WHITE + "Serv1" + Valor.RESET, 12, 500000, banca));
        ladoOeste.add(new Solar(Valor.PURPLE + "Solar6" + Valor.RESET, 11, 1400000, 700000, 100000, 1000000, 1000000, 200000, 400000, 1500000, 7500000, 1500000, 1500000, banca, null));

        for (Casilla c : ladoOeste) banca.anhadirPropiedad(c);

        Grupo grupoOrange = new Grupo(ladoOeste.get(0), ladoOeste.get(1), ladoOeste.get(3), "naranja");
        ladoOeste.get(0).setGrupo(grupoOrange); ladoOeste.get(1).setGrupo(grupoOrange); ladoOeste.get(3).setGrupo(grupoOrange);
        grupos.put("naranja", grupoOrange);

        Grupo grupoPurple = new Grupo(ladoOeste.get(5), ladoOeste.get(6), ladoOeste.get(8), "morado");
        ladoOeste.get(5).setGrupo(grupoPurple); ladoOeste.get(6).setGrupo(grupoPurple); ladoOeste.get(8).setGrupo(grupoPurple);
        grupos.put("morado", grupoPurple);
        posiciones.add(ladoOeste);
    }

    private void insertarLadoNorte() {
        ArrayList<Casilla> ladoNorte = new ArrayList<>(11);
        ladoNorte.add(new Especial(Valor.WHITE + "Parking" + Valor.RESET, 20, banca, this));
        ladoNorte.add(new Solar(Valor.RED + "Solar12" + Valor.RESET, 21, 2200000, 1100000, 180000, 1500000, 1500000, 300000, 600000, 2200000, 10500000, 2100000, 2100000, banca, null));
        ladoNorte.add(new Suerte(Valor.WHITE + "Suerte" + Valor.RESET, 22, banca));
        ladoNorte.add(new Solar(Valor.RED + "Solar13" + Valor.RESET, 23, 2200000, 1100000, 180000, 1500000, 1500000, 300000, 600000, 2200000, 10500000, 2100000, 2100000, banca, null));
        ladoNorte.add(new Solar(Valor.RED + "Solar14" + Valor.RESET, 24, 2400000, 1200000, 200000, 1500000, 1500000, 300000, 600000, 2325000, 11000000, 2200000, 2200000, banca, null));
        ladoNorte.add(new Transporte(Valor.WHITE + "Trans3" + Valor.RESET, 25, 500000, 250000, banca));
        ladoNorte.add(new Solar(Valor.BROWN + "Solar15" + Valor.RESET, 26, 2600000, 1300000, 220000, 1500000, 1500000, 300000, 600000, 2450000, 11500000, 2300000, 2300000, banca, null));
        ladoNorte.add(new Solar(Valor.BROWN + "Solar16" + Valor.RESET, 27, 2600000, 1300000, 220000, 1500000, 1500000, 300000, 600000, 2450000, 11500000, 2300000, 2300000, banca, null));
        ladoNorte.add(new Servicios(Valor.WHITE + "Serv2" + Valor.RESET, 28, 500000, banca));
        ladoNorte.add(new Solar(Valor.BROWN + "Solar17" + Valor.RESET, 29, 2800000, 1400000, 240000, 1500000, 1500000, 300000, 600000, 2600000, 12000000, 2400000, 2400000, banca, null));
        ladoNorte.add(new Especial(Valor.WHITE + "IrCarcel" + Valor.RESET, 30, banca, this));

        for (int i=0; i<ladoNorte.size(); i++) if(i!=0 && i!=10) banca.anhadirPropiedad(ladoNorte.get(i));

        Grupo grupoRed = new Grupo(ladoNorte.get(1), ladoNorte.get(3), ladoNorte.get(4), "rojo");
        ladoNorte.get(1).setGrupo(grupoRed); ladoNorte.get(3).setGrupo(grupoRed); ladoNorte.get(4).setGrupo(grupoRed);
        grupos.put("rojo", grupoRed);

        Grupo grupoBrown = new Grupo(ladoNorte.get(6), ladoNorte.get(7), ladoNorte.get(9), "marron");
        ladoNorte.get(6).setGrupo(grupoBrown); ladoNorte.get(7).setGrupo(grupoBrown); ladoNorte.get(9).setGrupo(grupoBrown);
        grupos.put("marron", grupoBrown);
        posiciones.add(ladoNorte);
    }

    private void insertarLadoEste() {
        ArrayList<Casilla> ladoEste = new ArrayList<>(9);
        ladoEste.add(new Solar(Valor.GREEN + "Solar18" + Valor.RESET, 31, 3000000, 1500000, 260000, 2000000, 2000000, 400000, 800000, 2750000, 12750000, 2550000, 2550000, banca, null));
        ladoEste.add(new Solar(Valor.GREEN + "Solar19" + Valor.RESET, 32, 3000000, 1500000, 260000, 2000000, 2000000, 400000, 800000, 2750000, 12750000, 2550000, 2550000, banca, null));
        ladoEste.add(new CajaComunidad(Valor.WHITE + "Caja" + Valor.RESET, 33, banca));
        ladoEste.add(new Solar(Valor.GREEN + "Solar20" + Valor.RESET, 34, 3200000, 1600000, 280000, 2000000, 2000000, 400000, 800000, 3000000, 14000000, 2800000, 2800000, banca, null));
        ladoEste.add(new Transporte(Valor.WHITE + "Trans4" + Valor.RESET, 35, 500000, 250000, banca));
        ladoEste.add(new Suerte(Valor.WHITE + "Suerte" + Valor.RESET, 36, banca));
        ladoEste.add(new Solar(Valor.BLUE + "Solar21" + Valor.RESET, 37, 3500000, 1750000, 350000, 2000000, 2000000, 400000, 800000, 3250000, 17000000, 3400000, 3400000, banca, null));
        ladoEste.add(new Impuestos(Valor.WHITE + "Imp2" + Valor.RESET, 38, 2000000, banca));
        ladoEste.add(new Solar(Valor.BLUE + "Solar22" + Valor.RESET, 39, 4000000, 2000000, 500000, 2000000, 2000000, 400000, 800000, 4250000, 20000000, 4000000, 4000000, banca, null));

        for (Casilla c : ladoEste) banca.anhadirPropiedad(c);

        Grupo grupoGreen = new Grupo(ladoEste.get(0), ladoEste.get(1), ladoEste.get(3), "verde");
        ladoEste.get(0).setGrupo(grupoGreen); ladoEste.get(1).setGrupo(grupoGreen); ladoEste.get(3).setGrupo(grupoGreen);
        grupos.put("verde", grupoGreen);

        Grupo grupoBlue = new Grupo(ladoEste.get(6), ladoEste.get(8), "azul");
        ladoEste.get(6).setGrupo(grupoBlue); ladoEste.get(8).setGrupo(grupoBlue);
        grupos.put("azul", grupoBlue);
        posiciones.add(ladoEste);
    }

    public Casilla getCasilla(int posicion) {
        if (posicion < 0 || posicion >= 40) return null;
        for (ArrayList<Casilla> lado : posiciones) {
            for (Casilla c : lado) if (c.getPosicion() == posicion) return c;
        }
        return null;
    }

    public Casilla encontrar_casilla(String nombre) {
        String objetivo = sinAnsi(nombre);
        for (ArrayList<Casilla> lado : posiciones) {
            for (Casilla c : lado) if (sinAnsi(c.getNombre()).equalsIgnoreCase(objetivo)) return c;
        }
        return null;
    }

    public ArrayList<Casilla> getCasillas() {
        ArrayList<Casilla> todas = new ArrayList<>(40);
        for (ArrayList<Casilla> lado : posiciones) todas.addAll(lado);
        return todas;
    }
}