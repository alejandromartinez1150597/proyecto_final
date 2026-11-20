package estructuras;

// cola generica hecha con nodos. el primero que entra es el primero que sale (FIFO).
// se usa para las preguntas de cada nivel y para los jugadores que esperan turno
public class Cola<T> {

    private Nodo<T> frente; // de aqui se sacan los datos
    private Nodo<T> fin; // aqui se meten los datos nuevos
    private int tamanio;

    public Cola() {
        this.frente = null;
        this.fin = null;
        this.tamanio = 0;
    }

    public void encolar(T dato) { // agrega al final
        Nodo<T> nuevo = new Nodo<>(dato);
        if (estaVacia()) { // si no hay nada, el nuevo es el frente y el fin a la vez
            frente = nuevo;
        } else {
            fin.setSiguiente(nuevo);
        }
        fin = nuevo;
        tamanio++;
    }

    public T desencolar() { // saca el del frente
        if (estaVacia()) {
            throw new IllegalStateException("La cola esta vacia");
        }

        T dato = frente.getDato();
        frente = frente.getSiguiente();
        if (frente == null) { // si se saco el ultimo, el fin tambien queda vacio
            fin = null;
        }
        tamanio--;
        return dato;
    }

    public T verFrente() { // mira el del frente sin sacarlo
        if (estaVacia()) {
            throw new IllegalStateException("La cola esta vacia");
        }
        return frente.getDato();
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public int tamanio() {
        return tamanio;
    }
}
