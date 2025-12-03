package excepciones.objetoNoExiste;

import excepciones.Excepcion;

/**
 * Excepcion intermedia para errores derivados de objetos inexistentes.
 */
public abstract class ObjetoNoExisteException extends Excepcion {

    public ObjetoNoExisteException(String mensaje) {
        super(mensaje);
    }
}
