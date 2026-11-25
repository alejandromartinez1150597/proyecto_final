package modelo;

import java.util.Random;

// simula que el publico vota por cada opcion y muestra los porcentajes.
// la respuesta correcta se lleva entre el 40% y el 70%, el resto se reparte entre las otras
public class ComodinPublico extends Comodin {

    private static final String LETRAS = "ABCD";

    private Random azar;
    private int[] porcentajes; // posicion 0 es la A, 1 la B, 2 la C y 3 la D

    public ComodinPublico() {
        this(new Random());
    }

    // este constructor deja pasar un Random con semilla para poder probarlo siempre igual
    public ComodinPublico(Random azar) {
        super("Ayuda del publico", 2000000L);
        this.azar = azar;
        this.porcentajes = new int[4];
    }

    @Override
    public void usar(Pregunta pregunta) {
        if (fueUsado()) {
            return;
        }

        int correcta = LETRAS.indexOf(pregunta.getRespuestaCorrecta().toUpperCase());
        porcentajes = new int[4];

        // solo votan por las incorrectas que siguen en pantalla (si antes usaron el 50/50 quedan menos)
        int[] incorrectas = new int[3];
        int cuantas = 0;
        for (int i = 0; i < 4; i++) {
            if (i != correcta && sigueEnPantalla(pregunta, i)) {
                incorrectas[cuantas] = i;
                cuantas++;
            }
        }

        // con las 4 opciones la correcta saca entre 40 y 70. si ya quedan menos (por el 50/50)
        // saca entre 55 y 80, porque si no la unica incorrecta se quedaria con todo y le ganaria
        if (cuantas == 3) {
            porcentajes[correcta] = 40 + azar.nextInt(31);
        } else {
            porcentajes[correcta] = 55 + azar.nextInt(26);
        }

        // los puntos que sobran se reparten de a uno entre las incorrectas,
        // asi cada una queda mas o menos parejo y casi nunca le gana a la correcta
        int sobrante = 100 - porcentajes[correcta];
        if (cuantas == 0) { // no quedan incorrectas, todo para la correcta
            porcentajes[correcta] = 100;
        } else {
            for (int i = 0; i < sobrante; i++) {
                porcentajes[incorrectas[azar.nextInt(cuantas)]]++;
            }
        }
        marcarUsado();
    }

    // revisa si la opcion con esa letra todavia esta en la lista de la pregunta.
    // si la pregunta no tiene opciones cargadas se toman las 4 como presentes
    private boolean sigueEnPantalla(Pregunta pregunta, int posicionLetra) {
        int total = pregunta.getOpciones().tamano();
        if (total == 0) {
            return true;
        }
        for (int i = 0; i < total; i++) {
            String opcion = (String) pregunta.getOpciones().obtener(i);
            if (opcion.charAt(0) == LETRAS.charAt(posicionLetra)) {
                return true;
            }
        }
        return false;
    }

    public int[] getPorcentajes() {
        return porcentajes;
    }

    // ejemplo: "A: 12%   B: 61%   C: 20%   D: 7%". las opciones que no recibieron votos no se muestran
    public String getResultado() {
        String texto = "";
        for (int i = 0; i < 4; i++) {
            if (porcentajes[i] > 0) {
                texto += LETRAS.charAt(i) + ": " + porcentajes[i] + "%   ";
            }
        }
        return texto.trim();
    }
}
