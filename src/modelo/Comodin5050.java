package modelo;

import java.util.Random;
import estructuras.ListaEnlazada;

public class Comodin5050 extends Comodin {
    private Random azar;

    public Comodin5050() {
        super("50/50");
        this.azar = new Random();
    }

    @Override 
    public void usar(Pregunta pregunta) {
        if (fueUsado()) {
            return;
    }
    ListaEnlazada<String> opciones = pregunta.getOpciones();
    int quitadas = 0;
    while (quitadas < 2 && opciones.tamano() > 2) {
        int pos = azar.nextInt(opciones.tamano());
        if(!pregunta.verificarRespuesta(opciones.obtener(pos))) {
            opciones.eliminar(pos);
            quitadas++;
        }
    }
    marcarUsado();
  }
}
