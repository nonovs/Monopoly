package partida;

import java.util.concurrent.ThreadLocalRandom;
import java.util.Random;

/**
 *
 * Responsabilidades:
 *  - Generar tiradas de 2 dados (valores 1..6)
 *  - Permitir tiradas "forzadas" (útil para comandos tipo: lanzar dados 2+4)
 *  - Llevar la cuenta de "dobles consecutivos" durante el turno actual.
 *
 * Notas de uso:
 *  - Llamar a iniciarTurno() al comienzo del turno de cada jugador, para poner
 *    a cero el contador de dobles y limpiar el estado de la última tirada.
 *  - tirar() produce valores aleatorios; tirarForzado(a,b) fija los valores (valida 1..6).
 *  - Si sale doble, doblesConsecutivos se incrementa; si no sale doble, se resetea a 0.
 *  - Regla del juego: a la 3ª vez que salen dobles en el mismo turno → ir a Cárcel.
 *    Esta clase SOLO informa del conteo; el controlador (Menu/Tablero) aplica la acción.
 */
public class Dado {

    // Ultimos valores obtenidos en la tirada
    private int d1 = 0;
    private int d2 = 0;
    private int valor;

    // Contador de dobles consecutivos dentro del turno actual, debe reiniciarse al principio de cada turno
    // por lo que se debe poner 0 con la funcion iniciar_turno
    private int doblesConsecutivos = 0;

    //Usaremos la clase Random de java para asignarle a la tirada de cada dado un valor aleatorio
    private Random numeroRandom = new Random();

    private boolean esCaraValida(int v) {
        return v >= 1 && v <= 6;
    }

    /**
     * Debe llamarse al comenzar el turno de cada jugador con el fin de resetear contadores y limpiar el estado
     */
    public void iniciarTurno() {
        doblesConsecutivos = 0;
        d1 = 0;
        d2 = 0;
        valor = 0;
    }

    /**
     * Realiza una tirada aleatoria de 2 dados (valores entre 1 y 6) y tras esto actualiza el estado interno (d1, d2) y el contador de dobles
     *
     * @return la suma de los dos dados sumados
     */
    public int tirar() {
        d1 = numeroRandom.nextInt(1, 7);
        d2 = numeroRandom.nextInt(1, 7);
        valor = d1 + d2;

        actualizarDobles();

        return valor;
    }

    /**
     * Realiza una tirada "forzada", asignando directamente los valores de cada dado
     *
     */
    public int tirarForzado(int a, int b) {
        if (!esCaraValida(a) || !esCaraValida(b)) {
            throw new IllegalArgumentException("Valores inválidos: " + a + "," + b + " (deben ser 1..6)");
        }
        d1 = a;
        d2 = b;
        valor = d1 + d2;

        actualizarDobles();
        return valor;
    }

    /**
     * Devuelve true si la última tirada fue un doble (mismo valor en ambos dados)
     * Devuelve false si aún no se ha realizado ninguna tirada (d1=d2=0)
     */
    public boolean esDoble() {
        // Comprobamos además que se haya tirado (evita (0,0) como doble)
        return d1 == d2 && d1 != 0;
    }

    public int getD1() { return d1; }


    public int getD2() { return d2; }

    public int getValor() {
        return valor;
    }

    private void actualizarDobles() {
        if (esDoble()) {
            doblesConsecutivos++;
        } else {
            doblesConsecutivos = 0;
        }
    }

}
