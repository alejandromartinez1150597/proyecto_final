package juego;
import modelo.*;

public class Main {
    public static void main(String[] args) {

        Consola consola = new Consola();


        Pregunta pregunta1 = new Pregunta("¿Cómo se llama la capital de Colombia?", 1, "C");

        // La clase Lista enlazadas aún no ha sido configurada, esto es una simulación de como debería de funcionar
        pregunta1.agregarOpcion("A) Medellín");
        pregunta1.agregarOpcion("B) Bucaramanga");
        pregunta1.agregarOpcion("C) Bogotá");
        pregunta1.agregarOpcion("D) Cali");

        // Se prueba que la clase consola se integra correctamente
        consola.mostrarMensaje("*** Nivel " + pregunta1.getNivel() + " *** \n");
        consola.mostrarMensaje(pregunta1.getEnunciado());
        consola.mostrarMensaje("\nA) Medellín");
        consola.mostrarMensaje("\nB) Bucaramanga");
        consola.mostrarMensaje("\nC) Bogotá");
        consola.mostrarMensaje("\nD) Cali");

        consola.mostrarMensaje("\nIngrese su respuesta por 10.000 pesos (A, B, C o D):");
        String respuesta = consola.leerOpcionPregunta();

        // Validamos que las clases se comunican con normalidad
        if (pregunta1.verificarRespuesta(respuesta)) {
            consola.mostrarMensaje("Perfecto ha ganado 10.000 foxes");
        } else {
            consola.mostrarMensaje("Que lastima has pérdido la platica jajjaj: " + pregunta1.getRespuestaCorrecta());
        }
    }
}
