package juego;

import modelo.Puntaje;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

// se encarga de guardar el ranking en un archivo de texto para que no se pierda
// al cerrar el juego. cada linea del archivo es: nombre;premio
public class ArchivoPuntajes {

    private Path ruta;

    public ArchivoPuntajes(String ruta) {
        this.ruta = Path.of(ruta);
    }

    // lee el archivo y mete cada puntaje en el ranking. se llama al abrir el juego
    public void cargar(Ranking ranking) {
        if (!Files.exists(ruta)) { // la primera vez el archivo no existe, se crea vacio
            crearArchivoVacio();
            return;
        }

        try {
            List<String> lineas = Files.readAllLines(ruta, StandardCharsets.UTF_8);
            for (String linea : lineas) {
                Puntaje puntaje = convertirLinea(linea);
                if (puntaje != null) {
                    ranking.agregar(puntaje);
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo de puntajes: " + e.getMessage());
        }
    }

    // escribe todos los puntajes del ranking en el archivo. se llama al terminar cada partida
    public void guardar(Ranking ranking) {
        ArrayList<String> lineas = new ArrayList<>();
        for (Puntaje p : ranking.obtenerTodos()) {
            String nombre = p.getNombre().replace(";", ","); // si el nombre trae ; dañaria el formato
            lineas.add(nombre + ";" + p.getPremio());
        }

        try {
            Files.write(ruta, lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("No se pudo guardar el archivo de puntajes: " + e.getMessage());
        }
    }

    // convierte "Ana;500000" en un Puntaje. si la linea esta mala devuelve null y se salta
    private Puntaje convertirLinea(String linea) {
        if (linea.isBlank()) {
            return null;
        }

        String[] partes = linea.split(";");
        if (partes.length != 2) {
            System.out.println("Linea ignorada en el archivo de puntajes: " + linea);
            return null;
        }

        try {
            long premio = Long.parseLong(partes[1].trim());
            return new Puntaje(partes[0].trim(), premio);
        } catch (NumberFormatException e) { // el premio no era un numero
            System.out.println("Linea ignorada en el archivo de puntajes: " + linea);
            return null;
        }
    }

    private void crearArchivoVacio() {
        try {
            Files.createFile(ruta);
        } catch (IOException e) {
            System.out.println("No se pudo crear el archivo de puntajes: " + e.getMessage());
        }
    }
}
