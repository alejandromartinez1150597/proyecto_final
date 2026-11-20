package juego;

import estructuras.ArbolBinarioBusqueda;
import modelo.Puntaje;
import java.util.ArrayList;

// guarda los puntajes de todas las partidas en el arbol binario de busqueda.
// como el arbol los deja ordenados, el top sale con el recorrido inorden inverso
public class Ranking {

    private ArbolBinarioBusqueda<Puntaje> arbol;

    public Ranking() {
        this.arbol = new ArbolBinarioBusqueda<>();
    }

    public void agregar(Puntaje puntaje) {
        arbol.insertar(puntaje);
    }

    public ArrayList<Puntaje> obtenerTop(int cantidad) { // los mejores, de mayor a menor
        return arbol.recorrerInordenInverso(cantidad);
    }

    public ArrayList<Puntaje> obtenerTodos() { // todos los puntajes, se usa para guardarlos en el archivo
        return arbol.recorrerInordenInverso(arbol.tamanio());
    }

    public String mostrarTop(int cantidad) {
        if (arbol.estaVacio()) {
            return "Todavia no hay puntajes";
        }

        ArrayList<Puntaje> top = obtenerTop(cantidad);
        String texto = "=== MEJORES PUNTAJES ===\n";
        for (int i = 0; i < top.size(); i++) {
            texto += (i + 1) + ". " + top.get(i) + "\n";
        }
        return texto;
    }

    public int cantidad() {
        return arbol.tamanio();
    }
}
