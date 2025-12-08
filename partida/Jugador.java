package partida;

import java.util.ArrayList;

import excepciones.Excepcion;
import excepciones.accionNoValida.FondosInsuficientesException;
import excepciones.accionNoValida.PropiedadNoHipotecadaException;
import excepciones.accionNoValida.PropiedadYaHipotecadaException;
import excepciones.objetoNoExiste.PropiedadNoExisteException;

// Importamos Juego para poder acceder a la consola, pero lo usamos dentro del wrapper
import monopoly.Juego;
import monopoly.casillas.Casilla;
import monopoly.casillas.propiedades.Propiedad;
import monopoly.casillas.propiedades.Solar;

public class Jugador {

    //Atributos:
    private String nombre;
    private Avatar avatar;
    private float fortuna;
    private float gastos;
    private boolean enCarcel;
    private int tiradasCarcel;
    private int vueltas;

    // Ahora guardamos Propiedades, no Casillas genéricas
    private ArrayList<Propiedad> propiedades;

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

    // Ahora guardamos Propiedades
    private ArrayList<Propiedad> hipotecadas;

    // Métodos de acumulación de estadísticas
    public void acumularDineroInvertido(float c) { sumarEstadistica("dineroInvertido", c); }
    public void acumularPagoTasasEImpuestos(float c) { sumarEstadistica("pagoTasasEImpuestos", c); }
    public void acumularPagoDeAlquileres(float c) { sumarEstadistica("pagoDeAlquileres", c); }
    public void acumularCobroDeAlquileres(float c) { sumarEstadistica("cobroDeAlquileres", c); }
    public void acumularPasarPorSalida(float c) { sumarEstadistica("pasarPorCasillaDeSalida", c); }
    public void acumularPremiosInversionesOBote(float c) { sumarEstadistica("premiosInversionesOBote", c); }
    public void incrementarVecesEnLaCarcel() { vecesEnLaCarcel++; }

    //Constructor vacío (Banca)
    public Jugador() {
        this.nombre="Banca";
        this.fortuna= Float.POSITIVE_INFINITY;
        this.gastos= 0;
        this.tiradasCarcel=0;
        this.vueltas=0;
        this.enCarcel=false;
        this.propiedades=new ArrayList<>();
        this.posicion=0;
        this.hipotecadas=new ArrayList<>();
        this.turnosEnCarcel=0;
        this.avatar=null;
    }

    // Constructor Jugador
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

    // OTROS METODOS

    // Ahora recibe propiedad
    public void anhadirPropiedad(Propiedad casilla) {
        if(!propiedades.contains(casilla)){
            propiedades.add(casilla);
        }
    }
    // Ahora recibe propiedad
    public void eliminarPropiedad(Propiedad casilla) {
        propiedades.remove(casilla);
        hipotecadas.remove(casilla);
    }

    public void sumarFortuna(float valor) {
        this.fortuna+=valor;
    }

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

    // Getters de estadísticas
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
        if (this.avatar != null) {
            this.avatar.setLugar(casillaCarcel);
            casillaCarcel.anhadirAvatar(this.avatar);
        }
        this.enCarcel = true;
        this.posicion = casillaCarcel.getPosicion();
        this.turnosEnCarcel = 0;
        this.tiradasCarcel = 0;
        this.incrementarVecesEnLaCarcel();

        // USO DEL NUEVO MÉTODO CORTO
        imprimir(nombre + " ha sido enviado a la cárcel.");
    }

    public void salirDeCarcel(){
        this.enCarcel = false;
        this.turnosEnCarcel = 0;
        this.tiradasCarcel=0;

        // USO DEL NUEVO MÉTODO CORTO
        imprimir(nombre + " sale de la cárcel.");
    }

    @Override
    public String toString() {
        return "{nombre: " + nombre + ", avatar: " + (avatar != null ? avatar.getId() : "null") + "}";
    }

    public String getNombre(){
        return nombre;
    }

    // Ahora recibe Propiedad
    public void hipotecarPropiedad(Propiedad c) throws Excepcion {
        if (!propiedades.contains(c)) {
            throw new PropiedadNoExisteException(
                    nombre + " no puede hipotecar " + c.getNombre() + ". No es una propiedad que le pertenece."
            );
        }
        if (c.isHipotecada()) {
            throw new PropiedadYaHipotecadaException(
                    nombre + " no puede hipotecar " + c.getNombre() + ". Ya esta hipotecada."
            );
        }
        if (c instanceof Solar) {
            Solar s = (Solar) c;
            if (!s.getEdificaciones().isEmpty()) {
                imprimir(nombre + " no puede hipotecar " + s.getNombre() +
                        ". Debe vender los edificios primero.");
                return;
            }
        }
        float cantidad = c.getPrecioHipoteca();
        fortuna += cantidad;

        c.setHipotecada(true);
        hipotecadas.add(c);
        propiedades.remove(c);

        imprimir(String.format(
                "%s recibe %.0f€ por la hipoteca de %s. No puede recibir alquileres ni edificar en el grupo %s.",
                nombre, cantidad, c.getNombre(),
                c.getGrupo() != null ? c.getGrupo().getColor() : "-"
        ));
    }

    // Ahora recibe Propiedad
    public void deshipotecarPropiedad(Propiedad c) throws Excepcion {
        if (!c.getDuenho().equals(this)) {
            throw new PropiedadNoExisteException(
                    nombre + " no puede deshipotecar " + c.getNombre() + ". No es una propiedad que le pertenece."
            );
        }
        if (!hipotecadas.contains(c)) {
            throw new PropiedadNoHipotecadaException(
                    nombre + " no puede deshipotecar " + c.getNombre() + ". No esta hipotecada."
            );
        }
        if (!(c instanceof Solar)) {
            throw new PropiedadNoHipotecadaException(
                    nombre + " no puede deshipotecar " + c.getNombre() + ". Solo los solares son hipotecables."
            );
        }

        Solar s = (Solar) c;
        double cantidad = s.getPrecioHipoteca();
        if (fortuna < cantidad) {
            throw new FondosInsuficientesException(
                    nombre + " no tiene suficiente dinero para deshipotecar " + s.getNombre() + "."
            );
        }
        fortuna -= cantidad;
        s.setHipotecada(false);
        propiedades.add(c);
        hipotecadas.remove(c);

        imprimir(String.format(
                "%s paga %.0f€ por deshipotecar %s. Ahora puede recibir alquileres y edificar en el grupo %s.",
                nombre, cantidad, c.getNombre(),
                c.getGrupo() != null ? c.getGrupo().getColor() : "-"
        ));
    }

    // Getters y Setters restantes
    public void setAvatar(Avatar nuevoAvatar) { this.avatar = nuevoAvatar; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Avatar getAvatar() { return avatar; }
    public void setFortuna(float fortuna) { this.fortuna = fortuna; }
    public float getGastos() { return gastos; }
    public void setGastos(float gastos) { this.gastos = gastos; }
    public boolean isEnCarcel() { return enCarcel; }
    public void setEnCarcel(boolean enCarcel) { this.enCarcel = enCarcel; }
    public int getTiradasCarcel() { return tiradasCarcel; }
    public void setTiradasCarcel(int tiradasCarcel) { this.tiradasCarcel = tiradasCarcel; }
    public int getVueltas() { return vueltas; }
    public void setVueltas() { this.vueltas++; }
    public ArrayList<Propiedad> getPropiedades() { return propiedades; }
    public int getPosicion() { return posicion; }
    public void setPosicion(int posicion) { this.posicion = posicion; }
    public int getTurnosEnCarcel() { return turnosEnCarcel; }
    public ArrayList<Propiedad> getHipotecadas() { return hipotecadas; }

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

    // ================================================================
    //   MÉTODOS AUXILIARES PARA ACCESO A CONSOLA
    // ================================================================
    /**
     * Método auxiliar para imprimir mensajes usando la interfaz Consola del Juego.
     * Evita tener que escribir Juego.consola.imprimir() cada vez.
     */
    private void imprimir(String mensaje) {
        Juego.consola.imprimir(mensaje);
    }
}