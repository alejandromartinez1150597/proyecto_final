package modelo;
import estructuras.*;

public class Pregunta {

    private String enunciado;
    private int nivel;
    private String respuestaCorrecta;
    private ListaEnlazada<String> opciones;

    public Pregunta(String enunciado, int nivel, String respuestaCorrecta){
        this.enunciado = enunciado;
        this.nivel = nivel;
        this.respuestaCorrecta = respuestaCorrecta;
        this.opciones = new ListaEnlazada<>();
    }

    public void agregarOpcion(String textoOpcion){ // Metodo para agregar las opciones a seleccionar
        this.opciones.agregar(textoOpcion);
    }

    public boolean verificarRespuesta(String respuestaJugador){ // Verifica si la respuesta es correcta
        return this.respuestaCorrecta.equalsIgnoreCase(respuestaJugador);
    }

    // Devuelve según el nivel de la pregunta
    public int getBloque() {
        if (nivel >= 1 && nivel <= 3) {
            return 1;
        } else if (nivel >= 4 && nivel <= 6) {
            return 2;
        } else if (nivel >= 7 && nivel <= 9) {
            return 3;
        } else if (nivel >= 10 && nivel <= 12) {
            return 4;
        }
        return 0; // fuera de rango
    }

    // Categoria pregunta
    public String getCategoria() {
        switch (getBloque()) {
            case 1:
                return "Bloque 1 - Fácil (Niveles 1-3)";
            case 2:
                return "Bloque 2 - Normal 2 (Niveles 4-6)";
            case 3:
                return "Bloque 3 - Difícil 3 (Niveles 7-9)";
            case 4:
                return "Bloque 4 - Muy difícil 4 (Niveles 10-12)";
            default:
                return "Sin categoría";
        }
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

    public ListaEnlazada<String> getOpciones(){
        return opciones;
    }
}
