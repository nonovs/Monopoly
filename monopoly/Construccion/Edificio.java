package monopoly.Construccion;

import monopoly.casillas.propiedades.Solar;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Clase base para edificacions. Xera IDs únicas por tipo.
 */
public abstract class  Edificio {

    private static final ConcurrentHashMap<String, AtomicInteger> CONTADORES = new ConcurrentHashMap<>();

    private final String id;
    private final String tipo;
    private final Solar solar;
    private final float precio;

    public  Edificio(String tipo, Solar solar, float precio) {
        this.tipo = tipo == null ? "desconocido" : tipo.toLowerCase();
        this.solar = solar;
        this.precio = precio;
        this.id = generarIdGlobal(this.tipo, solar != null ? solar.getNombre() : "sinSolar");
    }

    private static String generarIdGlobal(String tipo, String solarNombre) {
        AtomicInteger contador = CONTADORES.computeIfAbsent(tipo.toLowerCase(), k -> new AtomicInteger(0));
        int n = contador.incrementAndGet();
        return tipo.toLowerCase() + "-" + n ;
    }

    public String getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public Solar getSolar() {
        return solar;
    }

    public float getPrecio() {
        return precio;
    }

    /**
     * Método que pode ser sobrescrito pola  subclase se hai regras específicas.
     */
    public abstract boolean esEdificable();


    @Override
    public String toString() {
        return String.format("{id:%s, tipo:%s, solar:%s, precio:%.0f}", id, tipo, (solar != null ? solar.getNombre() : "null"), precio);
    }
}