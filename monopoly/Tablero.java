package monopoly;

import java.lang.invoke.LambdaConversionException;
import java.lang.reflect.Array;
import monopoly.casillas.Casilla;
import partida.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Tablero {
    // Atributos
    private ArrayList<ArrayList<Casilla>> posiciones;
    private HashMap<String, Grupo> grupos; // Grupos por color
    private Jugador banca;

    // Constructor
    public Tablero(Jugador banca) {
        this.banca = banca;
        this.grupos = new HashMap<>();
        this.posiciones = new ArrayList<>();
        this.generarCasillas();
    }

    // Genera las casillas del tablero
    private void generarCasillas() {
        this.insertarLadoSur();
        this.insertarLadoOeste();
        this.insertarLadoNorte();
        this.insertarLadoEste();
    }

    // Lado Sur
    private void insertarLadoSur() {
        ArrayList<Casilla> ladoSur = new ArrayList<>(11);

        Casilla casilla = new Casilla(Valor.WHITE + "Carcel" , "Especial", 10, banca);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.CYAN + "Solar5" , "Solar", 9, 520000 , banca);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.CYAN + "Solar4" , "Solar", 8,520000 , banca);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.WHITE + "Suerte" , "Suerte", 7, banca);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.CYAN + "Solar3" , "Solar", 6,520000 , banca);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.WHITE + "Trans1" ,"Transporte", 5,1301328.584f , banca);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.WHITE + "Imp1" , 4,650664.292f , banca);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.BLACK + "Solar2" , "Solar", 3,600000 , banca);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.WHITE + "Caja" , "Comunidad", 2, banca);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.BLACK + "Solar1" , "Solar", 1,600000 , banca);
        ladoSur.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla("Salida" , "Especial", 0, banca); //Crear unha casilla
        ladoSur.add(casilla); //Engadila ao arrayList
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

        this.posiciones.add(ladoSur);
    }

    // Lado Oeste
    private void insertarLadoOeste() {
        ArrayList<Casilla> ladoOeste = new ArrayList<>(9);

        Casilla casilla = new Casilla(Valor.ORANGE + "Solar11" , "Solar", 19, 878800 , banca);
        ladoOeste.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.ORANGE + "Solar10" , "Solar", 18, 878800 , banca);
        ladoOeste.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.WHITE + "Caja" , "Comunidad", 17,  banca);
        ladoOeste.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.ORANGE + "Solar9" , "Solar", 16, 878800 , banca);
        ladoOeste.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.WHITE + "Trans2" , "Transporte", 15, 1301328.584f , banca);
        ladoOeste.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.PURPLE + "Solar8" , "Solar", 14, 676000 , banca);
        ladoOeste.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.PURPLE + "Solar7" , "Solar", 13, 676000 , banca);
        ladoOeste.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.WHITE + "Serv1" , "Servicio", 12, 975996.438f, banca);
        ladoOeste.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.PURPLE + "Solar6" , "Solar", 11, 676000 , banca);
        ladoOeste.add(casilla);
        banca.anhadirPropiedad(casilla);

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

        this.posiciones.add(ladoOeste);

    }

    // Lado Norte
    private void insertarLadoNorte() {
        ArrayList<Casilla> ladoNorte = new ArrayList<>(11);

        Casilla casilla = new Casilla(Valor.WHITE + "Parking" , "Especial", 20, banca); //Crear unha casilla
        ladoNorte.add(casilla); //Engadila ao arrayList
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.RED + "Solar12" , "Solar", 21, 1142440, banca);
        ladoNorte.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.WHITE + "Suerte" , "Suerte", 22, banca);
        ladoNorte.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.RED + "Solar13" , "Solar", 23, 1142440, banca);
        ladoNorte.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.RED + "Solar14" , "Solar", 24, 1142440, banca);
        ladoNorte.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.WHITE + "Trans3" ,"Transporte", 25, 1301328.584f , banca);
        ladoNorte.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.BROWN + "Solar15" , "Solar", 26, 1485172 , banca);
        ladoNorte.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.BROWN + "Solar16" , "Solar", 27, 1485172 , banca);
        ladoNorte.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.WHITE + "Serv2" ,"Servicio", 28,975996.438f , banca);
        ladoNorte.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.BROWN + "Solar17" , "Solar", 29, 1485172 , banca);
        ladoNorte.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.WHITE + "IrCarcel" , "Especial", 30, banca);
        ladoNorte.add(casilla);
        banca.anhadirPropiedad(casilla);


        Grupo grupoRed = new Grupo(ladoNorte.get(1), ladoNorte.get(3), ladoNorte.get(4), "RED");
        ladoNorte.get(1).setGrupo(grupoRed);
        ladoNorte.get(3).setGrupo(grupoRed);
        ladoNorte.get(4).setGrupo(grupoRed);

        grupos.put("RED", grupoRed);

        Grupo grupoBrown = new Grupo(ladoNorte.get(6), ladoNorte.get(7),ladoNorte.get(9), "BROWN");
        ladoNorte.get(6).setGrupo(grupoBrown);
        ladoNorte.get(7).setGrupo(grupoBrown);
        ladoNorte.get(9).setGrupo(grupoBrown);


        grupos.put("BROWN", grupoBrown);

        this.posiciones.add(ladoNorte);
    }

    // Lado Este
    private void insertarLadoEste() {
        ArrayList<Casilla> ladoEste = new ArrayList<>(9);

        Casilla casilla = new Casilla(Valor.GREEN + "Solar18", "Solar", 31, 1930723.6f, banca);
        ladoEste.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.GREEN + "Solar19", "Solar", 32, 1930723.6f, banca);
        ladoEste.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.WHITE + "Caja", "Comunidad", 33,  banca);
        ladoEste.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.GREEN + "Solar20", "Solar", 34, 1930723.6f, banca);
        ladoEste.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.WHITE + "Trans4", "Transporte", 35, 1301328.584f, banca);
        ladoEste.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.WHITE + "Suerte", "Suerte", 36, banca);
        ladoEste.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.BLUE + "Solar21", "Solar", 37, 3764911.02f, banca);
        ladoEste.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.WHITE + "Imp2", 38, 650664.292f, banca);
        ladoEste.add(casilla);
        banca.anhadirPropiedad(casilla);

        casilla = new Casilla(Valor.BLUE + "Solar22", "Solar", 39, 3764911.02f, banca);
        ladoEste.add(casilla);
        banca.anhadirPropiedad(casilla);

        Grupo grupoGreen = new Grupo(ladoEste.get(0), ladoEste.get(1), ladoEste.get(3), "GREEN");
        ladoEste.get(0).setGrupo(grupoGreen);
        ladoEste.get(1).setGrupo(grupoGreen);
        ladoEste.get(3).setGrupo(grupoGreen);

        grupos.put("GREEN", grupoGreen);

        Grupo grupoBlue = new Grupo(ladoEste.get(6), ladoEste.get(8), "BLUE");
        ladoEste.get(6).setGrupo(grupoBlue);
        ladoEste.get(8).setGrupo(grupoBlue);

        grupos.put("BLUE", grupoBlue);

        this.posiciones.add(ladoEste);
    }

    // Imprime el tablero
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // lado norte correcto
        sb.append("|");
        for (int i = 0; i < 11; i++) {
            sb.append(String.format("%-10s|", posiciones.get(2).get(i).getNombre()));
        }
        sb.append("\n");

        // lado oeste correcto
        for (int i = 0; i <= 8; i++) { // lado Oeste de abaixo arriba
            sb.append(String.format("|%-12s|", posiciones.get(1).get(i).getNombre()));

            // Espacio central
            for (int j = 0; j < 9; j++) {
                sb.append("        ");
            }

            // Lado este correcto
            sb.append(String.format("|%-10s|\n", posiciones.get(3).get(8 - i).getNombre()));
        }

        // lado sur
        sb.append("|");
        for (int i = 0; i <= 10; i++) {
            sb.append(String.format("%-11s|", posiciones.get(0).get(i).getNombre()));
        }
        sb.append("\n");

        return sb.toString();
    }

    // Devuelve casilla por posición global (0-39)
    public Casilla getCasilla(int posicion) {
        if (posicion < 0 || posicion >= 40) return null;
        int lado = posicion / 10;
        int indice = posicion % 10;
        return posiciones.get(lado).get(indice);
    }

    //Método usado para buscar la casilla con el nombre pasado como argumento:
    public Casilla encontrar_casilla(String nombre) {
        for (int i = 0; i < 40; i++) {
            if (getCasilla(i).getNombre().equals(nombre)) {
                return getCasilla(i);
            }
        }
        return null;
    }


    public ArrayList<Casilla> getCasillas(){
        ArrayList<Casilla> todasCasillas = new ArrayList<>(40);
        for(int i=0;i<40;i++){
            todasCasillas.add(getCasilla(i));
        }
        return todasCasillas;
    }

    public void mostrarTablero() {
        System.out.println(this.toString());
    }
}