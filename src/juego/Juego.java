package juego;

import modelo.*;
import estructuras.*;

public class Juego {

    private Consola consola;
    private Jugador jugador;

    public Juego(String nombreJugador){
        this.consola = new Consola();
        this.jugador = new Jugador(nombreJugador);
    }

    //Este metodo inicia el ciclo de la partida
    public void iniciarPartida(){
        consola.mostrarMensaje("=========================================\n");
        consola.mostrarMensaje(" BIENVENIDO A LA TRIVIA: ¿TE VOLVERÁS MILLONARIO?! \n");
        consola.mostrarMensaje("=========================================\n");

        consola.mostrarMensaje("Concursante registrado: " + jugador.getNombre() + "\n");
        consola.mostrarMensaje("Premio actual: " + jugador.mostrarPremioAcumulado() + "\n");

        //Mostramos las 10 preguntas y las opciones de retiro/comodines
        consola.mostrarMensaje("[Temporal mientras se finalizan las demás mecanicas del juego]\n");
    }

    // Se añade menu de compra de comodines post nivel
    private void menuCompraComodin() {
        while (true) {
            int cant5050 = jugador.contarComodin("50/50");
            int cantCambio = jugador.contarComodin("Cambio de pregunta");

            consola.mostrarMensaje("\n--- TIENDA COMODINES (Máx. 2 por comodín) ---\n");
            consola.mostrarMensaje("1. Comprar 50/50 (Tienes: " + cant5050 + "/2)\n");
            consola.mostrarMensaje("2. Comprar Cambio de pregunta (Tienes: " + cantCambio + "/2)\n");
            consola.mostrarMensaje("3. Continuar al siguiente nivel\n");
            consola.mostrarMensaje("Elige una opción (1, 2 o 3): ");

            String opcion = consola.leerEntrada();

            if (opcion.equals("1")) {
                if (jugador.comprarComodin(new Comodin5050())) {
                    consola.mostrarMensaje("¡Compraste un comodín 50/50! Ahora tienes " + jugador.contarComodin("50/50") + "/2.\n");
                } else {
                    consola.mostrarMensaje("No puedes comprar más: ya tienes el máximo de 2 comodines 50/50.\n");
                }
            } else if (opcion.equals("2")) {
                if (jugador.comprarComodin(new ComodinCambioPregunta())) {
                    consola.mostrarMensaje("¡Compraste un comodín Cambio de pregunta! Ahora tienes " + jugador.contarComodin("Cambio de pregunta") + "/2.\n");
                } else {
                    consola.mostrarMensaje("No puedes comprar más: ya tienes el máximo de 2 comodines Cambio de pregunta.\n");
                }
            } else if (opcion.equals("3")) {
                break;
            } else {
                consola.mostrarMensaje("Opción inválida. Selecciona 1, 2 o 3.\n");
            }
        }
    }
}
