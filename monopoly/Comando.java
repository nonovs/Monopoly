package monopoly;

import excepciones.Excepcion;

public interface Comando {
    /// default void metododefecto(){
    /// System.out.printnl("HOlacaracola)
    /// } esto simplemente era para probar un metodo por defecto.
    void crearJugador(String nombre, String tipoAvatar);
    void mostrarJugadorEnTurno();
    void listarJugadores();
    void listarVenta();
    void listarAvatares();
    void listarEdificios();
    void listarEdificiosGrupo(String grupo);
    void listarCasillasGrupo(String grupo);
    void descJugador(String nombre) throws Excepcion;
    void descAvatar(String id);
    void descCasilla(String nombre)throws Excepcion;
    void lanzarDados();
    void lanzarDadosForzada(int a, int b);
    void comprar(String nombre) throws Excepcion;
    void salirCarcel();
    void acabarTurno();
    void mostrarTablero();
    void edificar(String tipo);
    void venderEdificio(String tipo, String solar, int cantidad);
    void hipotecar(String nombre) throws Excepcion;
    void deshipotecar(String nombre) throws Excepcion;
    void mostrarEstadisticas(String nombre) throws Excepcion;
    void mostrarEstadisticasJuego();
    void moverChetada(String casilla);
    void proponerTrato(String nombreDestinatario,String[] elementos);
    void aceptarTrato(String idTrato);
    void listarTratos();
    void eliminarTrato(String idTrato);
}