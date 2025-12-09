package monopoly;
import  monopoly.Juego;
import excepciones.*;
import excepciones.accionNoValida.JugadorEnCarcelNoPuedeComprarException;
import excepciones.accionNoValida.PropiedadNoHipotecadaException;
import excepciones.accionNoValida.PropiedadYaHipotecadaException;
import excepciones.accionNoValida.PropiedadYaTieneDuenhoException;
import excepciones.objetoNoExiste.JugadorNoExisteException;
import excepciones.objetoNoExiste.PropiedadNoExisteException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Menu {

    // CAMBIO 1: El tipo del atributo ahora es la Interfaz 'Comando'
    private Comando juego;

    public Menu() {
        // CAMBIO 2: Instanciamos Juego, pero lo guardamos en la variable de tipo Comando
        // Esto es polimorfismo.
        this.juego = new Juego();
    }

    public void iniciarPartida() {
        imprimir("Bienvenido al Monopoly");
        juego.mostrarTablero();
        imprimir("Introduce comandos. Escribe 'salir' para terminar.\n");
        procesarComandos();
    }



private void procesarComandos() {
    while (true) {
        String comando = leer("> ");

        if (comando.equalsIgnoreCase("salir")) {
            imprimir("Fin de la partida.");
            break;
        }

        try {
        
            analizarComando(comando);

        } catch (PropiedadNoExisteException e) {
            imprimir("Error: La propiedad indicada no existe.");
        } catch (JugadorNoExisteException e) {
            imprimir("Error: El jugador indicado no existe.");
        } catch (JugadorEnCarcelNoPuedeComprarException e) {
            imprimir("Error: El jugador se encuentra en cárcel y no puede comprar propiedades.");
        } catch (PropiedadYaTieneDuenhoException e) {
            imprimir("Error: La propiedad ya tiene dueño.");
        } catch (PropiedadYaHipotecadaException e) {
            imprimir("Error: La propiedad ya está hipotecada.");
        } catch (PropiedadNoHipotecadaException e) {
            imprimir("Error: La propiedad no está hipotecada.");
        } catch (Excepcion e) {
            // Cualquier otra excepción propia del juego
            imprimir("Error: " + e.getMessage());
        } catch (Exception e) {
            // Por si se cuela algo no previsto
            imprimir("Error inesperado: " + e.toString());
        }
    }
}




    private void analizarComando (String comando) throws Excepcion {
        if (comando.isEmpty()) return;
        String[] partes = comando.split(" ");
        String cmd = partes[0].toLowerCase();

        // El resto del switch NO cambia, porque todos los métodos que llamas
        // (juego.crearJugador, juego.lanzarDados, etc.) están definidos en la interfaz Comando.
        switch (cmd) {
            case "crear":
                if (partes.length == 4 && partes[1].equalsIgnoreCase("jugador")) {
                    juego.crearJugador(partes[2], partes[3]);
                } else {
                    imprimir("Uso: crear jugador <nombre> <tipo_avatar>");
                    imprimir("Avatares: Coche, Esfinge, Sombrero, Pelota");
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
                    else if (partes[1].equalsIgnoreCase("tratos")) juego.listarTratos();
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
                    imprimir("Uso: describir <casilla> | jugador <nombre> | avatar <id>");
                }
                break;

            case "lanzar":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("dados")) {
                    if (partes.length == 2) {
                        juego.lanzarDados();
                    } else {
                        String[] d = partes[2].split("\\+");
                        if (d.length == 2) {
                            try {
                                int a = Integer.parseInt(d[0]);
                                int b = Integer.parseInt(d[1]);
                                juego.lanzarDadosForzada(a, b);
                            } catch (NumberFormatException e) {
                                imprimir("Números inválidos.");
                            }
                        }
                    }
                } else {
                    imprimir("Uso: lanzar dados");
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
                else imprimir("Uso: edificar <tipo>");
                break;

            case "vender":
                if (partes.length >= 4) {
                    try {
                        juego.venderEdificio(partes[1], partes[2], Integer.parseInt(partes[3]));
                    } catch (NumberFormatException e) {
                        imprimir("Cantidad debe ser número.");
                    }
                } else if (partes.length >= 3) {
                    juego.venderEdificio(partes[1], partes[2], 1);
                } else {
                    imprimir("Uso: vender <tipo> <solar> [cantidad]");
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
                String grupo = leer("Grupo: ");
                juego.listarCasillasGrupo(grupo);
                break;
            case "trato":
                if (partes.length >= 3) {
                    String nombreDest = partes[1].replace(":", "");
                    String resto = comando.substring(comando.indexOf("cambiar")). trim();

                    // Extraer los elementos entre paréntesis
                    int inicio = resto.indexOf("(");
                    int fin = resto.indexOf(")");
                    if (inicio != -1 && fin != -1) {
                        String contenido = resto.substring(inicio + 1, fin);
                        String[] elementos = contenido.split(",");
                        juego.proponerTrato(nombreDest, elementos);
                    } else {
                        imprimir("Uso: trato <jugador>: cambiar (elem1, elem2)");
                    }
                } else {
                    imprimir("Uso: trato <jugador>: cambiar (elem1, elem2)");
                }
                break;

            case "aceptar":
                if (partes.length == 2) {
                    juego.aceptarTrato(partes[1]);

                } else {
                    imprimir("Uso: aceptar trato <id>");
                }
                break;
            case "eliminar":
                if (partes.length >= 3 && partes[1].equalsIgnoreCase("trato")|| partes.length==2) {
                    juego.eliminarTrato(partes[1]);
                } else {
                    imprimir("Uso: eliminar trato <id>");
                }
                break;

            default:
                imprimir("Comando no reconocido.");
        }
    }

public boolean procesarFichero(String fichero) {
    try (BufferedReader buffer = new BufferedReader(new FileReader(fichero))) {
        String linea;
        while ((linea = buffer.readLine()) != null) {
            linea = linea.trim();
            if (linea.isEmpty()) continue;

            imprimir("> " + linea);

            // Si el propio fichero pone "salir", terminamos y devolvemos true
            if (linea.equalsIgnoreCase("salir")) {
                return true;
            }

            try {
                // Aquí se pueden lanzar tus excepciones del juego
                analizarComando(linea);

            } catch (PropiedadNoExisteException e) {
                imprimir("Error: La propiedad indicada no existe.");
            } catch (JugadorNoExisteException e) {
                imprimir("Error: El jugador indicado no existe.");
            } catch (JugadorEnCarcelNoPuedeComprarException e) {
                imprimir("Error: El jugador en carcel no puede comprar propiedades.");
            } catch (PropiedadYaTieneDuenhoException e) {
                imprimir("Error: La propiedad ya tiene duenho.");
            } catch (PropiedadYaHipotecadaException e) {
                imprimir("Error: La propiedad ya esta hipotecada.");
            } catch (PropiedadNoHipotecadaException e) {
                imprimir("Error: La propiedad no esta hipotecada.");
            } catch (Excepcion e) {
                // Cualquier otra excepcion propia del juego
                imprimir("Error: " + e.getMessage());
            } catch (Exception e) {
                // Por si se cuela algo no previsto
                imprimir("Error inesperado: " + e.toString());
            }
        }
    } catch (FileNotFoundException e) {
        imprimir("Archivo no encontrado.");
    } catch (IOException e) {
        imprimir("Error de lectura.");
    }
    // Si hemos llegado al final del fichero sin ver "salir"
    return false;
}


    // MÉTODOS AUXILIARES (WRAPPERS)
    private void imprimir(String mensaje) {
        // Aquí seguimos accediendo a Juego.consola estáticamente.
        // Esto es válido porque consola es estático en la CLASE Juego,
        // independientemente de la interfaz Comando.
        Juego.consola.imprimir(mensaje);
    }

    private String leer(String descripcion) {
        return Juego.consola.leer(descripcion);
    }
}