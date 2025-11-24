package monopoly;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Menu {

    // Único atributo: La instancia del juego
    private monopoly.Juego juego;

    public Menu() {
        this.juego = new monopoly.Juego();
    }

    public void iniciarPartida() {
        System.out.println("Bienvenido al Monopoly");
        juego.mostrarTablero();
        System.out.println("Introduce comandos. Escribe 'salir' para terminar.\n");
        procesarComandos();
    }

    private void procesarComandos() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            if (sc.hasNextLine()) {
                String comando = sc.nextLine().trim();
                if (comando.equalsIgnoreCase("salir")) {
                    System.out.println("Fin de la partida.");
                    break;
                }
                analizarComando(comando);
            } else {
                break;
            }
        }
    }

    private void analizarComando(String comando) {
        if (comando.isEmpty()) return;
        String[] partes = comando.split(" ");
        String cmd = partes[0].toLowerCase();

        switch (cmd) {
            case "crear":
                if (partes.length == 4 && partes[1].equalsIgnoreCase("jugador")) {
                    juego.crearJugador(partes[2], partes[3]);
                } else {
                    System.out.println("Uso: crear jugador <nombre> <tipo_avatar>");
                }
                break;

            case "jugador":
                juego.mostrarJugadorEnTurno();
                break;

            case "listar":
                if (partes.length >= 2) {
                    if (partes[1].equalsIgnoreCase("jugadores")) juego.listarJugadores();
                    else if (partes[1].equalsIgnoreCase("enventa")) juego.listarVenta();
                    else if (partes[1].equalsIgnoreCase("avatares")) juego.listarAvatares();
                    else if (partes[1].equalsIgnoreCase("edificios")) {
                        if (partes.length >= 3) juego.listarEdificiosGrupo(partes[2]);
                        else juego.listarEdificios();
                    }
                }
                break;

            case "describir":
                if (partes.length >= 3) {
                    if (partes[1].equalsIgnoreCase("jugador")) juego.descJugador(partes[2]);
                    else if (partes[1].equalsIgnoreCase("avatar")) juego.descAvatar(partes[2]);
                } else if (partes.length >= 2) {
                    juego.descCasilla(partes[1]);
                } else {
                    System.out.println("Uso: describir <casilla> | jugador <nombre> | avatar <id>");
                }
                break;

            case "lanzar":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("dados")) {
                    if (partes.length == 2) {
                        juego.lanzarDados();
                    } else {
                        // lanzar dados 2+3
                        String[] d = partes[2].split("\\+");
                        if (d.length == 2) {
                            try {
                                int a = Integer.parseInt(d[0]);
                                int b = Integer.parseInt(d[1]);
                                juego.lanzarDadosForzada(a, b);
                            } catch (NumberFormatException e) {
                                System.out.println("Números inválidos.");
                            }
                        }
                    }
                } else {
                    System.out.println("Uso: lanzar dados");
                }
                break;

            case "comprar":
                if (partes.length >= 2) juego.comprar(partes[1]);
                break;

            case "salir":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("cárcel")) juego.salirCarcel();
                break;

            case "acabar":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("turno")) juego.acabarTurno();
                break;

            case "ver":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("tablero")) juego.mostrarTablero();
                break;

            case "edificar":
                if (partes.length >= 2) juego.edificar(partes[1]);
                else System.out.println("Uso: edificar <tipo>");
                break;

            case "vender":
                // Parsing de argumentos sigue aquí, pero la lógica va a Juego
                if (partes.length >= 4) {
                    try {
                        juego.venderEdificio(partes[1], partes[2], Integer.parseInt(partes[3]));
                    } catch (NumberFormatException e) {
                        System.out.println("Cantidad debe ser número.");
                    }
                } else if (partes.length >= 3) {
                    juego.venderEdificio(partes[1], partes[2], 1);
                } else {
                    System.out.println("Uso: vender <tipo> <solar> [cantidad]");
                }
                break;

            case "hipotecar":
                if (partes.length >= 2) juego.hipotecar(partes[1]);
                break;

            case "deshipotecar":
                if (partes.length >= 2) juego.deshipotecar(partes[1]);
                break;

            case "estadisticas":
                if (partes.length >= 2) juego.mostrarEstadisticas(partes[1]);
                else juego.mostrarEstadisticasJuego();
                break;

            case "mover":
                if (partes.length >= 2) juego.moverChetada(partes[1]);
                break;

            case "listargrupo":
                Scanner sc = new Scanner(System.in);
                System.out.print("Grupo: ");
                juego.listarCasillasGrupo(sc.nextLine().trim());
                break;

            default:
                System.out.println("Comando no reconocido.");
        }
    }

    public boolean procesarFichero(String fichero) {
        try (BufferedReader buffer = new BufferedReader(new FileReader(fichero))) {
            String linea;
            while ((linea = buffer.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                System.out.println("> " + linea);
                if (linea.equalsIgnoreCase("salir")) return true;
                analizarComando(linea);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado.");
        } catch (IOException e) {
            System.out.println("Error de lectura.");
        }
        return false;
    }
}