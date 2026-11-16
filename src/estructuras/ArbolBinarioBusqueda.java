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

    public void insertar(T dato) {
        raiz = insertarRecursivo(raiz, dato); // se empieza a buscar el lugar desde la raiz
        tamanio++;
    }

    private NodoArbol<T> insertarRecursivo(NodoArbol<T> actual, T dato) {
        if (actual == null) { // llegamos a un espacio vacio, aqui va el nuevo nodo
            return new NodoArbol<>(dato);
        }

        if (dato.compareTo(actual.getDato()) < 0) { // si es menor se va por la izquierda
            actual.setIzquierdo(insertarRecursivo(actual.getIzquierdo(), dato));
        } else { // si es mayor o igual se va por la derecha
            actual.setDerecho(insertarRecursivo(actual.getDerecho(), dato));
        }

        return actual; // se devuelve el mismo nodo para no romper el arbol
    }

    public boolean estaVacio() {
        return raiz == null;
    }

    public int tamanio() {
        return tamanio;
    }
}
