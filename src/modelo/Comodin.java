package modelo;

public abstract class Comodin {

    protected String nombre; // Protected? por comodidad, para que las subclases tenga acceso a estos atributos
    protected boolean usado;

    public Comodin(String nombre) {
        this.nombre = nombre;
        this.usado = false;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean fueUsado() {
        return usado;
    }

    protected void marcarUsado() {
        this.usado = true;
    }

    public abstract void usar(Pregunta pregunta);
}
