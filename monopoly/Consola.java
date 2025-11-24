package monopoly;

public interface Consola {
    void imprimir(String mensaje);
    String leer(String mensaje);

//Como consola.imprimir solo va aceptar string cambio printf por String.format dentro de impirmir
}
