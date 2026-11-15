package modelo;
import estructuras.*;

public class Jugador {

    private String nombre;
    private ListaEnlazada comodines; // Aquí se guradarán los comodines del jugador
    private EscaleraPremios escalera; // Controla los niveles ganados y premios

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
}
