/*package partida;

import monopoly.*;
import monopoly.casillas.Casilla;

import java.io.*;
import java.util.*;

public class Partida {
    private Tablero tablero;
    private List<Jugador> jugadores;
    private Jugador banca;
    private int turnoActual; // Indice jugador actual
    private List<Avatar> avataresCreados= new ArrayList<>();
    Casilla salida= tablero.getCasilla(0);

    public Partida() {
        this.banca = new Jugador(); // Por ejemplo
        this.tablero = new Tablero(banca);
        this.jugadores = new ArrayList<>();
        this.turnoActual = 0;
    }

    /*public void procesarFichero(String fichero) {
        try (BufferedReader buffer = new BufferedReader(new FileReader(fichero))) {
            String linea;
            while ((linea = buffer.readLine()) != null) {
                procesarComando(linea.trim());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado: " + fichero);
        } catch (IOException e) {
            System.out.println("Error leyendo el archivo: " + fichero);
        }
    }

    public void procesarComandos() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingresa comandos. Escribe 'salir' para terminar.");
        while (true) {
            System.out.print(">");
            String linea = sc.nextLine().trim();
            if (linea.equals("salir")) {
                System.out.println("Saliendo...");
                break;
            } else {
                procesarComando(linea);
            }
        }
    }
    */

    /*public void procesarComando(String linea) {
        if (linea.isEmpty()) return;
        String[] partes = linea.split(" ");
        switch (partes[0].toLowerCase()) {
            case "crear":
                if (partes.length == 4 && partes[1].equalsIgnoreCase("jugador")) {
                    crearJugador(partes[2], partes[3]);
                }
                break;
            case "jugador":
                System.out.println(getJugadorActual());
                break;
            case "listar":
                if (partes.length >= 2) {
                    if (partes[1].equalsIgnoreCase("jugadores")) {
                        listarJugadores();
                    } else if (partes[1].equalsIgnoreCase("enventa")) {
                        listarPropiedadesEnVenta();
                    }
                }
                break;
            case "lanzar":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("dados")) {
                    if (partes.length == 2) {
                        lanzarDados();
                    } else {
                        String[] dados = partes[2].split("\\+");
                        int dado1 = Integer.parseInt(dados[0]);
                        int dado2 = Integer.parseInt(dados[1]);
                        lanzarDados(dado1, dado2);
                    }
                }
                break;
            case "acabar":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("turno")) {
                    acabarTurno();
                }
                break;
            case "salir":
                if (partes.length >= 2 && (partes[1].equalsIgnoreCase("carcel") || partes[1].equalsIgnoreCase("carcel"))) {
                    salirCarcel();
                }
                break;
            case "describir":
                if (partes.length >= 2) {
                    if (partes[1].equalsIgnoreCase("jugador") && partes.length >= 3) {
                        describirJugador(partes[2]);
                    } else {
                        describirCasilla(partes[1]);
                    }
                }
                break;
            case "comprar":
                if (partes.length >= 2) {
                    comprar(partes[1]);
                }
                break;
            case "ver":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("tablero")) {
                    verTablero();
                }
                break;
            default:
                System.out.println("Comando no reconocido: " + linea);
        }
    }*/

    // Métodos de juego
   /* public void crearJugador(String nombre, String tipoavatar) {
        //Primero compruebo que el jugador no exista ya
        for(Jugador j : jugadores){
            if(j.getNombre().equalsIgnoreCase(nombre)){
                System.out.println("Ya hay un jugador con ese nombre");
                return;
            }
        }
        //Comprobar que no se supera el numero máximo de jugadores
        if (jugadores.size()>=4){
            System.out.println("No se puede crar otro jugadro, se excede el máximo(4)");
            return;
        }
        
        Jugador nuevoJugador = new Jugador(nombre,tipoavatar,salida, (ArrayList<Avatar>) avataresCreados);
        //Creo el avatar del jugador
        Avatar nuevoAvatar = new Avatar(tipoavatar,nuevoJugador,salida, (ArrayList<Avatar>) avataresCreados);
        nuevoJugador.setAvatar(nuevoAvatar);
        jugadores.add(nuevoJugador);
        System.out.println("{nombre: " + nombre + ", avatar: " + nuevoAvatar.getId() + "}");
        tablero.mostrarTablero();

    }
    public String getJugadorActual() { ... }
    public void listarJugadores() { ... }
    public void lanzarDados() { ... }
    public void lanzarDados(int d1, int d2) { ... }
    public void acabarTurno() { ... }
    public void salirCarcel() { ... }
    public void describirJugador(String nombre) { ... }
    public void comprar(String nombrePropiedad) { ... }
}*/