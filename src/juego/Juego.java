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
}
