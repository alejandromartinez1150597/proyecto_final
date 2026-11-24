package modelo;

public abstract class Comodin {

    protected String nombre; // Protected? por comodidad, para que las subclases tenga acceso a estos atributos
    protected long precio; // Precio del comodin en pesos
    protected boolean usado;

    public Comodin(String nombre, long precio) {
        this.nombre = nombre;
        this.precio = precio;
        this.usado = false;
    }

    public String getNombre() {
        return nombre;
    }

    public long getPrecio() {
        return precio;
    }

    public boolean fueUsado() {
        return usado;
    }

    protected void marcarUsado() {
        this.usado = true;
    }

    public abstract void usar(Pregunta pregunta);
}
