package modelo;

import java.util.Random;
import estructuras.ListaEnlazada;

public class Comodin5050 extends Comodin {
    private Random azar;

    public Comodin5050() {
        super("50/50", 1000000L);
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
            String letra = opciones.obtener(pos).substring(0, 1); // de "B) Verde" se toma solo la B
            if(!pregunta.verificarRespuesta(letra)) {
                opciones.eliminar(pos);
                quitadas++;
            }
        }
        marcarUsado();
    }
}
