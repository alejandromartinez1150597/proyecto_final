package juego;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

// la bolsa junta todo lo que pierden los jugadores que fallan una pregunta.
// el primero que responda las 12 se la lleva completa y vuelve a quedar en 0.
// se guarda en un archivo para que siga acumulando aunque se cierre el juego
public class Bolsa {

    private Path ruta;
    private long monto;

    public Bolsa(String ruta) {
        this.ruta = Path.of(ruta);
        this.monto = 0;
    }

    // lee el monto guardado. si el archivo no existe o esta malo la bolsa arranca en 0
    public void cargar() {
        if (!Files.exists(ruta)) {
            return;
        }
        try {
            List<String> lineas = Files.readAllLines(ruta, StandardCharsets.UTF_8);
            if (!lineas.isEmpty()) {
                monto = Long.parseLong(lineas.get(0).trim());
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("No se pudo leer la bolsa, se empieza en 0: " + e.getMessage());
            monto = 0;
        }
    }

    public void guardar() {
        try {
            Files.writeString(ruta, String.valueOf(monto), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.out.println("No se pudo guardar la bolsa: " + e.getMessage());
        }
    }

    // lo que perdio un jugador entra a la bolsa
    public void agregar(long valor) {
        if (valor > 0) {
            monto += valor;
        }
    }

    // el ganador se lleva todo y la bolsa queda vacia
    public long entregar() {
        long premio = monto;
        monto = 0;
        return premio;
    }

    public long getMonto() {
        return monto;
    }
}
