package juego;

import modelo.Comodin5050;
import modelo.Jugador;
import modelo.Pregunta;

public class Main {

    public static void main(String[] args) {
        Consola consola = new Consola();

        consola.mostrarMensaje("=========================================\n");
        consola.mostrarMensaje("   TRIVIA: ¿TE VOLVERÁS MILLONARIO? (FLOW) \n");
        consola.mostrarMensaje("=========================================\n");

        // 1. Pedir nombre del jugador
        consola.mostrarMensaje("Ingresa tu nombre: ");
        String nombre = consola.leerEntrada();
        if (nombre.isEmpty()) {
            nombre = "Concursante";
        }

        Jugador jugador = new Jugador(nombre);
        Comodin5050 comodin5050 = new Comodin5050();

        consola.mostrarMensaje("\n¡Hola, " + jugador.getNombre() + "! Premio inicial: " + jugador.mostrarPremioAcumulado() + "\n");

        // 2. Preguntas de prueba
        Pregunta p1 = new Pregunta("¿Qué estructura sigue el orden FIFO (primero en entrar, primero en salir)?", 1, "A");
        p1.agregarOpcion("A) Cola");
        p1.agregarOpcion("B) Pila");
        p1.agregarOpcion("C) Árbol");
        p1.agregarOpcion("D) Grafo");

        Pregunta p2 = new Pregunta("¿Cuál es la complejidad promedio de búsqueda en un árbol binario de búsqueda balanceado?", 2, "C");
        p2.agregarOpcion("A) O(n^2)");
        p2.agregarOpcion("B) O(n)");
        p2.agregarOpcion("C) O(log n)");
        p2.agregarOpcion("D) O(1)");

        Pregunta[] rondas = { p1, p2 };

        // 3. Flujo de rondas
        for (int i = 0; i < rondas.length; i++) {
            Pregunta actual = rondas[i];
            consola.mostrarMensaje("\n--- Ronda " + (i + 1) + " (Nivel " + actual.getNivel() + ") ---\n");
            consola.mostrarMensaje(actual.getEnunciado() + "\n");

            for (int j = 0; j < actual.getOpciones().tamano(); j++) {
                consola.mostrarMensaje(actual.getOpciones().obtener(j) + "\n");
            }

            // Ejemplo opcional: si aún no se ha usado el 50/50, preguntar si quiere probarlo
            if (!comodin5050.fueUsado()) {
                consola.mostrarMensaje("¿Deseas activar el comodín 50/50 antes de responder? (S/N): ");
                String usarComodin = consola.leerEntrada().toUpperCase();
                if (usarComodin.equals("S")) {
                    comodin5050.usar(actual);
                    consola.mostrarMensaje("Comodín aplicado. Opciones restantes:\n");
                    for (int j = 0; j < actual.getOpciones().tamano(); j++) {
                        consola.mostrarMensaje(actual.getOpciones().obtener(j) + "\n");
                    }
                }
            }

            consola.mostrarMensaje("Elige tu respuesta (A, B, C o D): ");
            String respuesta = consola.leerOpcionPregunta();

            if (actual.verificarRespuesta(respuesta)) {
                jugador.avanzarNivel();
                consola.mostrarMensaje("¡Correcto! Premio acumulado: " + jugador.mostrarPremioAcumulado() + "\n");
            } else {
                jugador.aplicarPenalidad();
                consola.mostrarMensaje("Incorrecto. La respuesta correcta era " + actual.getRespuestaCorrecta() + ".\n");
                consola.mostrarMensaje("Premio tras penalidad: " + jugador.mostrarPremioAcumulado() + "\n");
                break;
            }
        }

        consola.mostrarMensaje("\nFin de la partida de prueba. ¡Gracias por jugar, " + jugador.getNombre() + "!\n");
    }
}
