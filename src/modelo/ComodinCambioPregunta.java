package modelo;

import juego.*;

public class ComodinCambioPregunta extends Comodin {

    public ComodinCambioPregunta() {
        super("Cambio de pregunta");
    }

    public void usar(Pregunta pregunta) {
        marcarUsado();
    }

    public Pregunta cambiarPregunta(Pregunta preguntaActual, BancoPreguntas banco) {
        if (preguntaActual == null || banco == null) {
            return preguntaActual;
        }
        marcarUsado();
        return banco.siguientePreguntaDiferente(preguntaActual.getNivel(), preguntaActual.getEnunciado());
    }
}
