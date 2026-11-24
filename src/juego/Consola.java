package juego;

import java.util.Scanner;

public class Consola {

    private Scanner scanner; // Se genera el escaner para que el jugar introduzca los datos que se solicitan.

    public Consola (){
        this.scanner = new Scanner(System.in); // Constructor de la clase.
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
