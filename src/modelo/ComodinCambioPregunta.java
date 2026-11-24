package modelo;

import juego.*;

public class ComodinCambioPregunta extends Comodin {

    public ComodinCambioPregunta() {
        super("Cambio de pregunta", 750000L);
    }

    @Override
    public void usar(Pregunta pregunta) {
        marcarUsado();
    }

    // Cambia la pregunta actual por otra distinta del mismo nivel usando el banco
    public Pregunta cambiarPregunta(Pregunta preguntaActual, BancoPreguntas banco) {
        if (preguntaActual == null || banco == null) {
            return preguntaActual;
        }
        marcarUsado();
        return banco.siguientePreguntaDiferente(preguntaActual.getNivel(), preguntaActual.getEnunciado());
    }
}
