package monopoly;


public class Valor {

    // === FORTUNA INICIAL Y PREMIOS ===
    public static final double FORTUNA_INICIAL = 15_000_000;   // dinero inicial de cada jugador
    public static final double SUMA_VUELTA = 2_000_000;        // cobrar al pasar por Salida
    public static final float FORTUNA_BANCA = 500_000_000;

    // === TRANSPORTES Y SERVICIOS ===
    public static final double PRECIO_TRANSPORTE_SERVICIO = 500_000; // precio de compra de transportes/servicios
    public static final double ALQUILER_TRANSPORTE = 250_000;        // alquiler fijo de transporte
    public static final double FACTOR_SERVICIO = 50_000;             // multiplicador por tirada total en servicios

    // === IMPUESTOS, CÁRCEL Y OTROS ===
    public static final double IMPUESTO_FIJO = 2_000_000;   // casillas de impuesto
    public static final double SALIR_CARCEL = 500_000;      // pagar para salir de la cárcel

    
    //Colores del texto:
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String BROWN = "\u001B[38;2;165;42;42m";
    public static final String ORANGE = "\u001B[38;2;255;165;0m";
    public static final String BOLD_STRING = "\u001B[1m";
    public static final String SUBRAYADO   = "\u001B[4m";

}
