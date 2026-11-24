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
        consola.mostrarMensaje("Comodines iniciales: 1x Ayuda del público | 1x Cambio de pregunta | 1x 50/50\n");

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
            long valorBase = jugador.getEscalera().getValorNivel(preguntaActual.getNivel());
            long gananciaRonda = valorBase * jugador.getMultiplicador();

            consola.mostrarMensaje("\n--- " + preguntaActual.getCategoria() + " | Nivel " + preguntaActual.getNivel()
                    + " | Acumulado: " + jugador.mostrarPremioAcumulado()
                    + " | Valor pregunta: $" + String.format("%,d", valorBase)
                    + " (x" + jugador.getMultiplicador() + " = +$" + String.format("%,d", gananciaRonda) + ") ---\n");
            consola.mostrarMensaje(preguntaActual.getEnunciado() + "\n");

            for (int i = 0; i < preguntaActual.getOpciones().tamano(); i++) {
                consola.mostrarMensaje(preguntaActual.getOpciones().obtener(i) + "\n");
            }

            int cantPublico = jugador.contarComodin("Ayuda del publico");
            int cantCambio = jugador.contarComodin("Cambio de pregunta");
            int cant5050 = jugador.contarComodin("50/50");

            // Muestra los comodines disponibles antes de responder la pregunta
            if (cantPublico > 0 || cantCambio > 0 || cant5050 > 0) {
                consola.mostrarMensaje("\nComodines disponibles (usar uno reinicia tu multiplicador a x1):\n");
                consola.mostrarMensaje("  [1] Ayuda del público (" + cantPublico + "/2)\n");
                consola.mostrarMensaje("  [2] Cambio de pregunta (" + cantCambio + "/2)\n");
                consola.mostrarMensaje("  [3] 50/50 (" + cant5050 + "/2)\n");
                consola.mostrarMensaje("  [0] No usar comodín y responder\n");
                consola.mostrarMensaje("Elige una opción: ");

                String opcionComodin = consola.leerEntrada();

                if (opcionComodin.equals("1")) {
                    Comodin consumido = jugador.usarYConsumirComodin("Ayuda del publico");
                    if (consumido instanceof ComodinPublico) {
                        consumido.usar(preguntaActual);
                        usoComodinEnRonda = true;
                        jugador.resetearMultiplicador();
                        consola.mostrarMensaje("Votación del público: " + ((ComodinPublico) consumido).getResultado() + "\n");
                        continue;
                    } else {
                        consola.mostrarMensaje("No tienes comodines de Ayuda del público.\n");
                        continue;
                    }
                } else if (opcionComodin.equals("2")) {
                    Comodin consumido = jugador.usarYConsumirComodin("Cambio de pregunta");
                    if (consumido instanceof ComodinCambioPregunta) {
                        Pregunta nueva = ((ComodinCambioPregunta) consumido).cambiarPregunta(preguntaActual, banco);
                        usoComodinEnRonda = true;
                        jugador.resetearMultiplicador();

                        if (nueva != null && !nueva.getEnunciado().equals(preguntaActual.getEnunciado())) {
                            preguntaActual = nueva;
                            consola.mostrarMensaje("¡Pregunta cambiada! Multiplicador ahora en x" + jugador.getMultiplicador() + ".\n");
                        } else {
                            consola.mostrarMensaje("No había otra pregunta distinta disponible en este nivel; mantienes la actual.\n");
                        }
                        continue;
                    } else {
                        consola.mostrarMensaje("No tienes comodines de Cambio de pregunta.\n");
                        continue;
                    }
                } else if (opcionComodin.equals("3")) {
                    Comodin consumido = jugador.usarYConsumirComodin("50/50");
                    if (consumido instanceof Comodin5050) {
                        consumido.usar(preguntaActual);
                        usoComodinEnRonda = true;
                        jugador.resetearMultiplicador();
                        consola.mostrarMensaje("¡Comodín 50/50 aplicado! Se eliminaron 2 opciones incorrectas.\n");
                        continue;
                    } else {
                        consola.mostrarMensaje("No tienes comodines 50/50.\n");
                        continue;
                    }
                } else if (!opcionComodin.equals("0")) {
                    consola.mostrarMensaje("Opción inválida.\n");
                    continue;
                }
            }
            break;
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

        // Primero suma el premio y luego lo multiplica
        if (preguntaActual.verificarRespuesta(respuesta)) {
            jugador.avanzarNivel();
            if (!usoComodinEnRonda) {
                jugador.registrarAciertoSinComodin();
            }
            consola.mostrarMensaje("¡Correcto! Multiplicador siguiente nivel: x" + jugador.getMultiplicador()
                    + " | Premio: " + jugador.mostrarPremioAcumulado() + "\n");
            return true;
        } else {
            jugador.aplicarPenalidad();
            consola.mostrarMensaje("Incorrecto. La respuesta correcta era " + preguntaActual.getRespuestaCorrecta() + ".\n");
            consola.mostrarMensaje("Premio tras penalidad: " + jugador.mostrarPremioAcumulado() + "\n");
            return false;
        }
    }

    // Se añade menú de compra de comodines post nivel con precios y validación de saldo
    private void menuCompraComodin() {
        while (true) {
            int cantPublico = jugador.contarComodin("Ayuda del publico");
            int cantCambio = jugador.contarComodin("Cambio de pregunta");
            int cant5050 = jugador.contarComodin("50/50");

            consola.mostrarMensaje("\n--- TIENDA COMODINES (Saldo: " + jugador.mostrarPremioAcumulado() + " | Máx. 2 por comodín) ---\n");
            consola.mostrarMensaje("1. Comprar Ayuda del público  - $500,000   (Tienes: " + cantPublico + "/2)\n");
            consola.mostrarMensaje("2. Comprar Cambio de pregunta - $750,000   (Tienes: " + cantCambio + "/2)\n");
            consola.mostrarMensaje("3. Comprar 50/50              - $1,000,000 (Tienes: " + cant5050 + "/2)\n");
            consola.mostrarMensaje("4. Continuar al siguiente nivel\n");
            consola.mostrarMensaje("Elige una opción (1-4): ");

            String opcion = consola.leerEntrada();

            if (opcion.equals("1")) {
                intentarCompra(new ComodinPublico());
            } else if (opcion.equals("2")) {
                intentarCompra(new ComodinCambioPregunta());
            } else if (opcion.equals("3")) {
                intentarCompra(new Comodin5050());
            } else if (opcion.equals("4")) {
                break;
            } else {
                consola.mostrarMensaje("Opción inválida. Selecciona 1, 2, 3 o 4.\n");
            }
        }
    }

    // Valida el límite de 2 unidades por tipo y que el jugador tenga saldo suficiente
    private void intentarCompra(Comodin comodin) {
        if (!jugador.puedeComprarComodin(comodin.getNombre())) {
            consola.mostrarMensaje("No puedes comprar más: ya tienes el máximo de 2 comodines " + comodin.getNombre() + ".\n");
            return;
        }
        if (jugador.comprarComodin(comodin)) {
            consola.mostrarMensaje("¡Compraste un comodín " + comodin.getNombre() + " por $"
                    + String.format("%,d", comodin.getPrecio()) + "! Saldo restante: "
                    + jugador.mostrarPremioAcumulado() + "\n");
        } else {
            consola.mostrarMensaje("Saldo insuficiente. Necesitas $" + String.format("%,d", comodin.getPrecio())
                    + " y tienes " + jugador.mostrarPremioAcumulado() + ".\n");
        }
    }
}
