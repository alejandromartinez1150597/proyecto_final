package juego;

public class Main {

    public static void main(String[] args) {
        Consola consola = new Consola();

        consola.mostrarMensaje("=========================================\n");
        consola.mostrarMensaje("   VUELVETE MILLONARIO \n");
        consola.mostrarMensaje("=========================================\n");

        // 1. Jugardor ingresa nombre obligatoriamente (trim() detecta nombre de solo espacios, detectado como vacio)
        String nombre;
        do {
            consola.mostrarMensaje("Ingresa tu nombre: ");
            nombre = consola.leerEntrada().trim();

            if (nombre.isEmpty()) {
                consola.mostrarMensaje("Error: debes ingresar un nombre para continuar.\n");
            }
        } while (nombre.isEmpty());

        // 2. Se cargan las preguntas del archivo al banco (arbol de niveles con su cola de preguntas)
        BancoPreguntas banco = new BancoPreguntas();
        int cargadas = new LectorPreguntas("banco_preguntas.txt").cargar(banco);
        if (cargadas == 0) {
            consola.mostrarMensaje("No hay preguntas para jugar, revisa el archivo banco_preguntas.txt\n");
            return;
        }

        // 3. Se crea el juego con la misma consola y se inicia la partida
        Juego juego = new Juego(nombre, banco, consola);
        juego.iniciarPartida();
    }
}
