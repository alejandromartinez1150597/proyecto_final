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
        super("Ayuda del publico");
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
        porcentajes[correcta] = 40 + azar.nextInt(31); // entre 40 y 70

        // los puntos que sobran se reparten de a uno entre las incorrectas,
        // asi cada una queda mas o menos parejo y casi nunca le gana a la correcta
        int sobrante = 100 - porcentajes[correcta];
        for (int i = 0; i < sobrante; i++) {
            int opcion = azar.nextInt(3);
            if (opcion >= correcta) { // se salta la correcta
                opcion++;
            }
            porcentajes[opcion]++;
        }
        marcarUsado();
    }

    public int[] getPorcentajes() {
        return porcentajes;
    }

    // ejemplo: "A: 12%   B: 61%   C: 20%   D: 7%"
    public String getResultado() {
        String texto = "";
        for (int i = 0; i < 4; i++) {
            texto += LETRAS.charAt(i) + ": " + porcentajes[i] + "%   ";
        }
        return texto.trim();
    }
}
