package monopoly.casillas;

import partida.Jugador;

public class Servicios extends Casilla {

    private float factorServicio = 50000; // Coste dos servicios

    // Constructor
    public Servicios(String nombre, int posicion,int precio ,Jugador duenho) {
        super(nombre, "Servicio", posicion, duenho);
        this.setValor(precio);
    }

    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        // Si la casilla no tiene dueño o es del mismo jugador/banca  no tiene que pagar nada
        if (getDuenho() == null || getDuenho() == actual || getDuenho() == banca)
            return true;
        int numServicios = 0;
        for (Casilla c : getDuenho().getPropiedades()) {
            if (c instanceof Servicios) {
                numServicios++;
            }
        }
        // Cálculo de alquiler: 4 × tirada × factorServicio
        // Multiplicador: 4 si tiene 1 servicio, 10 si tiene 2 o más
        int multiplicador = (numServicios == 1) ? 4 : 10;
        float alquiler = multiplicador * tirada * factorServicio;

        if (actual.getFortuna() >= alquiler) {
            actual.pagar(alquiler);
            getDuenho().recibir(alquiler);

            //Para tener en cuenta en las estadisticas
            actual.acumularPagoDeAlquileres(alquiler);  
            getDuenho().acumularCobroDeAlquileres(alquiler);
            this.sumarAlquilerGenerado(alquiler);
            
            System.out.printf("%s paga %.0f€ a %s por caer en %s (%d servicio(s), x%d).%n",
                    actual.getNombre(), alquiler, getDuenho().getNombre(),
                    getNombre(), numServicios, multiplicador);
            return true;
        } else {
            System.out.printf("%s no puede pagar el alquiler de %.0f en %s.%n",
                    actual.getNombre(), alquiler, getNombre());
            return false;
        }
    }

    @Override
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        if (getDuenho() != banca) {
            System.out.println("Este servicio ya tiene dueño.");
            return;
        }

        if (solicitante.getPosicion() != this.getPosicion()) {
            System.out.println("Solo puedes comprar la casilla en la que estás situado.");
            return;
        }

        if (solicitante.getFortuna() < getValor()) {
            System.out.println(solicitante.getNombre() + " no tiene suficiente dinero para comprar " + getNombre());
            return;
        }

        solicitante.pagar(getValor());//Hacemos que el que compre la casilla pague su valor
        setDuenho(solicitante);//Le establecemos la casilla a su propiedad

        solicitante.anhadirPropiedad(this);

        //añadido para estadisticas
        float precioCompra = getValor(); 
        solicitante.acumularDineroInvertido(precioCompra);//Para sus estadisticas

        System.out.printf(
                "El jugador %s compra el servicio %s por %.0f€. Su fortuna actual es %.0f€.\n",
                solicitante.getNombre(), getNombre(), getValor(), solicitante.getFortuna()
        );
    }

    @Override
    public String infoCasilla() {
        String duenhoStr = (getDuenho() != null) ? getDuenho().getNombre() : "banca";
        return String.format(
                "{%n" +
                        " tipo: Servicio,%n" +
                        " nombre: %s,%n" +
                        " posicion: %d,%n" +
                        " propietario: %s,%n" +
                        " valor: %.0f,%n" +
                        " factor servicio: %.0f%n" +
                        "}",
                getNombre(), getPosicion(), duenhoStr, getValor(), factorServicio
        );
    }

    @Override
    public String casEnVenta() {
        if (getDuenho() != null && !"Banca".equalsIgnoreCase(getDuenho().getNombre())) {
            return "";
        }
        return String.format(
                "{\n    tipo: servicio,\n   valor: %.0f\n}",
                getValor()
        );
    }
}
