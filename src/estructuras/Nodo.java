package estructuras;

//clase Nodo para listas enlazadas. En listas genericas.
public class Nodo <T>{
    T dato; // el dato que contiene el nodo
    Nodo<T> siguiente; // referencia al siguiente nodo
    Nodo<T> anterior; // referencia al nodo anterior, para listas doblemente enlazadas

    //Constructor
    public Nodo(T dato){
        this.dato = dato; // se inicializa el dato del nodo 
        this.siguiente = null; // cuando se crea no tiene siguiente
        this.anterior = null; // cuando se crea no tiene anterior
    }    
}
