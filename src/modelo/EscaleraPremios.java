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
    private long gastado; // lo que se ha gastado comprando comodines, se le descuenta al premio
    private Pila<Integer> nivelesSuperados; // cada nivel que se pasa se apila, el de arriba es el ultimo

    public EscaleraPremios() {
        this.nivelActual = 0;
        this.rachaSinComodin = 0;
        this.multiplicador = 1;
        this.gastado = 0;
        this.nivelesSuperados = new Pila<>();
    }

    public void subir(){
        this.nivelActual++;
        nivelesSuperados.apilar(nivelActual);
    }

    public String premioActual(){
        return "$" + String.format("%,d", getPremio());
    }

    // lo que lleva ganado segun el ultimo nivel superado, menos lo que gasto en comodines.
    // si cae a un nivel seguro con menos plata de la que gasto, queda en 0 y no en negativo
    public long getPremio() {
        if (nivelActual == 0) {
            return 0;
        }
        long premioNivel = PREMIOS[Math.min(nivelActual, PREMIOS.length) - 1];
        return Math.max(0, premioNivel - gastado);
    }

    // descuenta el precio si le alcanza. devuelve false si no tiene suficiente
    public boolean pagar(long monto) {
        if (monto > getPremio()) {
            return false;
        }
        gastado += monto;
        return true;
    }

    public long getGastado() {
        return gastado;
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
