package monopoly;

import java.util.*;
import partida.*;
import monopoly.casillas.Casilla;

public class Menu {

    //Atributos
    private ArrayList<Jugador> jugadores; //Jugadores de la partida.
    private ArrayList<Avatar> avatares; //Avatares en la partida.
    private int turno = 0; //Índice correspondiente a la posición en el arrayList del jugador (y el avatar) que tienen el turno
    private int lanzamientos = 0; //Variable para contar el número de lanzamientos de un jugador en un turno.
    private Tablero tablero; //Tablero en el que se juega.
    private Dado dado1; //Dos dados para lanzar y avanzar casillas.
    private Dado dado2;
    private Jugador banca; //El jugador banca.
    private boolean tirado = false; //Booleano para comprobar si el jugador que tiene el turno ha tirado o no.
    private boolean solvente = true; //Booleano para comprobar si el jugador que tiene el turno es solvente, es decir, si ha pagado sus deudas.

    //CONSTRUCTOR
    public Menu(){
        this.banca = new Jugador();
        this.tablero = new Tablero(banca);
        this.dado1 = new Dado();
        this.dado2 = new Dado();
    }

    // Método para inciar una partida: crea los jugadores y avatares.
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


    /*Método que interpreta el comando introducido y toma la accion correspondiente.
    * Parámetro: cadena de caracteres (el comando).
    */
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
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("dados"))
                   // lanzarDados();
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

    private void mostrarTablero(){
        tablero.mostrarTablero();
    }

    private void crearJugador(String nombre, String tipoAvatar) {
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

        Casilla salida = tablero.getCasilla(0);
        Jugador nuevo = new Jugador(nombre, tipoAvatar, salida, avatares);
        Avatar avatar = nuevo.getAvatar();

        jugadores.add(nuevo);
        avatares.add(avatar);

        System.out.printf("{nombre: %s, avatar: %s}%n", nombre, avatar.getId());
        tablero.mostrarTablero();
    }

    /*Método que realiza las acciones asociadas al comando 'describir jugador'.
    * Parámetro: comando introducido
     */
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

    /*Método que realiza las acciones asociadas al comando 'describir avatar'.
    * Parámetro: id del avatar a describir.
    */
    private void descAvatar(String ID) {
        for (Avatar a : avatares) {
            if (a.getId().equalsIgnoreCase(ID)) {
                System.out.println(a);
                return;
            }
        }
        System.out.println("No existe un avatar con ID: " + ID);
    }

    /* Método que realiza las acciones asociadas al comando 'describir nombre_casilla'.
    * Parámetros: nombre de la casilla a describir.
    */
    private void descCasilla(String nombre) {
        Casilla c = tablero.encontrar_casilla(nombre);
        if (c != null)
            System.out.println(c.infoCasilla());
        else
            System.out.println("Casilla no encontrada: " + nombre);
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'lanzar dados'.
   /* private void lanzarDados() {
        if (jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida.");
            return;
        }

        Jugador actual = jugadores.get(turno);
        int d1 = dado1.tirar();
        int d2 = dado2.tirar();
        int suma = d1 + d2;

        System.out.printf("%s ha sacado %d + %d = %d%n", actual.getNombre(), d1, d2, suma);
        actual.mover(suma,tablero);
        tirado = true;
    }
        */
    /*Método que ejecuta todas las acciones realizadas con el comando 'comprar nombre_casilla'.
    * Parámetro: cadena de caracteres con el nombre de la casilla.
     */
    private void comprar(String nombre) {
        Jugador actual = jugadores.get(turno);
        Casilla cas = tablero.encontrar_casilla(nombre);
        if (cas != null) {
            cas.comprarCasilla(actual, banca);
        } else {
            System.out.println("Esa casilla no existe.");
        }
    }

    //Método que ejecuta todas las acciones relacionadas con el comando 'salir carcel'. 
    private void salirCarcel() {
        Jugador actual = jugadores.get(turno);
        if (actual.isEnCarcel()) {
            actual.salirDeCarcel();
        } else {
            System.out.println("No estás en la cárcel.");
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar enventa'.
    private void listarVenta() {
        for(Casilla c: tablero.getCasillas()) {
            if (c.getDuenho() == banca) {
                System.out.println(c.casEnVenta());
            }
        }

    }

    // Método que realiza las acciones asociadas al comando 'listar jugadores'.
    private void listarJugadores() {
        for (Jugador j : jugadores) {
            System.out.println(j);
        }
    }

    // Método que realiza las acciones asociadas al comando 'listar avatares'.
    private void listarAvatares() {
        for (Avatar a : avatares) {
            System.out.println(a);
        }
    }

    // Método que realiza las acciones asociadas al comando 'acabar turno'.
    private void acabarTurno() {
        if (!tirado) {
            System.out.println("Debes lanzar los dados antes de acabar el turno.");
            return;
        }
        turno = (turno + 1) % jugadores.size();
        tirado = false;
        System.out.println("Turno terminado. Ahora juega: " + jugadores.get(turno).getNombre());
    }

}
