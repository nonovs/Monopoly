package partida;

import java.util.ArrayList;

import monopoly.casillas.Casilla;
import monopoly.casillas.Solar;


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
    private float dineroInvertido;
    private float pagoTasasEImpuestos;
    private float pagoDeAlquileres;
    private float cobroDeAlquileres;
    private float pasarPorCasillaDeSalida;
    private float premiosInversionesOBote;
    private int vecesEnLaCarcel;



    //Atributos definidos por mi
    private int posicion;
    private int turnosEnCarcel;
    private ArrayList<Casilla> hipotecadas;


    public void acumularDineroInvertido(float c) { sumarEstadistica("dineroInvertido", c); }
    public void acumularPagoTasasEImpuestos(float c) { sumarEstadistica("pagoTasasEImpuestos", c); }
    public void acumularPagoDeAlquileres(float c) { sumarEstadistica("pagoDeAlquileres", c); }
    public void acumularCobroDeAlquileres(float c) { sumarEstadistica("cobroDeAlquileres", c); }
    public void acumularPasarPorSalida(float c) { sumarEstadistica("pasarPorCasillaDeSalida", c); }
    public void acumularPremiosInversionesOBote(float c) { sumarEstadistica("premiosInversionesOBote", c); }
    public void incrementarVecesEnLaCarcel() { vecesEnLaCarcel++; }

    //Constructor vacío. Se usará para crear la banca.
    public Jugador() {
        this.nombre="Banca";
        this.fortuna= Float.POSITIVE_INFINITY;//NO hay limite de dinero
        this.gastos= 0;
        this.tiradasCarcel=0;
        this.vueltas=0;
        this.enCarcel=false;
        this.propiedades=new ArrayList<Casilla>();
        this.posicion=0;
        this.hipotecadas=new ArrayList<Casilla>();
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
        this.propiedades=new ArrayList<>();
        this.hipotecadas=new ArrayList<>();
        this.posicion= inicio != null ? inicio.getPosicion() : 0;
        this.turnosEnCarcel=0;
        this.avatar=new Avatar(tipoAvatar,this,inicio,avCreados);

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



    public void pagar(float cantidad){
        fortuna -= cantidad;
        sumarGastos(cantidad);
    }

    public float getFortuna(){
        return fortuna;
    }

    //Nuevos getters de estadísticas
    public float getDineroInvertido() { return dineroInvertido; }
    public float getPagoTasasEImpuestos() { return pagoTasasEImpuestos; }
    public float getPagoDeAlquileres() { return pagoDeAlquileres; }
    public float getCobroDeAlquileres() { return cobroDeAlquileres; }
    public float getPasarPorCasillaDeSalida() { return pasarPorCasillaDeSalida; }
    public float getPremiosInversionesOBote() { return premiosInversionesOBote; }
    public int getVecesEnLaCarcel() { return vecesEnLaCarcel; }

    public void recibir(float cantidad){
        sumarFortuna(cantidad);
    }

    public void enviarACarcel(Casilla casillaCarcel){
        if (this.avatar != null && this.avatar.getLugar()!=null) {
            this.avatar.getLugar().eliminarAvatar(this.avatar);
        }
        // mover avatar a carcel si existe
        if (this.avatar != null) {
            this.avatar.setLugar(casillaCarcel);
            casillaCarcel.anhadirAvatar(this.avatar);
        }
        this.enCarcel = true;
        this.posicion = casillaCarcel.getPosicion();
        this.turnosEnCarcel = 0;   // reiniciar turnos en carcel al entrar
        this.tiradasCarcel = 0;    // reiniciar contador de tiradas en carcel
        this.incrementarVecesEnLaCarcel();
        System.out.println(nombre + " ha sido enviado a carcel");
    }
    public void salirDeCarcel(){
        this.enCarcel = false;
        this.turnosEnCarcel = 0;
        this.tiradasCarcel=0;
        System.out.println(nombre + "  sale de la Carcel");
    }
    @Override//Esto método para que me funcione ben listar jugadores
    public String toString() {
        return "{nombre: " + nombre + ", avatar: " + (avatar != null ? avatar.getId() : "null") + "}";
    }

    public String getNombre(){
        return nombre;
    }

    public void hipotecarPropiedad(Casilla c){
        if (!propiedades.contains(c)) {
            System.out.println(nombre + " no puede hipotecar " + c.getNombre() + ". No es una propiedad que le pertenece.");
            return;
        }
        if (c.isHipotecada()) {
            System.out.println(nombre + " no puede hipotecar " + c.getNombre() + ". Ya está hipotecada.");
            return;
        }
        if (c instanceof Solar) {
            Solar s = (Solar) c;
            if (!s.getEdificaciones().isEmpty()) {
                System.out.println(nombre + " no puede hipotecar " + s.getNombre() + ". Debe vender los edificios primero.");
                return;
            }
        }
        float cantidad = c.getPrecioHipoteca();
        fortuna += cantidad;

        c.setHipotecada(true);
        hipotecadas.add(c);
        propiedades.remove(c);

        System.out.println(nombre + " recibe " + String.format("%.0f€", cantidad) + " por la hipoteca de " + c.getNombre() +
                ". No puede recibir alquileres ni edificar en el grupo " + c.getGrupo().getColor() + ".");
    }

    public void deshipotecarPropiedad(Casilla c){
        if (!c.getDuenho().equals(this)) {
            System.out.println(nombre + " no puede deshipotecar " + c.getNombre() + ". No es una propiedad que le pertenece.");
            return;
        }
        if (!(c instanceof Solar)) {
            System.out.println(nombre + " no puede deshipotecar " + c.getNombre() + ". Solo los solares son hipotecables.");
            return;
        }
        Solar s = (Solar) c;
        if (!hipotecadas.contains(c)) {
            System.out.println(nombre + " no puede deshipotecar " + s.getNombre() + ". No está hipotecada.");
            return;
        }
        double cantidad = s.getPrecioHipoteca();
        if (fortuna < cantidad) {
            System.out.println(nombre + " no tiene suficiente dinero para deshipotecar " + s.getNombre() + ".");
            return;
        }
        fortuna -= cantidad;
        s.setHipotecada(false);
        propiedades.add(c);
        hipotecadas.remove(c);

        System.out.println(nombre + " paga " + String.format("%.0f€", cantidad) + " por deshipotecar " + c.getNombre() +
                ". Ahora puede recibir alquileres y edificar en el grupo " + c.getGrupo().getColor() + ".");
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
    public void setVueltas() {
        this.vueltas++;
    }
    public ArrayList<Casilla> getPropiedades() {
        return propiedades;
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

    public ArrayList<Casilla> getHipotecadas() {
        return hipotecadas;
    }

    private void sumarEstadistica(String campo, float cantidad) {
    if (cantidad <= 0) return;
        switch (campo) {
            case "dineroInvertido": dineroInvertido += cantidad; break;
            case "pagoTasasEImpuestos": pagoTasasEImpuestos += cantidad; break;
            case "pagoDeAlquileres": pagoDeAlquileres += cantidad; break;
            case "cobroDeAlquileres": cobroDeAlquileres += cantidad; break;
            case "pasarPorCasillaDeSalida": pasarPorCasillaDeSalida += cantidad; break;
            case "premiosInversionesOBote": premiosInversionesOBote += cantidad; break;
        }
    }




}
