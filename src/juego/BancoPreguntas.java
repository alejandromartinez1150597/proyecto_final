package juego;

import estructuras.ArbolBinarioBusqueda;
import estructuras.Cola;
import modelo.Pregunta;

// guarda todas las preguntas del juego organizadas por nivel.
// el arbol sirve para encontrar rapido el nivel y la cola para sacar las preguntas en orden
public class BancoPreguntas {

    private ArbolBinarioBusqueda<NivelPreguntas> niveles;

    public BancoPreguntas() {
        this.niveles = new ArbolBinarioBusqueda<>();
    }

    public void agregarPregunta(Pregunta pregunta) {
        NivelPreguntas nivel = buscarNivel(pregunta.getNivel());
        if (nivel == null) { // es la primera pregunta de ese nivel, se crea el nivel en el arbol
            nivel = new NivelPreguntas(pregunta.getNivel());
            niveles.insertar(nivel);
        }
        nivel.getPreguntas().encolar(pregunta);
    }

    // saca la siguiente pregunta del nivel y la vuelve a meter al final de la cola.
    // asi las preguntas van rotando y no se repiten hasta que salgan todas las de ese nivel
    public Pregunta siguientePregunta(int numeroNivel) {
        if (!hayPreguntas(numeroNivel)) {
            return null;
        }

        Cola<Pregunta> cola = buscarNivel(numeroNivel).getPreguntas();
        Pregunta pregunta = cola.desencolar();
        cola.encolar(pregunta);
        return pregunta;
    }

    public boolean hayPreguntas(int numeroNivel) {
        NivelPreguntas nivel = buscarNivel(numeroNivel);
        return nivel != null && !nivel.getPreguntas().estaVacia();
    }

    public int cantidadPreguntas(int numeroNivel) {
        NivelPreguntas nivel = buscarNivel(numeroNivel);
        return (nivel == null) ? 0 : nivel.getPreguntas().tamanio();
    }

    public int cantidadNiveles() {
        return niveles.tamanio();
    }

    // para buscar en el arbol se crea un NivelPreguntas solo con el numero,
    // como compareTo solo mira el numero, el arbol devuelve el nivel guardado con sus preguntas
    private NivelPreguntas buscarNivel(int numeroNivel) {
        return niveles.buscar(new NivelPreguntas(numeroNivel));
    }
}
