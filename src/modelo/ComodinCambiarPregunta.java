package modelo;

import juego.BancoPreguntas;

// cambia la pregunta actual por otra del mismo nivel.
// la nueva sale de la cola de ese nivel en el banco de preguntas
public class ComodinCambiarPregunta extends Comodin {

    private BancoPreguntas banco;
    private Pregunta nuevaPregunta; // aqui queda la pregunta que reemplaza a la actual

    public ComodinCambiarPregunta(BancoPreguntas banco) {
        super("Cambiar pregunta");
        this.banco = banco;
    }

    @Override
    public void usar(Pregunta pregunta) {
        if (fueUsado()) {
            return;
        }

        // como el banco rota las preguntas, la siguiente de la cola ya es otra distinta.
        // si el nivel solo tiene esa pregunta, se queda la misma
        Pregunta otra = banco.siguientePregunta(pregunta.getNivel());
        nuevaPregunta = (otra != null) ? otra : pregunta;
        marcarUsado();
    }

    // el juego la llama despues de usar() para mostrar la pregunta nueva
    public Pregunta getNuevaPregunta() {
        return nuevaPregunta;
    }
}
