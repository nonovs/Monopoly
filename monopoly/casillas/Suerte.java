package monopoly.casillas;

public class Suerte extends Casilla {

    public Suerte(String nombre, int posicion, Jugador duenho) {
        super(nombre, "Suerte", posicion, duenho);
    }

    @Override
    public String infoCasilla() {
        return "{tipo: Suerte, nombre: " + getNombre() + "}";
    }
}