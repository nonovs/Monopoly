package monopoly;

import partida.Jugador;//a
import partida.Partida;

import java.util.jar.JarEntry;

public class MonopolyETSE {

    public static void main(String[] args) {
        new Menu();
        new Partida partida=new Partida();
        if (args.length>0){
            partida.procesarArchivo(args[0]);

        }else{
            partida.procesarComandos();
        }

    }
    
}
