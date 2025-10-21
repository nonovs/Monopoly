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

    private void insertarLadoSur() {
        ArrayList<Casilla> ladoSur = new ArrayList<>(11);

        // Casillas (posición 10 -> 0)
        Casilla casilla = new Especial(Valor.WHITE + "Carcel", 10, banca, this);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Solar(Valor.CYAN + "Solar5", 9, 520000, 200000, 50000, banca, null);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Solar(Valor.CYAN + "Solar4", 8, 520000, 200000, 50000, banca, null);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Suerte(Valor.WHITE + "Suerte", 7, banca);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Solar(Valor.CYAN + "Solar3", 6, 520000, 200000, 50000, banca, null);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Transporte(Valor.WHITE + "Trans1", 5, 1301328.584f, 50000, banca);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.WHITE + "Imp1", 4, 650664.292f, banca); // impuesto
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Solar(Valor.BLACK + "Solar2", 3, 600000, 250000, 60000, banca, null);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.WHITE + "Caja", "Comunidad", 2, banca);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Solar(Valor.BLACK + "Solar1", 1, 600000, 250000, 60000, banca, null);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Especial("Salida", 0, banca, this);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        // Grupos (usar índices correctos ahora que hay 11 entradas)
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

    private void insertarLadoOeste() {
        ArrayList<Casilla> ladoOeste = new ArrayList<>(9);

        ladoOeste.add(new Solar(Valor.ORANGE + "Solar11", 19, 878800, 300000, 70000, banca, null));
        ladoOeste.add(new Solar(Valor.ORANGE + "Solar10", 18, 878800, 300000, 70000, banca, null));
        ladoOeste.add(new Casilla(Valor.WHITE + "Caja", "Comunidad", 17, banca));
        ladoOeste.add(new Solar(Valor.ORANGE + "Solar9", 16, 878800, 300000, 70000, banca, null));
        ladoOeste.add(new Transporte(Valor.WHITE + "Trans2", 15, 1301328.584f, 50000, banca));
        ladoOeste.add(new Solar(Valor.PURPLE + "Solar8", 14, 676000, 200000, 50000, banca, null));
        ladoOeste.add(new Solar(Valor.PURPLE + "Solar7", 13, 676000, 200000, 50000, banca, null));
        ladoOeste.add(new Servicios(Valor.WHITE + "Serv1", 12, banca));
        ladoOeste.add(new Solar(Valor.PURPLE + "Solar6", 11, 676000, 200000, 50000, banca, null));

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

    private void insertarLadoNorte() {
        ArrayList<Casilla> ladoNorte = new ArrayList<>(11);

        ladoNorte.add(new Especial(Valor.WHITE + "Parking", 20, banca, this)); // pos 20
        ladoNorte.add(new Solar(Valor.RED + "Solar12", 21, 1142440, 400000, 90000, banca, null));
        ladoNorte.add(new Suerte(Valor.WHITE + "Suerte", 22, banca));
        ladoNorte.add(new Solar(Valor.RED + "Solar13", 23, 1142440, 400000, 90000, banca, null));
        ladoNorte.add(new Solar(Valor.RED + "Solar14", 24, 1142440, 400000, 90000, banca, null));
        ladoNorte.add(new Transporte(Valor.WHITE + "Trans3", 25, 1301328.584f, 50000, banca));
        ladoNorte.add(new Solar(Valor.BROWN + "Solar15", 26, 1485172, 500000, 100000, banca, null));
        ladoNorte.add(new Solar(Valor.BROWN + "Solar16", 27, 1485172, 500000, 100000, banca, null));
        ladoNorte.add(new Servicios(Valor.WHITE + "Serv2", 28, banca));
        ladoNorte.add(new Solar(Valor.BROWN + "Solar17", 29, 1485172, 500000, 100000, banca, null));
        ladoNorte.add(new Especial(Valor.WHITE + "IrCarcel", 30, banca, this)); // pos 30

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

    private void insertarLadoEste() {
        ArrayList<Casilla> ladoEste = new ArrayList<>(9);

        ladoEste.add(new Solar(Valor.GREEN + "Solar18", 31, 1930723.6f, 600000, 120000, banca, null));
        ladoEste.add(new Solar(Valor.GREEN + "Solar19", 32, 1930723.6f, 600000, 120000, banca, null));
        ladoEste.add(new Casilla(Valor.WHITE + "Caja", "Comunidad", 33, banca));
        ladoEste.add(new Solar(Valor.GREEN + "Solar20", 34, 1930723.6f, 600000, 120000, banca, null));
        ladoEste.add(new Transporte(Valor.WHITE + "Trans4", 35, 1301328.584f, 50000, banca));
        ladoEste.add(new Suerte(Valor.WHITE + "Suerte", 36, banca));
        ladoEste.add(new Solar(Valor.BLUE + "Solar21", 37, 3764911.02f, 800000, 200000, banca, null));
        ladoEste.add(new Casilla(Valor.WHITE + "Imp2", "Impuesto", 38, banca));
        ladoEste.add(new Solar(Valor.BLUE + "Solar22", 39, 3764911.02f, 800000, 200000, banca, null));

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
            Casilla e = posiciones.get(3).get(8-i);
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