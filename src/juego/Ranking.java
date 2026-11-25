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

    // todos los puntajes en preorden, se usa para guardarlos en el archivo.
    // si se guardaran de mayor a menor, al cargarlos cada uno se iria a la izquierda del
    // anterior y el arbol quedaria como una lista. en preorden se vuelve a armar igual
    public ArrayList<Puntaje> obtenerTodos() {
        return arbol.recorrerPreorden();
    }

    public int altura() {
        return arbol.altura();
    }

    public String mostrarTop(int cantidad) {
        if (arbol.estaVacio()) {
            return "Todavia no hay puntajes";
        }

        ArrayList<Puntaje> top = obtenerTop(cantidad);
        // cada fila queda alineada: puesto, nombre y premio con separador de miles
        String texto = " MEJORES PUNTAJES\n";
        for (int i = 0; i < top.size(); i++) {
            Puntaje p = top.get(i);
            texto += String.format("  %d. %-20s %15s%n", i + 1, p.getNombre(), Consola.dinero(p.getPremio()));
        }
        return texto;
    }

    public int cantidad() {
        return arbol.tamanio();
    }
}
