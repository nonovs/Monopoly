package monopoly.Construccion;

import monopoly.casillas.Solar;

/**
 * Pista de deporte: require hotel e piscina previas.
 */
public class PistaDeporte extends Edificio {

    public PistaDeporte(Solar solar, float precio) {
        super("pista", solar, precio);
    }

    @Override
    public boolean esEdificable() {
        Solar s = getSolar();
        if (s == null) return false;
        return  s.hasHotel() && s.hasPiscina() && !s.hasPistaDeporte();

    }
}