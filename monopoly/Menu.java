package monopoly;

import java.util.*;
import partida.*;
import monopoly.casillas.Casilla;

public class Menu {

    // Atributos
    private ArrayList<Jugador> jugadores; // Jugadores de la partida
    private ArrayList<Avatar> avatares;   // Avatares en la partida
    private int turno = 0;                // Indice del jugador que tiene el turno
    private int lanzamientos = 0;         // Lanzamientos en el turno actual
    private Tablero tablero;              // Tablero en el que se juega
    private Dado dado1;                   // Si los usas en otras partes, los dejo inicializados
    private Dado dado2;
    private Jugador banca;                // La banca
    private boolean tirado = false;       // Si el jugador actual ya tiró
    private boolean solvente = true;      // Si el jugador actual está solvente tras evaluar casilla
    private Dado dado = new Dado();       // Un solo Dado que gestiona 2 dados

    // CONSTRUCTOR
    public Menu() {
        this.banca = new Jugador();
        this.tablero = new Tablero(banca);
        this.dado1 = new Dado();
        this.dado2 = new Dado();
        this.jugadores = new ArrayList<>();
        this.avatares  = new ArrayList<>();
    }

    // Método para inciar una partida
    public void iniciarPartida() {
        System.out.println("Bienvenido al Monopoly");
        mostrarTablero();
        System.out.println("Introduce comandos. Escribe 'salir' para temrinar \n");
        procesarComandos();
    }

    /** Bucle principal de lectura de comandos **/
    private void procesarComandos() {
        while (true) {
            System.out.print("> ");
            Scanner sc = new Scanner(System.in);
            String comando = sc.nextLine().trim();
            if (comando.equalsIgnoreCase("salir")) {
                System.out.println("Fin de la partida.");
                break;
            }
            analizarComando(comando);
        }
    }

    /* Interpreta el comando introducido y toma la accion correspondiente */
    private void analizarComando(String comando) {
        if (comando.isEmpty()) return;
        String[] partes = comando.split(" ");

        switch (partes[0].toLowerCase()) {
            case "crear":
                if (partes.length == 4 && partes[1].equalsIgnoreCase("jugador")) {
                    crearJugador(partes[2], partes[3]);
                } else {
                    System.out.println("Uso: crear jugador <nombre> <tipo_avatar>");
                }
                break;
            case "jugador":
                mostrarJugadorEnTurno();
                break;
            case "listar":
                if (partes.length >= 2) {
                    if (partes[1].equalsIgnoreCase("jugadores")) listarJugadores();
                    else if (partes[1].equalsIgnoreCase("enventa")) listarVenta();
                    else if (partes[1].equalsIgnoreCase("avatares")) listarAvatares();
                }
                break;

            case "describir":
                if (partes.length >= 3 && partes[1].equalsIgnoreCase("jugador"))
                    descJugador(partes);
                else if (partes.length >= 3 && partes[1].equalsIgnoreCase("avatar"))
                    descAvatar(partes[2]);
                else if (partes.length >= 2)
                    descCasilla(partes[1]);
                break;

            case "lanzar":
                // lanzar dados          -> tirada aleatoria
                // lanzar dados 2+4      -> tirada forzada
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("dados")) {
                    if (partes.length == 2) {
                        lanzarDados(); // aleatoria
                    } else {
                        String[] d = partes[2].split("\\+");
                        if (d.length == 2) {
                            int a = Integer.parseInt(d[0]);
                            int b = Integer.parseInt(d[1]);
                            lanzarDadosForzada(a, b); // forzada
                        } else {
                            System.out.println("Uso: lanzar dados | lanzar dados X+Y");
                        }
                    }
                } else {
                    System.out.println("Uso: lanzar dados");
                }
                break;

            case "comprar":
                if (partes.length >= 2)
                    comprar(partes[1]);
                else
                    System.out.println("Uso: comprar <nombre_casilla>");
                break;

            case "salir":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("carcel"))
                    salirCarcel();
                break;

            case "acabar":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("turno"))
                    acabarTurno();
                break;

            default:
                System.out.println("Comando no reconocido.");
                System.out.println(" COMANDOS DISPONIBLES:");
                System.out.println("  crear jugador <nombre> <tipo_avatar>");
                System.out.println("  listar jugadores");
                System.out.println("  listar enventa");
                System.out.println("  listar avatares");
                System.out.println("  describir jugador <nombre>");
                System.out.println("  describir avatar <id_avatar>");
                System.out.println("  describir <nombre_casilla>");
                System.out.println("  lanzar dados");
                System.out.println("  comprar <nombre_casilla>");
                System.out.println("  salir carcel");
                System.out.println("  acabar turno");
        }
    }

    private void mostrarTablero() {
        tablero.mostrarTablero();
    }

    private void crearJugador(String nombre, String tipoAvatar) {
        // evitar duplicados
        for (Jugador j : jugadores) {
            if (j.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println("Ya existe un jugador con ese nombre.");
                return;
            }
        }
        if (jugadores.size() >= 4) {
            System.out.println("No se pueden crear más de 4 jugadores.");
            return;
        }

        Casilla salida = tablero.encontrar_casilla("Salida");

        // crea jugador
        Jugador nuevo = new Jugador(nombre, tipoAvatar, salida, avatares);

        // crea avatar y lo asigna explicitamente
        Avatar avatar = new Avatar(tipoAvatar, nuevo, salida, avatares);
        nuevo.setAvatar(avatar);

        jugadores.add(nuevo);
        avatares.add(avatar);

        System.out.printf("{nombre: %s, avatar: %s}%n", nombre, avatar.getId());
        tablero.mostrarTablero();
    }

    // --- muestra el jugador que tiene el turno actual ---
    private void mostrarJugadorEnTurno() {
        // valida que haya jugadores creados
        if (jugadores == null || jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida");
            return;
        }

        // obtiene el jugador del indice turno
        Jugador actual = jugadores.get(turno);

        // obtiene datos basicos para mostrar
        String nombre = actual.getNombre();
        String avatarId = (actual.getAvatar() != null) ? actual.getAvatar().getId() : "-";
        int pos = actual.getPosicion();

        // imprime el turno con informacion util
        System.out.printf("Turno de: %s (avatar %s) - posicion %d%n", nombre, avatarId, pos);
    }


    /* describir jugador <nombre> */
    private void descJugador(String[] partes) {
        String nombre = partes[2];
        for (Jugador j : jugadores) {
            if (j.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println(j);
                return;
            }
        }
        System.out.println("Jugador no encontrado: " + nombre);
    }

    /* describir avatar <ID> */
    private void descAvatar(String ID) {
        for (Avatar a : avatares) {
            if (a.getId().equalsIgnoreCase(ID)) {
                System.out.println(a);
                return;
            }
        }
        System.out.println("No existe un avatar con ID: " + ID);
    }

    /* describir <nombre_casilla> */
    private void descCasilla(String nombre) {
        Casilla c = tablero.encontrar_casilla(nombre);
        if (c != null)
            System.out.println(c.infoCasilla());
        else
            System.out.println("Casilla no encontrada: " + nombre);
    }

    // --- lanzar dados aleatoria ---
    private void lanzarDados() {
        if (jugadores == null || jugadores.isEmpty()) {
            System.out.println("Primero crea jugadores con: crear jugador <nombre> <tipoAvatar>");
            return;
        }
        Jugador actual = jugadores.get(turno);

        // si es la primera tirada del turno, resetea estado del dado
        if (lanzamientos == 0) {
            dado.iniciarTurno(); // limpia d1, d2, valor y contador de dobles internos
        }

        int suma = dado.tirar(); // tu Dado ya genera 2 dados y actualiza su estado
        int d1 = dado.getD1();
        int d2 = dado.getD2();
        lanzamientos++;
        tirado = true;

        System.out.printf("%s tira los dados -> %d + %d = %d%n", actual.getNombre(), d1, d2, suma);

        moverYEvaluar(actual, suma);

        // regla de dobles: a la 3a vez de dobles en el mismo turno -> carcel
        if (dado.esDoble()) { // la propia clase indica si la ultima tirada fue doble
            if (lanzamientos >= 3) {
                System.out.println("Tres dobles en el mismo turno. Vas a la carcel");
                irACarcel(actual);
                acabarTurno(); // fuerza fin de turno
            } else {
                System.out.println("Has sacado dobles, puedes volver a lanzar");
            }
        } else {
            System.out.println("Usa 'acabar turno' para pasar al siguiente jugador");
        }
    }

    // --- lanzar dados forzada X+Y ---
    private void lanzarDadosForzada(int a, int b) {
        if (jugadores == null || jugadores.isEmpty()) {
            System.out.println("Primero crea jugadores con: crear jugador <nombre> <tipoAvatar>");
            return;
        }
        if (a < 1 || a > 6 || b < 1 || b > 6) {
            System.out.println("Valores invalidos. Deben ser 1..6");
            return;
        }

        Jugador actual = jugadores.get(turno);

        if (lanzamientos == 0) {
            dado.iniciarTurno();
        }

        int suma = dado.tirarForzado(a, b); // fija los dos dados y actualiza estado
        int d1 = dado.getD1();
        int d2 = dado.getD2();
        lanzamientos++;
        tirado = true;

        System.out.printf("%s tira dados forzados -> %d + %d = %d%n", actual.getNombre(), d1, d2, suma);

        moverYEvaluar(actual, suma);

        if (dado.esDoble()) {
            if (lanzamientos >= 3) {
                System.out.println("Tres dobles en el mismo turno. Vas a la carcel");
                irACarcel(actual);
                acabarTurno();
            } else {
                System.out.println("Has sacado dobles, puedes volver a lanzar");
            }
        } else {
            System.out.println("Usa 'acabar turno' para pasar al siguiente jugador");
        }
    }

    // --- mover y evaluar la casilla destino ---
    private void moverYEvaluar(Jugador j, int pasos) {
        int posIni = j.getPosicion();
        int posFin = (posIni + pasos) % 40;

        // pasar por salida -> cobra SUMA_VUELTA
        if (posIni + pasos >= 40) {
            j.sumarFortuna((float) Valor.SUMA_VUELTA);
            System.out.printf("%s pasa por Salida y cobra %.0f%n", j.getNombre(), Valor.SUMA_VUELTA);
        }

        // actualizar casillas: quitar avatar de la actual y poner en destino
        Casilla origen = tablero.getCasilla(posIni);
        Casilla destino = tablero.getCasilla(posFin);
        if (origen != null && j.getAvatar() != null) origen.eliminarAvatar(j.getAvatar());
        if (destino != null && j.getAvatar() != null) destino.anhadirAvatar(j.getAvatar());

        // actualizar posicion en jugador
        j.setPosicion(posFin);

        System.out.printf("%s avanza a %s (pos %d)%n", j.getNombre(),
                destino != null ? destino.getNombre() : "desconocida", posFin);

        // evaluar casilla segun su tipo, usando polimorfismo
        if (destino != null) {
            boolean ok = destino.evaluarCasilla(j, banca, pasos);
            if (!ok) {
                System.out.println("No has podido pagar tus deudas. Revisa hipotecas o declarate en bancarrota");
                solvente = false;
            } else {
                solvente = true;
            }
        }

        // repinta tablero tras movimiento
        mostrarTablero();
    }

    // --- ir a carcel: lleva al jugador a la posicion 10 ---
    private void irACarcel(Jugador j) {
        int posCarcel = 10; // ajusta si tu tablero sitúa cárcel en otra posición
        Casilla origen = tablero.getCasilla(j.getPosicion());
        Casilla carcel = tablero.getCasilla(posCarcel);
        if (origen != null && j.getAvatar() != null) origen.eliminarAvatar(j.getAvatar());
        if (carcel != null && j.getAvatar() != null) carcel.anhadirAvatar(j.getAvatar());
        j.setPosicion(posCarcel);
        System.out.printf("%s ha sido enviado a Carcel%n", j.getNombre());
    }

    /* comprar <nombre_casilla> */
    private void comprar(String nombre) {
        Jugador actual = jugadores.get(turno);
        Casilla cas = tablero.encontrar_casilla(nombre);
        if (cas != null) {
            cas.comprarCasilla(actual, banca);
        } else {
            System.out.println("Esa casilla no existe.");
        }
    }

    // salir carcel
    private void salirCarcel() {
        Jugador actual = jugadores.get(turno);
        if (actual.isEnCarcel()) {
            actual.salirDeCarcel();
        } else {
            System.out.println("No estás en la cárcel.");
        }
    }

    // listar en venta
    private void listarVenta() {
        for (Casilla c : tablero.getCasillas()) {
            if (c.getDuenho() == banca) {
                System.out.println(c.casEnVenta());
            }
        }
    }

    // listar jugadores
    private void listarJugadores() {
        for (Jugador j : jugadores) {
            System.out.println(j);
        }
    }

    // listar avatares
    private void listarAvatares() {
        for (Avatar a : avatares) {
            System.out.println(a);
        }
    }

    // acabar turno
    private void acabarTurno() {
        if (!tirado) {
            System.out.println("Debes lanzar los dados antes de acabar el turno.");
            return;
        }
        turno = (turno + 1) % jugadores.size();
        tirado = false;
        lanzamientos = 0;   // resetea contador de dobles del turno
        solvente = true;
        System.out.println("Turno terminado. Ahora juega: " + jugadores.get(turno).getNombre());
    }
}
