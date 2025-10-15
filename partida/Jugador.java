package partida;

import java.util.ArrayList;

import monopoly.casillas.Casilla;


public class Jugador {

    //Atributos:
    private String nombre; //Nombre del jugador
    private Avatar avatar; //Avatar que tiene en la partida.
    private float fortuna; //Dinero que posee.
    private float gastos; //Gastos realizados a lo largo del juego.
    private boolean enCarcel; //Será true si el jugador está en la carcel
    private int tiradasCarcel; //Cuando está en la carcel, contará las tiradas sin éxito que ha hecho allí para intentar salir (se usa para limitar el numero de intentos).
    private int vueltas; //Cuenta las vueltas dadas al tablero.
    private ArrayList<Casilla> propiedades; //Propiedades que posee el jugador.

    //Atributos definidos por mi
    private int posicion;
    private int turnosEnCarcel;
    private ArrayList<Casilla> hipotecadas;
    private Object casilla;


    //Constructor vacío. Se usará para crear la banca.
    public Jugador() {
        this.nombre="Banca";
        this.fortuna= Float.POSITIVE_INFINITY;//NO hay limite de dinero
        this.gastos= 0;
        this.tiradasCarcel=0;
        this.vueltas=0;
        this.enCarcel=false;
        this.propiedades=new ArrayList();
        this.posicion=0;
        this.hipotecadas=new ArrayList();
        this.turnosEnCarcel=0;
        this.avatar=null;

    }

    /*Constructor principal. Requiere parámetros:
    * Nombre del jugador, tipo del avatar que tendrá, casilla en la que empezará y ArrayList de
    * avatares creados (usado para dos propósitos: evitar que dos jugadores tengan el mismo nombre y
    * que dos avatares tengan mismo ID). Desde este constructor también se crea el avatar.
     */
    public Jugador(String nombre, String tipoAvatar, Casilla inicio, ArrayList<Avatar> avCreados) {
        this.nombre=nombre;
        this.fortuna= 15000000;
        this.gastos= 0;
        this.tiradasCarcel=0;
        this.enCarcel=false;
        this.vueltas=0;
        this.propiedades=new ArrayList();
        this.hipotecadas=new ArrayList();
        this.posicion= inicio != null ? inicio.getPosicion() : 0;
        this.turnosEnCarcel=0;
        this.avatar=null;

    }

    //Otros métodos:
    //Método para añadir una propiedad al jugador. Como parámetro, la casilla a añadir.
    public void anhadirPropiedad(Casilla casilla) {
        if(!propiedades.contains(casilla)){
            propiedades.add(casilla);
        }
    }

    //Método para eliminar una propiedad del arraylist de propiedades de jugador.
    public void eliminarPropiedad(Casilla casilla) {
        propiedades.remove(casilla);
        hipotecadas.remove(casilla);
    }

    //Método para añadir fortuna a un jugador
    //Como parámetro se pide el valor a añadir. Si hay que restar fortuna, se pasaría un valor negativo.
    public void sumarFortuna(float valor) {

        this.fortuna+=valor;
    }

    //Método para sumar gastos a un jugador.
    //Parámetro: valor a añadir a los gastos del jugador (será el precio de un solar, impuestos pagados...).
    public void sumarGastos(float valor) {
        this.gastos+=valor;
    }

    /*Método para establecer al jugador en la cárcel. 
    * Se requiere disponer de las casillas del tablero para ello (por eso se pasan como parámetro).*/
    public void encarcelar(ArrayList<ArrayList<Casilla>> pos) {
        this.enCarcel=true;
        this.tiradasCarcel=0;
        this.turnosEnCarcel=0;
        this.posicion=10;//Posicion da carcel

    }

    public void pagar(float cantidad){
        fortuna -= cantidad;
        sumarGastos(cantidad);
    }

    public float getFortuna(){
        return fortuna;
    }

    public void recibir(float cantidad){
        sumarFortuna(cantidad);
    }

    public void enviarACarcel(){
        this.avatar.getLugar().eliminarAvatar(this.avatar);
        this.avatar.setLugar(carcel);

        this.enCarcel = true;
        this.posicion = 10;
        this.turnosEnCarcel +=1;
        System.out.println(nombre + "  enviando a la Carcel");
        this.avatar.getLugar().anhadirAvatar(this.avatar);
    }
    public void salirDeCarcel(){
        this.enCarcel = false;
        this.turnosEnCarcel = 0;
        this.tiradasCarcel=0;
        System.out.println(nombre + "  saliando a la Carcel");
    }
    public String getNombre(){
        return nombre;
    }

    public boolean tieneHipoteca(Casilla c){
        return hipotecadas.contains(c);
    }
    public void hipotecarPropiedad(Casilla c){
        if(propiedades.contains(c)){
            hipotecadas.add(c);
            this.fortuna+=c.getHipoteca();
            System.out.println(nombre + "  ha hipotecado " + c.getNombre() + "por " +c.getHipoteca());
        }
    }

    public void deshipotecarPropiedad(Casilla c){
        if(propiedades.contains(c)){
            hipotecadas.remove(c);
            this.fortuna-=c.getHipoteca();
            System.out.println(nombre + "  deshipotecado " + c.getNombre() + "por " +c.getHipoteca());
        }
    }

    public void setAvatar(Avatar nuevoAvatar) {
        this.avatar = nuevoAvatar;

    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setFortuna(float fortuna) {
        this.fortuna = fortuna;
    }

    public float getGastos() {
        return gastos;
    }

    public void setGastos(float gastos) {
        this.gastos = gastos;
    }

    public boolean isEnCarcel() {
        return enCarcel;
    }

    public void setEnCarcel(boolean enCarcel) {
        this.enCarcel = enCarcel;
    }

    public int getTiradasCarcel() {
        return tiradasCarcel;
    }

    public void setTiradasCarcel(int tiradasCarcel) {
        this.tiradasCarcel = tiradasCarcel;
    }

    public int getVueltas() {
        return vueltas;
    }

    public void setVueltas(int vueltas) {
        this.vueltas = vueltas;
    }

    public ArrayList<Casilla> getPropiedades() {
        return propiedades;
    }

    public void setPropiedades(ArrayList<Casilla> propiedades) {
        this.propiedades = propiedades;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int getTurnosEnCarcel() {
        return turnosEnCarcel;
    }

    public void setTurnosEnCarcel(int turnosEnCarcel) {
        this.turnosEnCarcel = turnosEnCarcel;
    }

    public ArrayList<Casilla> getHipotecadas() {
        return hipotecadas;
    }

    public void setHipotecadas(ArrayList<Casilla> hipotecadas) {
        this.hipotecadas = hipotecadas;
    }

    public Object getCasilla() {
        return casilla;
    }

    public void setCasilla(Object casilla) {
        this.casilla = casilla;
    }
}
