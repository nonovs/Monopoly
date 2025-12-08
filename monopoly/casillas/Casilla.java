package monopoly.casillas;

import partida.*;

import java.util.ArrayList;

/** Representa una posición del tablero.
 * Cada casilla puede tener distinto tipo (solar, servicio...) y este determina cómo se comporta cuando se cae en ella.
 * Es la clase base de la jerarquía de casillas.
 */
public abstract class Casilla {

    //Atributos comunes a TODAS las casillas::
    protected String nombre;
    protected int posicion;
    protected Jugador duenho;   // Lo mantenemos aquí porque Especiales e Impuestos también tiene "duenho" (banca
    protected ArrayList<Avatar> avatares;
    protected int vecesVisitada;    // Para el requisito FrecuenciaVisitada

    // Constructor base simplificiado
    public Casilla (String nombre, int posicion, Jugador duenho) {
        this.nombre = nombre;
        this.posicion = posicion;
        this.duenho = duenho;
        this.avatares = new ArrayList<>();
        this.vecesVisitada = 0;
    }

    // MÉTODOS OBLIGATORIOS SOLICITADOS EN EL GUIÓN
    public boolean estaAvatar(Avatar avatar){
        return avatares.contains(avatar);
    }

    public int FrecuenciaVisita(){
        return vecesVisitada;
    }

    @Override
    public String toString() {
        // Formato básico
        return String.format("%s (Posicion: %d)", nombre, posicion);
    }

    // OTROS MÉTODOS COMUNES NECESARIOS
    public String getNombre(){ return nombre;}

    public int getPosicion() { return posicion; }

    public Jugador getDuenho() { return duenho; }

    public void setDuenho(Jugador j) { this.duenho = j; }

    public ArrayList<Avatar> getAvatares() {  return avatares == null ? new ArrayList<>(): new ArrayList<>(avatares);}

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
    public abstract boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada);

    public void incrementarVisitas() {
        vecesVisitada++;
    }

    /**
     * Estos métodos son necesarios para que el menu 'listar enventa' no falle
     * aunque devuelvan cadenas vacías por defecto.
     */

    /* Método para mostrar información de una casilla en venta.
     * Valor devuelto: texto con esa información.
     */
    public String casEnVenta() { return ""; }

    /*Método para mostrar información sobre una casilla.
     * Devuelve una cadena con información específica de cada tipo de casilla.*/
    public String infoCasilla() { return toString(); }


    // Detectar la casilla "Ir a la carcel" por nombre
    public boolean esIrACarcel() {
        if (this.nombre == null) return false;
        // quitar códigos ANSI y normalizar (minúsculas, sin espacios, sin acentos)
        String clean = this.nombre.replaceAll("\\u001B\\[[;\\d]*m", "").toLowerCase().trim();
        String compact = clean.replaceAll("\\s+", "");

        if (compact.equals("carcel")) return false;
        if (compact.contains("iracarcel")) return true;
        if (compact.contains("ir") && compact.contains("carcel")) return true;

        return false;
    }


}