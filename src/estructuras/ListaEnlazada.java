package estructuras;

//clase ListaEnlazada para listas enlazadas. En listas genericas.
public class ListaEnlazada<T> {
    private Nodo<T> cabeza; // referencia al primer nodo de la lista 
    private Nodo<T> cola; // referencia al ultimo nodo de la lista

    public ListaEnlazada() {
        this.cabeza = null; // cuando se crea la lista no tiene nodos
        this.cola = null;
    }

    public void agregar(T dato){
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        if (cabeza == null){
            cabeza = nuevoNodo;
            cola = nuevoNodo;
        }else{
            cola.siguiente = nuevoNodo;
            nuevoNodo.anterior = cola;
            cola = nuevoNodo;
        }
    }
}
