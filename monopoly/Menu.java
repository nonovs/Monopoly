package monopoly;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import partida.*;
import monopoly.casillas.Casilla;
import monopoly.casillas.Solar;
import monopoly.Construccion.Edificio;
import java.text.Normalizer;

import static partida.GestorEdificaciones.eliminarEdificio;


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
                    else if (partes[1].equalsIgnoreCase("edificios")) {
                        if (partes.length >= 3){
                            listarEdificiosGrupo(partes[2]);
                        }else listarEdificios();
                    }
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
            case "listargrupo":
                Scanner sc = new Scanner(System.in);
                System.out.print("Ingrese el nombre del grupo: ");
                String grupo = sc.nextLine().trim();
                listarCasillasGrupo(grupo);
                break;
            case "comprar":
                if (partes.length >= 2)
                    comprar(partes[1]);
                else
                    System.out.println("Uso: comprar <nombre_casilla>");
                break;

            case "salir":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("cárcel"))
                    salirCarcel();
                break;

            case "acabar":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("turno"))
                    acabarTurno();
                break;
            case "ver":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("tablero"))
                    mostrarTablero();
                break;

            case "edificar":
                // nuevo comando: edificar <tipo>
                if (partes.length >= 2) {
                    edificar(partes[1]);
                } else {
                    System.out.println("Uso: edificar <casa|hotel|piscina|pista_deporte>");
                }
                break;

            case "vender":
                    if (partes.length >= 3) {
                        venderEdificio(partes[1], partes[2]);


                    }else System.out.println("Uso: vender <edificio> <solar>");
                
            /*case "hipotecar":
                if (partes.length >= 2) {
                    hipotecar(partes[1]);
                } else {
                    System.out.println("Uso: hipotecar <nombre_casilla>");
                }
                break;

            case "deshipotecar":
                if (partes.length >= 2){
                    deshipotecar(partes[1]);
                } else {
                    System.out.println("Uso: deshipotecar <nombre_casilla>");
                }
                break;*/

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
                System.out.println("  ver tablero");
                System.out.println("  edificar <casa|hotel|piscina|pista_deporte>");
                System.out.println("  vender <edificio> <solar>");
                System.out.println("  hipotecar <nombre_casilla>");
                System.out.println("  deshipotecar <nombre_casilla>");
        }
    }

    private void mostrarTablero() {
        tablero.mostrarTablero();
    }

    private void crearJugador(String nombre, String tipoAvatar) {
        // evitar duplicados en nombre
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
        if (!tipoAvatar.equalsIgnoreCase("Coche") && !tipoAvatar.equalsIgnoreCase("Esfinge") && !tipoAvatar.equalsIgnoreCase("Sombrero") && !tipoAvatar.equalsIgnoreCase("Pelota")) {
            System.out.println("No se puede crear un jugador con ese tipo de avatar.");

            return;

        }

        Casilla salida = tablero.encontrar_casilla("Salida");
        if (salida == null) {
            System.out.println("No se encontró la casilla Salida.");
            return;
        }

        // crear jugador (si tu constructor ya crea avatar, lo devolverá en getAvatar)
        Jugador nuevo = new Jugador(nombre, tipoAvatar, salida, avatares);
        Avatar avatar = nuevo.getAvatar();

        // Si el constructor del Jugador NO creó el avatar, créalo aquí
        if (avatar == null) {
            avatar = new Avatar(tipoAvatar, nuevo, salida, avatares);
            nuevo.setAvatar(avatar);
        }

        // registrar jugador sin duplicar avatar en la lista de avatares
        jugadores.add(nuevo);
        boolean ya = false;
        for (Avatar a : avatares) {
            if (a.getId().equalsIgnoreCase(avatar.getId())) { ya = true; break; }
        }
        if (!ya) avatares.add(avatar);

        // añadir avatar a la casilla Salida solo si no está ya
        boolean presente = false;
        for (Avatar a : salida.getAvatares()) {
            if (a.getId().equalsIgnoreCase(avatar.getId())) { presente = true; break; }
        }
        if (!presente) {
            salida.anhadirAvatar(avatar);
            // sincronizar lugar del avatar (muy importante para que enviarACarcel elimine correctamente)
            avatar.setLugar(salida);
        }

        // sincronizar posicion del jugador
        nuevo.setPosicion(salida.getPosicion());

        System.out.printf("{nombre: %s, avatar: %s}%n", nombre, avatar.getId());
        tablero.mostrarTablero();
    }
    // --- muestra el jugador que tiene el turno actual ---
    private void mostrarJugadorEnTurno() {
        if (jugadores == null || jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida");
            return;
        }

        Jugador actual = jugadores.get(turno);
        String avatarId = (actual.getAvatar() != null) ? actual.getAvatar().getId() : "-";
        System.out.printf("Turno de: %s (avatar %s) - posicion %d%n",
                actual.getNombre(), avatarId, actual.getPosicion());

        // si esta en carcel, solo informar
        if (actual.isEnCarcel()) {
            System.out.println(actual.getNombre() + " esta en la carcel y debe pagar 500000 para salir");
            System.out.println("Usa 'salir carcel' para pagar y poder lanzar");
        }
    }

    /**
     * abjalskj
     * Nuevo método: edificar el tipo pedido para el jugador que tiene el turno.
     * Delegamos la lógica en GestorEdificaciones.
     */
    private void edificar(String tipo) {
        if (jugadores == null || jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida.");
            return;
        }
        Jugador actual = jugadores.get(turno);
        // Delegar en GestorEdificaciones
        GestorEdificaciones.edificar(actual, tipo);
    }

    public boolean procesarFichero(String fichero) {
        try (BufferedReader buffer = new BufferedReader(new FileReader(fichero))) {
            String linea;
            while ((linea = buffer.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                // eco opcional para seguir la ejecución del fichero
                System.out.println("> " + linea);

                // Si la línea es exactamente "salir" (sin argumentos), solicitamos terminar la ejecución
                if (linea.equalsIgnoreCase("salir")) {
                    System.out.println("Se encontró 'salir' en el fichero. Terminando ejecución según especificación.");
                    return true;
                }

                // ejecutar la línea como comando normal
                analizarComando(linea);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Archivo no encontrado: " + fichero);
        } catch (IOException e) {
            System.out.println("Error leyendo el archivo: " + fichero);
        }
        return false;
    }

    /* describir jugador <nombre> */
    private void descJugador(String[] partes) {
        String nombre = partes[2];
        for (Jugador j : jugadores) {
            if (j.getNombre().equalsIgnoreCase(nombre)) {

                String avatarId = (j.getAvatar() != null) ? j.getAvatar().getId() : "-";

                //Propiedades
                ArrayList<String> props = new ArrayList<>();
                for (Casilla c : j.getPropiedades())
                    props.add((c.getNombre()));

                // Hipotecas
                ArrayList<String> hips = new ArrayList<>();
                for (Casilla c : j.getHipotecadas())
                    hips.add(c.getNombre());

                // Edificios: extraemos desde las propiedades tipo Solar
                ArrayList<String> edifs = new ArrayList<>();
                for (Casilla c : j.getPropiedades()) {
                    if (c instanceof Solar) {
                        Solar s = (Solar) c;
                        for (Edificio e : s.getEdificaciones()) {
                            edifs.add(e.getId());
                        }
                    }
                }

                System.out.println("{");
                System.out.println("  nombre: " + j.getNombre() + ",");
                System.out.println("  avatar: " + avatarId + ",");
                System.out.println("  fortuna: " + String.format("%.0f", j.getFortuna()) + ",");
                System.out.println("  propiedades: " + (props.isEmpty() ? "-" : props) + ",");
                System.out.println("  hipotecas: " + (hips.isEmpty() ? "-" : hips) + ",");
                System.out.println("  edificios: " + (edifs.isEmpty() ? "-" : edifs));
                System.out.println("}");
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

        // si es la primera tirada del turno resetea estado del dado
        if (lanzamientos == 0) {
            dado.iniciarTurno();
        }

        int suma = dado.tirar();
        int d1 = dado.getD1();
        int d2 = dado.getD2();
        lanzamientos++;
        tirado = true;

        // si esta en carcel se aplica la regla especial y salimos si ya resolvio
        if (actual.isEnCarcel()) {
            boolean resuelto = procesarTiradaEnCarcel(actual, d1, d2, suma);
            if (!resuelto) {
                // sigue preso y no se mueve
                return;
            }
            // si se resolvio (salio y quiza se movio) no se aplica logica de dobles normal
            return;
        }

        // flujo normal fuera de carcel
        System.out.printf("%s tira los dados -> %d + %d = %d%n", actual.getNombre(), d1, d2, suma);
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

        int suma = dado.tirarForzado(a, b);
        int d1 = dado.getD1();
        int d2 = dado.getD2();
        lanzamientos++;
        tirado = true;

        if (actual.isEnCarcel()) {
            boolean resuelto = procesarTiradaEnCarcel(actual, d1, d2, suma);
            if (!resuelto) return;
            return;
        }

        System.out.printf("%s tira dados forzados -> %d + %d = %d%n", actual.getNombre(), d1, d2, suma);
        moverYEvaluar(actual, suma);

        if (dado.esDoble()) {
            if (lanzamientos >= 3) {
                System.out.println("Tres dobles en el mismo turno. Vas a la carcel");
                irACarcel(actual);
                mostrarTablero();
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
        if (origen != null && j.getAvatar() != null) {
            origen.eliminarAvatar(j.getAvatar());
        }
        if (destino != null && j.getAvatar() != null) {
            destino.anhadirAvatar(j.getAvatar());
            // sincronizar lugar del avatar para mantener coherencia entre Avatar y Casilla
            j.getAvatar().setLugar(destino);
        }

        // actualizar posicion en jugador
        j.setPosicion(posFin);

        // Detectar "Ir a la carcel" usando el método de Casilla (más robusto que comparar con 30).
        if (destino != null && destino.esIrACarcel()) {
            System.out.println("Vas a la carcel");
            irACarcel(j);
            mostrarTablero();
            return; // no evaluar mas esta tirada
        }

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
        Casilla carcel = tablero.getCasilla(10); // tu carcel es la pos 10
        /*if (carcel == null) {
            System.out.println("No se encontro la casilla de carcel");
            return;
        }*/
        j.enviarACarcel(carcel); // marca flag, posiciona, mueve avatar y reinicia contadores
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
        if (!actual.isEnCarcel()) {
            System.out.println("No estas en la carcel.");
            return;
        }

        // comprobar saldo
        if (actual.getFortuna() < 500000) {
            System.out.println("No tienes suficiente dinero para pagar la fianza (500000).");
            return;
        }

        // pagar y salir

        comprobarCarcel(actual.getAvatar().getJugador());// este metodo ya imprime el mensaje de salida
    }


    // listar en venta
    private void listarVenta() {
        for (Casilla c : tablero.getCasillas()) {
            if (c.getDuenho() == banca) {
                String info = c.casEnVenta();
                if (!info.isEmpty()) {
                    System.out.println(info + ",");
                }
            }
        }
    }
    /*Listar en venta pero que eu lle mande a cor do grupo(cales do grupoo azul estan en venta)*/
    private void listarCasillasGrupo(String colorgrupo){

        for (Casilla c : tablero.getCasillas()){
            if (c.getGrupo()!= null && c.getGrupo().getColor().equalsIgnoreCase(colorgrupo) && c.getDuenho() == banca){
                String info = c.casEnVenta();
                if (!info.isEmpty()) {
                    System.out.println(info + ",");
                }
            }
        }


    }
    // listar jugadores
    private void listarJugadores() {
        if (jugadores.isEmpty()){
            System.out.println("No hay jugadores en la partida.");
            return;
        }
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador j = jugadores.get(i);

            String avatarId = (j.getAvatar() != null) ? j.getAvatar().getId() : "-";

            // Propiedades
            ArrayList<String> props = new ArrayList<>();
            for (Casilla c : j.getPropiedades())
                props.add(c.getNombre());

            // Hipotecas
            ArrayList<String> hips = new ArrayList<>();
            for (Casilla c : j.getHipotecadas())
                hips.add(c.getNombre());

            // Edificios: extraer ids desde las propiedades tipo Solar
            ArrayList<String> edifs = new ArrayList<>();
            for (Casilla c : j.getPropiedades()) {
                if (c instanceof Solar) {
                    Solar s = (Solar) c;
                    for (Edificio e : s.getEdificaciones()) {
                        String casillaNombre = (e.getSolar() != null) ? e.getSolar().getNombre() : s.getNombre();
                        String tipo = (e.getTipo() != null) ? e.getTipo() : "-";
                        String detalle = String.format("{id:%s, tipo:%s, casilla:%s, coste:%.0f } %n",
                                e.getId(), tipo, casillaNombre, e.getPrecio());
                        edifs.add(detalle);
                    }
                }
            }

            System.out.println("{");
            System.out.println("  nombre: " + j.getNombre() + ",");
            System.out.println("  avatar: " + avatarId + ",");
            System.out.println("  fortuna: " + String.format("%.0f", j.getFortuna()) + ",");
            System.out.println("  propiedades: " + (props.isEmpty() ? "-" : props) + ",");
            System.out.println("  hipotecas: " + (hips.isEmpty() ? "-" : hips) + ",");
            System.out.println("  edificios: " + (edifs.isEmpty() ? "-" : edifs));
            System.out.print("}");

            // Si no es el último jugador, añadir coma
            if (i < jugadores.size() - 1) {
                System.out.println(",");
            } else {
                System.out.println();
            }
        }
    }

    // listar avatares
    private void listarAvatares() {
        for (Avatar a : avatares) {
            System.out.println(a);
        }
    }
    //listar Edificios

    private void listarEdificios(){
        // Recorremos todas las casillas, buscamos los Solar y listamos cada Edificio
        boolean encontrado = false;
        List<Casilla> casillas = tablero.getCasillas();
        if (casillas == null || casillas.isEmpty()) {
            System.out.println("No hay casillas en el tablero.");
            return;
        }

        for (Casilla c : casillas) {
            if (c instanceof Solar) {
                Solar s = (Solar) c;
                List<Edificio> edificaciones = s.getEdificaciones();
                if (edificaciones == null || edificaciones.isEmpty()) continue;

                for (Edificio e : edificaciones) {
                    encontrado = true;
                    String id = e.getId();
                    String propietario = (s.getDuenho() != null) ? s.getDuenho().getNombre() : "sin propietario";
                    String casilla = s.getNombre();
                    String grupo = (s.getGrupo() != null && s.getGrupo().getColor() != null) ? s.getGrupo().getColor() : "-";
                    float coste = e.getPrecio();

                    System.out.println("{");
                    System.out.println(" id: " + id + ",");
                    System.out.println(" propietario: " + propietario + ",");
                    System.out.println(" casilla: " + casilla + ",");
                    System.out.println(" grupo: " + grupo + ",");
                    System.out.println(" coste: " + String.format("%.0f", coste));
                    System.out.println("},");
                }
            }
        }

        if (!encontrado) {
            System.out.println("No hay edificios construidos.");
        }
    }


    private void listarEdificiosGrupo(String colorGrupo) {
        if (colorGrupo == null || colorGrupo.trim().isEmpty()) {
            System.out.println("Uso: listar edificios <color_grupo>");
            return;
        }
        colorGrupo = colorGrupo.trim();

        List<Casilla> casillas = tablero.getCasillas();
        if (casillas == null || casillas.isEmpty()) {
            System.out.println("No hay casillas en el tablero.");
            return;
        }

        boolean tienealgo = false;

        // Flags para saber si a nivel de grupo queda posibilidad de construir cada tipo
        boolean puedeCasa = false;
        boolean puedeHotel = false;
        boolean puedePiscina = false;
        boolean puedePista = false;

        // Lista de solares del grupo para imprimir en el mismo orden del tablero
        List<Solar> solaresGrupo = new ArrayList<>();

        for (Casilla c : casillas) {
            if (c instanceof Solar) {
                Solar s = (Solar) c;
                if (s.getGrupo() != null && s.getGrupo().getColor() != null
                        && s.getGrupo().getColor().equalsIgnoreCase(colorGrupo)) {
                    tienealgo = true;
                    solaresGrupo.add(s);
                }
            }
        }

        if (!tienealgo || solaresGrupo.isEmpty()) {
            System.out.println("No se encontraron solares del grupo: " + colorGrupo);
            return;
        }

        // Imprimir por cada solar sus edificaciones y calcular posibilidades
        for (Solar s : solaresGrupo) {
            List<Edificio> eds = s.getEdificaciones();
            List<String> casasIds = new ArrayList<>();
            List<String> hotelesIds = new ArrayList<>();
            List<String> piscinasIds = new ArrayList<>();
            List<String> pistasIds = new ArrayList<>();

            if (eds != null) {
                for (Edificio e : eds) {
                    String tipo = e.getTipo() != null ? e.getTipo().toLowerCase() : "";
                    switch (tipo) {
                        case "casa":
                            casasIds.add(e.getId());
                            break;
                        case "hotel":
                            hotelesIds.add(e.getId());
                            break;
                        case "piscina":
                            piscinasIds.add(e.getId());
                            break;
                        case "pista":
                        case "pistadeporte":
                        case "pista_deporte":
                        case "pista_deportes":
                        case "pistadeportes":
                            pistasIds.add(e.getId());
                            break;
                        default:
                            // si hay otros tipos, ignorar o añadir según necesites
                    }
                }
            }

            // Imprimir bloque del solar
            System.out.println("{");
            System.out.println(" propiedad: " + s.getNombre() + ",");
            System.out.println(" hoteles: " + (hotelesIds.isEmpty() ? "-" : hotelesIds.toString()) + ",");
            System.out.println(" casas: " + (casasIds.isEmpty() ? "-" : casasIds.toString()) + ",");
            System.out.println(" piscinas: " + (piscinasIds.isEmpty() ? "-" : piscinasIds.toString()) + ",");
            System.out.println(" pistasDeDeporte: " + (pistasIds.isEmpty() ? "-" : pistasIds.toString()) + ",");
            // alquiler actual calculado por Solar
            float alquiler = s.calcularAlquiler();
            System.out.println(" alquiler: " + String.format("%.0f", alquiler));
            System.out.println("},");

            // Determinar posibilidades para este solar (según reglas ya implementadas en GestorEdificaciones/Solar)
            Jugador duenho = s.getDuenho();
            Grupo grupo = s.getGrupo();

            // Casas: necesita ser dueño del grupo, no tener hotel y tener <4 casas
            if (duenho != null && grupo != null && grupo.esDuenhoGrupo(duenho) && s.getCasas() < 4 && !s.hasHotel()) {
                puedeCasa = true;
            }

            // Hotel: necesita 4 casas y no tener hotel y ser dueño del grupo
            if (duenho != null && grupo != null && grupo.esDuenhoGrupo(duenho) && s.getCasas() == 4 && !s.hasHotel()) {
                puedeHotel = true;
            }

            // Piscina: necesita hotel y no tener piscina (propietario no se requiere que tenga todo el grupo)
            if (s.hasHotel() && !s.hasPiscina()) {
                // además, normalmente debe pertenecer al dueño que construyó el hotel;
                // comprobamos que exista dueño (si no hay dueño no se puede edificar)
                if (duenho != null) puedePiscina = true;
            }

            // Pista: necesita hotel y piscina y no tener pista
            if (s.hasHotel() && s.hasPiscina() && !s.hasPistaDeporte()) {
                if (duenho != null) puedePista = true;
            }
        }

        // Mensajes finales: qué aún se puede edificar y qué no
        List<String> permitidos = new ArrayList<>();
        if (puedePista) permitidos.add("una pista de deporte");
        if (puedePiscina) permitidos.add("una piscina");
        if (puedeHotel) permitidos.add("un hotel");
        if (puedeCasa) permitidos.add("una casa");

        List<String> noPermitidos = new ArrayList<>();
        if (!puedePista) noPermitidos.add("pistas de deporte");
        if (!puedePiscina) noPermitidos.add("piscinas");
        if (!puedeHotel) noPermitidos.add("hoteles");
        if (!puedeCasa) noPermitidos.add("casas");

        if (!permitidos.isEmpty()) {
            // construir frase como en el ejemplo: "Aún se puede edificar una pista de deporte y una piscina."
            StringBuilder sb = new StringBuilder("Aún se puede edificar ");
            for (int i = 0; i < permitidos.size(); i++) {
                if (i > 0 && i == permitidos.size() - 1) sb.append(" y ");
                else if (i > 0) sb.append(", ");
                sb.append(permitidos.get(i));
            }
            sb.append(".");
            System.out.println(sb.toString());
        } else {
            System.out.println("Ya no se puede edificar ningún tipo de mejora en este grupo.");
        }

        // Frase sobre lo que ya no se puede construir (opcional, con el formato del ejemplo)
        if (!noPermitidos.isEmpty()) {
            // Si todos están prohibidos, mostrar frase tipo "Ya no se pueden construir ni hoteles ni casas."
            // Construimos lista con formato "hoteles", "piscinas", etc. y la unimos con " ni "
            StringBuilder sb2 = new StringBuilder("Ya no se pueden construir ");
            for (int i = 0; i < noPermitidos.size(); i++) {
                if (i > 0 && i == noPermitidos.size() - 1) sb2.append(" ni ");
                else if (i > 0) sb2.append(", ");
                sb2.append(noPermitidos.get(i));
            }
            sb2.append(".");
            System.out.println(sb2.toString());
        }
    }

    private void venderEdificio(String nombreSolar, String idEdificio) {
        if (jugadores == null || jugadores.isEmpty()) {
            System.out.println("No hay jugadores en la partida.");
            return;
        }
        Jugador actual = jugadores.get(turno);

        // Buscar el solar por nombre
        Casilla casilla = tablero.encontrar_casilla(nombreSolar);
        if (casilla == null) {
            System.out.printf("No se encontró la casilla '%s'.%n", nombreSolar);
            return;
        }
        if (!(casilla instanceof Solar)) {
            System.out.printf("La casilla '%s' no es un solar.%n", nombreSolar);
            return;
        }
        Solar solar = (Solar) casilla;
        // Verificar que el jugador actual sea el dueño
        if (solar.getDuenho() == null || solar.getDuenho() != actual) {
            System.out.printf("%s no es el propietario de %s.%n",
                    actual.getNombre(), nombreSolar);
            return;
        }
        // Buscar el edificio por ID en las edificaciones del solar
        List<Edificio> edificaciones = solar.getEdificaciones();
        Edificio edificioAVender = null;
        for (Edificio e : edificaciones) {
            if (e.getId().equalsIgnoreCase(idEdificio)) {
                edificioAVender = e;
                break;
            }
        }

        if (edificioAVender == null) {
            System.out.printf("No se encontró el edificio '%s' en %s.%n",
                    idEdificio, nombreSolar);
            return;
        }

        // Eliminar el edificio usando el método ya implementado
        boolean eliminado = eliminarEdificio(edificioAVender);

        if (eliminado) {
            System.out.printf("El jugador %s ha vendido exitosamente el edificio %s de %s.%n",
                    actual.getNombre(), idEdificio, nombreSolar);
        } else {
            System.out.printf("No se pudo vender el edificio %s de %s.%n",
                    idEdificio, nombreSolar);
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

    // comprobar si el jugador puede jugar (no en carcel o paga fianza)

    private boolean comprobarCarcel(Jugador j) {
        if (j.isEnCarcel()) {
            System.out.println(j.getNombre() + " esta en la carcel y debe pagar 500000 para salir");
            if (j.getFortuna() >= 500000) {
                j.pagar(500000);
                j.salirDeCarcel();
                System.out.println(j.getNombre() + " paga 500000 y sale de la carcel tras pagar la fianza de 500000");
                return true; // ya puede jugar
            } else {
                System.out.println("No tienes suficiente dinero para pagar la fianza. No puedes lanzar los dados");
                return false; // no puede jugar
            }
        }
        return true; // no estaba en carcel
    }

    private boolean procesarTiradaEnCarcel(Jugador actual, int d1, int d2, int suma) {
        System.out.printf("%s esta en la carcel y ha tirado %d + %d%n", actual.getNombre(), d1, d2);

        if (d1 == d2) {
            // dobles -> sale sin pagar y avanza la suma
            actual.salirDeCarcel();
            System.out.println("Has sacado dobles y sales de la carcel");
            moverYEvaluar(actual, suma);
            System.out.println("Usa 'acabar turno' para pasar al siguiente jugador");
            return true;
        }

        // no dobles
        int intentos = actual.getTiradasCarcel() + 1;
        actual.setTiradasCarcel(intentos);

        if (intentos < 3) {
            System.out.println("No has sacado dobles. Sigues en la carcel");
            System.out.println("Intentos realizados en carcel: " + intentos + " de 3");
            System.out.println("Usa 'acabar turno' para pasar al siguiente jugador");
            return false; // sigue preso sin mover
        }

        // tercer intento sin dobles -> debe pagar y avanzar
        System.out.println("Tercer intento sin dobles. Debes pagar 500000 y avanzar la suma tirada");
        if (actual.getFortuna() >= 500000) {
            actual.pagar(500000);
            actual.salirDeCarcel();
            moverYEvaluar(actual, suma);
            System.out.println("Usa 'acabar turno' para pasar al siguiente jugador");
            return true;
        } else {
            System.out.println("No tienes suficiente dinero para pagar la fianza. Sigues en la carcel");
            System.out.println("Usa 'acabar turno' para pasar al siguiente jugador");
            return false;
        }
    }
    /*
    private void hipotecar(String nombreCasilla) {
        Jugador actual = jugadores.get(turno);
        Casilla c = tablero.encontrar_casilla(nombreCasilla);

        if (c == null) {
            System.out.println("No existe la casilla " + nombreCasilla);
            return;
        }

        if (c.getDuenho() != actual) {
            System.out.println(actual.getNombre() + " no puede hipotecar " + nombreCasilla + ". No es una propiedad que le pertenece.");
            return;
        }

        if (actual.getHipotecadas().contains(c)) {
            System.out.println(actual.getNombre() + " no puede hipotecar " + nombreCasilla + ". Ya está hipotecada.");
            return;
        }

        // Si es Solar y tiene edificios, no puede hipotecar
        if (c instanceof Solar) {
            Solar s = (Solar) c;
            if (!s.getEdificaciones().isEmpty()) {
                System.out.println("No puedes hipotecar " + nombreCasilla + ". Debes vender los edificios primero.");
                return;
            }
        }

        // Valor de hipoteca: mitad del precio de compra
        float valorHipoteca = c.getPrecioCompra() / 2;

        actual.sumarFortuna(valorHipoteca);
        actual.getHipotecadas().add(c);

        System.out.printf("%s recibe %.0f€ por la hipoteca de %s. No puede recibir alquileres ni edificar en el grupo %s.%n",
                actual.getNombre(), valorHipoteca, c.getNombre(),
                c.getGrupo() != null ? c.getGrupo().getColor() : "-");
    }

    private void deshipotecar(String nombreCasilla) {
        Jugador actual = jugadores.get(turno);
        Casilla c = tablero.encontrar_casilla(nombreCasilla);

        if (c == null) {
            System.out.println("No existe la casilla " + nombreCasilla);
            return;
        }

        if (c.getDuenho() != actual) {
            System.out.println(actual.getNombre() + " no puede deshipotecar " + nombreCasilla + ". No es una propiedad que le pertenece.");
            return;
        }

        if (!actual.getHipotecadas().contains(c)) {
            System.out.println(actual.getNombre() + " no puede deshipotecar " + nombreCasilla + ". No está hipotecada.");
            return;
        }

        float valorHipoteca = c.getPrecioCompra() / 2;

        if (actual.getFortuna() < valorHipoteca) {
            System.out.println("No tienes suficiente dinero para deshipotecar esta propiedad.");
            return;
        }

        actual.pagar(valorHipoteca);
        actual.getHipotecadas().remove(c);

        System.out.printf("%s paga %.0f€ por deshipotecar %s. Ahora puede recibir alquileres y edificar en el grupo %s.%n",
                actual.getNombre(), valorHipoteca, c.getNombre(),
                c.getGrupo() != null ? c.getGrupo().getColor() : "-");
    }
    */
}