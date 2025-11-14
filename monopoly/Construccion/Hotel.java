package monopoly.Construccion;

import monopoly.casillas.Solar;

/**
 * Hotel: require 4 casas e non ter hotel previo.
 */
public class Hotel extends Edificio {

    public Hotel(Solar solar, float precio) {
        super("hotel", solar, precio);
    }

    @Override
    public boolean esEdificable() {
        Solar s = getSolar();
        if (s == null) return false;
        return s.getCasas() == 4 && !s.hasHotel();
    }


}