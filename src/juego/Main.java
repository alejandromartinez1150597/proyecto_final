package juego;

public class Main {

    public static void main(String[] args) {
        Consola consola = new Consola();

        consola.mostrarMensaje("=========================================\n");
        consola.mostrarMensaje("   VUELVETE MILLONARIO \n");
        consola.mostrarMensaje("=========================================\n");

        // 1. Se cargan las preguntas del archivo al banco (arbol de niveles con su cola de preguntas).
        // se carga una sola vez, y como cada nivel es una cola, el siguiente jugador recibe otras preguntas
        BancoPreguntas banco = new BancoPreguntas();
        int cargadas = new LectorPreguntas("banco_preguntas.txt").cargar(banco);
        if (cargadas == 0) {
            consola.mostrarMensaje("No hay preguntas para jugar, revisa el archivo banco_preguntas.txt\n");
            return;
        }

        // 2. Se juega una partida por jugador hasta que no quieran jugar mas
        do {
            String nombre = pedirNombre(consola);
            Juego juego = new Juego(nombre, banco, consola);
            juego.iniciarPartida();
        } while (quiereOtraPartida(consola));

        consola.mostrarMensaje("\n¡Gracias por jugar!\n");
    }

    // Jugardor ingresa nombre obligatoriamente (trim() detecta nombre de solo espacios, detectado como vacio)
    private static String pedirNombre(Consola consola) {
        String nombre;
        do {
            consola.mostrarMensaje("\nIngresa tu nombre: ");
            nombre = consola.leerEntrada().trim();

            if (nombre.isEmpty()) {
                consola.mostrarMensaje("Error: debes ingresar un nombre para continuar.\n");
            }
        } while (nombre.isEmpty());
        return nombre;
    }

    // al terminar cada partida se pregunta si juega otra persona, solo se acepta S o N
    private static boolean quiereOtraPartida(Consola consola) {
        while (true) {
            consola.mostrarMensaje("\n¿Quiere jugar otro concursante? (S/N): ");
            String respuesta = consola.leerEntrada().toUpperCase();
            if (respuesta.equals("S")) {
                return true;
            }
            if (respuesta.equals("N")) {
                return false;
            }
            consola.mostrarMensaje("Opción inválida. Escribe S o N.\n");
        }
    }
}
