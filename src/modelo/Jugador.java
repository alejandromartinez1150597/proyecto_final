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

    public String mostrarPremioAcumulado(){ // El jugador puede consultar su premio actual
        return escalera.premioActual();
    }

    public void avanzarNivel(){ // Se maneja el progreso de los niveles
        escalera.subir();
    }

    public void aplicarPenalidad(){ // Se maneja el progreso de los niveles y se aplica
        escalera.caerNivelSeguro(); // la penalidad dependendiendo del nivel en el que se encuentre
    }
}
