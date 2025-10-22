package monopoly;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import partida.*;
import monopoly.casillas.Casilla;
import java.text.Normalizer;


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

                // Edificios (en esta entrega no están implementados, lo dejamos vacío o placeholder)
                ArrayList<String> edifs = new ArrayList<>();
                // si más adelante tienes una lista de edificios, puedes recorrerla igual que las anteriores

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
        actual.pagar(500000);
        actual.salirDeCarcel(); // este metodo ya imprime el mensaje de salida
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

            // Edificios (placeholder)
            ArrayList<String> edifs = new ArrayList<>();
            // si más adelante implementas edificios, los añades aquí

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


}