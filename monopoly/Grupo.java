package monopoly;
import monopoly.casillas.*;

import java.util.ArrayList;
import partida.Jugador;

public class Grupo {

    //Atributos
    private ArrayList<Casilla> miembros; //Casillas miembros del grupo.
    private String colorGrupo; //Color del grupo
    private int numCasillas; //Número de casillas del grupo.

    //Atributos definidos por mi (para gestionar alquileres y solares)
    private float alquilerCasa;
    private float alquilerHotel;
    private float alquilerPiscina;
    private float alquilerPista;
    private ArrayList<Solar> solares;
    private ArrayList<Especial>especiales;//Referencia directa a los solares del grupo

    /// Valores para construir edificios
    private float valorCasa;
    private float valorHotel;
    private float valorPiscina;
    private float valorPista;



    //Constructor vacío.
    public Grupo() {
        this.miembros = new ArrayList<>();
        this.solares = new ArrayList<>();
        this.especiales = new ArrayList<>();
    }

    /*Constructor para cuando el grupo está formado por DOS CASILLAS:
    * Requiere como parámetros las dos casillas miembro y el color del grupo.
     */
    public Grupo(Casilla cas1, Casilla cas2, String colorGrupo) {
        this.miembros = new ArrayList<>();
        this.solares = new ArrayList<>();
    
        this.colorGrupo = colorGrupo;
        this.miembros.add(cas1);
        this.miembros.add(cas2);
        numCasillas = 2;
    }

    /*Constructor para cuando el grupo está formado por TRES CASILLAS:
    * Requiere como parámetros las tres casillas miembro y el color del grupo.
     */
    public Grupo(Casilla cas1, Casilla cas2, Casilla cas3, String colorGrupo) {
        
        this.miembros = new ArrayList<>();
        this.solares = new ArrayList<>();
        this.colorGrupo = colorGrupo; 
        this.miembros.add(cas1);
        this.miembros.add(cas2);
        this.miembros.add(cas3);
        numCasillas = 3;
    }

    /* Método que añade una casilla al array de casillas miembro de un grupo.
    * Parámetro: casilla que se quiere añadir.
     */
    public void anhadirCasilla(Casilla miembro) {
        if (!miembros.contains(miembro)){
            miembros.add(miembro);
            numCasillas = miembros.size();
            if (miembro instanceof Solar){
                solares.add((Solar)miembro);
            }
        }
    }

    /*Método que comprueba si el jugador pasado tiene en su haber todas las casillas del grupo:
    * Parámetro: jugador que se quiere evaluar.
    * Valor devuelto: true si es dueño de todas las casillas del grupo, false en otro caso.
     */
    public boolean esDuenhoGrupo(Jugador jugador) {
        if (jugador == null) return false;

        for (Casilla c : miembros) {
            if (c instanceof Solar) {
                Solar s = (Solar) c;
                // Como ya comprobamos que jugador != null, es seguro usar jugador.equals(...)
                if (!jugador.equals(s.getDuenho())) {
                    return false;
                }
            }
        }
        return true;
    }

    //Añadir un solar al grupo
    public String getColor(){
        return colorGrupo;
    }

    public float getAlquilerCasa(){
        return alquilerCasa;
    }
    public float getAlquilerHotel(){
        return alquilerHotel;
    }
    public float getAlquilerPiscina(){
        return alquilerPiscina;
    }
    public float getAlquilerPista(){
        return alquilerPista;
    }

    public ArrayList<Solar> getSolares(){
        return solares;
    }

    public int getNumCasillas(){
        return numCasillas;
    }

    //Permite ajustar las tarifas de alquiler por tipo
    public void setTarifas(float casa, float hotel, float piscina, float pista){
        this.alquilerCasa = casa;
        this.alquilerHotel = hotel;
        this.alquilerPiscina = piscina;
        this.alquilerPista = pista;
    }
    public Float getValorCasa(){ return valorCasa;}
    public Float getValorHotel(){ return valorHotel;}
    public Float getValorPiscina(){ return valorPiscina;}
    public Float getValorPista(){ return valorPista;}
    public void setValorConstruccion(float casa, float hotel, float piscina, float pista){
        this.valorCasa = casa;
        this.valorHotel = hotel;
        this.valorPiscina = piscina;
        this.valorPista = pista;
    }
    public String toString(){
        return String.format("Grupo %s (%d solares)", colorGrupo, miembros.size());
    }

}
