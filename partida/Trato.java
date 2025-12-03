package partida;
import monopoly.Excepciones.TratoException;
import monopoly.casillas.Casilla;
public class Trato {
    private Jugador jugador1;
    private Jugador jugador2;
    private Casilla casillajugador1;
    private Casilla casillajugador2;
    private float dineroJugador1;
    private float dineroJugador2;
    private String id;
    private static int contador=0;

    //LOGICA GENERAL JUGADOR 1 LE PROPONE TRATO A JUGADOR 2

    //Comenzo creando o constructor para CAMBIO PROPIEDAD POR PROPIEDAD
    // Constructor para CAMBIO PROPIEDAD POR PROPIEDAD
    public Trato(Jugador jugador1, Jugador jugador2, Casilla casillajugador1, Casilla casillajugador2) {
        this.id = generarId();
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.casillajugador1 = casillajugador1;
        this.casillajugador2 = casillajugador2;
        this.dineroJugador1 = 0;
        this.dineroJugador2 = 0;
    }

    // Constructor para CAMBIO PROPIEDAD POR DINERO
    public Trato(Jugador jugador1, Jugador jugador2, Casilla casillajugador1, float dineroJugador2) {
        this.id = generarId();
        this. jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.casillajugador1 = casillajugador1;
        this.casillajugador2 = null;
        this. dineroJugador1 = 0;
        this.dineroJugador2 = dineroJugador2;
    }

    // Constructor para CAMBIO DINERO POR PROPIEDAD
    public Trato(Jugador jugador1, Jugador jugador2, float dineroJugador1, Casilla casillajugador2) {
        this.id = generarId();
        this. jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.casillajugador1 = null;
        this. casillajugador2 = casillajugador2;
        this.dineroJugador1 = dineroJugador1;
        this.dineroJugador2 = 0;
    }

    // Constructor para cambiar PROPIEDAD Y DINERO POR PROPIEDAD
    public Trato(Jugador jugador1, Jugador jugador2, Casilla casillajugador1, float dineroJugador1, Casilla casillajugador2) {
        this.id = generarId();
        this.jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.casillajugador1 = casillajugador1;
        this.casillajugador2 = casillajugador2;
        this.dineroJugador1 = dineroJugador1;
        this.dineroJugador2 = 0;
    }

    // Constructor para cambiar PROPIEDAD POR PROPIEDAD Y DINERO
    public Trato(Jugador jugador1, Jugador jugador2, Casilla casillajugador1, Casilla casillajugador2, float dineroJugador2) {
        this.id = generarId();
        this. jugador1 = jugador1;
        this.jugador2 = jugador2;
        this.casillajugador1 = casillajugador1;
        this.casillajugador2 = casillajugador2;
        this. dineroJugador1 = 0;
        this.dineroJugador2 = dineroJugador2;
    }


    private String generarId() {
        contador++;
        return "trato" + contador;
    }


    /*
     * Metodo para validar si se puede realizar el trato
     *
     * */
    public void validarTrato() throws TratoException {
        //valido si la propiedad del jugador 1 le pertenece
        if (casillajugador1 != null && !jugador1.getPropiedades().contains(casillajugador1)) {
            throw new TratoException("El jugador 1 no posee la propiedad ofrecida");
        }

        //valido si la propiedad del jugador 2 le pertenece
        if (casillajugador2 != null && !jugador2.getPropiedades().contains(casillajugador2)) {
            throw new TratoException("El jugador 2 no posee la propiedad ofrecida");
        }

        //Compruebo que jugador 1 tenga dinero suficiente
        if (dineroJugador1 > 0 && jugador1.getFortuna() < dineroJugador1) {
            throw new TratoException("El jugador 1 no tiene suficiente dinero");
        }


        //Lo mismo para el jugador 2
        if (dineroJugador2 > 0 && jugador2.getFortuna() < dineroJugador2) {
            throw new TratoException("El jugador 2 no tiene suficiente dinero");
        }

    }
    /*
    * Ejecuto el trato
    *
    * */
    public void ejecutar() {
        // Transferir propiedad del proponente al destinatario
        if (casillajugador1 != null) {
            jugador1.eliminarPropiedad(casillajugador1);
            jugador2.anhadirPropiedad(casillajugador1);
            casillajugador1. setDuenho(jugador2);
        }

        // Transferir propiedad del destinatario al proponente
        if (casillajugador2 != null) {
            jugador2.eliminarPropiedad(casillajugador2);
            jugador1.anhadirPropiedad(casillajugador2);
            casillajugador2.setDuenho(jugador1);
        }

        // Transferir dinero del proponente al destinatario
        if (dineroJugador1 > 0) {
            jugador1.pagar(dineroJugador1);
            jugador2.recibir(dineroJugador1);
        }

        // Transferir dinero del destinatario al proponente
        if (dineroJugador2 > 0) {
            jugador2. pagar(dineroJugador2);
            jugador1. recibir(dineroJugador2);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(jugador2.getNombre()).append(", ¿te doy ");

        // Parte del proponente
        if (casillajugador1 != null) {
            sb.append(casillajugador1.getNombre());
        }
        if (dineroJugador1 > 0) {
            if (casillajugador1 != null) sb.append(" y ");
            sb.append(String.format("%.0f€", dineroJugador1));
        }

        sb.append(" y tú me das ");

        // Parte del destinatario
        if (casillajugador2 != null) {
            sb.append(casillajugador2.getNombre());
        }
        if (dineroJugador2 > 0) {
            if (casillajugador2 != null) sb.append(" y ");
            sb.append(String.format("%.0f€", dineroJugador2));
        }

        sb.append("?");
        return sb.toString();
    }


    public String getId() {
        return id;
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }

    /**
     * Versión del toString para mostrar al destinatario del trato
     */
    public String toStringParaDestinatario() {
        StringBuilder sb = new StringBuilder();
        sb.append("Te doy ");

        // Lo que RECIBE el destinatario (viene del jugador1)
        if (casillajugador1 != null) {
            sb.append(casillajugador1.getNombre());
        }
        if (dineroJugador1 > 0) {
            if (casillajugador1 != null) sb.append(" y ");
            sb.append(String.format("%.0f€", dineroJugador1));
        }

        sb.append(" por ");

        // Lo que DA el destinatario (viene del jugador2)
        if (casillajugador2 != null) {
            sb.append(casillajugador2.getNombre());
        }
        if (dineroJugador2 > 0) {
            if (casillajugador2 != null) sb.append(" y ");
            sb.append(String.format("%.0f€", dineroJugador2));
        }

        return sb.toString();
    }

    /**
     * Mensaje cuando se acepta el trato
     */
    public String toStringAceptado() {
        StringBuilder sb = new StringBuilder();
        sb.append("le doy ");

        // Lo que DA el que acepta (jugador2)
        if (casillajugador2 != null) {
            sb.append(casillajugador2.getNombre());
        }
        if (dineroJugador2 > 0) {
            if (casillajugador2 != null) sb.append(" y ");
            sb.append(String.format("%.0f€", dineroJugador2));
        }

        sb.append(" y ").append(jugador1.getNombre()).append(" me da ");

        // Lo que RECIBE el que acepta (viene de jugador1)
        if (casillajugador1 != null) {
            sb.append(casillajugador1.getNombre());
        }
        if (dineroJugador1 > 0) {
            if (casillajugador1 != null) sb.append(" y ");
            sb.append(String.format("%.0f€", dineroJugador1));
        }

        sb.append(".");
        return sb.toString();
    }
}