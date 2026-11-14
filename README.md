# Proyecto final — Trivia de escalera de premios

Proyecto final de **Estructura de Datos**, Instituto Tecnológico Metropolitano (ITM), semestre 2026-2.
Docente: William Giraldo.

## De qué se trata

La idea es un juego de preguntas por consola con escalera de premios. El concursante va a
responder 10 preguntas de dificultad creciente con 4 opciones cada una. Tendrá 3 comodines
(50/50, ayuda del público y cambiar pregunta), podrá retirarse con lo que lleve ganado y, si falla,
se queda con el premio del último nivel seguro. Varios concursantes juegan por turnos y al final
se muestra el ranking, que se guardará en el archivo `puntajes.txt` para no perderlo al cerrar el juego.

## Estado del proyecto

En desarrollo. Por ahora el programa solo muestra una pregunta de prueba por consola.

| Parte | Estado |
|---|---|
| Consola y clase Pregunta | Hecho |
| Lista enlazada | En desarrollo |
| Pila y cola | Pendiente |
| Árbol (banco de preguntas y ranking) | En desarrollo 
| Comodines y escalera de premios | Pendiente |
| Archivos de preguntas y puntajes | Pendiente |
| Juego completo | Pendiente |

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
```

## Dónde va cada estructura (lo planeado)

| Estructura      | Clase                              | Qué guarda en el juego |
|-----------------|------------------------------------|------------------------|
| Lista enlazada  | `estructuras.ListaEnlazada`        | Comodines disponibles y opciones de cada pregunta |
| Pila            | `estructuras.Pila`                 | Escalera de premios ganados (retirarse y caer al nivel seguro) |
| Cola            | `estructuras.Cola`                 | Concursantes en espera y preguntas de cada nivel |
| Árbol           | `estructuras.ArbolBinarioBusqueda` | Banco de preguntas por nivel y ranking de premios |

## Integrantes

| Nombre    | Usuario de GitHub          | Parte principal |
|-----------|----------------------------|-----------------|
| Martínez  | @alejandromartinez1150597  | Juego, consola, preguntas e integración (P1) |
| Chica     | @                          | Nodo, lista enlazada y comodines (P2) |
| Rubiano   | @                          | Pila, cola, escalera de premios y lectura de preguntas (P3) |
| Rivera    | @JuanAlejo-Rivera          | Árbol, banco de preguntas, ranking y archivo de puntajes (P4) |
