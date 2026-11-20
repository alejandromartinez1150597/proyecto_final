package modelo;

// guarda el resultado de un jugador al terminar la partida.
// implementa Comparable para que el arbol del ranking sepa cual es mayor
public class Puntaje implements Comparable<Puntaje> {

    private String nombre;
    private long premio; // en pesos, sin decimales

    public Puntaje(String nombre, long premio) {
        this.nombre = nombre;
        this.premio = premio;
    }

    public String getNombre() {
        return nombre;
    }

    public long getPremio() {
        return premio;
    }

    @Override
    public int compareTo(Puntaje otro) { // se compara solo por el premio
        return Long.compare(this.premio, otro.premio);
    }

    @Override
    public String toString() {
        return nombre + " - $" + premio;
    }
}
