package juego;

import modelo.*;

public class Juego {

    private Consola consola;
    private Jugador jugador;
    private BancoPreguntas banco;
    private boolean seRetiro; // queda en true si el jugador decide plantarse
    private Bolsa bolsa = new Bolsa("bolsa.txt"); // lo que pierden los demas, se la lleva el que gane las 12
    private EventoPeligroso eventoPeligroso = new EventoPeligroso(16); // probabilidad de que salga el evento
    private static final int NIVEL_TIENDA = 5; // despues de esta pregunta se abre la tienda, solo esa vez
    
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
        bolsa.cargar();
        consola.mostrarTitulo("¡BIENVENIDO, " + jugador.getNombre().toUpperCase() + "!");
        consola.mostrarMensaje(" - Responde 12 preguntas, cada una mas dificil y mejor pagada.\n");
        consola.mostrarMensaje(" - Si fallas, pierdes todo lo que llevas y se va a la bolsa.\n");
        consola.mostrarMensaje(" - Si respondes las 12, te llevas la bolsa completa.\n");
        consola.mostrarMensaje(" - Aciertos seguidos sin comodines suben tu multiplicador (x2, x3...).\n");
        consola.mostrarMensaje(" - Empiezas con 1 comodin de cada tipo. Para usar uno escribe 0 en vez de la respuesta.\n");
        consola.mostrarMensaje(" - Despues de la pregunta 5 abre la tienda (solo esa vez) para recomprar los que gastaste.\n");
        consola.mostrarMensaje(" - En cualquier pregunta puedes escribir R para retirarte.\n");
        consola.mostrarMensaje("\n La bolsa tiene " + Consola.dinero(bolsa.getMonto()) + ".\n");

        boolean completo = false; // queda en true si responde bien las 12
        for (int nivel = 1; nivel <= 12; nivel++) {
            Pregunta pregunta = null;
            boolean riesgo = false;
            long bono = jugador.getEscalera().getValorNivel(nivel); // el bono es igual al valor del nivel
            
            // El evento peligroso NO se crea en el primer nivel (nivel > 1)
            if (nivel > 1 && eventoPeligroso.sale(nivel) && eventoPeligroso.ofrecer(consola, bono)) {
                pregunta = banco.siguientePregunta(eventoPeligroso.nivelDificil(nivel));
                riesgo = (pregunta != null);
            }
            
            if (pregunta == null) {
                pregunta = banco.siguientePregunta(nivel); // seguir normal
            }
            
            if (pregunta == null) { 
                consola.mostrarMensaje(" >> No hay más preguntas disponibles en el banco. Fin del juego.\n");
                break; 
            }
            
            // Si es riesgo (!riesgo pasa true para permitir, !riesgo con true pasa false para prohibir comodines)
            boolean ganoRonda = jugarRonda(pregunta, banco, !riesgo);
            if (!ganoRonda) { break; }
            
            if (riesgo) {
                jugador.getEscalera().sumarBolsa(bono);
                consola.mostrarMensaje(" >> ¡Bono de riesgo! Ganas " + Consola.dinero(bono) + " extra.\n");
            }
            
            // la tienda se abre una sola vez, despues de responder bien la pregunta 5
            if (nivel == NIVEL_TIENDA) {
                menuCompraComodin(nivel + 1);
            }
            if (nivel == 12) {
                completo = true;
            }
        }

        // el que responde las 12 se lleva la bolsa y queda vacia para los siguientes
        if (completo && bolsa.getMonto() > 0) {
            long premioBolsa = bolsa.entregar();
            jugador.getEscalera().sumarBolsa(premioBolsa);
            bolsa.guardar();
            consola.mostrarMensaje("\n >> ¡TE LLEVAS LA BOLSA! Se suman " + Consola.dinero(premioBolsa) + " a tu premio.\n");
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

    private boolean jugarRonda(Pregunta preguntaActual, BancoPreguntas banco, boolean permitirComodines) {
        boolean usoComodinEnRonda = false;

        // el numero y el valor salen del nivel en que va el jugador, no del nivel de la pregunta,
        // porque en la pregunta de riesgo la pregunta es de dos niveles mas arriba
        int nivelJugador = jugador.getEscalera().getNivelActual() + 1;
        String votoPublico = null; // se guarda para mostrarlo debajo de las opciones y no se pierda en pantalla
        String respuesta; // la letra que responde, o R si se retira

        while (true) {
            long valorBase = jugador.getEscalera().getValorNivel(nivelJugador);
            long gananciaRonda = valorBase * jugador.getMultiplicador();

            String valor = Consola.dinero(gananciaRonda);
            if (jugador.getMultiplicador() > 1) {
                valor += " (" + Consola.dinero(valorBase) + " x" + jugador.getMultiplicador() + ")";
            }
            String titulo = "PREGUNTA " + nivelJugador + " DE 12   |   ";
            titulo += permitirComodines ? preguntaActual.getCategoria() : "PREGUNTA DE RIESGO";
            consola.mostrarSeccion(titulo,
                    "Llevas: " + jugador.mostrarPremioAcumulado() + "   |   Esta pregunta vale: " + valor);
            consola.mostrarMensaje("\n " + preguntaActual.getEnunciado() + "\n\n");

            for (int i = 0; i < preguntaActual.getOpciones().tamano(); i++) {
                consola.mostrarMensaje("     " + preguntaActual.getOpciones().obtener(i) + "\n");
            }
            if (votoPublico != null) {
                consola.mostrarMensaje("\n >> Votación del público: " + votoPublico + "\n");
            }

            if (!permitirComodines) {
                consola.mostrarMensaje("\n >> [EVENTO PELIGROSO: Los comodines están prohibidos en esta pregunta]\n");
            }

            int cantPublico = jugador.contarComodin("Ayuda del publico");
            int cantCambio = jugador.contarComodin("Cambio de pregunta");
            int cant5050 = jugador.contarComodin("50/50");
            boolean puedeUsarComodin = permitirComodines && (cantPublico > 0 || cantCambio > 0 || cant5050 > 0);

            // se responde directo con la letra. el 0 solo se escribe si quiere usar un comodin
            if (puedeUsarComodin) {
                consola.mostrarMensaje("\n Comodines: Público (" + cantPublico + ")   Cambio de pregunta (" + cantCambio
                        + ")   50/50 (" + cant5050 + ")\n");
                consola.mostrarMensaje(" Tu respuesta (A, B, C o D), 0 para usar un comodín o R para retirarte con "
                        + jugador.mostrarPremioAcumulado() + ": ");
                respuesta = consola.leerRespuestaComodinORetiro();
            } else {
                consola.mostrarMensaje("\n Tu respuesta (A, B, C o D) o R para retirarte con "
                        + jugador.mostrarPremioAcumulado() + ": ");
                respuesta = consola.leerRespuestaORetiro();
            }

            if (!respuesta.equals("0")) {
                break; // respondio con una letra o con R
            }

            // escribio 0: se abre el menu de comodines
            consola.mostrarMensaje("\n Comodines (usar uno reinicia tu multiplicador a x1):\n");
            consola.mostrarMensaje("   [1] Público (" + cantPublico + ")   [2] Cambio de pregunta (" + cantCambio
                    + ")   [3] 50/50 (" + cant5050 + ")\n");
            consola.mostrarMensaje("   [0] Volver a la pregunta\n");
            consola.mostrarMensaje(" Elige un comodín: ");

            String opcionComodin = consola.leerEntrada();

            if (opcionComodin.equals("1")) {
                Comodin consumido = jugador.usarYConsumirComodin("Ayuda del publico");
                if (consumido instanceof ComodinPublico) {
                    consumido.usar(preguntaActual);
                    usoComodinEnRonda = true;
                    jugador.resetearMultiplicador();
                    votoPublico = ((ComodinPublico) consumido).getResultado();
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
                        votoPublico = null; // la votacion era de la pregunta anterior
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
            }
        }

        // al retirarse no pierde nada, se lleva lo que tenia ganado
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
            if (jugador.getMultiplicador() > 1 && nivelJugador < 12) {
                consola.mostrarMensaje(" >> Vas en racha: la siguiente pregunta paga x" + jugador.getMultiplicador() + ".\n");
            }
            return true;
        } else {
            // pierde todo y eso se va a la bolsa
            long perdido = jugador.aplicarPenalidad();
            bolsa.agregar(perdido);
            bolsa.guardar();
            consola.mostrarMensaje("\n >> INCORRECTO. La respuesta era " + textoOpcion(preguntaActual) + ".\n");
            if (perdido > 0) {
                consola.mostrarMensaje(" >> Pierdes todo: " + Consola.dinero(perdido) + " se van a la bolsa.\n");
            } else {
                consola.mostrarMensaje(" >> Te vas sin nada.\n");
            }
            consola.mostrarMensaje(" >> La bolsa ahora tiene " + Consola.dinero(bolsa.getMonto()) + ".\n");
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
            consola.mostrarMensaje(" >> Abrió la tienda de comodines, pero no te alcanza: el más barato vale "
                    + Consola.dinero(masBarato) + ".\n");
            return;
        }

        while (true) {
            int cantPublico = jugador.contarComodin("Ayuda del publico");
            int cantCambio = jugador.contarComodin("Cambio de pregunta");
            int cant5050 = jugador.contarComodin("50/50");

            consola.mostrarSeccion("TIENDA DE COMODINES   |   Tu saldo: " + jugador.mostrarPremioAcumulado(),
                    "Solo abre esta vez. Puedes tener 1 de cada uno y el precio se descuenta del premio.");
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

    // una fila de la tienda alineada, ejemplo "[1] Ayuda del público    $2,000,000   tienes 0/1"
    private String filaTienda(int numero, String nombre, long precio, int tiene) {
        return String.format("   [%d] %-20s %12s   tienes %d/%d%n", numero, nombre, Consola.dinero(precio), tiene,
                Jugador.MAX_COMODIN_TIPO);
    }

    // Valida que no tenga ya ese comodin y que el jugador tenga saldo suficiente
    private void intentarCompra(Comodin comodin) {
        if (!jugador.puedeComprarComodin(comodin.getNombre())) {
            consola.mostrarMensaje(" >> Ya tienes el comodín " + comodin.getNombre() + ", solo se puede tener uno.\n");
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