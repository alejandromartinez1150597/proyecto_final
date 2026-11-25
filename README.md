# Proyecto final — Trivia Millonario

Proyecto final de **Estructura de Datos**, Instituto Tecnológico Metropolitano (ITM), semestre 2026-2.
Docente: William Giraldo.

## De qué se trata

Es un juego de preguntas por consola con escalera de premios. Cada concursante responde
12 preguntas divididas en **4 bloques de dificultad creciente** (3 niveles por bloque), cada una con 4 opciones (`A`, `B`, `C` y `D`). Las preguntas
salen al azar de un banco de **500 preguntas** guardadas en `banco_preguntas.txt`.

- **Bloques y premios:** cada nivel paga más que el anterior y se va sumando al acumulado, desde $100,000 hasta $100,000,000:
  - *Bloque 1 — Fácil (Niveles 1–3):* $100,000, $200,000 y $300,000.
  - *Bloque 2 — Normal (Niveles 4–6):* $500,000, $1,000,000 y $2,000,000.
  - *Bloque 3 — Difícil (Niveles 7–9):* $4,000,000, $8,000,000 y $16,000,000.
  - *Bloque 4 — Muy difícil (Niveles 10–12):* $32,000,000, $64,000,000 y $100,000,000.
- **Multiplicador:** si aciertas 2 o más preguntas seguidas sin usar comodines, la siguiente paga x2, x3, x4... Si usas un comodín, la racha vuelve a 0 y el multiplicador se reinicia a x1.
- **Fallar y niveles seguros:** los niveles **3, 6 y 9** (el final de cada bloque) son niveles seguros. Si respondes mal, pierdes lo ganado en el bloque actual y caes al último nivel seguro alcanzado (o a $0 si no habías llegado al nivel 3).
- **Retirarse:** en cualquier pregunta puedes escribir `R` y te llevas lo que llevas ganado sin caer al nivel seguro.
- **Comodines:** empiezas con uno de cada tipo y puedes tener máximo 2 de cada uno.
  Después de cada pregunta se abre una tienda para comprar más descontando el valor de lo que llevas ganado.

| Comodín            | Qué hace                                                | Precio     |
|--------------------|---------------------------------------------------------|------------|
| Ayuda del público  | Muestra qué porcentaje del público votó por cada opción | $500,000   |
| Cambio de pregunta | Cambia la pregunta por otra distinta del mismo nivel    | $750,000   |
| 50/50              | Quita dos opciones incorrectas                           | $1,000,000 |

Al final de cada partida se muestra el top 5 de mejores puntajes. Los puntajes se guardan en `puntajes.txt`, así que no se
pierden al cerrar el juego.

## Requisitos

- Java **17** o superior (desarrollado y probado en **JDK 26.0.1**, usa `java -version` para comprobar).
- No usa librerías externas.

## Cómo compilar y jugar

### Para jugar directamente sin tocar código (carpeta `TriviaMillonario`)

Para que cualquier persona pueda jugar sin abrir un IDE ni modificar el código fuente, se generó la carpeta lista para ejecutar **`TriviaMillonario`**, que contiene:
- `proyecto_final.jar` (el juego ya compilado).
- `banco_preguntas.txt` (el banco con las 500 preguntas).
- `jugar.bat` (ejecutable de Windows que configura la consola en UTF-8 y lanza el `.jar`).


### Desde el código fuente (repositorio)

Desde la raíz del repositorio (donde están `src/` y `banco_preguntas.txt`):

```bash
javac -encoding UTF-8 -d bin -sourcepath src src/juego/Main.java
```

```bash
java -cp bin juego.Main
```

El juego tiene que ejecutarse desde la carpeta donde está `banco_preguntas.txt`, porque ahí lee las preguntas y crea `puntajes.txt`.
En la terminal de Windows las tildes y la ñ se ven bien sin configurar nada.

## Estructura del proyecto

```text
src/
├── estructuras/   Nodo, NodoArbol, ListaEnlazada, Pila, Cola y ArbolBinarioBusqueda
├── modelo/        Pregunta, Jugador, Comodin (y sus 3 subclases), EscaleraPremios y Puntaje
└── juego/         Main, Juego, Consola, BancoPreguntas, NivelPreguntas, LectorPreguntas, Ranking y ArchivoPuntajes
banco_preguntas.txt   Las 500 preguntas, una por línea: nivel;enunciado;A;B;C;D;correcta
puntajes.txt          Historial de puntajes guardado automáticamente al terminar cada partida
```

## Dónde se usa cada estructura y por qué

Todas las estructuras las implementamos nosotros, son genéricas y no usan las de `java.util`.

| Estructura | En qué clase vive | Qué guarda | Por qué esta estructura y no otra |
| :--- | :--- | :--- | :--- |
| **Lista enlazada** (`estructuras.ListaEnlazada`) | `modelo.Pregunta` (`opciones`) y `modelo.Jugador` (`comodines`) | Las 4 opciones de texto de cada pregunta y los objetos `Comodin` del inventario del jugador. | Permite eliminar elementos en cualquier posición intermedia sin restricciones de extremo: el `50/50` elimina dos opciones incorrectas por índice (`eliminar(pos)`), y al gastar un comodín específico (`usarYConsumirComodin`) se busca y se borra de la lista para liberar cupo hasta el máximo de 2 por tipo. Un arreglo fijo obligaría a mover elementos a mano o dejar huecos `null`. |
| **Pila** (`estructuras.Pila`) | `modelo.EscaleraPremios` (`nivelesSuperados`) | Los números enteros (`Integer`) de cada nivel superado, dejando en la cima el último nivel alcanzado. | El retroceso al fallar sigue exactamente el orden **LIFO** (último en entrar, primero en salir). Al avanzar se apila el nivel (`apilar`) y, si el jugador falla, `caerNivelSeguro()` va desapilando (`desapilar`) desde la cima hacia atrás hasta llegar a un nivel seguro (múltiplo de 3) o dejar la pila vacía. Una cola obligaría a recorrer desde el nivel 1. |
| **Cola** (`estructuras.Cola`) | `juego.NivelPreguntas` (`preguntas`), gestionada por `juego.BancoPreguntas` | Las instancias de `Pregunta` correspondientes a cada nivel del 1 al 12. | Permite una rotación cíclica **FIFO** sin perder preguntas: para elegir una al azar en `siguientePregunta`, la cola se rota un número aleatorio de veces (`encolar(desencolar())`), se saca la del frente y se vuelve a encolar al final. Esto también permite que `Cambio de pregunta` entregue otra distinta sin descartar preguntas del banco. |
| **Árbol binario de búsqueda** (`estructuras.ArbolBinarioBusqueda`) | `juego.BancoPreguntas` (`niveles`) y `juego.Ranking` (`arbol`) | En `BancoPreguntas` guarda objetos `NivelPreguntas` ordenados por número de nivel; en `Ranking` guarda objetos `Puntaje` ordenados por premio. | Permite búsqueda rápida y ordenamiento automático sin algoritmos externos. En `BancoPreguntas`, `buscar()` localiza la cola de un nivel por clave; en `Ranking`, cada puntaje entra ordenado y el Top 5 sale directo con un recorrido **inorden inverso** (`derecha -> raíz -> izquierda`) limitado a 5 nodos. |

## Diagrama de clases y responsabilidades

### Diagrama de relaciones

```text
Main ──> LectorPreguntas ──> BancoPreguntas
  │                                │
  ▼                                ▼
Juego ──> Consola          ArbolBinarioBusqueda<NivelPreguntas>
  │                                │
  ▼                                ▼
Jugador                      Cola<Pregunta>
  ├──> ListaEnlazada<Comodin> <── Comodin5050 / ComodinPublico / ComodinCambioPregunta
  └──> EscaleraPremios ──> Pila<Integer>

Juego ──> ArchivoPuntajes ──> Ranking ──> ArbolBinarioBusqueda<Puntaje>
```

### Responsabilidad de cada clase (en una línea)

- **Paquete `estructuras`**
  - `Nodo<T>`: Representa un nodo simple con un dato genérico y la referencia al siguiente nodo.
  - `NodoArbol<T>`: Representa un nodo de árbol binario con un dato genérico y referencias a hijo izquierdo y derecho.
  - `ListaEnlazada<T>`: Implementa una lista simplemente enlazada genérica con inserción al final, lectura y borrado por índice.
  - `Pila<T>`: Implementa una pila genérica LIFO (`apilar`, `desapilar`, `verCima`) con nodos enlazados.
  - `Cola<T>`: Implementa una cola genérica FIFO (`encolar`, `desencolar`, `verFrente`) con punteros al frente y al fin.
  - `ArbolBinarioBusqueda<T>`: Implementa un ABB genérico para tipos `Comparable` con inserción, búsqueda, altura y recorrido inorden inverso.
- **Paquete `modelo`**
  - `Pregunta`: Guarda el enunciado, nivel, bloque de dificultad, respuesta correcta y lista enlazada de opciones.
  - `Jugador`: Representa al concursante con su nombre, inventario de comodines (máx. 2 por tipo) y escalera de premios.
  - `EscaleraPremios`: Controla el nivel actual, el acumulado, el multiplicador por racha, las compras en tienda y la caída a niveles seguros.
  - `Comodin`: Clase abstracta que define nombre, precio, estado de uso y el método `usar` de los comodines.
  - `Comodin5050`: Elimina al azar dos opciones incorrectas de la lista de opciones de la pregunta.
  - `ComodinPublico`: Simula porcentajes de votación del público favoreciendo la respuesta correcta.
  - `ComodinCambioPregunta`: Pide al banco una pregunta distinta del mismo nivel para reemplazar la actual.
  - `Puntaje`: Guarda el nombre y premio final de una partida e implementa `Comparable` para ordenarse en el ranking.
- **Paquete `juego`**
  - `Main`: Punto de entrada que carga `banco_preguntas.txt`, pide el nombre del jugador e inicia la partida.
  - `Juego`: Controla el ciclo de los 12 niveles, el uso de comodines en ronda, la tienda post-nivel y el cierre con ranking.
  - `Consola`: Encapsula el `Scanner` para leer opciones validadas del teclado e imprimir mensajes en pantalla.
  - `BancoPreguntas`: Organiza las preguntas por nivel en un ABB y las rota aleatoriamente usando colas.
  - `NivelPreguntas`: Agrupa un número de nivel con su cola de preguntas como nodo comparable del banco.
  - `LectorPreguntas`: Lee y valida `banco_preguntas.txt`, mezclando las líneas antes de cargarlas al banco.
  - `Ranking`: Administra los puntajes en un ABB y genera el texto con los mejores resultados.
  - `ArchivoPuntajes`: Lee y escribe los puntajes de forma persistente en el archivo `puntajes.txt`.

## Manejo de archivos

- `banco_preguntas.txt`: lo lee `LectorPreguntas` al iniciar el juego, mezcla las líneas con `Collections.shuffle` y las mete en `BancoPreguntas`. Las líneas con errores o fuera de los niveles 1–12 se ignoran.
- `puntajes.txt`: lo lee y lo escribe `ArchivoPuntajes`. Al terminar cada partida se cargan los puntajes que había en el archivo, se agrega el nuevo al `Ranking` y se vuelven a guardar en formato `nombre;premio`. Si el archivo no existe, se crea solo.

## Integrantes y reparto del trabajo

| Nombre    | Usuario de GitHub          | Rol | Clases y partes del código desarrolladas |
|-----------|----------------------------|-----|------------------------------------------|
| Martínez  | @alejandromartinez1150597  | P1  | `Juego` (ciclo de 12 niveles, uso de los 3 comodines en ronda y tienda post-nivel), `Jugador` (inventario y compras), `Pregunta` (bloques y categorías), `Consola` y `Main`. |
| Chica     | @Chika002                  | P2  | `Nodo`, `ListaEnlazada`, clase abstracta `Comodin` (con precios) y las tres subclases: `Comodin5050`, `ComodinPublico` y `ComodinCambioPregunta`. |
| Rubiano   | @Arubiano662               | P3  | `Pila`, `Cola`, `EscaleraPremios` (tabla de premios, multiplicador por racha, descuento de compras y niveles seguros 3, 6 y 9) y `LectorPreguntas`. |
| Rivera    | @JuanAlejo-Rivera          | P4  | `NodoArbol`, `ArbolBinarioBusqueda`, `BancoPreguntas` (rotación aleatoria y consultas por bloque), `NivelPreguntas`, `Ranking`, `Puntaje` y `ArchivoPuntajes`. |

## Lo que no alcanzamos a hacer y lo que haríamos distinto

- **Lo que no alcanzamos a hacer:**
  - **Pozo acumulado ("la bolsa"):** al principio pensamos guardar en un archivo (`bolsa.txt`) el dinero que perdían los jugadores que fallaban para dárselo a quien completara los 12 niveles, pero al implementar los niveles seguros (`3`, `6` y `9`) y la compra de comodines con saldo propio, esa idea quedó por fuera.
  - **Sincronizar `50/50` con `Ayuda del público`:** si en una misma pregunta el jugador usa primero `50/50` (quedando solo 2 opciones en la lista) y luego usa `Ayuda del público`, este último sigue repartiendo porcentajes entre las 4 letras (`A, B, C, D`) en vez de repartir el 100% solo entre las 2 que quedan.
  - **Turnos de varios jugadores en una misma ejecución:** dejamos la `Cola` lista para manejar turnos, pero `Main` ejecuta una partida por corrida y hay que volver a abrirlo para el siguiente jugador.
- **Lo que haríamos distinto:**
  - **Evitar el desbalanceo del árbol al cargar `puntajes.txt`:** `ArchivoPuntajes` guarda los puntajes usando `obtenerTodos()`, que recorre en inorden inverso (de mayor a menor). Al volver a abrir el juego y reinsertarlos en ese orden, cada puntaje se va siempre a la izquierda del anterior y el ABB queda degenerado como una lista. Lo cambiaríamos para guardarlos en **preorden** (raíz, izquierda, derecha) y reconstruir la forma original del árbol.
  - **Separar la consola de la lógica del juego:** `Juego` imprime mensajes directamente con `Consola` mientras cambia el estado de `Jugador` y `EscaleraPremios`; separar ambas capas facilitaría pasar el juego a interfaz gráfica sin tocar las reglas.
  - **Evitar repetidos en el ranking:** si una misma persona juega varias veces, hoy queda registrada varias veces en `puntajes.txt`; lo ideal sería actualizar su nodo solo si supera su mejor puntaje.
