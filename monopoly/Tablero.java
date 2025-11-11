package monopoly;

import monopoly.casillas.*;
import partida.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tablero {

    private ArrayList<ArrayList<Casilla>> posiciones;
    private HashMap<String, Grupo> grupos; // Grupos por color
    private Jugador banca;

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

   
    // ---------------- LADO SUR ----------------
    private void insertarLadoSur() {
        ArrayList<Casilla> ladoSur = new ArrayList<>(11);

        Casilla casilla = new Especial(Valor.WHITE + "Carcel", 10, banca, this);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Solar(Valor.CYAN + "Solar5", 9,
                1200000, 600000, 80000,
                500000, 500000, 100000, 200000,
                1250000, 6000000, 1200000, 1200000,
                banca, null);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Solar(Valor.CYAN + "Solar4", 8,
                1000000, 500000, 60000,
                500000, 500000, 100000, 200000,
                1000000, 5500000, 1100000, 1100000,
                banca, null);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Suerte(Valor.WHITE + "Suerte", 7, banca);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Solar(Valor.CYAN + "Solar3", 6,
                1000000, 500000, 60000,
                500000, 500000, 100000, 200000,
                1000000, 5500000, 1100000, 1100000,
                
                banca, null);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Transporte(Valor.WHITE + "Trans1", 5, 500000, 250000, banca);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Impuestos(Valor.WHITE + "Imp1", 4, 1000000, banca);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Solar(Valor.BLACK + "Solar2", 3,
                600000, 300000, 40000,
                500000, 500000, 100000, 200000,
                800000, 4500000, 900000, 900000,
                banca, null);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new CajaComunidad(Valor.WHITE + "Caja Comunidad", 2, banca);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Solar(Valor.BLACK + "Solar1", 1,
                600000, 300000, 20000,
                500000, 500000, 100000, 200000,
                400000, 2500000, 500000, 500000,
                banca, null);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Especial("Salida", 0, banca, this);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        Grupo grupoCyan = new Grupo(ladoSur.get(1), ladoSur.get(2), ladoSur.get(4), "CYAN");
        ladoSur.get(1).setGrupo(grupoCyan);
        ladoSur.get(2).setGrupo(grupoCyan);
        ladoSur.get(4).setGrupo(grupoCyan);
        grupos.put("CYAN", grupoCyan);

        Grupo grupoBlack = new Grupo(ladoSur.get(7), ladoSur.get(9), "BLACK");
        ladoSur.get(7).setGrupo(grupoBlack);
        ladoSur.get(9).setGrupo(grupoBlack);
        grupos.put("BLACK", grupoBlack);

        posiciones.add(ladoSur);
    }
  // ---------------- LADO OESTE ----------------
    private void insertarLadoOeste() {
        ArrayList<Casilla> ladoOeste = new ArrayList<>(9);

        ladoOeste.add(new Solar(Valor.ORANGE + "Solar11", 19,
                2200000, 1000000, 160000,
                1000000, 1000000, 200000, 400000,
                2000000, 10000000, 2000000, 2000000,
                banca, null));

        ladoOeste.add(new Solar(Valor.ORANGE + "Solar10", 18,
                1800000, 900000, 140000,
                1000000, 1000000, 200000, 400000,
                1850000, 9500000, 1900000, 1900000,
                banca, null));

        ladoOeste.add(new CajaComunidad(Valor.WHITE + "Caja Comunidad", 17, banca));

        ladoOeste.add(new Solar(Valor.ORANGE + "Solar9", 16,
                1800000, 900000, 140000,
                1000000, 1000000, 200000, 400000,
                1850000, 9500000, 1900000, 1900000,
                banca, null));

        ladoOeste.add(new Transporte(Valor.WHITE + "Trans2", 15, 500000, 250000, banca));

        ladoOeste.add(new Solar(Valor.PURPLE + "Solar8", 14,
                1600000, 800000, 120000,
                1000000, 1000000, 200000, 400000,
                1750000, 9000000, 1800000, 1800000,
                banca, null));

        ladoOeste.add(new Solar(Valor.PURPLE + "Solar7", 13,
                1400000, 700000, 100000,
                1000000, 1000000, 200000, 400000,
                1500000, 7500000, 1500000, 1500000,
                banca, null));

        ladoOeste.add(new Servicios(Valor.WHITE + "Serv1", 12,500000, banca));

        ladoOeste.add(new Solar(Valor.PURPLE + "Solar6", 11,
                1400000, 700000, 100000,
                1000000, 1000000, 200000, 400000,
                1500000, 7500000, 1500000, 1500000,
                banca, null));

        for (Casilla c : ladoOeste) banca.anhadirPropiedad(c);

        Grupo grupoOrange = new Grupo(ladoOeste.get(0), ladoOeste.get(1), ladoOeste.get(3), "ORANGE");
        ladoOeste.get(0).setGrupo(grupoOrange);
        ladoOeste.get(1).setGrupo(grupoOrange);
        ladoOeste.get(3).setGrupo(grupoOrange);
        grupos.put("ORANGE", grupoOrange);

        Grupo grupoPurple = new Grupo(ladoOeste.get(5), ladoOeste.get(6), ladoOeste.get(8), "PURPLE");
        ladoOeste.get(5).setGrupo(grupoPurple);
        ladoOeste.get(6).setGrupo(grupoPurple);
        ladoOeste.get(8).setGrupo(grupoPurple);
        grupos.put("PURPLE", grupoPurple);

        posiciones.add(ladoOeste);
    }

   // ---------------- LADO NORTE ----------------
    private void insertarLadoNorte() {
        ArrayList<Casilla> ladoNorte = new ArrayList<>(11);

        ladoNorte.add(new Especial(Valor.WHITE + "Parking", 20, banca, this));

        ladoNorte.add(new Solar(Valor.RED + "Solar12", 21,
                2200000, 1100000, 180000,
                1500000, 1500000, 300000, 600000,
                2200000, 10500000, 2100000, 2100000,
                banca, null));

        ladoNorte.add(new Suerte(Valor.WHITE + "Suerte", 22, banca));

        ladoNorte.add(new Solar(Valor.RED + "Solar13", 23,
                2200000, 1100000, 180000,
                1500000, 1500000, 300000, 600000,
                2200000, 10500000, 2100000, 2100000,
                banca, null));

        ladoNorte.add(new Solar(Valor.RED + "Solar14", 24,
                2400000, 1200000, 200000,
                1500000, 1500000, 300000, 600000,
                2325000, 11000000, 2200000, 2200000,
                banca, null));

        ladoNorte.add(new Transporte(Valor.WHITE + "Trans3", 25, 500000, 250000, banca));

        ladoNorte.add(new Solar(Valor.BROWN + "Solar15", 26,
                2600000, 1300000, 220000,
                1500000, 1500000, 300000, 600000,
                2450000, 11500000, 2300000, 2300000,
                banca, null));

        ladoNorte.add(new Solar(Valor.BROWN + "Solar16", 27,
                2600000, 1300000, 220000,
                1500000, 1500000, 300000, 600000,
                2450000, 11500000, 2300000, 2300000,
                banca, null));

        ladoNorte.add(new Servicios(Valor.WHITE + "Serv2", 28, 500000, banca));

        ladoNorte.add(new Solar(Valor.BROWN + "Solar17", 29,
                2800000, 1400000, 240000,
                1500000, 1500000, 300000, 600000,
                2600000, 12000000, 2400000, 2400000,
                banca, null));

        ladoNorte.add(new Especial(Valor.WHITE + "IrCarcel", 30, banca, this));

        for (Casilla c : ladoNorte) banca.anhadirPropiedad(c);

        Grupo grupoRed = new Grupo(ladoNorte.get(1), ladoNorte.get(3), ladoNorte.get(4), "RED");
        ladoNorte.get(1).setGrupo(grupoRed);
        ladoNorte.get(3).setGrupo(grupoRed);
        ladoNorte.get(4).setGrupo(grupoRed);
        grupos.put("RED", grupoRed);

        Grupo grupoBrown = new Grupo(ladoNorte.get(6), ladoNorte.get(7), ladoNorte.get(9), "BROWN");
        ladoNorte.get(6).setGrupo(grupoBrown);
        ladoNorte.get(7).setGrupo(grupoBrown);
        ladoNorte.get(9).setGrupo(grupoBrown);
        grupos.put("BROWN", grupoBrown);

        posiciones.add(ladoNorte);
    }

     // ---------------- LADO ESTE ----------------
    private void insertarLadoEste() {
        ArrayList<Casilla> ladoEste = new ArrayList<>(9);

        ladoEste.add(new Solar(Valor.GREEN + "Solar18", 31,
                3000000, 1500000, 260000,
                2000000, 2000000, 400000, 800000,
                2750000, 12750000, 2550000, 2550000,
                banca, null));

        ladoEste.add(new Solar(Valor.GREEN + "Solar19", 32,
                3000000, 1500000, 260000,
                2000000, 2000000, 400000, 800000,
                2750000, 12750000, 2550000, 2550000,
                banca, null));

        ladoEste.add(new CajaComunidad(Valor.WHITE + "Caja Comunidad", 33, banca));

        ladoEste.add(new Solar(Valor.GREEN + "Solar20", 34,
                3200000, 1600000, 280000,
                2000000, 2000000, 400000, 800000,
                3000000, 14000000, 2800000, 2800000,
                banca, null));

        ladoEste.add(new Transporte(Valor.WHITE + "Trans4", 35, 500000, 250000, banca));
        ladoEste.add(new Suerte(Valor.WHITE + "Suerte", 36, banca));

        ladoEste.add(new Solar(Valor.BLUE + "Solar21", 37,
                3500000, 1750000, 350000,
                2000000, 2000000, 400000, 800000,
                3250000, 17000000, 3400000, 3400000,
                banca, null));

        ladoEste.add(new Impuestos(Valor.WHITE + "Imp2",  38,2000000, banca));

        ladoEste.add(new Solar(Valor.BLUE + "Solar22", 39,
                4000000, 2000000, 500000,
                2000000, 2000000, 400000, 800000,
                4250000, 20000000, 4000000, 4000000,
                banca, null));

        for (Casilla c : ladoEste) banca.anhadirPropiedad(c);

        Grupo grupoGreen = new Grupo(ladoEste.get(0), ladoEste.get(1), ladoEste.get(3), "GREEN");
        ladoEste.get(0).setGrupo(grupoGreen);
        ladoEste.get(1).setGrupo(grupoGreen);
        ladoEste.get(3).setGrupo(grupoGreen);
        grupos.put("GREEN", grupoGreen);

        Grupo grupoBlue = new Grupo(ladoEste.get(6), ladoEste.get(8), "BLUE");
        ladoEste.get(6).setGrupo(grupoBlue);
        ladoEste.get(8).setGrupo(grupoBlue);
        grupos.put("BLUE", grupoBlue);

        posiciones.add(ladoEste);
    }

    public String jugadoresTablero(Casilla casilla) {
        if (casilla == null) return "";
        List<Avatar> avs = casilla.getAvatares();
        if (avs == null || avs.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        sb.append("&");
        for (Avatar a : avs) if (a != null && a.getId() != null) sb.append(a.getId());
        String res = sb.toString();
        int max = Math.max(1, Valor.NCHARS_CASILLA - 1);
        return res.length() > max ? res.substring(0, max - 1) + "…" : res;
    }

    // Imprime el tablero
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        //Lado norte
        sb.append("|");
        for (int i=0;i<11;i++){
            Casilla c = posiciones.get(2).get(i);
            String nombreColored = c.getNombre();
            String avs = jugadoresTablero(c); // "&A" ou ""
            String content = avs.isEmpty() ? nombreColored : nombreColored + " " + avs;
            sb.append(String.format("%-10s|", content));
        }
        sb.append("\n");
        // centro
        for (int i=0;i<=8;i++){//Lado oeste
            Casilla o = posiciones.get(1).get(i);
            String nameO = o.getNombre();
            String avsO = jugadoresTablero(o);
            String contentO = avsO.isEmpty() ? nameO : nameO + " " + avsO;
            sb.append(String.format("|%-12s|", contentO));
            for (int j=0;j<9;j++) sb.append("        ");//Lado este
            Casilla e = posiciones.get(3).get(i);
            String nameE = e.getNombre();
            String avsE = jugadoresTablero(e);
            String contentE = avsE.isEmpty() ? nameE : nameE + " " + avsE;
            sb.append(String.format("|%-10s|\n", contentE));
        }
        // lado sur
        sb.append("|");
        for (int i=0;i<=10;i++){
            Casilla c = posiciones.get(0).get(i);
            String nombreColored = c.getNombre();
            String avs = jugadoresTablero(c);
            String content = avs.isEmpty() ? nombreColored : nombreColored + " " + avs;
            sb.append(String.format("%-11s|", content));
        }
        sb.append("\n");
        return sb.toString();
    }
    public String conEspacios(int n) {
        if (n <= 0) return "";
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) sb.append(' ');
        return sb.toString();
    }

    public String subrayar(String texto) {
        return Valor.SUBRAYADO + texto + Valor.RESET;
    }

    private static String sinAnsi(String s) {
        return s == null ? "" : s.replaceAll("\\u001B\\[[;\\d]*m", "");
    }

    /**
     * Devuelve la cadena de fichas (avatares) para la parte inferior de la casilla.
     * Mantengo tu función 'fichas' (la que pintaba &ID en la línea inferior subrayada).
     */
    public String fichas(Casilla casilla) {
        int nj = casilla.getAvatares().size();
        String fichas = "";

        if (nj == 0) {
            fichas += conEspacios(Valor.NCHARS_CASILLA);
        } else {
            fichas += Valor.BOLD_STRING + "&";
            int i = 0;
            for (; i < nj && i < Valor.NCHARS_CASILLA - 1; i++) {
                String id = casilla.getAvatares().get(i).getId();
                if (id == null) id = "";
                fichas += id;
            }
            int rellenar = Valor.NCHARS_CASILLA - i - 1;
            fichas += conEspacios(Math.max(0, rellenar));
        }
        return fichas;
    }

    public String formatoFichas(Casilla casilla) {
        return subrayar(fichas(casilla)) + Valor.BARRA;
    }


    // Devuelve casilla por posición global (0-39)
    public Casilla getCasilla(int posicion) {
        if (posicion < 0 || posicion >= 40) return null;
        for (ArrayList<Casilla> lado : posiciones) {
            for (Casilla c : lado) {
                if (c.getPosicion() == posicion) return c;
            }
        }
        return null;
    }



    public Casilla encontrar_casilla(String nombre) {
        String objetivo = sinAnsi(nombre);
        for (ArrayList<Casilla> lado : posiciones) {
            for (Casilla c : lado) {
                if (sinAnsi(c.getNombre()).equalsIgnoreCase(objetivo)) {
                    return c;
                }
            }
        }
        return null;
    }


    public ArrayList<Casilla> getCasillas() {
        ArrayList<Casilla> todas = new ArrayList<>(40);
        for (ArrayList<Casilla> lado : posiciones) {
            todas.addAll(lado);
        }
        return todas;
    }


    public void mostrarTablero() {
        System.out.println(this.toString());
    }
}