package modelo;
import estructuras.*;

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

    public void agregarOpcion(String textoOpcion){ // Metodo para agregar las opciones a seleccionar
        this.opciones.agregar(textoOpcion);
    }

    public boolean verificarRespuesta(String respuestaJugador){ // Verifica si la respuesta es correcta
        return this.respuestaCorrecta.equalsIgnoreCase(respuestaJugador);
    }

    // Se generan getters para los atributos de la clase, ya que estos son privados
    public String getEnunciado(){
        return enunciado;
    }

    public int getNivel(){
        return nivel;
    }

    public String getRespuestaCorrecta(){
        return respuestaCorrecta;
    }
}
