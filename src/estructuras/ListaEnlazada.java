package estructuras;


public class ListaEnlazada<T> {
    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamano;

    public ListaEnlazada() {
        this.cabeza = null;
        this.cola = null;
        this.tamano = 0;
    }

    public void agregar(T dato) {
        Nodo<T> nuevoNodo = new Nodo<>(dato);
        if (cabeza == null) {
            cabeza = nuevoNodo;
            cola = nuevoNodo;
        } else {
            cola.setSiguiente(nuevoNodo);
            cola = nuevoNodo;
        }
        tamano++;
    }

    public boolean eliminar(int posicion){
        if(estaVacia() || posicion < 0 || posicion >= tamano){
            return false;
        }
        if(posicion == 0){
            cabeza = cabeza.getSiguiente();
            if(cabeza == null){
                cola = null;
            }
            tamano--;
            return true;
        }
        Nodo<T> actual = cabeza;
        for(int i = 0; i < posicion - 1; i++){ 
            actual = actual.getSiguiente();
        }
        if(actual.getSiguiente() == cola){
            cola = actual; 
        }
        actual.setSiguiente(actual.getSiguiente().getSiguiente());
        tamano--;
        return true;    
    }

    public void mostrar(){
        if(estaVacia()){
            System.out.println("La lista esta vacia");
            return;
        }
        Nodo<T> actual = cabeza;
        while(actual != null){
            System.out.println(actual.getDato());
            actual = actual.getSiguiente(); 
        }
        System.out.println("Fin de la lista");
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public int tamano() {
        return tamano;
    }

    public T obtener(int posicion){
        if(estaVacia() || posicion < 0 || posicion >= tamano){
            return null;
        }
        Nodo<T> actual = cabeza;
        for(int i = 0; i < posicion; i++){
            actual = actual.getSiguiente();
        }
        return actual.getDato();
    }

    public int getTamano() {
        return tamano;
    }
}
