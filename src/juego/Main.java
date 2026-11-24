package juego;

public class Main {

    public static void main(String[] args) {
        Consola consola = new Consola();

        consola.mostrarTitulo("VUELVETE MILLONARIO");

        // Carga las preguntas desde el archivo banco_preguntas.txt ubicado en la raíz del proyecto.
        // se carga una sola vez y sirve para todos los jugadores
        BancoPreguntas banco = new BancoPreguntas();
        LectorPreguntas lector = new LectorPreguntas("banco_preguntas.txt");
        int cargadas = lector.cargar(banco);

        if (cargadas == 0) {
            consola.mostrarMensaje("No se cargaron preguntas desde banco_preguntas.txt. Verifica que el archivo esté en la raíz del proyecto.\n");
            return;
        }

        consola.mostrarMensaje(" " + cargadas + " preguntas listas en " + banco.cantidadNiveles() + " niveles.\n");

        // Se juega una partida por jugador hasta que no quieran jugar mas.
        // todos comparten la misma consola para evitar conflictos con Scanner
        do {
            String nombre = pedirNombre(consola);
            Juego juego = new Juego(nombre, banco, consola);
            juego.iniciarPartida();
        } while (quiereOtraPartida(consola));

        consola.mostrarTitulo("¡GRACIAS POR JUGAR!");
    }

    // si el jugador no escribe nombre se le pone "Concursante"
    private static String pedirNombre(Consola consola) {
        consola.mostrarMensaje("\n Ingresa tu nombre de concursante: ");
        String nombre = consola.leerEntrada();
        if (nombre.isEmpty()) {
            nombre = "Concursante";
        }
        return nombre;
    }

    // al terminar cada partida se pregunta si juega otra persona, solo se acepta S o N
    private static boolean quiereOtraPartida(Consola consola) {
        while (true) {
            consola.mostrarMensaje("\n ¿Quiere jugar otro concursante? (S/N): ");
            String respuesta = consola.leerEntrada().toUpperCase();
            if (respuesta.equals("S")) {
                return true;
            }
            if (respuesta.equals("N")) {
                return false;
            }
            consola.mostrarMensaje(" Opción inválida. Escribe S o N.\n");
        }
    }
}
