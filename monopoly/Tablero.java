package monopoly;

import monopoly.casillas.Casilla;
import partida.*;
import java.util.ArrayList;
import java.util.HashMap;


public class Tablero {
    //Atributos.
    private ArrayList<ArrayList<Casilla>> posiciones; //Posiciones del tablero: se define como un arraylist de arraylists de casillas (uno por cada lado del tablero).
    private HashMap<String, Grupo> grupos; //Grupos del tablero, almacenados como un HashMap con clave String (será el color del grupo).
    private Jugador banca; //Un jugador que será la banca.

    //Constructor: únicamente le pasamos el jugador banca (que se creará desde el menú).
    public Tablero(Jugador banca) {
        this.banca = banca;
        this.grupos = new HashMap<>();
        this.posiciones = new ArrayList<>();
        generarCasillas();
    }

    
    //Método para crear todas las casillas del tablero. Formado a su vez por cuatro métodos (1/lado).
    private void generarCasillas() {
        this.insertarLadoSur();
        this.insertarLadoOeste();
        this.insertarLadoNorte();
        this.insertarLadoEste();
    }
    
    //Método para insertar las casillas del lado norte.
    private void insertarLadoNorte() {
        ArrayList<Casilla> ladoNorte = new ArrayList<>();
        //ladoNorte.add(new Especial())
    }

    //Método para insertar las casillas del lado sur.
    private void insertarLadoSur() {
        ArrayList<Casilla> ladoSur = new ArrayList<>();
    }

    //Método que inserta casillas del lado oeste.
    private void insertarLadoOeste() {
        ArrayList<Casilla> ladoOeste = new ArrayList<>();
    }

    //Método que inserta las casillas del lado este.
    private void insertarLadoEste() {
        ArrayList<Casilla> ladoEste = new ArrayList<>();
    }

    //Para imprimir el tablero, modificamos el método toString().
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        //Imprime sur (de izquierda a derecha)
        for (Casilla c: posiciones.get(0)) {
            sb.append(String.format("|%-10s", c.getNombre()));
            sb.append("|\n");
        }
        for (int i=1;i<9;i++){
            sb.append(String.format("|%-10s",posiciones.get(1).get(9-i).getNombre()));//Oeste de abaixo arriba
            for (int j=0;j<8;j++){
                sb.append("     ");
                sb.append(String.format("|%-10s",posiciones.get(3).get(i).getNombre()));//este de arriba abajo
            }
        }
        for (int i=9; i>=0;i--){
            sb.append(String.format("|%-10s",posiciones.get(2).get(i).getNombre()));
            sb.append("|\n");
        }
        return sb.toString();
    }
    // Metodo que me devolve a casilla por posicion
    public Casilla getCasilla(int posicion){
        if (posicion >= 0 && posicion < 40){
            return this.posiciones.get(posicion/10).get(posicion%10);// Posicion/10 indica que ala del tablero usar 0-9 son el sur etc
        }//%10 indica la posicion dentro de ese lado
        else return null;
    }















    //Método usado para buscar la casilla con el nombre pasado como argumento:
    public Casilla encontrar_casilla(String nombre){
        int i;
        for (i=0;i<40;i++){
            if(getCasilla(i).getNombre().equals(nombre)){
                return getCasilla(i);
            }
        }
        return  null;
    }
}
