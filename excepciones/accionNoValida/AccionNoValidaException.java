package excepciones.accionNoValida;

import excepciones.Excepcion;

/**
 * Excepcion intermedia para errores por acciones invalidas.
 */
public abstract class AccionNoValidaException extends Excepcion {

    public AccionNoValidaException(String mensaje) {
        super(mensaje);
    }
}
