package juego;

import modelo.Comodin5050;
import modelo.Jugador;
import modelo.Pregunta;

public class Main {

    public static void main(String[] args) {
        Consola consola = new Consola();

        consola.mostrarMensaje("=========================================\n");
        consola.mostrarMensaje("   VUELVETE MILLONARIO \n");
        consola.mostrarMensaje("=========================================\n");

        // nombre de jugador
        consola.mostrarMensaje("Ingresa tu nombre: ");
        String nombre = consola.leerEntrada();
        if (nombre.isEmpty()) {
            nombre = "Jugador";
        }

        Jugador jugador = new Jugador(nombre);
        Comodin5050 comodin5050 = new Comodin5050();

        consola.mostrarMensaje("\n¡Hola, " + jugador.getNombre() + "! Premio inicial: " + jugador.mostrarPremioAcumulado() + "\n");

        // pregunta de prueba
        Pregunta p1 = new Pregunta("¿Quién ganó la Copa Mundial de la FIFA en el año 1962?", 1, "A");
        p1.agregarOpcion("A) Brasil");
        p1.agregarOpcion("B) Francia");
        p1.agregarOpcion("C) Alemania");
        p1.agregarOpcion("D) Italia");

        Pregunta p2 = new Pregunta("¿Cuál es la capital de Colombia?", 2, "C");
        p2.agregarOpcion("A) Medellín");
        p2.agregarOpcion("B) Bucaramanga");
        p2.agregarOpcion("C) Bogotá");
        p2.agregarOpcion("D) Cali");

        Pregunta[] rondas = { p1, p2 };

        // flujo de partidas
        for (int i = 0; i < rondas.length; i++) {
            Pregunta actual = rondas[i];
            consola.mostrarMensaje("\n--- Ronda " + (i + 1) + " (Nivel " + actual.getNivel() + ") ---\n");
            consola.mostrarMensaje(actual.getEnunciado() + "\n");

            for (int j = 0; j < actual.getOpciones().tamano(); j++) {
                consola.mostrarMensaje(actual.getOpciones().obtener(j) + "\n");
            }

            // Validar el comodín 50/50
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
