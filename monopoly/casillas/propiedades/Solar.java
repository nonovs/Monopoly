package monopoly.casillas.propiedades;

import monopoly.Construccion.*;
import monopoly.casillas.Grupo;
import partida.GestorEdificaciones;
import partida.Jugador;
import java.util.ArrayList;
import java.util.List;
import static monopoly.Juego.consola;

public class Solar extends Propiedad {

    // Atributos específicos de Solar (precios de edificación y alquileres)
    private float alquilerBase;
    private int casas;          // 0..4
    private boolean hotel;      // true si hay hotel
    private boolean piscina;    // true si hay piscina
    private boolean pistaDeporte; // true si hay pista

    // Atributos de precios y alquileres
    private float precioCasa;
    private float precioHotel;
    private float precioPiscina;
    private float precioPista;
    private float alquilerCasa;
    private float alquilerHotel;
    private float alquilerPiscina;
    private float alquilerPista;

    private boolean hipotecado = false;

    // Registro de edificios (Composición)
    private final List<Edificio> edificaciones;

    public Solar(
            String nombre, int posicion,
            int valor, float hipoteca, float alquilerBase,
            float precioCasa, float precioHotel, float precioPiscina, float precioPista,
            float alquilerCasa, float alquilerHotel, float alquilerPiscina, float alquilerPista,
            Jugador duenho, Grupo grupo
    ) {

        // Llamamos al constructor de Propiedad
        // Propiedad se encarga de: nombre, posicion, valor, duenho, grupo
        super(nombre, posicion, valor, duenho, grupo);

        this.alquilerBase = alquilerBase;
        /**
         * La hipoteca base ya se calcula en Propiedad (valor/2), pero si el juego pasa a una específica,
         * podríamos sobreescribirla o ignorarla. Aquí asumimos la lógica estandar, pero sino:
         * this.hipotecarBase = hipoteca;
         */
        this.precioCasa = precioCasa;
        this.precioHotel = precioHotel;
        this.precioPiscina = precioPiscina;
        this.precioPista = precioPista;

        this.alquilerCasa = alquilerCasa;
        this.alquilerHotel = alquilerHotel;
        this.alquilerPiscina = alquilerPiscina;
        this.alquilerPista = alquilerPista;

        this.casas = 0;
        this.hotel = false;
        this.piscina = false;
        this.pistaDeporte = false;
        this.edificaciones = new ArrayList<>();
    }

    // IMPLEMENTACIÓN DE MÉTODOS DE LA CLASE PADRE (PROPIEDAD)

    @Override
    public float valor() { return valor; }

    @Override
    public boolean alquiler (Jugador actual, int tirada) {
        if (isHipotecada()) {
            consola.imprimir(String.format("El solar %s está hipotecado. No pagas alquiiler.", nombre));
            return true;
        }

        float monto = calcularAlquilerNumerico();
        if (actual.getFortuna() >= monto) {
            actual.pagar(monto);
            if (duenho != null) duenho.recibir(monto);

            //Estadísticas
            actual.acumularPagoDeAlquileres(monto);
            if (duenho != null) duenho.acumularCobroDeAlquileres(monto);
            sumarAlquileresGenerado(monto);

            consola.imprimir(String.format("%s paga %.0f€ de alquiler en %s.",
                    actual.getNombre(), monto, nombre));
            return true;
        } else {
            consola.imprimir(String.format("%s no tiene fondos para pagar el alquiler de %.0f€ en %s.",
                    actual.getNombre(), monto, nombre));
            return false;
        }
    }

    // IMPLEMENTACIÓN DE MÉTODOS SOLICITADOS EN EL GUIÓN PARA SOLAR

    public void edificar(String tipoEdificio) {
        // Verificaciones básicas antes de edificar
        if (duenho == null) {
            consola.imprimir("No se puede edificar: el solar no tiene dueño.");
            return;
        }
        // Llamamos al método estático que hemos creado en GestorEdificaciones
        // Pasamos el dueño de este solar y 'this' como el solar objetivo.
        GestorEdificaciones.procesarEdificacion(this.duenho, this, tipoEdificio);
    }

    public void hipotecar() {
        if (hipotecada) {
            consola.imprimir("Ya está hipotecada.");
            return;
        }
        if (!edificaciones.isEmpty()) {
            consola.imprimir("Debes vender los edificios antes de hipotecar.");
            return;
        }
        // Ejecutamos hipoteca
        float valorHipoteca = valor() / 2;
        duenho.recibir(valorHipoteca);
        setHipotecada(true);
        if (duenho.getPropiedades().contains(this)) {
            // Opcional: mover a lista de hipotecadas en Jugador si mantienes esa lógica
            //duenho.moverAHipotecadas(this);
        }
        consola.imprimir(String.format("Has hipotecado %s por %.0f€.", nombre, valorHipoteca));
    }

    public boolean estaHipotecada() { return isHipotecada(); }


    // IMPLEMENTACIÓN DE CASILLA
    /** Se ejecuta cuando un jugador cae en la casilla
     * -Si no tiene dueño o el dueño es la banca, no ocurre nada
     * -si perteneces al mismo jugador, tampoco.
     * -Si pertenece a otro jugador, se calcula y cobra el alquiler.
     */
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        // Si no tiene dueño o es mío, no pago
        if (duenho == null || duenho == banca || duenho == actual) {
            return true;
        }
        // Si tiene dueño, intentamos cobrar alquiler
        return alquiler(actual, tirada);
    }

    // MÉTODOS AUXILIARES Y GETTERS

    public float calcularAlquilerNumerico() {
        float total = 0;

        // Si hay edificios, se cobra solo el alquiler de los edificios
        if (casas > 0 || hotel || piscina || pistaDeporte) {
            if (casas > 0) total += casas * alquilerCasa;
            if (hotel)     total += alquilerHotel;
            if (piscina)   total += alquilerPiscina;
            if (pistaDeporte) total += alquilerPista;
        } else {
            total = alquilerBase;
            if (grupo != null && grupo.esDuenhoGrupo(duenho)) {
                total *= 2;
            }
        }
        return total;
    }

    // Lógica interna de control de contadores
    public boolean construirCasa() {
        Grupo g = getGrupo();
        if (g != null && casas < 4 && !hotel && g.esDuenhoGrupo(getDuenho())) {
            casas++;
            return true;
        }
        return false;
    }

    public boolean construirHotel() {
        if (casas == 4 && !hotel) {
            List<Edificio> casasAEliminar = new ArrayList<>();
            for (Edificio e : edificaciones) {
                if (e instanceof Casa) {
                    casasAEliminar.add(e);
                }
            }
            // Eliminar solo 4 casas puido fallar o codigo e crear mais de 4
            int eliminadas = 0;
            for (Edificio casa : casasAEliminar) {
                if (eliminadas < 4) {
                    edificaciones.remove(casa);
                    eliminadas++;
                }
            }
            casas = 0;      //casas a 0
            hotel = true;   // hotel1
            return true;
        }
        return false;
    }

    public boolean construirPiscina() {
        if (hotel && !piscina) {
            piscina = true;
            return true;
        }
        return false;
    }

    public boolean construirPista() {
        if (hotel && piscina && !pistaDeporte) {
            pistaDeporte = true;
            return true;
        }
        return false;
    }

    // Engade a edificacion ao rexistro do solar.
    public void anhadirEdificacion(Edificio e) {
        if (e != null) {
            edificaciones.add(e);
        }
    }

    // Elimina a edificacion do rexistro do solar.
    public void eliminarEdificacion(Edificio edificio) {
        if (edificio != null) {
            edificaciones.remove(edificio);
        }
    }

    // Devolve unha copia da lista de edificacions deste solar.
    public List<Edificio> getEdificaciones() {
        return new ArrayList<>(edificaciones);
    }

    @Override
    public String infoCasilla() {
        String color = (getGrupo() != null && getGrupo().getColor() != null) ? getGrupo().getColor() : "N/A";
        String duenhoStr = (getDuenho() != null) ? getDuenho().getNombre() : "banca";

        return String.format(
                "{%n" +
                        " tipo: Solar,%n" +
                        " grupo: %s,%n" +
                        " propietario: %s,%n" +
                        " valor: %.0f,%n" +
                        " hipoteca: %.0f,%n" +
                        " alquiler base: %.0f,%n" +
                        " precio casa: %.0f,%n" +
                        " precio hotel: %.0f,%n" +
                        " precio piscina: %.0f,%n" +
                        " precio pista: %.0f,%n" +
                        " alquiler casa: %.0f,%n" +
                        " alquiler hotel: %.0f,%n" +
                        " alquiler piscina: %.0f,%n" +
                        " alquiler pista: %.0f%n" +
                        "}",
                color, duenhoStr,
                valor(), getPrecioHipoteca(), alquilerBase,
                precioCasa, precioHotel, precioPiscina, precioPista,
                alquilerCasa, alquilerHotel, alquilerPiscina, alquilerPista
        );
    }

    //Aqui vou gestionar coossas de edificcacions
    public boolean romperCasa(Casa casa) {
        if (casa == null || !edificaciones.contains(casa)) {
            return false;
        }
        float devolucion = getPrecioCasa();
        if (getDuenho() != null) {
            getDuenho().recibir(devolucion);
            consola.imprimir(String.format("Se ha demolido la casa %s en %s. %s recibe %.0f€.",
                    casa.getId(), getNombre(), getDuenho().getNombre(), devolucion));
        }
        // Decrementar el contador de casas
        if (casas > 0) {
            casas--;
        }
        return true;
    }

    public boolean romperHotel(Hotel hotel) {
        if (hotel == null || !edificaciones.contains(hotel)) {
            return false;
        }
        float devolucion = getPrecioHotel() ;
        if (getDuenho() != null) {
            getDuenho().recibir(devolucion);
            consola.imprimir(String.format("Se ha demolido el hotel %s en %s. %s recibe %.0f€.",
                    hotel.getId(), getNombre(), getDuenho().getNombre(), devolucion));
        }
        // Marcar que ya no hay hotel
        this.hotel = false;
        // Volver a añadir 4 casas al demoler un hotel
        if (casas + 4 <= 4) {
            for (int i = 0; i < 4; i++) {
                Casa casa = new Casa(this, getPrecioCasa());
                edificaciones.add(casa);
                casas++;
            }
            consola.imprimir(String.format("Se han añadido 4 casas en %s tras demoler el hotel.", getNombre()));
        }
        return true;
    }

    public boolean romperPiscina(Piscina piscina) {
        if (piscina == null || !edificaciones.contains(piscina)) {
            return false;
        }
        float devolucion = getPrecioPiscina();
        if (getDuenho() != null) {
            getDuenho().recibir(devolucion);
            consola.imprimir(String.format("Se ha demolido la piscina %s en %s. %s recibe %.0f€.",
                    piscina.getId(), getNombre(), getDuenho().getNombre(), devolucion));
        }
        // Marcar que ya no hay piscina
        this.piscina = false;
        return true;
    }

    public boolean romperPista(PistaDeporte pista) {
        if (pista == null || !edificaciones.contains(pista)) {
            return false;
        }
        float devolucion =  getPrecioPista();
        if (getDuenho() != null) {
            getDuenho().recibir(devolucion);
            consola.imprimir(String.format("Se ha demolido la pista de deporte %s en %s. %s recibe %.0f€.",
                    pista.getId(), getNombre(), getDuenho().getNombre(), devolucion));
        }
        // Marcar que ya no hay pista
        this.pistaDeporte = false;
        return true;
    }

    // Getters de precios para uso externo si fuera necesario
    public float getPrecioCasa() { return precioCasa; }
    public float getPrecioHotel() { return precioHotel; }
    public float getPrecioPiscina() { return precioPiscina; }
    public float getPrecioPista() { return precioPista; }
    public int getCasas() { return casas; }
    public boolean hasHotel() { return hotel; }
    public boolean hasPiscina() { return piscina; }
    public boolean hasPistaDeporte() { return pistaDeporte; }
}