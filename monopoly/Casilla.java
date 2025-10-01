package monopoly;

import partida.*;
import java.util.ArrayList;


public class Casilla {

    //Atributos:
    private String nombre; //Nombre de la casilla
    private String tipo; //Tipo de casilla (Solar, Especial, Transporte, Servicios, Comunidad, Suerte y Impuesto).
    private float valor; //Valor de esa casilla (en la mayoría será valor de compra, en la casilla parking se usará como el bote).
    private int posicion; //Posición que ocupa la casilla en el tablero (entero entre 1 y 40).
    private Jugador duenho; //Dueño de la casilla (por defecto sería la banca).
    private Grupo grupo; //Grupo al que pertenece la casilla (si es solar).
    private float impuesto; //Cantidad a pagar por caer en la casilla: el alquiler en solares/servicios/transportes o impuestos.
    private float hipoteca; //Valor otorgado por hipotecar una casilla
    private ArrayList<Avatar> avatares; //Avatares que están situados en la casilla.

    //Constructores:
    public Casilla() {
        avatares = new ArrayList<>();
    }//Parámetros vacíos

    /*Constructor para casillas tipo Solar, Servicios o Transporte:
    * Parámetros: nombre casilla, tipo (debe ser solar, serv. o transporte), posición en el tablero, valor y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, float valor, Jugador duenho) {
        this();
        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;
        this.valor = valor;
        this.duenho = duenho;
    }

    /*Constructor utilizado para inicializar las casillas de tipo IMPUESTOS.
    * Parámetros: nombre, posición en el tablero, impuesto establecido y dueño.
     */
    public Casilla(String nombre, int posicion, float impuesto, Jugador duenho) {
        this();
        this.nombre = nombre;
        this.tipo = "Impuesto";
        this.posicion = posicion;
        this.impuesto = impuesto;
        this.duenho = duenho;
    }

    /*Constructor utilizado para crear las otras casillas (Suerte, Caja de comunidad y Especiales):
    * Parámetros: nombre, tipo de la casilla (será uno de los que queda), posición en el tablero y dueño.
     */
    public Casilla(String nombre, String tipo, int posicion, Jugador duenho) {
        this();
        this.nombre = nombre;
        this.tipo = tipo;
        this.posicion = posicion;
        this.duenho = duenho;
    }

    //Método utilizado para añadir un avatar al array de avatares en casilla.
    public void anhadirAvatar(Avatar av) {
        avatares.add(av);
    }

    //Método utilizado para eliminar un avatar del array de avatares en casilla.
    public void eliminarAvatar(Avatar av) {
        avatares.remove(av);
    }

    /*Método para evaluar qué hacer en una casilla concreta. Parámetros:
    * - Jugador cuyo avatar está en esa casilla.
    * - La banca (para ciertas comprobaciones).
    * - El valor de la tirada: para determinar impuesto a pagar en casillas de servicios.
    * Valor devuelto: true en caso de ser solvente (es decir, de cumplir las deudas), y false
    * en caso de no cumplirlas.*/
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        switch (tipo){
            case "Solar":
                if (duenho == banca){
                    System.out.println("El solar " + nombre + "esta en venta por la banca " + valor + "€");
                    return true;
                } else if (duenho != null && duenho != actual){
                    if (actual.getFortuna() >= impuesto){
                        actual.restarFortuna(impuesto);
                        duenho.sumarFortuna(impuesto);
                        System.out.println(actual.getNombre() + "paga " + impuesto + "€ de alquiler a " + duenho.getNombre());
                        return true;
                    } else {
                        System.out.println(actual.getNombre() + " no puede pagar el alquiler ");
                        return false;
                    }
                }
                break;

            case "Servicio":
                if (duenho != banca && duenho != null && duenho != actual){
                    float cantidad = tirada * 4 * 50000; //factor servicio
                    if (actual.getFortuna() >= cantidad) {
                        actual.restarFortuna(cantidad);
                        duenho.sumarFortuna(cantidad);
                        System.out.println(actual.getNombre() + " paga " + cantidad + "€ al dueño del servicio");
                        return true;
                    } else {
                        System.out.println(actual.getNombre() + " no puede pagar el servicio");
                        return false;
                    }
                }
                break;

            case  "Transporte":
                if (duenho != banca && duenho != null && duenho != actual){
                    float cantidad = 250000; //alquiler fijo
                    if (actual.getFortuna() >= cantidad){
                        actual.restarFortuna(cantidad);
                        duenho.sumarFortuna(cantidad);
                        System.out.println(actual.getNombre() + " paga " + cantidad + "€ por transporte");
                        return true;
                    } else {
                        System.out.println(actual.getNombre() + " no puede pagar transporte");
                        return false;
                    }
                }
                break;

            case "Impuesto":
                actual.restarFortuna(impuesto);
                System.out.println(actual.getNombre() + " paga " + impuesto + "€ en impuestos.");
                return true;

            case "IrACarcel":
                System.out.println(actual.getNombre() + " va a la Cárcel.");
                return true;

            case "Parking":
                actual.sumarFortuna(valor);
                System.out.println(actual.getNombre() + " recibe " + valor + "€ del bote.");
                valor = 0; // reinicia el bote
                return true;

            case "Suerte":
                case "Comunidad":
                System.out.println("Casilla de " + tipo + ". (En esta entrega no se realiza ninguna acción).");
                return true;

            default:
                return true;
        }
        return true;
    }

    /*Método usado para comprar una casilla determinada. Parámetros:
    * - Jugador que solicita la compra de la casilla.
    * - Banca del monopoly (es el dueño de las casillas no compradas aún).*/
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        if (duenho == banca && solicitante.getFortuna() >= valor){
            solicitante.restarFortuna(valor);
            this.duenho = solicitante;
            System.out.println(solicitante.getNombre() + " compra la casilla " + nombre + " por " + valor + "€");
        } else {
            System.out.println("No se puede compara la casilla " + nombre);
        }
    }

    /*Método para añadir valor a una casilla. Utilidad:
    * - Sumar valor a la casilla de parking.
    * - Sumar valor a las casillas de solar al no comprarlas tras cuatro vueltas de todos los jugadores.
    * Este método toma como argumento la cantidad a añadir del valor de la casilla.*/
    public void sumarValor(float suma) {
        this.valor += suma;
    }

    /*Método para mostrar información sobre una casilla.
    * Devuelve una cadena con información específica de cada tipo de casilla.*/
    public String infoCasilla() {
        return "{tipo: " + tipo + ", nombre: " + nombre + ", valor: " + valor + ", alquiler: " + impuesto + "}";
    }


    /* Método para mostrar información de una casilla en venta.
     * Valor devuelto: texto con esa información.
     */
    public String casEnVenta() {
        if (duenho != null && duenho.getNombre().equalsIgnoreCase("Banca")){
            return "{tipo: " + tipo + ", nombre: " + nombre + ", valor: " + valor + "}";
        }else {
            return "La casilla " + nombre + "no esta en venta";
        }
    }



        //GETTERS Y SETTERS


    public String getNombre() {
        return nombre;
    }
    public String getTipo() {
        return tipo;
    }
    public int getPosicion() {
        return posicion;
    }
    public Jugador getDuenho() {
        return duenho;
    }
    public void  setDuenho(Jugador duenho) {
        this.duenho = duenho;
    }

}
