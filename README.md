# Proyecto final — Trivia de escalera de premios

Proyecto final de **Estructura de Datos** — Instituto Tecnológico Metropolitano (ITM), semestre 2026-2.
Docente: William Giraldo.

## De qué se trata

Juego de preguntas por consola con escalera de premios. El concursante responde 10 preguntas
de dificultad creciente con 4 opciones cada una. Tiene 3 comodines (50/50, ayuda del público y
cambiar pregunta), puede retirarse con lo que ha ganado y, si falla, se queda con el premio del
último nivel seguro. Varios concursantes juegan por turnos y al final se muestra el ranking,
que se guarda en el archivo `puntajes.txt` para no perderlo al cerrar el juego.

## Requisitos

- Java **17** o superior (`java -version` para comprobar).
- No usa librerías externas.

## Cómo compilar y ejecutar

Desde la raíz del repositorio:

```bash
javac -d bin -sourcepath src src/juego/Main.java
```

```bash
java -cp bin juego.Main
```

## Estructura del proyecto

```
src/
├── estructuras/   Lista enlazada, pila, cola y árbol (con sus nodos)
├── modelo/        Clases del juego (pregunta, concursante, comodines...)
└── juego/         Lógica del juego, consola, archivos y Main
docs/
└── DOCUMENTO_PROYECTO.md   Documento del proyecto (4 a 6 páginas)
```

## Dónde está cada estructura (resumen)

| Estructura      | Clase                              | Qué guarda en el juego |
|-----------------|------------------------------------|------------------------|
| Lista enlazada  | `estructuras.ListaEnlazada`        | Comodines disponibles y opciones de cada pregunta |
| Pila            | `estructuras.Pila`                 | Escalera de premios ganados (retirarse y caer al nivel seguro) |
| Cola            | `estructuras.Cola`                 | Concursantes en espera y preguntas de cada nivel |
| Árbol           | `estructuras.ArbolBinarioBusqueda` | Banco de preguntas por nivel y ranking de premios |

La explicación completa (por qué cada una y no otra) está en [docs/DOCUMENTO_PROYECTO.md](docs/DOCUMENTO_PROYECTO.md).

## Integrantes

| Nombre    | Usuario de GitHub          | Parte principal |
|-----------|----------------------------|-----------------|
| Martínez  | @alejandromartinez1150597  | Juego, consola, preguntas e integración (P1) |
| Chica     | @                          | Nodo, lista enlazada y comodines (P2) |
| Rubiano   | @                          | Pila, cola, escalera de premios y lectura de preguntas (P3) |
| Rivera    | @JuanAlejo-Rivera          | Árbol, banco de preguntas, ranking y archivo de puntajes (P4) |
