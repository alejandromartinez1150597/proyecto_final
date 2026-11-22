package estructuras;

// pila generica hecha con nodos. el ultimo que entra es el primero que sale (LIFO).
// en el juego guarda los niveles que el jugador va superando
public class Pila<T> {

    private Nodo<T> cima; // el de arriba, el unico que se puede sacar
    private int tamanio;

    public Pila() {
        this.cima = null;
        this.tamanio = 0;
    }

    public void apilar(T dato) { // pone el dato encima de todos
        Nodo<T> nuevo = new Nodo<>(dato);
        nuevo.setSiguiente(cima); // el nuevo queda apuntando al que antes estaba arriba
        cima = nuevo;
        tamanio++;
    }

    public T desapilar() { // saca el de arriba
        if (estaVacia()) {
            throw new IllegalStateException("La pila esta vacia");
        }

        T dato = cima.getDato();
        cima = cima.getSiguiente(); // el de abajo pasa a ser la nueva cima
        tamanio--;
        return dato;
    }

    public T verCima() { // mira el de arriba sin sacarlo
        if (estaVacia()) {
            throw new IllegalStateException("La pila esta vacia");
        }
        return cima.getDato();
    }

    public boolean estaVacia() {
        return cima == null;
    }

    public int tamanio() {
        return tamanio;
    }
}
