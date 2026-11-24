package juego;

import modelo.Pregunta;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Random;

// lee el archivo preguntas.txt y mete cada pregunta en el banco.
// cada linea del archivo es: nivel;enunciado;opcionA;opcionB;opcionC;opcionD;correcta
public class LectorPreguntas {

    private Path ruta;

    public LectorPreguntas(String ruta) {
        this.ruta = Path.of(ruta);
    }

    // devuelve cuantas preguntas se cargaron, asi el juego sabe si quedo vacio
    public int cargar(BancoPreguntas banco) {
        if (!Files.exists(ruta)) {
            System.out.println("No se encontro el archivo de preguntas: " + ruta);
            return 0;
        }

        int cargadas = 0;
        try {
            List<String> lineas = Files.readAllLines(ruta, StandardCharsets.UTF_8);
            Collections.shuffle(lineas, new Random()); // mezcla aleatoriamente todas las preguntas al cargar
            for (String linea : lineas) {
                Pregunta pregunta = convertirLinea(linea);
                if (pregunta != null) {
                    banco.agregarPregunta(pregunta);
                    cargadas++;
                }
            }
        } catch (IOException e) {
            System.out.println("No se pudo leer el archivo de preguntas: " + e.getMessage());
        }
        return cargadas;
    }

    // convierte una linea del archivo en una Pregunta. si algo esta mal devuelve null y se salta
    private Pregunta convertirLinea(String linea) {
        linea = linea.replace("\uFEFF", ""); // algunos editores meten este caracter invisible al inicio
        if (linea.isBlank()) {
            return null;
        }

        String[] partes = linea.split(";");
        if (partes.length != 7) {
            System.out.println("Linea ignorada en preguntas, no tiene 7 partes: " + linea);
            return null;
        }

        int nivel;
        try {
            nivel = Integer.parseInt(partes[0].trim());
        } catch (NumberFormatException e) {
            System.out.println("Linea ignorada en preguntas, el nivel no es un numero: " + linea);
            return null;
        }

        String correcta = partes[6].trim().toUpperCase();
        if (nivel < 1 || nivel > 12 || !correcta.matches("[A-D]")) {
            System.out.println("Linea ignorada en preguntas, nivel o respuesta invalida: " + linea);
            return null;
        }

        Pregunta pregunta = new Pregunta(partes[1].trim(), nivel, correcta);
        String letras = "ABCD";
        for (int i = 2; i <= 5; i++) { // las 4 opciones quedan en orden y con su letra: "A) texto"
            pregunta.agregarOpcion(letras.charAt(i - 2) + ") " + partes[i].trim());
        }
        return pregunta;
    }
}
