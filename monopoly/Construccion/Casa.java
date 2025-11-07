package monopoly.Construccion;

import monopoly.casillas.Solar;

/**
 * Casa: implementación básica.
 */
public class Casa extends Edificio {

    public Casa(Solar solar, float precio) {
        super("casa", solar, precio);
    }

    @Override
    public boolean esEdificable() {
        Solar s = getSolar();
        if (s == null) return false;
        // non se permiten casas se xa hai hotel
        if (s.hasHotel()) return false;
        // máximo 4 casas
        return s.getCasas() < 4;
    }

    @Override
    protected float calcularCoste(float valorGrupo) {
        // exemplo: 60% do valor do grupo
        return valorGrupo * 0.60f;
    }

}