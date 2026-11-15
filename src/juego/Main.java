package juego;
import modelo.*;


public class Main {
    public static void main(String[] args) {
        // Iniciamos la consola de juego
        Consola consola = new Consola();

        // Creamos al jugador
        Jugador jugador = new Jugador("Alejo");
        consola.mostrarMensaje("¡Bienvenido a la trivia, " + jugador.getNombre() + "!\n");
        consola.mostrarMensaje("Premio inicial: " + jugador.mostrarPremioAcumulado() + "\n");

        // Simulamos una pregunta, ya que aún no se ha creado el banco de estas
        Pregunta pregunta1 = new Pregunta("¿Qué estructura de datos sigue el principio LIFO?", 1, "B");

        // Simulamos opciones
        pregunta1.agregarOpcion("A) Cola");
        pregunta1.agregarOpcion("B) Pila");
        pregunta1.agregarOpcion("C) Árbol");
        pregunta1.agregarOpcion("D) Lista");

        // Mostramos las opciones
        consola.mostrarMensaje("\n --- Nivel " + pregunta1.getNivel() + " ---");
        consola.mostrarMensaje(pregunta1.getEnunciado());
        consola.mostrarMensaje("\nA) Cola");
        consola.mostrarMensaje("\nB) Pila");
        consola.mostrarMensaje("\nC) Árbol");
        consola.mostrarMensaje("\nD) Lista");

        // Simlulamos el ingreso de una respuesta del jugador
        consola.mostrarMensaje("\nIngresa tu respuesta (A, B, C o D):");
        String respuesta = consola.leerOpcionPregunta();

        // Evaluamos la respuesta y actualizamos los puntos y esas vainas
        if (pregunta1.verificarRespuesta(respuesta)) {
            jugador.avanzarNivel();
            consola.mostrarMensaje("¡Respuesta Correcta! Subes de nivel. ");
            consola.mostrarMensaje("Premio acumulado actual: " + jugador.mostrarPremioAcumulado());
        } else {
            jugador.aplicarPenalidad();
            consola.mostrarMensaje("¡Respuesta Incorrecta! Aplicando penalidad de la escalera.");
            consola.mostrarMensaje("Premio tras la penalidad: " + jugador.mostrarPremioAcumulado());
        }
    }
}
