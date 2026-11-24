package modelo;

import estructuras.Pila;

public class EscaleraPremios {

    // premio base de cada nivel, la posicion 0 es el nivel 1
    private static final long[] PREMIOS = {
            100000, 200000, 300000, 500000, 1000000, 2000000,
            4000000, 8000000, 16000000, 32000000, 64000000, 100000000
    };

    private int nivelActual;
    private int rachaSinComodin;
    private int multiplicador;
    private long premioGanado; // guarda el premio acumulado con el multiplicador activo en cada ronda
    private long premioNivelSeguro; // respaldo del premio acumulado al alcanzar un nivel seguro
    private Pila<Integer> nivelesSuperados; // cada nivel que se pasa se apila, el de arriba es el ultimo

    public EscaleraPremios() {
        this.nivelActual = 0;
        this.rachaSinComodin = 0;
        this.multiplicador = 1;
        this.premioGanado = 0;
        this.premioNivelSeguro = 0;
        this.nivelesSuperados = new Pila<>();
    }

    public void subir(){
        this.nivelActual++;
        nivelesSuperados.apilar(nivelActual);
        long premioNivel = getValorNivel(nivelActual) * multiplicador;
        this.premioGanado += premioNivel; // suma el premio ganado en este nivel al acumulado

        if (esNivelSeguro(nivelActual)) {
            this.premioNivelSeguro = this.premioGanado;
        }
    }

    // devuelve el valor base del nivel indicado (1 a 12)
    public long getValorNivel(int nivel) {
        if (nivel < 1) {
            return 0;
        }
        return PREMIOS[Math.min(nivel, PREMIOS.length) - 1];
    }

    public String premioActual(){
        return "$" + String.format("%,d", getPremio());
    }

    // lo que lleva ganado acumulado
    public long getPremio() {
        return premioGanado;
    }

    // descuenta el costo de un comodin si el jugador tiene premio suficiente para pagarlo
    public boolean descontarCompra(long costo) {
        if (costo <= 0 || premioGanado < costo) {
            return false;
        }
        this.premioGanado -= costo;
        if (this.premioNivelSeguro > this.premioGanado) {
            this.premioNivelSeguro = this.premioGanado;
        }
        return true;
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
        this.premioGanado = (this.nivelActual == 0) ? 0 : this.premioNivelSeguro;
    }

    // los niveles seguros son el final de cada bloque: 3, 6 y 9
    public boolean esNivelSeguro(int nivel) {
        return nivel % 3 == 0;
    }

    public int getNivelActual() {
        return nivelActual;
    }

    // Suma acierto sin comodin: a partir de 2 seguidos sube el multiplicador para la siguiente pregunta (x2, x3, x4...)
    public void registrarAciertoSinComodin() {
        this.rachaSinComodin++;
        if (this.rachaSinComodin >= 2) {
            this.multiplicador = this.rachaSinComodin; // Se cambia el multiplicador, ya que estaba fijo en x2
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
