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
        consola.mostrarTitulo("¡BIENVENIDO, " + jugador.getNombre().toUpperCase() + "!");
        consola.mostrarMensaje(" - Responde 12 preguntas, cada una mas dificil y mejor pagada.\n");
        consola.mostrarMensaje(" - Niveles seguros: 3, 6 y 9. Si fallas, caes al ultimo que pasaste.\n");
        consola.mostrarMensaje(" - Aciertos seguidos sin comodines suben tu multiplicador (x2, x3...).\n");
        consola.mostrarMensaje(" - Empiezas con 1 comodin de cada tipo y puedes comprar mas.\n");
        consola.mostrarMensaje(" - En cualquier pregunta puedes escribir R para retirarte.\n");

        boolean completo = false; // queda en true si responde bien las 12
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
                menuCompraComodin(nivel + 1);
            } else {
                completo = true;
            }
        }

        String resultado;
        if (completo) {
            resultado = "¡Respondio las 12 preguntas!";
        } else if (seRetiro) {
            resultado = "Se retiro";
        } else {
            resultado = "Fallo una pregunta";
        }
        consola.mostrarTitulo("FIN DE LA PARTIDA");
        consola.mostrarMensaje(" Concursante:  " + jugador.getNombre() + "\n");
        consola.mostrarMensaje(" Resultado:    " + resultado + "\n");
        consola.mostrarMensaje(" Premio final: " + jugador.mostrarPremioAcumulado() + "\n");
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

            String valor = Consola.dinero(gananciaRonda);
            if (jugador.getMultiplicador() > 1) {
                valor += " (" + Consola.dinero(valorBase) + " x" + jugador.getMultiplicador() + ")";
            }
            consola.mostrarSeccion(
                    "PREGUNTA " + preguntaActual.getNivel() + " DE 12   |   " + preguntaActual.getCategoria(),
                    "Llevas: " + jugador.mostrarPremioAcumulado() + "   |   Esta pregunta vale: " + valor);
            consola.mostrarMensaje("\n " + preguntaActual.getEnunciado() + "\n\n");

            for (int i = 0; i < preguntaActual.getOpciones().tamano(); i++) {
                consola.mostrarMensaje("     " + preguntaActual.getOpciones().obtener(i) + "\n");
            }

            int cantPublico = jugador.contarComodin("Ayuda del publico");
            int cantCambio = jugador.contarComodin("Cambio de pregunta");
            int cant5050 = jugador.contarComodin("50/50");

            // Muestra los comodines disponibles antes de responder la pregunta
            if (cantPublico > 0 || cantCambio > 0 || cant5050 > 0) {
                consola.mostrarMensaje("\n Comodines (usar uno reinicia tu multiplicador a x1):\n");
                consola.mostrarMensaje("   [1] Público (" + cantPublico + ")   [2] Cambio de pregunta (" + cantCambio
                        + ")   [3] 50/50 (" + cant5050 + ")\n");
                consola.mostrarMensaje("   [0] Responder sin comodín\n");
                consola.mostrarMensaje(" Elige una opción: ");

                String opcionComodin = consola.leerEntrada();

                if (opcionComodin.equals("1")) {
                    Comodin consumido = jugador.usarYConsumirComodin("Ayuda del publico");
                    if (consumido instanceof ComodinPublico) {
                        consumido.usar(preguntaActual);
                        usoComodinEnRonda = true;
                        jugador.resetearMultiplicador();
                        consola.mostrarMensaje(" >> Votación del público: " + ((ComodinPublico) consumido).getResultado() + "\n");
                        continue;
                    } else {
                        consola.mostrarMensaje(" >> No tienes comodines de Ayuda del público.\n");
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
                            consola.mostrarMensaje(" >> ¡Pregunta cambiada! Multiplicador ahora en x" + jugador.getMultiplicador() + ".\n");
                        } else {
                            consola.mostrarMensaje(" >> No había otra pregunta distinta disponible en este nivel; mantienes la actual.\n");
                        }
                        continue;
                    } else {
                        consola.mostrarMensaje(" >> No tienes comodines de Cambio de pregunta.\n");
                        continue;
                    }
                } else if (opcionComodin.equals("3")) {
                    Comodin consumido = jugador.usarYConsumirComodin("50/50");
                    if (consumido instanceof Comodin5050) {
                        consumido.usar(preguntaActual);
                        usoComodinEnRonda = true;
                        jugador.resetearMultiplicador();
                        consola.mostrarMensaje(" >> ¡Comodín 50/50 aplicado! Se eliminaron 2 opciones incorrectas.\n");
                        continue;
                    } else {
                        consola.mostrarMensaje(" >> No tienes comodines 50/50.\n");
                        continue;
                    }
                } else if (!opcionComodin.equals("0")) {
                    consola.mostrarMensaje(" >> Opción inválida.\n");
                    continue;
                }
            }
            break;
        }

        consola.mostrarMensaje("\n Tu respuesta (A, B, C o D) o R para retirarte con "
                + jugador.mostrarPremioAcumulado() + ": ");
        String respuesta = consola.leerRespuestaORetiro();

        // al retirarse no se cae al nivel seguro, se lleva lo que tenia ganado
        if (respuesta.equals("R")) {
            seRetiro = true;
            consola.mostrarMensaje("\n >> Te retiraste. Te llevas " + jugador.mostrarPremioAcumulado() + ".\n");
            return false;
        }

        // Primero suma el premio y luego lo multiplica
        if (preguntaActual.verificarRespuesta(respuesta)) {
            long antes = jugador.getEscalera().getPremio();
            jugador.avanzarNivel();
            if (!usoComodinEnRonda) {
                jugador.registrarAciertoSinComodin();
            }
            long gano = jugador.getEscalera().getPremio() - antes;
            consola.mostrarMensaje("\n >> ¡CORRECTO! Ganaste " + Consola.dinero(gano)
                    + ". Llevas " + jugador.mostrarPremioAcumulado() + ".\n");
            if (jugador.getMultiplicador() > 1 && preguntaActual.getNivel() < 12) {
                consola.mostrarMensaje(" >> Vas en racha: la siguiente pregunta paga x" + jugador.getMultiplicador() + ".\n");
            }
            return true;
        } else {
            jugador.aplicarPenalidad();
            consola.mostrarMensaje("\n >> INCORRECTO. La respuesta era " + textoOpcion(preguntaActual) + ".\n");
            consola.mostrarMensaje(" >> Caes al ultimo nivel seguro con " + jugador.mostrarPremioAcumulado() + ".\n");
            return false;
        }
    }

    // busca el texto completo de la respuesta correcta, ejemplo "B) Piano"
    private String textoOpcion(Pregunta pregunta) {
        String letra = pregunta.getRespuestaCorrecta().toUpperCase();
        for (int i = 0; i < pregunta.getOpciones().tamano(); i++) {
            String opcion = pregunta.getOpciones().obtener(i);
            if (opcion.startsWith(letra)) {
                return opcion;
            }
        }
        return letra;
    }

    // Se añade menú de compra de comodines post nivel con precios y validación de saldo
    private void menuCompraComodin(int siguienteNivel) {
        // si no le alcanza ni para el mas barato no tiene sentido mostrar la tienda
        long masBarato = Math.min(new ComodinPublico().getPrecio(),
                Math.min(new ComodinCambioPregunta().getPrecio(), new Comodin5050().getPrecio()));
        if (jugador.getEscalera().getPremio() < masBarato) {
            consola.mostrarMensaje(" (La tienda de comodines se abre cuando tengas " + Consola.dinero(masBarato) + ")\n");
            return;
        }

        while (true) {
            int cantPublico = jugador.contarComodin("Ayuda del publico");
            int cantCambio = jugador.contarComodin("Cambio de pregunta");
            int cant5050 = jugador.contarComodin("50/50");

            consola.mostrarSeccion("TIENDA DE COMODINES   |   Tu saldo: " + jugador.mostrarPremioAcumulado(),
                    "Puedes tener maximo 2 de cada uno. El precio se descuenta del premio.");
            consola.mostrarMensaje(filaTienda(1, "Ayuda del público", new ComodinPublico().getPrecio(), cantPublico));
            consola.mostrarMensaje(filaTienda(2, "Cambio de pregunta", new ComodinCambioPregunta().getPrecio(), cantCambio));
            consola.mostrarMensaje(filaTienda(3, "50/50", new Comodin5050().getPrecio(), cant5050));
            consola.mostrarMensaje("   [4] Seguir a la pregunta " + siguienteNivel + "\n");
            consola.mostrarMensaje(" Elige una opción (1-4): ");

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
                consola.mostrarMensaje(" >> Opción inválida. Selecciona 1, 2, 3 o 4.\n");
            }
        }
    }

    // una fila de la tienda alineada, ejemplo "[1] Ayuda del público      $500,000   tienes 1/2"
    private String filaTienda(int numero, String nombre, long precio, int tiene) {
        return String.format("   [%d] %-20s %12s   tienes %d/2%n", numero, nombre, Consola.dinero(precio), tiene);
    }

    // Valida el límite de 2 unidades por tipo y que el jugador tenga saldo suficiente
    private void intentarCompra(Comodin comodin) {
        if (!jugador.puedeComprarComodin(comodin.getNombre())) {
            consola.mostrarMensaje(" >> No puedes comprar más: ya tienes el máximo de 2 comodines " + comodin.getNombre() + ".\n");
            return;
        }
        if (jugador.comprarComodin(comodin)) {
            consola.mostrarMensaje(" >> ¡Compraste un comodín " + comodin.getNombre() + " por $"
                    + String.format("%,d", comodin.getPrecio()) + "! Saldo restante: "
                    + jugador.mostrarPremioAcumulado() + "\n");
        } else {
            consola.mostrarMensaje(" >> Saldo insuficiente. Necesitas $" + String.format("%,d", comodin.getPrecio())
                    + " y tienes " + jugador.mostrarPremioAcumulado() + ".\n");
        }
    }
}
