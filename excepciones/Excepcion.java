package excepciones;

/**
 * Excepcion base para todos los errores del Monopoly.
 */
public abstract class Excepcion extends Exception {

    public Excepcion(String mensaje) {
        super(mensaje);
    }

    public Excepcion(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
