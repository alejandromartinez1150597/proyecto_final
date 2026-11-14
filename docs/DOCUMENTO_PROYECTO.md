# Documento del proyecto — Trivia de escalera de premios

**Asignatura:** Estructura de Datos — ITM
**Docente:** William Giraldo
**Integrantes:** Martínez, Chica, Rubiano, Rivera
**Repositorio:** https://github.com/alejandromartinez1150597/proyecto_final
**Fecha:** 25 de noviembre de 2026

> Extensión pedida: 4 a 6 páginas. Las secciones siguen el orden de la tabla del enunciado.
> La sección 2 es **la que más pesa**: escríbanla con más detalle que las demás.
> Cada integrante escribe la sección de sus clases.

---

## 1. De qué trata el juego y cómo se juega (media página) — Martínez

**Objetivo:** <qué tiene que lograr el concursante>

**Reglas:**
1. <escalera de 10 niveles y premios>
2. <niveles seguros 5 y 10, retirarse>
3. <comodines: 50/50, ayuda del público, cambiar pregunta>
4. <cómo se gana / cómo se pierde, ranking final>

**Ejemplo de un turno:** <3 o 4 líneas copiadas de la consola>

---

## 2. Dónde está cada estructura (LA PARTE QUE MÁS PESA)

Para cada estructura respondan siempre las mismas preguntas:
**¿En qué clase vive? ¿Qué guarda? ¿Qué operaciones usa el juego? ¿Por qué esta y no otra?**

### 2.1 Lista enlazada — Chica

- **Clase:** `estructuras.ListaEnlazada` (usada en `modelo.Pregunta` y `modelo.Concursante`)
- **Qué guarda:** <las opciones de cada pregunta y los comodines del concursante>
- **Operaciones que usa el juego:** <agregar, obtener por posición, eliminar por posición...>
- **Por qué una lista y no otra:** <se eliminan elementos de cualquier posición: el comodín que se usa
  y las dos opciones que quita el 50/50. Una pila o una cola solo sacan por un extremo.>
- **Complejidad:** <agregar O(1) con referencia al último, obtener y eliminar O(n)>

### 2.2 Pila — Rubiano

- **Clase:** `estructuras.Pila` (usada en `modelo.EscaleraPremios`)
- **Qué guarda:** <los niveles ganados por el concursante>
- **Operaciones:** `push`, `pop`, `peek`, `estaVacia`
- **Por qué una pila:** <siempre importa el último nivel ganado (LIFO). Retirarse = ver el tope;
  fallar = desapilar hasta el último nivel seguro.>

### 2.3 Cola — Rubiano

- **Clase:** `estructuras.Cola` (usada en `juego.Juego` y `juego.BancoPreguntas`)
- **Qué guarda:** <los concursantes en espera y las preguntas de cada nivel>
- **Operaciones:** `encolar`, `desencolar`, `verFrente`, `estaVacia`
- **Por qué una cola:** <el primero que se inscribe es el primero que juega (FIFO); con una pila
  jugaría primero el último en llegar.>

### 2.4 Árbol — Rivera

- **Clase:** `estructuras.ArbolBinarioBusqueda` (usada en `juego.BancoPreguntas` y `juego.Ranking`)
- **Qué guarda:** <las preguntas agrupadas por nivel, y los puntajes del ranking>
- **Operaciones:** <insertar, buscar, recorrido inorden inverso, altura (todas recursivas)>
- **Por qué un árbol:** <buscar un nivel cuesta O(log n); el ranking sale ordenado de mayor a menor
  con el recorrido inorden inverso, sin tener que ordenar cada vez.>
- **¿Qué pasa si queda desbalanceado?** <si los datos llegan ya ordenados, el árbol degenera en una
  lista y buscar pasa de O(log n) a O(n). Lo resolveríamos con un árbol AVL.>
  ← el docente avisó que esta pregunta puede salir en la sustentación.

### 2.5 Manejo de archivos — Rubiano y Rivera

- `juego.LectorPreguntas` lee `preguntas.txt` (formato `nivel;enunciado;A;B;C;D;correcta`).
- `juego.ArchivoPuntajes` lee y guarda `puntajes.txt` (formato `nombre;premio`).

---

## 3. Clases y responsabilidades — Martínez

<!-- Diagrama de clases (imagen en docs/diagrama.png) o, como mínimo, esta tabla. -->

| Clase | Paquete | Responsabilidad (una línea) |
|-------|---------|-----------------------------|
| `Main` | juego | Punto de entrada; crea el `Juego` y lo inicia. |
| `Juego` | juego | Ciclo de la partida y reglas. |
| `Consola` | juego | Mostrar mensajes y leer las opciones del jugador. |
| `Pregunta` | modelo | Enunciado, opciones, respuesta correcta y nivel. |
| `Concursante` | modelo | Nombre, comodines y escalera de premios. |
| `Comodin` (abstracta) | modelo | Base de los tres comodines. |
| `EscaleraPremios` | modelo | Niveles ganados y premio actual. |
| `Puntaje` | modelo | Nombre y premio, comparable para el ranking. |
| `Nodo` | estructuras | Nodo usado por lista, pila y cola. |
| `NodoArbol` | estructuras | Nodo del árbol con hijo izquierdo y derecho. |
| ... | ... | ... |

**Herencia / interfaces usadas:** <`Comodin` es abstracta y de ella heredan `Comodin5050`,
`ComodinPublico` y `ComodinCambio`; `Comparable` se usa para ordenar en el árbol.>

---

## 4. Cómo se repartió el trabajo

| Integrante | Partes del código (clases) | Además |
|------------|----------------------------|--------|
| Martínez | `Main`, `Juego`, `Consola`, `Pregunta`, `Concursante` | Integración, README, diagrama |
| Chica | `Nodo`, `ListaEnlazada`, `Comodin` y sus tres hijos | Sección 2.1 |
| Rubiano | `Pila`, `Cola`, `EscaleraPremios`, `Puntaje`, `LectorPreguntas` | Secciones 2.2 y 2.3 |
| Rivera | `NodoArbol`, `ArbolBinarioBusqueda`, `BancoPreguntas`, `Ranking`, `ArchivoPuntajes` | Sección 2.4, `preguntas.txt` |

Cada Pull Request fue revisado por un integrante distinto al autor, para que todos conocieran
el código de los demás.

---

## 5. Lo que no alcanzamos a hacer y lo que haríamos distinto — todos

> Esta sección **suma** nota: muestra que entendieron el problema.

- **No alcanzamos:** <...>
- **Haríamos distinto:** <...>
