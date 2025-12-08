package monopoly.casillas.propiedades;

import monopoly.casillas.Casilla;
import partida.Jugador;
import static monopoly.Juego.consola;

public class Servicios extends Propiedad {

    private float factorServicio = 50000; // Valor fijo para el cálculo del alquiler

    public Servicios(String nombre, int posicion, float valor, Jugador duenho) {
        super(nombre, posicion, valor, duenho, null); // Sin grupo
    }

    // IMPLEMENTACIÓN DE PROPIEDAD
    @Override
    public float valor() { return valor;}

    @Override
    public boolean alquiler (Jugador actual, int tirada) {
        if (isHipotecada()) return true;

        int numServicios = 0;
        for (Casilla c : duenho.getPropiedades()) {
            if (c instanceof Servicios) numServicios++;
        }
        // Cálculo de alquiler: 4 × tirada × factorServicio
        // Multiplicador: 4 si tiene 1 servicio, 10 si tiene 2 o más
        int multiplicador = (numServicios == 1) ? 4 : 10;
        float alquiler = multiplicador * tirada * factorServicio;

        if (actual.getFortuna() >= alquiler) {
            actual.pagar(alquiler);
            duenho.recibir(alquiler);

            //Para tener en cuenta en las estadisticas
            actual.acumularPagoDeAlquileres(alquiler);
            duenho.acumularCobroDeAlquileres(alquiler);
            this.sumarAlquileresGenerado(alquiler);

            consola.imprimir(String.format("%s paga %.0f€ a %s por caer en %s (%d servicio(s), x%d).",
                    actual.getNombre(), alquiler, duenho.getNombre(),
                    getNombre(), numServicios, multiplicador));
            return true;
        } else {
            consola.imprimir(String.format("%s no puede pagar el alquiler de %.0f en %s.",
                    actual.getNombre(), alquiler, getNombre()));
            return false;
        }
    }
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        // Si la casilla no tiene dueño o es del mismo jugador/banca no tiene que pagar nada
        if (duenho == null || duenho == actual || duenho == banca) return true;

        return alquiler(actual, tirada);
    }
}