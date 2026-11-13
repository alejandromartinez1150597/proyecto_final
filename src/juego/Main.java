package juego;

public class Main {

    public static void main(String[] args) {

        Consola consola = new Consola();

        consola.mostrarMensaje("Bienvenido al desafio de preguntas!!");
        consola.mostrarMensaje("Por favor, elija una opción (A,B,C o D): ");

        String respuesta = consola.leerOpcionPregunta();

        consola.mostrarMensaje("Has seleccionado la opción: " + respuesta);

    }
}
