package juego;

import modelo.*;

public class Main {

    public static void main(String[] args) {
        Consola consola = new Consola();

        consola.mostrarMensaje("=========================================\n");
        consola.mostrarMensaje("   PRUEBA DEL FLUJO COMPLETO - TRIVIA    \n");
        consola.mostrarMensaje("=========================================\n");

        // 1. Pedir el nombre del jugador
        consola.mostrarMensaje("Ingresa tu nombre: ");
        String nombre = consola.leerEntrada();
        if (nombre.isEmpty()) {
            nombre = "Jugador";
        }

        // 2. Crear banco de preguntas de prueba
        BancoPreguntas banco = cargarPreguntasDePrueba();

        // 3. Crear el juego con el nombre y el banco, e iniciar la partida
        Juego juego = new Juego(nombre, banco);
        juego.iniciarPartida();
    }

    private static BancoPreguntas cargarPreguntasDePrueba() {
        BancoPreguntas banco = new BancoPreguntas();

        // Dos preguntas de Nivel 1 (Bloque 1) para probar el comodín "Cambio de pregunta"
        Pregunta p1a = new Pregunta("¿Qué estructura sigue el principio FIFO?", 1, "A");
        p1a.agregarOpcion("A) Cola");
        p1a.agregarOpcion("B) Pila");
        p1a.agregarOpcion("C) Árbol");
        p1a.agregarOpcion("D) Grafo");

        Pregunta p1b = new Pregunta("¿Qué estructura sigue el principio LIFO?", 1, "B");
        p1b.agregarOpcion("A) Cola");
        p1b.agregarOpcion("B) Pila");
        p1b.agregarOpcion("C) Lista doble");
        p1b.agregarOpcion("D) Arreglo");

        // Pregunta de Nivel 2 (Bloque 1)
        Pregunta p2 = new Pregunta("¿Qué palabra clave en Java impide instanciar una clase directamente?", 2, "C");
        p2.agregarOpcion("A) static");
        p2.agregarOpcion("B) final");
        p2.agregarOpcion("C) abstract");
        p2.agregarOpcion("D) private");

        // Pregunta de Nivel 4 (Bloque 2) para ver el cambio de categoría/bloque
        Pregunta p4 = new Pregunta("¿Cuál es el recorrido de un ABB que devuelve los elementos de mayor a menor?", 4, "D");
        p4.agregarOpcion("A) Preorden");
        p4.agregarOpcion("B) Inorden normal");
        p4.agregarOpcion("C) Por niveles");
        p4.agregarOpcion("D) Inorden inverso");

        banco.agregarPregunta(p1a);
        banco.agregarPregunta(p1b);
        banco.agregarPregunta(p2);
        banco.agregarPregunta(p4);

        return banco;
    }
}