package monopoly.casillas;

import java.util.ArrayList;

import monopoly.casillas.propiedades.Propiedad;
import partida.Jugador;

public class Grupo {

    // Atributos:
    private String colorGrupo; // Color del grupo
    private ArrayList<Propiedad> propiedades; // Composición solicitada: Lista de PROPIEDADES

    //Constructor vacío.
    public Grupo() {
        this.propiedades = new ArrayList<>();
    }

    //Constructor general.
    public Grupo (String color, Propiedad ... propiedadesIniciales){
        this.colorGrupo = color;
        this.propiedades = new ArrayList<>();
        for (Propiedad p : propiedadesIniciales) {
            anhadirPropiedad(p);
        }
    }

    /// ADAPTACIÓN PARA CONSTRUCTORES ANTIGUOS QUE RECIBÍAN Casilla (esto evita romper el tablero)

    /*Constructor para cuando el grupo está formado por DOS CASILLAS:
    * Requiere como parámetros las dos casillas miembro y el color del grupo.
     */
    public Grupo(monopoly.casillas.Casilla cas1, monopoly.casillas.Casilla cas2, String color) {
        this(color, (Propiedad) cas1, (Propiedad) cas2);
    }

    /*Constructor para cuando el grupo está formado por TRES CASILLAS:
    * Requiere como parámetros las tres casillas miembro y el color del grupo.
     */
    public Grupo(monopoly.casillas.Casilla cas1, monopoly.casillas.Casilla cas2, monopoly.casillas.Casilla cas3, String color) {
        this(color, (Propiedad) cas1, (Propiedad) cas2, (Propiedad) cas3);
    }

    /* Método que añade una casilla al array de casillas miembro de un grupo.
    * Parámetro: casilla que se quiere añadir.
     */
    public void anhadirPropiedad(Propiedad miembro) {
        if (!propiedades.contains(miembro)){
            propiedades.add(miembro);
            //Establecer la relación bidireccional si es necesario
            miembro.setGrupo(this);
        }
    }

    /*Método que comprueba si el jugador pasado tiene en su haber todas las casillas del grupo:
    * Parámetro: jugador que se quiere evaluar.
    * Valor devuelto: true si es  dueño de todas las casillas del grupo, false en otro caso.
     */
    public boolean esDuenhoGrupo(Jugador jugador) {
        if (jugador == null) return false;
        for (Propiedad p : propiedades) {
            if (p.getDuenho() != jugador){
                return false;
            }
        }
        return true;
    }

    public String getColor() { return colorGrupo;}

    public ArrayList<Propiedad> getPropiedades() { return propiedades;}
}
