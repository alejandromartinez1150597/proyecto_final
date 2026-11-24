package juego;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Consola {

    private Scanner scanner; // Se genera el escaner para que el jugar introduzca los datos que se solicitan.

    public Consola (){
        prepararAcentos();
        this.scanner = new Scanner(System.in); // Constructor de la clase.
    }

    // en la terminal de Windows las tildes y la ñ salian como simbolos raros (Cu├íl en vez de Cuál)
    // porque la consola usa otra codificacion. aqui se pasa la consola a UTF-8 (chcp 65001)
    // y se imprime en UTF-8, asi se ve bien sin importar desde donde se ejecute
    private static void prepararAcentos() {
        boolean esWindows = System.getProperty("os.name").toLowerCase().contains("windows");
        if (!esWindows || System.console() == null) {
            return; // en otros sistemas o si la salida no es una consola se deja como esta
        }
        try {
            Process chcp = new ProcessBuilder("cmd", "/c", "chcp 65001 > nul").inheritIO().start();
            if (chcp.waitFor() == 0) {
                System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out), true, StandardCharsets.UTF_8));
            }
        } catch (Exception e) {
            // si no se pudo cambiar se sigue con la codificacion que tenga la consola
        }
    }

    public void mostrarMensaje(String mensaje){
        System.out.print(mensaje); // Se usa para imprimir los mensajes del juego.
    }

    // ancho de las lineas que encierran los titulos y secciones
    private static final int ANCHO = 56;

    // titulo grande centrado entre dos lineas de =
    public void mostrarTitulo(String texto){
        String linea = "=".repeat(ANCHO);
        int espacios = Math.max(0, (ANCHO - texto.length()) / 2);
        mostrarMensaje("\n" + linea + "\n" + " ".repeat(espacios) + texto + "\n" + linea + "\n");
    }

    // encabezado de una parte del juego (pregunta, tienda...) entre lineas de -
    public void mostrarSeccion(String... renglones){
        String linea = "-".repeat(ANCHO);
        mostrarMensaje("\n" + linea + "\n");
        for (String renglon : renglones) {
            mostrarMensaje(" " + renglon + "\n");
        }
        mostrarMensaje(linea + "\n");
    }

    // convierte un valor a texto de plata, ejemplo 1500000 -> $1,500,000
    public static String dinero(long valor){
        return "$" + String.format("%,d", valor);
    }

    public String leerEntrada(){
        return scanner.nextLine().trim(); // Lee los datos ingresados del jugador.
    }

    public String leerOpcionPregunta(){ // Se usa para responder las preguntas, solo es valido A,B,C o D.
        while (true) {
            String entrada = leerEntrada().toUpperCase();
            if (entrada.matches("[A-D]")){
                return entrada;
            }
            mostrarMensaje("Opción inválida. Por favor, ingresa A, B, C o D:");
        }
    }

    // igual que la anterior pero tambien acepta R, que es la opcion para retirarse
    public String leerRespuestaORetiro(){
        while (true) {
            String entrada = leerEntrada().toUpperCase();
            if (entrada.matches("[A-DR]")){
                return entrada;
            }
            mostrarMensaje(" Opción inválida. Ingresa A, B, C, D o R para retirarte: ");
        }
    }
}
