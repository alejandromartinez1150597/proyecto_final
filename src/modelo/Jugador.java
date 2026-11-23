package modelo;
import estructuras.*;

public class Jugador {

    private String nombre;
    private ListaEnlazada<Comodin> comodines; // Aquí se guradarán los comodines del jugador
    private EscaleraPremios escalera; // Controla los niveles ganados y premios

    public static final int MAX_COMODIN_TIPO = 2; // Se fija el maximo de comodines que puede tener por tipo a 2

    public Jugador(String nombre){
        this.nombre = nombre;
        this.comodines = new ListaEnlazada();
        this.escalera = new EscaleraPremios();
    }

    // Se utilizan getters para acceder a los datos del jugador, ya que los atributtos son privados
    public String getNombre() {
        return nombre;
    }

    public ListaEnlazada getComodines(){
        return comodines;
    }

    public EscaleraPremios getEscalera() {
        return escalera;
    }

    public String mostrarPremioAcumulado(){ // El jugador puede consultar su premio actual
        return escalera.premioActual();
    }

    public void avanzarNivel(){ // Se maneja el progreso de los niveles
        escalera.subir();
    }

    public void aplicarPenalidad(){ // Se maneja el progreso de los niveles y se aplica
        escalera.caerNivelSeguro(); // la penalidad dependendiendo del nivel en el que se encuentre
    }

    // Cuenta cuabtos comodines de un tipo tiene
    public int contarComodin(String nombreComodin) {
        int total = 0;
        for (int i = 0; i < comodines.tamano(); i++) {
            Comodin actual = comodines.obtener(i);
            if (actual != null && actual.getNombre().equalsIgnoreCase(nombreComodin)) {
                total++;
            }
        }
        return total;
    }

    // Solo permite comprar si tiene menos de 2 de ese tipo
    public boolean puedeComprarComodin(String nombreComodin) {
        return contarComodin(nombreComodin) < MAX_COMODIN_TIPO;
    }

    // Agrega el comodín si no supera el límite de 2
    public boolean comprarComodin(Comodin nuevoComodin) {
        if (nuevoComodin == null) {
            return false;
        }
        if (!puedeComprarComodin(nuevoComodin.getNombre())) {
            return false;
        }
        comodines.agregar(nuevoComodin);
        return true;
    }

    // Busca un comodín disponible por nombre y lo quita de la mochila al gastarlo
    public Comodin usarYConsumirComodin(String nombreComodin) {
        for (int i = 0; i < comodines.tamano(); i++) {
            Comodin actual = comodines.obtener(i);
            if (actual != null && actual.getNombre().equalsIgnoreCase(nombreComodin)) {
                comodines.eliminar(i);
                return actual;
            }
        }
        return null;
    }

    public void registrarAciertoSinComodin() {
        escalera.registrarAciertoSinComodin();
    }

    public void resetearMultiplicador() {
        escalera.resetearMultiplicador();
    }

    public int getMultiplicador() {
        return escalera.getMultiplicador();
    }
}
