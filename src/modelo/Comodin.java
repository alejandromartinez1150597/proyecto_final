package modelo;

public abstract class Comodin { // Se seleciona una clase abstracta, ya que servirá de plantillas para los comodines

    protected String nombre; // Protected? por comodidad, para que las subclases tenga acceso a estos atributos
    protected boolean usado;

    public abstract void usar (Pregunta pregunta);
    public abstract boolean fueUsado();
}
