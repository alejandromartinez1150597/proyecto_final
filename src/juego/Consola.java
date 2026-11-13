package juego;

import java.util.Scanner;

public class Consola {

    private Scanner scanner;

    public Consola (){
        this.scanner = new Scanner(System.in);
    }

    public void mostrarMensaje(String mensaje){
        System.out.print(mensaje);
    }

    


}
