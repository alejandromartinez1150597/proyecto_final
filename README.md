# Proyecto final — Vuélvete Millonario

Proyecto final de **Estructura de Datos**, Instituto Tecnológico Metropolitano (ITM), semestre 2026-2.
Docente: William Giraldo.

## De qué se trata

Es un juego de preguntas por consola con escalera de premios. Cada concursante responde
12 preguntas de dificultad creciente, cada una con 4 opciones (A, B, C y D). Las preguntas
salen al azar de un banco de más de 700 preguntas guardadas en `banco_preguntas.txt`.

- **Premios:** cada nivel paga más que el anterior, desde $100,000 hasta $100,000,000.
- **Multiplicador:** si aciertas 2 o más preguntas seguidas sin usar comodines, la siguiente paga x2, x3, x4...
- **Fallar:** si respondes mal pierdes todo lo que llevabas, y esa plata se va a **la bolsa**.
- **La bolsa:** junta lo que pierden todos los que fallan. El primero que responda las 12 preguntas
  se la lleva completa, se le suma a su premio y queda en el ranking. Después la bolsa vuelve a $0.
- **Retirarse:** en cualquier pregunta puedes escribir `R` y te llevas lo que llevas ganado.
- **Comodines:** empiezas con uno de cada tipo y puedes tener máximo 2 de cada uno.
  Después de cada pregunta se abre una tienda para comprar más con lo que llevas ganado.

| Comodín            | Qué hace                                         | Precio     |
|--------------------|--------------------------------------------------|------------|
| Ayuda del público  | Muestra qué porcentaje del público votó por cada opción | $500,000   |
| Cambio de pregunta | Cambia la pregunta por otra del mismo nivel      | $750,000   |
| 50/50              | Quita dos opciones incorrectas                   | $1,000,000 |

Pueden jugar varios concursantes, uno detrás de otro. Al final de cada partida se muestra el
top 5 de mejores puntajes. Los puntajes se guardan en `puntajes.txt` y la bolsa en `bolsa.txt`, así que no se
pierden al cerrar el juego.

## Requisitos

- Java **17** o superior (`java -version` para comprobar).
- No usa librerías externas.

## Cómo compilar y jugar

Desde la raíz del repositorio:

```bash
javac -encoding UTF-8 -d bin -sourcepath src src/juego/Main.java
```

```bash
java -cp bin juego.Main
```

El juego tiene que ejecutarse desde la raíz del repositorio, porque ahí está `banco_preguntas.txt`.
En la terminal de Windows las tildes y la ñ se ven bien sin configurar nada.

## Estructura del proyecto

```
src/
├── estructuras/   Nodo, lista enlazada, pila, cola y árbol binario de búsqueda
├── modelo/        Pregunta, jugador, comodines, escalera de premios y puntaje
└── juego/         Main, juego, consola, banco y lector de preguntas, ranking, archivo de puntajes y bolsa
banco_preguntas.txt   Las preguntas, una por línea: nivel;enunciado;A;B;C;D;correcta
```

## Dónde se usa cada estructura

Todas las estructuras las implementamos nosotros, son genéricas y no usan las de `java.util`.

| Estructura     | Clase                              | Para qué se usa en el juego |
|----------------|------------------------------------|-----------------------------|
| Lista enlazada | `estructuras.ListaEnlazada`        | Las 4 opciones de cada pregunta (`Pregunta`) y los comodines del jugador (`Jugador`). El 50/50 recorre la lista de opciones para quitar dos incorrectas. |
| Pila           | `estructuras.Pila`                 | Los niveles que va superando el jugador (`EscaleraPremios`). El de arriba es el último nivel pasado. Si falla, se desapilan todos y queda en 0. |
| Cola           | `estructuras.Cola`                 | Las preguntas de cada nivel (`NivelPreguntas`). Para escoger una al azar se rota la cola un número aleatorio de veces; la que queda al frente es la que sale y vuelve al final. |
| Árbol binario de búsqueda | `estructuras.ArbolBinarioBusqueda` | Dos usos: el banco de preguntas, ordenado por número de nivel (`BancoPreguntas`), y el ranking, ordenado por premio (`Ranking`). El top sale con el recorrido inorden inverso. |

## Manejo de archivos

- `banco_preguntas.txt`: lo lee `LectorPreguntas` al iniciar el juego. Las líneas con errores se ignoran.
- `puntajes.txt`: lo lee y lo escribe `ArchivoPuntajes`. Al terminar cada partida se cargan los puntajes que había y se vuelven a guardar con el nuevo.
  Si no existe, se crea solo. Los puntajes se guardan en **preorden** (nodo, izquierda, derecha): así, al cargarlos,
  el árbol del ranking queda con la misma forma. Si se guardaran de mayor a menor, cada puntaje se iría a la izquierda
  del anterior y el árbol quedaría como una lista.
- `bolsa.txt`: lo lee y lo escribe `Bolsa`. Guarda un solo número, lo que tiene la bolsa. Se actualiza cada vez que
  alguien falla o gana las 12. Si no existe o está dañado, la bolsa empieza en $0.

## Integrantes

| Nombre    | Usuario de GitHub          | Parte principal |
|-----------|----------------------------|-----------------|
| Martínez  | @alejandromartinez1150597  | Juego, consola, escalera de premios, multiplicador y tienda de comodines (P1) |
| Chica     | @Chika002                  | Nodo, lista enlazada y comodines (P2) |
| Rubiano   | @Arubiano662               | Pila, cola, escalera de premios y lectura de preguntas (P3) |
| Rivera    | @JuanAlejo-Rivera          | Árbol, banco de preguntas, ranking y archivo de puntajes (P4) |
