package monopoly.casillas.propiedades;

import monopoly.casillas.Casilla;
import partida.Jugador;
import static monopoly.Juego.consola;

public class Transporte extends Propiedad {

    private final float alquilerBase;

    public Transporte(String nombre, int posicion, float valor, float alquilerBase, Jugador duenho) {
        // Transporte no pertenece a ningún grupo de color, pasamos null
        super(nombre, posicion, valor, duenho, null);
        this.alquilerBase = alquilerBase;
    }

    // IMPLEMENTACIÓN DE PROPIEDAD
    @Override
    public float valor () { return valor; }

    @Override
    public boolean alquiler(Jugador actual, int tirada) {
        if (isHipotecada()) {
            consola.imprimir("El transporte está hipotecado. No se paga alquiler.");
            return true;
        }
        // Calculamos cuántos transportes tiene el dueño
        int numTransportes = 0;
        // Nota: Como 'getPropiedades' devuelve Casillas o Propiedades, filtramos:
        for (Casilla c : duenho.getPropiedades()) {
            if (c instanceof Transporte) {
                numTransportes++;
            }
        }
        float total = alquilerBase * numTransportes;
        if (actual.getFortuna() >= total) {
            actual.pagar(total);
            duenho.recibir(total);
            // Estadísticas
            actual.acumularPagoDeAlquileres(total);
            duenho.acumularCobroDeAlquileres(total);
            sumarAlquileresGenerado(total);
            consola.imprimir(String.format("%s paga %.0f€ a %s por el transporte %s.",
                    actual.getNombre(), total, duenho.getNombre(), nombre));
            return true;
        } else {
            consola.imprimir(String.format("%s no puede pagar el alquiler de %.0f€.",
                    actual.getNombre(), total));
            return false;
        }
    }

    // IMPLEMENTACIÓN DE CASILLA
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (duenho == null || duenho == actual || duenho == banca)  return true;

        return alquiler(actual, tirada);
    }

    // NUEVO MÉTODO NECESARIO PARA CARTA SUERTE
    public float getAlquilerBase() { return alquilerBase;}

    @Override
    public String casEnVenta() {
        if (getDuenho() != null && !"Banca".equalsIgnoreCase(getDuenho().getNombre())){
            return "";
        }
        return String.format("{\n  tipo: transporte,\n  valor: %.0f\n}", valor);
    }
}