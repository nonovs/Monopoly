package monopoly;

import partida.Jugador;//a

import java.util.jar.JarEntry;

public class MonopolyETSE {

    public static void main(String[] args) {
        new Menu();
        //Creo  a banca(jugador especial con todas las propiedades del tablero)
        Jugador banca=new Jugador();

        //Creo o tablero
        Tablero tablero=new Tablero(banca);

        if (args.length>0){//Comproba si se pasou un ficheiro por linea de comandos
            String  archivo= args[0];
            procesarFichero(archivo,tablero);
        }else{
            procesarComandos(tablero);
        }

    }
    
}

//Metodo para leer os comandos dun archivo
private static void procesarFichero(String fichero, Tablero tablero) {

    try {
        FileReader archivo= new FileReader(fichero);
        BufferedReader buffer= new BufferedReader(archivo);

        String linea;
        //bucle para ir leendo as lineas
        while((linea=buffer.readLine()!=null)){
            procesarComando(linea,tablero);
        }
    }catch(FileNotFoundException e){

    }
}
//Metodo que toma os comandos que se introducen

private static void procesarComandos(Tablero tablero){
    Scanner sc=new Scanner(System.in);
    System.out.println("Ingresa comandos. Escribe 'salir' para terminar.");
    while(true){
        System.out.printf(">");
        String linea= sc.nextLine().trim();
        if(linea.equals("salir")){
            System.out.printf("Saliendo...");
            break;
        }else procesarComandos(linea,tablero);
    }

}

//Metodo que interpreta e ejecuta os comandos
private static  void procesarComando(String linea,Tablero tablero){
    if (linea.isEmpty())return;
    String[] partes= linea.split(" ");
    switch (partes[0]){

        case "crear":
            if (partes.length==4 && partes[1].equalsIgnoreCase("Jugador")){
                String nombre= partes[2];
                String avatar= partes[3];
                //aqui facer que se cree un xogador´
                tablero.crearJugador(nombre,avatar);
            }
            break;
        case "jugador":
            System.out.println(tablero.getJugadorActual());

            break;
        case "listar":
            if(partes.length>=2){
                if(partes[1].equalsIgnoreCase("jugadores")){
                    tablero.listarJugadores();
                    
                } else if (partes[1].equalsIgnoreCase("enventa")) {
                    tablero.listarPropiedadesEnVenta();
                }
            }
        case "lanzar":
            if (partes.length>=2 && partes[1].equalsIgnoreCase("dados")) {
                if(partes.length == 2)){

                }

            }
    }


    /*case "lanzar":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("dados")) {
                    if (partes.length == 2) {
                        tablero.lanzarDados(); // aleatorio
                    } else {
                        // lanzar dados con valor forzado, ej: "lanzar dados 2+4"
                        String[] valores = partes[2].split("\\+");
                        int dado1 = Integer.parseInt(valores[0]);
                        int dado2 = Integer.parseInt(valores[1]);
                        tablero.lanzarDados(dado1, dado2);
                    }
                }
                break;

            case "acabar":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("turno")) {
                    tablero.acabarTurno();
                }
                break;

            case "salir":
                if (partes.length >= 2 && (partes[1].equalsIgnoreCase("cárcel") || partes[1].equalsIgnoreCase("carcel"))) {
                    tablero.salirCarcel();
                }
                break;

            case "describir":
                if (partes.length >= 2) {
                    if (partes[1].equalsIgnoreCase("jugador") && partes.length >= 3) {
                        tablero.describirJugador(partes[2]);
                    } else {
                        tablero.describirCasilla(partes[1]);
                    }
                }
                break;

            case "comprar":
                if (partes.length >= 2) {
                    tablero.comprar(partes[1]);
                }
                break;

            case "ver":
                if (partes.length >= 2 && partes[1].equalsIgnoreCase("tablero")) {
                    System.out.println(tablero);
                }
                break;

            default:
                System.out.println("Comando no reconocido: " + linea);
        }
    }
}*/
}