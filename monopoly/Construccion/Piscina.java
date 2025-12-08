package monopoly.Construccion;

import monopoly.casillas.propiedades.Solar;

/**
 * Piscina: require hotel e que non exista xa piscina.
 */
public class Piscina extends Edificio {

    public Piscina(Solar solar, float precio) {
        super("piscina", solar, precio);
    }

    @Override
    public boolean esEdificable() {
        Solar s = getSolar();
        if (s == null) return false;
        return s.hasHotel() &&  !s.hasPiscina();

    }
}