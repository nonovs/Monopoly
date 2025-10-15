package partida;

import monopoly.casillas.Casilla;
import monopoly.Tablero;
import java.security.PrivilegedActionException;
import java.util.ArrayList;


public class Avatar {

    //Atributos
    private String id; //Identificador: una letra generada aleatoriamente.
    private String tipo; //Sombrero, Esfinge, Pelota, Coche
    private Jugador jugador; //Un jugador al que pertenece ese avatar.
    private Casilla lugar; //Los avatares se sitúan en casillas del tablero.

    //Constructor vacío
    //public Avatar() {
    //}

    /*Constructor principal. Requiere éstos parámetros:
    * Tipo del avatar, jugador al que pertenece, lugar en el que estará ubicado, y un arraylist con los
    * avatares creados (usado para crear un ID distinto del de los demás avatares).
     */
    public Avatar(String tipo, Jugador jugador, Casilla lugar, ArrayList<Avatar> avCreados) {
        this.tipo = tipo;
        this.jugador = jugador;
        this.lugar = lugar;
        generarId(avCreados);
        avCreados.add(this);

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Casilla getLugar() {
        return lugar;
    }

    public void setLugar(Casilla lugar) {
        this.lugar = lugar;
    }

    //A continuación, tenemos otros métodos útiles para el desarrollo del juego.
    /*Método que permite mover a un avatar a una casilla concreta. Parámetros:
    * - Un array con las casillas del tablero. Se trata de un arrayList de arrayList de casillas (uno por lado).
    * - Un entero que indica el numero de casillas a moverse (será el valor sacado en la tirada de los dados).
    * EN ESTA VERSIÓN SUPONEMOS QUE valorTirada siemrpe es positivo.
     */
    public void moverAvatar(ArrayList<ArrayList<Casilla>> casillas, int valorTirada) {
        ArrayList<Casilla> todasCasillas = new ArrayList<>();
        for (ArrayList<Casilla>  lado: casillas) {
            todasCasillas.addAll(lado);;

        }
        int posActual = todasCasillas.indexOf(this.lugar);
        int nuevaPosicion = (posActual + valorTirada)% todasCasillas.size();//%todas para aseguranos que non se sale do rango de casillas
        if (this.lugar != null) {//Quitar avatar da casilla  actual
            this.lugar.eliminarAvatar(this);
        }
        this.lugar = todasCasillas.get(nuevaPosicion);
        this.lugar.anhadirAvatar(this);

        if (this.jugador != null) {
            this.jugador.setPosicion(nuevaPosicion); //ACABAR CLASE JUGADOR
        }

        table
    }

    /*Método que permite generar un ID para un avatar. Sólo lo usamos en esta clase (por ello es privado).
    * El ID generado será una letra mayúscula. Parámetros:
    * - Un arraylist de los avatares ya creados, con el objetivo de evitar que se generen dos ID iguales.
     */
    private void generarId(ArrayList<Avatar> avCreados) {
        for(char c= 'A'; c <= 'Z'; c++){
            boolean idUsada= false;
            for(Avatar avatar : avCreados){
                if (avatar.getId() !=  null && avatar.getId().equals(c)) {
                    idUsada = true;
                    break;
                }
            }if(!idUsada){
                this.id= String.valueOf(c);
                return;
            }
        }
        throw new PrivilegedActionException("No hay mas identificadores de avatares")
    }

}
