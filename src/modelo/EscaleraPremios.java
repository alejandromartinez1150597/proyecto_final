package modelo;

import estructuras.Pila;

public class EscaleraPremios {

    // premio que se gana al superar cada nivel, la posicion 0 es el nivel 1
    private static final long[] PREMIOS = {
        100000, 200000, 300000, 500000, 1000000, 2000000,
        4000000, 8000000, 16000000, 32000000, 64000000, 100000000
    };

    private int nivelActual;
    private int rachaSinComodin;
    private int multiplicador;
    private Pila<Integer> nivelesSuperados; // cada nivel que se pasa se apila, el de arriba es el ultimo

    public EscaleraPremios() {
        this.nivelActual = 0;
        this.rachaSinComodin = 0;
        this.multiplicador = 1;
        this.nivelesSuperados = new Pila<>();
    }

    public void subir(){
        this.nivelActual++;
        nivelesSuperados.apilar(nivelActual);
    }

    public String premioActual(){
        return "$" + String.format("%,d", getPremio());
    }

    // lo que lleva ganado segun el ultimo nivel superado
    public long getPremio() {
        if (nivelActual == 0) {
            return 0;
        }
        return PREMIOS[Math.min(nivelActual, PREMIOS.length) - 1];
    }

    // al perder se van sacando niveles de la pila hasta llegar a un nivel seguro.
    // si no habia llegado a ninguno, la pila queda vacia y se va sin nada
    public void caerNivelSeguro(){
        while (!nivelesSuperados.estaVacia() && !esNivelSeguro(nivelesSuperados.verCima())) {
            nivelesSuperados.desapilar();
        }
        this.nivelActual = nivelesSuperados.estaVacia() ? 0 : nivelesSuperados.verCima();
        this.rachaSinComodin = 0;
        this.multiplicador = 1;
    }

    // los niveles seguros son el final de cada bloque: 3, 6 y 9
    public boolean esNivelSeguro(int nivel) {
        return nivel % 3 == 0;
    }

    public int getNivelActual() {
        return nivelActual;
    }

    // Suma acierto sin comodin, por ejemplo, cada 2 seguidos sube el multiplicador
    public void registrarAciertoSinComodin() {
        this.rachaSinComodin++;
        if (this.rachaSinComodin >= 2) {
            this.multiplicador = 2;
        }
    }

    // Si usa comodin, pierde el multiplicador y reinicia la racha
    public void resetearMultiplicador() {
        this.rachaSinComodin = 0;
        this.multiplicador = 1;
    }

    public int getMultiplicador() {
        return multiplicador;
    }

    public int getRachaSinComodin() {
        return rachaSinComodin;
    }

}
