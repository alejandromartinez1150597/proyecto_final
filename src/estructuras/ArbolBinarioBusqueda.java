package estructuras;

// Arbol binario de busqueda generico. T tiene que ser Comparable para poder
// decidir si un dato va a la izquierda o a la derecha
public class ArbolBinarioBusqueda<T extends Comparable<T>> {

    private NodoArbol<T> raiz;
    private int tamanio;

    public ArbolBinarioBusqueda() {
        this.raiz = null; // el arbol empieza vacio
        this.tamanio = 0;
    }

    public boolean estaVacio() {
        return raiz == null;
    }

    public int tamanio() {
        return tamanio;
    }
}
