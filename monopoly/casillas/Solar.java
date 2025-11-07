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
    private float precioCompra;

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
            return true;
        } else {
            System.out.println(actual.getNombre() + " no puede pagar el alquiler de " + alquiler);
            return false;
        }
    }

    @Override
    public void comprarCasilla(Jugador solicitante, Jugador banca) {
        if (getDuenho() != banca) {
            System.out.println("Este solar ya tiene dueño.");
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
        solicitante.pagar(getValor());
        setDuenho(solicitante);
        solicitante.anhadirPropiedad(this);

        System.out.printf(
                "El jugador %s compra la casilla %s por %.0f€. Su fortuna actual es %.0f€.\n",
                solicitante.getNombre(), getNombre(), getValor(), solicitante.getFortuna()
        );
    }

    public float calcularAlquiler() {
        float total = alquilerBase;

        if (casas > 0) total += casas * alquilerCasa;
        if (hotel)     total += alquilerHotel;
        if (piscina)   total += alquilerPiscina;
        if (pistaDeporte) total += alquilerPista;

        Grupo g = getGrupo();
        if (g != null && g.esDuenhoGrupo(getDuenho()) && casas == 0 && !hotel && !piscina && !pistaDeporte) {
            total *= 2;
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
            casas = 0;
            hotel = true;
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

    public void hipotecar(Jugador jugador) {
        if (getDuenho() != jugador) {
            System.out.printf("%s no puede hipotecar %s. No es una propiedad que le pertenece.%n", jugador.getNombre(), getNombre());
            return;
        }

        if (hipotecado) {
            System.out.printf("%s no puede hipotecar %s. Ya está hipotecada.%n", jugador.getNombre(), getNombre());
            return;
        }

        if (casas > 0 || hotel || piscina || pistaDeporte) {
            System.out.printf("%s no puede hipotecar %s. Primero debe vender todos los edificios.%n", jugador.getNombre(), getNombre());
            return;
        }
        //Calcular dinero recibido
        float cantidad = getValor() / 2.0f;
        jugador.recibir(cantidad);
        hipotecado = true;

        System.out.printf("%s recibe %.0f€ por la hipoteca de %s. No puede recibir alquileres ni edificar en el grupo %s.%n", jugador.getNombre(), cantidad, getNombre(), getGrupo().getColor());
    }

    public void deshipotecar(Jugador jugador) {
        if (getDuenho() != jugador) {
            System.out.printf("%s no puede deshipotecar %s. No es una propiedad que le pertenece.%n",
                    jugador.getNombre(), getNombre());
            return;
        }

        if (!hipotecado) {
            System.out.printf("%s no puede deshipotecar %s. No está hipotecada.%n",
                    jugador.getNombre(), getNombre());
            return;
        }

        float cantidad = getValor() / 2.0f;
        if (jugador.getFortuna() < cantidad) {
            System.out.printf("La fortuna de %s no es suficiente para deshipotecar %s.%n",
                    jugador.getNombre(), getNombre());
            return;
        }

        jugador.pagar(cantidad);
        hipotecado = false;

        System.out.printf("%s paga %.0f€ por deshipotecar %s. Ahora puede recibir alquileres y edificar en el grupo %s.%n",
                jugador.getNombre(), cantidad, getNombre(), getGrupo().getColor());
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

        // Devolver la mitad del precio al dueño
        float devolucion = getPrecioCasa() / 2;
        if (getDuenho() != null) {
            getDuenho().recibir(devolucion);
            System.out.printf("Se ha demolido la casa %s en %s. %s recibe %.0f€.%n",
                    casa.getId(), getNombre(), getDuenho().getNombre(), devolucion);
        }

        // Decrementar el contador de casas (usa la variable existente)
        if (casas > 0) {
            casas--;
        }

        return true;
    }

    public boolean romperHotel(Hotel hotel) {
        if (hotel == null || !edificaciones.contains(hotel)) {
            return false;
        }

        // Devolver la mitad del precio al dueño
        float devolucion = getPrecioHotel() / 2;
        if (getDuenho() != null) {
            getDuenho().recibir(devolucion);
            System.out.printf("Se ha demolido el hotel %s en %s. %s recibe %.0f€.%n",
                    hotel.getId(), getNombre(), getDuenho().getNombre(), devolucion);
        }

        // Marcar que ya no hay hotel
        this.hotel = false;

        // Volver a añadir 4 casas al demoler un hotel (regla del Monopoly)
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

        // Devolver la mitad del precio al dueño
        float devolucion = getPrecioPiscina() / 2;
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

        // Devolver la mitad del precio al dueño
        float devolucion = getPrecioPista() / 2;
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
    public boolean estaHipotecada() { return hipotecado; }

    public float getPrecioCompra() { return precioCompra; }

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
}