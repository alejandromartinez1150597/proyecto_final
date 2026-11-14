package estructuras;

// Nodo para el arbol binario. Se deja generico (T) para poder usar el mismo arbol
// en el banco de preguntas y en el ranking
public class NodoArbol<T> {

    private T dato;
    private NodoArbol<T> izquierdo; // aqui van los menores
    private NodoArbol<T> derecho; // aqui van los mayores o iguales

    public NodoArbol(T dato) {
        this.dato = dato;
        this.izquierdo = null; // cuando se crea no tiene hijos
        this.derecho = null;
    }

    public T getDato() {
        return dato;
    }

    public NodoArbol<T> getIzquierdo() {
        return izquierdo;
    }

    public void setIzquierdo(NodoArbol<T> izquierdo) {
        this.izquierdo = izquierdo;
    }

    public NodoArbol<T> getDerecho() {
        return derecho;
    }

    public void setDerecho(NodoArbol<T> derecho) {
        this.derecho = derecho;
    }

    public boolean esHoja() { // si no tiene ningun hijo es una hoja
        return izquierdo == null && derecho == null;
    }
}
