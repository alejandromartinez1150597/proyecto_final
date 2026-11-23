package juego;

import modelo.*;
import estructuras.*;

public class Juego {

    private Consola consola;
    private Jugador jugador;
    private BancoPreguntas banco;

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

        consola.mostrarMensaje("\nPartida finalizada. Premio final: " + jugador.mostrarPremioAcumulado() + "\n");
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

            int cantCambio = jugador.contarComodin("Cambio de pregunta");
            if (cantCambio > 0) {
                consola.mostrarMensaje("\n¿Deseas usar 'Cambio de pregunta' (tienes " + cantCambio
                        + "/2)? Esto reiniciará tu multiplicador. (S/N): ");
                String eleccion = consola.leerEntrada().toUpperCase();

                if (eleccion.equals("S")) {
                    Comodin consumido = jugador.usarYConsumirComodin("Cambio de pregunta");
                    if (consumido instanceof ComodinCambioPregunta) {
                        Pregunta nueva = ((ComodinCambioPregunta) consumido).cambiarPregunta(preguntaActual, banco);
                        usoComodinEnRonda = true;
                        jugador.resetearMultiplicador();

                        if (nueva != null && !nueva.getEnunciado().equals(preguntaActual.getEnunciado())) {
                            preguntaActual = nueva;
                            consola.mostrarMensaje("¡Pregunta cambiada! Multiplicador ahora en x" + jugador.getMultiplicador() + ".\n");
                            continue;
                        } else {
                            consola.mostrarMensaje("No había otra pregunta distinta disponible en este nivel; mantienes la actual.\n");
                        }
                    }
                }
            }
            break;
        }

        consola.mostrarMensaje("Ingresa tu respuesta (A, B, C o D): ");
        String respuesta = consola.leerOpcionPregunta();

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

    // Se añade menú de compra de comodines post nivel
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
