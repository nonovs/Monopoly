package monopoly;
import java.util.Scanner;

public class ConsolaNormal implements Consola{
    Scanner entrada = new Scanner(System.in);

    private Scanner sc;
    public ConsolaNormal(){
        sc = new Scanner(System.in);
    }

    @Override
    public void imprimir(String mensaje) {
        System.out.println(mensaje);
    }
    @Override
    public String leer(String mensaje) {
        System.out.print(mensaje);
        return entrada.nextLine().trim();
    }
}
