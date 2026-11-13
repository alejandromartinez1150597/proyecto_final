package modelo;

public class Pregunta {

    private String enunciado;
    private int nivel;
    private String respuestaCorrecta;
    private ListaEnlazada opciones; // Se requiere crear la clase ListaEnlazada

    public Pregunta(String enunciado, int nivel, String respuestaCorrecta){
        this.enunciado = enunciado;
        this.nivel = nivel;
        this.respuestaCorrecta = respuestaCorrecta;
        this.opciones = new ListaEnlazada();
    }

}
