package juego;

public class Main {

    public static void main(String[] args) {
        Consola consola = new Consola();
        BancoPreguntas banco = new BancoPreguntas();

        // Carga las preguntas desde el archivo banco_preguntas.txt ubicado en la raíz del proyecto
        LectorPreguntas lector = new LectorPreguntas("banco_preguntas.txt");
        int cargadas = lector.cargar(banco);

        if (cargadas == 0) {
            consola.mostrarMensaje("No se cargaron preguntas desde banco_preguntas.txt. Verifica que el archivo esté en la raíz del proyecto.\n");
            return;
        }

        consola.mostrarMensaje("Banco cargado con éxito (" + cargadas + " preguntas en " + banco.cantidadNiveles() + " niveles).\n\n");
        consola.mostrarMensaje("Ingresa tu nombre de concursante: ");
        String nombre = consola.leerEntrada();
        if (nombre.isEmpty()) {
            nombre = "Concursante";
        }

        // Inicia la partida compartiendo la misma consola para evitar conflictos con Scanner
        Juego juego = new Juego(nombre, banco, consola);
        juego.iniciarPartida();
    }
}
