package modelo;

public class EscaleraPremios {

    private int nivelActual;
    private int rachaSinComodin;
    private int multiplicador;

    public EscaleraPremios() {
        this.nivelActual = 0;
        this.rachaSinComodin = 0;
        this.multiplicador = 1;
    }

    public void subir(){
        this.nivelActual++;
    }

    public String premioActual(){
        return "0.0$";
    }

    public void caerNivelSeguro(){
        this.rachaSinComodin = 0;
        this.multiplicador = 1;
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
