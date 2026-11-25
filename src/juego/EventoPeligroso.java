package juego;

import java.util.Random;

// Evento aleatorio: a veces, antes de una pregunta, el jugador puede elegir
// una pregunta mas dificil que paga un bono extra, o seguir normal.
public class EventoPeligroso {

    private Random azar; // para decidir si el evento sale o no
    private int probabilidad; // de 0 a 100, por ejemplo 25 = sale en 1 de cada 4 rondas

    public EventoPeligroso(int probabilidad) {
        this.azar = new Random();
        this.probabilidad = probabilidad;
    }

    // dice si en esta ronda sale el evento; hasta el nivel 10 para que la dificil exista (nivel + 2)
    public boolean sale(int nivel) {
        return nivel <= 10 && azar.nextInt(100) < probabilidad;
    }

    // nivel de la pregunta dificil: dos niveles arriba
    public int nivelDificil(int nivel) {
        return Math.min(nivel + 2, 12);
    }

    // muestra la oferta y devuelve true si el jugador acepta la pregunta dificil
    public boolean ofrecer(Consola consola, long bono) {
        consola.mostrarSeccion("EVENTO ALEATORIO   |   Pregunta de riesgo",
                "Si aceptas, la pregunta es mas dificil, pero ganas " + Consola.dinero(bono) + " extra si aciertas.");
        while (true) {
            consola.mostrarMensaje("   [1] Aceptar la pregunta dificil\n");
            consola.mostrarMensaje("   [2] Seguir normal\n");
            consola.mostrarMensaje(" Elige una opción (1-2): ");
            String opcion = consola.leerEntrada();
            if (opcion.equals("1")) {
                return true;
            }
            if (opcion.equals("2")) {
                return false;
            }
            consola.mostrarMensaje(" >> Opción inválida. Escribe 1 o 2.\n");
        }
    }
}
