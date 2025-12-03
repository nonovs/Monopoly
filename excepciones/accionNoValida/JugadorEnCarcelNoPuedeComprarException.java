package excepciones.accionNoValida;

public class JugadorEnCarcelNoPuedeComprarException extends AccionNoValidaException {

    public JugadorEnCarcelNoPuedeComprarException(String mensaje) {
        super(mensaje);
    }
}

