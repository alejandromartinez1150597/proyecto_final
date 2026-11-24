package estructuras;

import java.util.ArrayList;

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

    public T buscar(T dato) { // devuelve el dato guardado en el arbol, o null si no esta
        return buscarRecursivo(raiz, dato);
    }

    private T buscarRecursivo(NodoArbol<T> actual, T dato) {
        if (actual == null) { // se acabo el camino y no se encontro
            return null;
        }

        int comparacion = dato.compareTo(actual.getDato());

        if (comparacion == 0) { // lo encontramos
            return actual.getDato();
        } else if (comparacion < 0) { // es menor, se busca solo por la izquierda
            return buscarRecursivo(actual.getIzquierdo(), dato);
        } else { // es mayor, se busca solo por la derecha
            return buscarRecursivo(actual.getDerecho(), dato);
        }
    }

    // devuelve los datos de mayor a menor, maximo 'limite' datos (para el top del ranking)
    // se usa ArrayList porque la ListaEnlazada del proyecto por ahora solo guarda String
    public ArrayList<T> recorrerInordenInverso(int limite) {
        ArrayList<T> resultado = new ArrayList<>();
        inordenInversoRecursivo(raiz, resultado, limite);
        return resultado;
    }

    private void inordenInversoRecursivo(NodoArbol<T> actual, ArrayList<T> resultado, int limite) {
        if (actual == null || resultado.size() >= limite) { // no hay nodo o ya tenemos los que se pidieron
            return;
        }

        inordenInversoRecursivo(actual.getDerecho(), resultado, limite); // primero los mayores

        if (resultado.size() < limite) {
            resultado.add(actual.getDato()); // despues el nodo actual
        }

        inordenInversoRecursivo(actual.getIzquierdo(), resultado, limite); // al final los menores
    }

    // recorrido preorden: primero el nodo, despues la izquierda y al final la derecha.
    // si se insertan los datos en este mismo orden, el arbol queda con la misma forma
    public ArrayList<T> recorrerPreorden() {
        ArrayList<T> resultado = new ArrayList<>();
        preordenRecursivo(raiz, resultado);
        return resultado;
    }

    private void preordenRecursivo(NodoArbol<T> actual, ArrayList<T> resultado) {
        if (actual == null) {
            return;
        }
        resultado.add(actual.getDato()); // primero el nodo
        preordenRecursivo(actual.getIzquierdo(), resultado); // despues los menores
        preordenRecursivo(actual.getDerecho(), resultado); // y al final los mayores
    }

    // la altura sirve para ver si el arbol quedo desbalanceado
    // balanceado: altura cerca de log2(n). desbalanceado: altura cerca de n
    public int altura() {
        return alturaRecursiva(raiz);
    }

    private int alturaRecursiva(NodoArbol<T> actual) {
        if (actual == null) { // un arbol vacio tiene altura 0
            return 0;
        }

        int alturaIzquierda = alturaRecursiva(actual.getIzquierdo());
        int alturaDerecha = alturaRecursiva(actual.getDerecho());

        return 1 + Math.max(alturaIzquierda, alturaDerecha); // el nodo actual mas el lado mas alto
    }

    public boolean estaVacio() {
        return raiz == null;
    }

    public int tamanio() {
        return tamanio;
    }
}
