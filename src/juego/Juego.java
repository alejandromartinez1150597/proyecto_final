package juego;

import modelo.*;
import estructuras.*;

public class Juego {

    private Consola consola;
    private Jugador jugador;
    private BancoPreguntas banco;
    private boolean seRetiro; // queda en true si el jugador decide plantarse

    public Juego(String nombreJugador) {
        this.consola = new Consola();
        this.jugador = new Jugador(nombreJugador);
        this.banco = new BancoPreguntas();
    }

    public Juego(String nombreJugador, BancoPreguntas banco) {
        this(nombreJugador, banco, new Consola());
    }

    // recibe la misma consola de Main para no tener dos Scanner leyendo el teclado
    public Juego(String nombreJugador, BancoPreguntas banco, Consola consola) {
        this.consola = consola;
        this.jugador = new Jugador(nombreJugador);
        this.banco = (banco != null) ? banco : new BancoPreguntas();
    }

    // Este metodo inicia el ciclo de la partida
    public void iniciarPartida() {
        consola.mostrarMensaje("=========================================\n");
        consola.mostrarMensaje(" BIENVENIDO A LA TRIVIA: ¿TE VOLVERÁS MILLONARIO?! \n");
        consola.mostrarMensaje("=========================================\n");

        consola.mostrarMensaje("Jugador registrado: " + jugador.getNombre() + "\n");
        consola.mostrarMensaje("Premio actual: " + jugador.mostrarPremioAcumulado() + "\n");

        for (int nivel = 1; nivel <= 12; nivel++) {
            Pregunta pregunta = banco.siguientePregunta(nivel);
            if (pregunta == null) {
                consola.mostrarMensaje("\nNo hay preguntas cargadas para el nivel " + nivel + ". Fin del ciclo de prueba.\n");
                break;
            }

            boolean ganoRonda = jugarRonda(pregunta, banco);
            if (!ganoRonda) {
                break;
            }

            if (nivel < 12) {
                menuCompraComodin();
            }
        }

        String comoTermino = seRetiro ? " (se retiró)" : "";
        consola.mostrarMensaje("\nPartida finalizada" + comoTermino + ". Premio final: " + jugador.mostrarPremioAcumulado() + "\n");
        guardarEnRanking();
    }

    // al terminar se carga el ranking del archivo, se agrega el puntaje de esta partida,
    // se guarda otra vez y se muestran los 5 mejores
    private void guardarEnRanking() {
        Ranking ranking = new Ranking();
        ArchivoPuntajes archivo = new ArchivoPuntajes("puntajes.txt");
        archivo.cargar(ranking);

        ranking.agregar(new Puntaje(jugador.getNombre(), jugador.getEscalera().getPremio()));
        archivo.guardar(ranking);

        consola.mostrarMensaje("\n" + ranking.mostrarTop(5) + "\n");
    }

    private boolean jugarRonda(Pregunta preguntaActual, BancoPreguntas banco) {
        boolean usoComodinEnRonda = false;

        while (true) {
            consola.mostrarMensaje("\n--- " + preguntaActual.getCategoria() + " | Nivel " + preguntaActual.getNivel()
                    + " | Multiplicador actual: x" + jugador.getMultiplicador() + " ---\n");
            consola.mostrarMensaje(preguntaActual.getEnunciado() + "\n");

            for (int i = 0; i < preguntaActual.getOpciones().tamano(); i++) {
                consola.mostrarMensaje(preguntaActual.getOpciones().obtener(i) + "\n");
            }

            String eleccion = elegirComodin(preguntaActual);
            if (eleccion.isEmpty()) { // no tiene comodines o decidio responder de una
                break;
            }

            // cualquier comodin que use le quita el multiplicador
            usoComodinEnRonda = true;
            jugador.resetearMultiplicador();

            if (eleccion.equals("1")) {
                Comodin consumido = jugador.usarYConsumirComodin("50/50");
                consumido.usar(preguntaActual);
                consola.mostrarMensaje("¡Se quitaron dos respuestas incorrectas!\n");
            } else if (eleccion.equals("2")) {
                Comodin consumido = jugador.usarYConsumirComodin("Cambio de pregunta");
                Pregunta nueva = ((ComodinCambioPregunta) consumido).cambiarPregunta(preguntaActual, banco);
                if (nueva != null && !nueva.getEnunciado().equals(preguntaActual.getEnunciado())) {
                    preguntaActual = nueva;
                    consola.mostrarMensaje("¡Pregunta cambiada!\n");
                } else {
                    consola.mostrarMensaje("No había otra pregunta distinta disponible en este nivel; mantienes la actual.\n");
                }
            } else if (eleccion.equals("3")) {
                ComodinPublico publico = (ComodinPublico) jugador.usarYConsumirComodin("Ayuda del publico");
                publico.usar(preguntaActual);
                consola.mostrarMensaje("El público votó:  " + publico.getResultado() + "\n");
            }
            // se vuelve a mostrar la pregunta como quedo despues del comodin
        }

        consola.mostrarMensaje("Ingresa tu respuesta (A, B, C o D) o R para retirarte con "
                + jugador.mostrarPremioAcumulado() + ": ");
        String respuesta = consola.leerRespuestaORetiro();

        // al retirarse no se cae al nivel seguro, se lleva lo que tenia ganado
        if (respuesta.equals("R")) {
            seRetiro = true;
            consola.mostrarMensaje("Te retiraste. Te llevas " + jugador.mostrarPremioAcumulado() + ".\n");
            return false;
        }

        if (preguntaActual.verificarRespuesta(respuesta)) {
            if (!usoComodinEnRonda) {
                jugador.registrarAciertoSinComodin();
            }
            jugador.avanzarNivel();
            consola.mostrarMensaje("¡Correcto! Multiplicador: x" + jugador.getMultiplicador()
                    + " | Premio: " + jugador.mostrarPremioAcumulado() + "\n");
            return true;
        } else {
            jugador.aplicarPenalidad();
            consola.mostrarMensaje("Incorrecto. La respuesta correcta era " + preguntaActual.getRespuestaCorrecta() + ".\n");
            consola.mostrarMensaje("Premio tras penalidad: " + jugador.mostrarPremioAcumulado() + "\n");
            return false;
        }
    }

    // muestra los comodines que tiene y devuelve "1", "2" o "3" segun el que escoja.
    // devuelve "" si no tiene ninguno o si presiona Enter para responder de una
    private String elegirComodin(Pregunta pregunta) {
        int cant5050 = jugador.contarComodin("50/50");
        int cantCambio = jugador.contarComodin("Cambio de pregunta");
        int cantPublico = jugador.contarComodin("Ayuda del publico");
        if (cant5050 + cantCambio + cantPublico == 0) {
            return "";
        }

        while (true) {
            consola.mostrarMensaje("\nTus comodines: 1. 50/50 (" + cant5050 + ")   2. Cambio de pregunta (" + cantCambio
                    + ")   3. Ayuda del público (" + cantPublico + ")\n");
            consola.mostrarMensaje("Usar un comodín reinicia tu multiplicador. Escribe 1, 2 o 3, o solo Enter para responder: ");
            String opcion = consola.leerEntrada();

            if (opcion.isEmpty()) {
                return "";
            } else if (opcion.equals("1") && cant5050 > 0) {
                if (pregunta.getOpciones().tamano() <= 2) { // ya se le habia aplicado un 50/50
                    consola.mostrarMensaje("Ya solo quedan dos opciones, el 50/50 no serviría.\n");
                } else {
                    return opcion;
                }
            } else if ((opcion.equals("2") && cantCambio > 0) || (opcion.equals("3") && cantPublico > 0)) {
                return opcion;
            } else {
                consola.mostrarMensaje("No tienes ese comodín o la opción no es válida.\n");
            }
        }
    }

    // Se añade menú de compra de comodines post nivel. cada comodin se paga con lo que lleva ganado
    private void menuCompraComodin() {
        while (true) {
            consola.mostrarMensaje("\n--- TIENDA COMODINES (Máx. 2 por comodín) ---\n");
            consola.mostrarMensaje("Tu dinero: " + jugador.mostrarPremioAcumulado() + "\n");
            consola.mostrarMensaje("1. Comprar 50/50 - " + precio(new Comodin5050())
                    + " (Tienes: " + jugador.contarComodin("50/50") + "/2)\n");
            consola.mostrarMensaje("2. Comprar Cambio de pregunta - " + precio(new ComodinCambioPregunta())
                    + " (Tienes: " + jugador.contarComodin("Cambio de pregunta") + "/2)\n");
            consola.mostrarMensaje("3. Comprar Ayuda del público - " + precio(new ComodinPublico())
                    + " (Tienes: " + jugador.contarComodin("Ayuda del publico") + "/2)\n");
            consola.mostrarMensaje("4. Continuar al siguiente nivel\n");
            consola.mostrarMensaje("Elige una opción (1, 2, 3 o 4): ");

            String opcion = consola.leerEntrada();

            if (opcion.equals("1")) {
                comprar(new Comodin5050());
            } else if (opcion.equals("2")) {
                comprar(new ComodinCambioPregunta());
            } else if (opcion.equals("3")) {
                comprar(new ComodinPublico());
            } else if (opcion.equals("4")) {
                break;
            } else {
                consola.mostrarMensaje("Opción inválida. Selecciona 1, 2, 3 o 4.\n");
            }
        }
    }

    // intenta comprar el comodin y dice por que no se pudo si falla
    private void comprar(Comodin comodin) {
        String nombre = comodin.getNombre();
        if (!jugador.puedeComprarComodin(nombre)) {
            consola.mostrarMensaje("No puedes comprar más: ya tienes el máximo de 2 comodines " + nombre + ".\n");
        } else if (!jugador.leAlcanzaPara(comodin)) {
            consola.mostrarMensaje("No te alcanza: el " + nombre + " cuesta " + precio(comodin)
                    + " y tienes " + jugador.mostrarPremioAcumulado() + ".\n");
        } else if (jugador.comprarComodin(comodin)) {
            consola.mostrarMensaje("¡Compraste un comodín " + nombre + "! Ahora tienes " + jugador.contarComodin(nombre)
                    + "/2. Te quedan " + jugador.mostrarPremioAcumulado() + ".\n");
        }
    }

    private String precio(Comodin comodin) {
        return "$" + String.format("%,d", comodin.getPrecio());
    }
}
