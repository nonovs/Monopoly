package monopoly.casillas;

import monopoly.Construccion.*;
import monopoly.Grupo;
import partida.Jugador;

import java.util.ArrayList;          // <-- novo
import java.util.List;               // <-- novo

public class Solar extends Casilla {

    // Base
    private float alquilerBase;

    // Estado de edificaciones
    private int casas;          // 0..4
    private boolean hotel;      // true si hay hotel
    private boolean piscina;    // true si hay piscina
    private boolean pistaDeporte; // true si hay pista
    private float precioCasa;
    private float precioHotel;
    private float precioPiscina;
    private float precioPista;
    private float alquilerCasa;
    private float alquilerHotel;
    private float alquilerPiscina;
    private float alquilerPista;
    private boolean hipotecado = false;
    private ArrayList<Edificio> edificio = new ArrayList<>();

    // Rexistro de edificacions construídas neste solar
    private final List<Edificio> edificaciones; // <-- novo

    public Solar(
            String nombre, int posicion,
            int valor, float hipoteca, float alquilerBase,
            float precioCasa, float precioHotel, float precioPiscina, float precioPista,
            float alquilerCasa, float alquilerHotel, float alquilerPiscina, float alquilerPista,
            Jugador duenho, Grupo grupo
    ) {
        super(nombre, "Solar", posicion, valor, duenho);// Llama al constructor de Casilla,
        // this.setHipoteca(hipoteca);
        this.alquilerBase = alquilerBase;
        this.setGrupo(grupo);

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

        this.edificaciones = new ArrayList<>(); // <-- inicializar
    }

    /** Se ejecuta cuando un jugador cae en la casilla
     * -Si no tiene dueño o el dueño es la banca, no ocurre nada
     * -si perteneces al mismo jugador, tampoco.
     * -Si pertenece a otro jugador, se calcula y cobra el alquiler.
     */
    @Override
    public boolean evaluarCasilla(Jugador actual, Jugador banca, int tirada) {
        if (getDuenho() == null || getDuenho() == actual || getDuenho() == banca)
            return true;

        //Si está hipotecada, no se cobra alquiler
        if (hipotecado) {
            System.out.printf("La propiedad %s está hipotecada. No se cobra alquiler.%n", getNombre());
            return true;
        }

        float alquiler = calcularAlquiler();
        if (actual.getFortuna() >= alquiler) {
            actual.pagar(alquiler);
            getDuenho().recibir(alquiler);
            System.out.printf("%s paga %.0f€ de alquiler a %s por %s.%n",
                    actual.getNombre(), alquiler, getDuenho().getNombre(), getNombre());

            return true;
        } else {
            System.out.println(actual.getNombre() + " no puede pagar el alquiler de " + alquiler);
            return false;
        }
    }
    //  Permite al jugador comprar la casilla si está libre y se encuentra en ella
    @Override
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        //Verifica que la propiedad no esté ya vendida
        if (getDuenho() != banca) {
            System.out.println("Este solar ya tiene dueño.");
            return;
        }
        // Solo se puede comprar si el jugador está sobre la casilla
        if (solicitante.getPosicion() != this.getPosicion()) {
            System.out.println("Solo puedes comprar la casilla en la que estás situado.");
            return;
        }
        // verifica que el jugador tenga suficiente dinero
        if (solicitante.getFortuna() < getValor()) {
            System.out.println(solicitante.getNombre() + " no tiene suficiente dinero para comprar " + getNombre());
            return;
        }
        //Efectua la compra
        solicitante.pagar(getValor());
        setDuenho(solicitante);
        solicitante.anhadirPropiedad(this);

        System.out.printf(
                "El jugador %s compra la casilla %s por %.0f€. Su fortuna actual es %.0f€.\n",
                solicitante.getNombre(), getNombre(), getValor(), solicitante.getFortuna()
        );
    }
    //Calcula el alquieler actual de la casilla, teniendo en cuenta edificaciones y grupos
    public float calcularAlquiler() {
        float total = 0;

        // Si hay edificios, se cobra solo el alquiler de los edificios
        if (casas > 0 || hotel || piscina || pistaDeporte) {
            if (casas > 0) total += casas * alquilerCasa;
            if (hotel)     total += alquilerHotel;
            if (piscina)   total += alquilerPiscina;
            if (pistaDeporte) total += alquilerPista;
        } else {
            // Si NO hay edificios, cobra el alquiler base
            total = alquilerBase;

            // Si el dueño tiene todo el grupo, duplica el alquiler base
            Grupo g = getGrupo();
            if (g != null && g.esDuenhoGrupo(getDuenho())) {
                total *= 2;
            }
        }

        return total;
    }


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

    // ===============================
    // Métodos para xestionar edificacions
    // ===============================
    /**
     * Engade a edificacion ao rexistro do solar.
     */
    public void anhadirEdificacion(Edificio e) {
        if (e != null) {
            edificaciones.add(e);
        }
    }

    /**
     * Devolve unha copia da lista de edificacions deste solar.
     */
    public List<Edificio> getEdificaciones() {
        return new ArrayList<>(edificaciones);
    }

    /**
     * Conveniencia: saber se o solar está "completo" (criterio: hotel+piscina+pista).
     */
    public boolean estaCompleto() {
        return hotel && piscina && pistaDeporte;
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
                getValor(), getHipoteca(), alquilerBase,
                precioCasa, precioHotel, precioPiscina, precioPista,
                alquilerCasa, alquilerHotel, alquilerPiscina, alquilerPista
        );
    }

    @Override
    public String casEnVenta() {
        if (getDuenho() != null && !"Banca".equalsIgnoreCase(getDuenho().getNombre())) {
            return "";
        }
        return String.format("{%n  tipo: solar,%n  grupo: %s,%n  valor: %.0f%n}", getGrupo(), getValor());
    }



    //Aqui vou gestionar coossas de edificcacions
    public boolean romperCasa(Casa casa) {
        if (casa == null || !edificaciones.contains(casa)) {
            return false;
        }


        float devolucion = getPrecioCasa();
        if (getDuenho() != null) {
            getDuenho().recibir(devolucion);
            System.out.printf("Se ha demolido la casa %s en %s. %s recibe %.0f€.%n",
                    casa.getId(), getNombre(), getDuenho().getNombre(), devolucion);
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
            System.out.printf("Se ha demolido el hotel %s en %s. %s recibe %.0f€.%n",
                    hotel.getId(), getNombre(), getDuenho().getNombre(), devolucion);
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
            System.out.printf("Se han añadido 4 casas en %s tras demoler el hotel.%n", getNombre());
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
            System.out.printf("Se ha demolido la piscina %s en %s. %s recibe %.0f€.%n",
                    piscina.getId(), getNombre(), getDuenho().getNombre(), devolucion);
        }

        // Marcar que ya no hay piscina
        this.piscina = false;

        return true;
    }

    public boolean romperPista(PistaDeporte pista) {
        if (pista == null || !edificaciones.contains(pista)) {
            return false;
        }


        float devolucion = getPrecioPista();
        if (getDuenho() != null) {
            getDuenho().recibir(devolucion);
            System.out.printf("Se ha demolido la pista de deporte %s en %s. %s recibe %.0f€.%n",
                    pista.getId(), getNombre(), getDuenho().getNombre(), devolucion);
        }

        // Marcar que ya no hay pista
        this.pistaDeporte = false;

        return true;
    }

    public void eliminarEdificacion(Edificio edificio) {
        if (edificio != null) {
            edificaciones.remove(edificio);
        }
    }



    public float getAlquilerBase() { return alquilerBase; }

    public float getPrecioCasa() { return precioCasa; }
    public float getPrecioHotel() { return precioHotel; }
    public float getPrecioPiscina() { return precioPiscina; }
    public float getPrecioPista() { return precioPista; }

    public float getAlquilerCasa() { return alquilerCasa; }
    public float getAlquilerHotel() { return alquilerHotel; }
    public float getAlquilerPiscina() { return alquilerPiscina; }
    public float getAlquilerPista() { return alquilerPista; }

    public int getCasas() { return casas; }
    public boolean hasHotel() { return hotel; }
    public boolean hasPiscina() { return piscina; }
    public boolean hasPistaDeporte() { return pistaDeporte; }

    public void setPreciosMejoras(float precioCasa, float precioHotel, float precioPiscina, float precioPista) {
        this.precioCasa = precioCasa;
        this.precioHotel = precioHotel;
        this.precioPiscina = precioPiscina;
        this.precioPista = precioPista;
    }

    public void setAlquileresMejoras(float alquilerCasa, float alquilerHotel, float alquilerPiscina, float alquilerPista) {
        this.alquilerCasa = alquilerCasa;
        this.alquilerHotel = alquilerHotel;
        this.alquilerPiscina = alquilerPiscina;
        this.alquilerPista = alquilerPista;
    }

    public boolean tieneEdificios() {
        return !edificio.isEmpty();
    }
}