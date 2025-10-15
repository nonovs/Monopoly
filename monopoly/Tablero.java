package monopoly;

import monopoly.casillas.Casilla;
import partida.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Tablero {
    // Atributos
    private ArrayList<ArrayList<Casilla>> posiciones; // 4 lados del tablero
    private HashMap<String, Grupo> grupos; // Grupos por color
    private Jugador banca;

    // Constructor
    public Tablero(Jugador banca) {
        this.banca = banca;
        this.grupos = new HashMap<>();
        this.posiciones = new ArrayList<>();
        generarCasillas();
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
        ArrayList<Casilla> ladoSur = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            // Ejemplo: colores alternos según posición
            String color = (i % 2 == 0) ? Valor.RED : Valor.GREEN;
            ladoSur.add(new Casilla(color + "Sur " + i + Valor.RESET));
        }
        posiciones.add(ladoSur);
    }

    // Lado Oeste
    private void insertarLadoOeste() {
        ArrayList<Casilla> ladoOeste = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String color = (i % 2 == 0) ? Valor.BLUE : Valor.YELLOW;
            ladoOeste.add(new Casilla(color + "Oeste " + i + Valor.RESET));
        }
        posiciones.add(ladoOeste);
    }

    // Lado Norte
    private void insertarLadoNorte() {
        ArrayList<Casilla> ladoNorte = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String color = (i % 2 == 0) ? Valor.CYAN : Valor.PURPLE;
            ladoNorte.add(new Casilla(color + "Norte " + i + Valor.RESET));
        }
        posiciones.add(ladoNorte);
    }

    // Lado Este
    private void insertarLadoEste() {
        ArrayList<Casilla> ladoEste = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            String color = (i % 2 == 0) ? Valor.BLACK : Valor.WHITE;
            ladoEste.add(new Casilla(color + "Este " + i + Valor.RESET));
        }
        posiciones.add(ladoEste);
    }

    // Imprime el tablero
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // Sur (izquierda a derecha)
        for (Casilla c : posiciones.get(0)) {
            sb.append(String.format("|%-15s", c.getNombre()));
        }
        sb.append("\n");

        // Lados Este/Oeste (centro del tablero)
        for (int i = 1; i < 9; i++) {
            sb.append(String.format("|%-15s", posiciones.get(1).get(9 - i).getNombre())); // Oeste arriba-abajo
            for (int j = 0; j < 8; j++) {
                sb.append(String.format("%-15s", " ")); // Espacio central
            }
            sb.append(String.format("|%-15s", posiciones.get(3).get(i).getNombre())); // Este arriba-abajo
            sb.append("\n");
        }

        // Norte (derecha a izquierda)
        for (int i = 9; i >= 0; i--) {
            sb.append(String.format("|%-15s", posiciones.get(2).get(i).getNombre()));
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


    public void mostrarTablero() {
        System.out.println(this.toString());
    }
}
