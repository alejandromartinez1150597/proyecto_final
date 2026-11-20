package juego;

import estructuras.Cola;
import modelo.Pregunta;

// representa un nivel del juego (1 al 10) con su cola de preguntas.
// es lo que se guarda en cada nodo del arbol del banco de preguntas
public class NivelPreguntas implements Comparable<NivelPreguntas> {

    private int nivel;
    private Cola<Pregunta> preguntas;

    public NivelPreguntas(int nivel) {
        this.nivel = nivel;
        this.preguntas = new Cola<>();
    }

    public int getNivel() {
        return nivel;
    }

    public Cola<Pregunta> getPreguntas() {
        return preguntas;
    }

    @Override
    public int compareTo(NivelPreguntas otro) { // el arbol los ordena por numero de nivel
        return Integer.compare(this.nivel, otro.nivel);
    }
}
