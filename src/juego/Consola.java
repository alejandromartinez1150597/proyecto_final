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
}
