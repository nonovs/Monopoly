package monopoly.casillas.propiedades;

import monopoly.casillas.Casilla;
import monopoly.casillas.Grupo;
import partida.Jugador;

import static monopoly.Juego.consola;

public abstract class Propiedad extends Casilla {

    //Atributos exclusivos de propiedades (Solares, Transportes, Servicios)
    protected float valor;
    protected boolean hipotecada;
    protected Grupo grupo;  // Movemos la referencia de grupo aquí
    protected float alquileresGenerados;    // Para estadísticas

    public Propiedad (String nombre, int posicion, float valor, Jugador duenho, Grupo grupo) {
        super(nombre, posicion, duenho);    // Llama al constructor de Casilla (sin valor)
        this.valor = valor;
        this.grupo = grupo;
        this.hipotecada = false;
        this.alquileresGenerados = 0;
    }

    // MÉTODOS OBLIGATORIOS SOLICITADOS EN EL GUIÓN
    public boolean perteneceAJugador (Jugador jugador){
        return this.duenho != null && this.duenho.equals(jugador);
    }

    //Método abstracto: cada tipo de propiedad calcula su alquiler distinto
    public abstract boolean alquiler (Jugador actual, int tirada);

    //Método abstracto solicitado en el guión
    public abstract float valor ();

    public void comprar (Jugador solicitante) {
        if (duenho != null && !duenho.getNombre().equalsIgnoreCase("Banca")){
            consola.imprimir("Esta propiedad ya tiene dueño.");
            return;
        }
        if (solicitante.getPosicion() != this.posicion){
            consola.imprimir("Debes estar en la casilla para poder comprarla.");
            return;
        }
        if (solicitante.getFortuna() < valor) {
            consola.imprimir("No tienes suficiente dinero para comprarla.");
            return;
        }
        solicitante.pagar(valor);
        solicitante.acumularDineroInvertido(valor);
        setDuenho(solicitante);
        solicitante.anhadirPropiedad(this);

        consola.imprimir(String.format("El jugador %s compra la casilla %s por %.0f€. Su fortuna actual es %.0f€.",
                solicitante.getNombre(), this.nombre, valor, solicitante.getFortuna()));
    }

    // GETTERS Y SETTERS COMUNES
    public boolean isHipotecada() { return hipotecada;}

    public void setHipotecada(boolean hipotecada) { this.hipotecada = hipotecada;}

    public Grupo getGrupo() { return grupo;}

    public void setGrupo(Grupo grupo) { this.grupo = grupo;}

    public float getPrecioHipoteca() { return valor / 2;}

    public void sumarAlquileresGenerado(float cantidad) { this.alquileresGenerados += cantidad; }

    public float getAlquileresGenerados() { return alquileresGenerados;}

    @Override
    public String casEnVenta() {
        if (getDuenho() != null && !"Banca".equalsIgnoreCase(getDuenho().getNombre())){
            return "";
        }
        return String.format("{Nombre: %s, valor: %.0f}",nombre, valor);
    }

}
