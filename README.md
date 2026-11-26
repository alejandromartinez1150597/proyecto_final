# Proyecto final — Trivia Millonario

Proyecto final de **Estructura de Datos**, Instituto Tecnológico Metropolitano (ITM), semestre 2026-2.
Docente: William Giraldo.

## De qué se trata

Es un juego de preguntas por consola con escalera de premios. Cada concursante responde
12 preguntas divididas en **4 bloques de dificultad creciente** (3 niveles por bloque), cada una con 4 opciones (`A`, `B`, `C` y `D`). Las preguntas
salen al azar de un banco de **695 preguntas** guardadas en `banco_preguntas.txt`.

- **Bloques y premios:** cada nivel paga más que el anterior y se va sumando al acumulado, desde $100,000 hasta $100,000,000:
  - *Bloque 1 — Fácil (Niveles 1–3):* $100,000, $200,000 y $300,000.
  - *Bloque 2 — Normal (Niveles 4–6):* $500,000, $1,000,000 y $2,000,000.
  - *Bloque 3 — Difícil (Niveles 7–9):* $4,000,000, $8,000,000 y $16,000,000.
  - *Bloque 4 — Muy difícil (Niveles 10–12):* $32,000,000, $64,000,000 y $100,000,000.
- **Multiplicador:** si aciertas 2 o más preguntas seguidas sin usar comodines, la siguiente paga x2, x3, x4... Si usas un comodín, la racha vuelve a 0 y el multiplicador se reinicia a x1.
- **Fallar:** si respondes mal pierdes **todo** lo que llevabas, y esa plata se va a **la bolsa**.
- **La bolsa:** junta lo que pierden todos los que fallan. El primero que responda las 12 preguntas se la lleva
  completa, se le suma a su premio y queda en el ranking. Después la bolsa vuelve a $0. Se guarda en `bolsa.txt`,
  así que sigue acumulando aunque se cierre el juego.
- **Pregunta de riesgo (evento aleatorio):** desde la pregunta 2 y hasta la 10, a veces (16% de probabilidad) el juego
  ofrece cambiar la pregunta por una de dos niveles más arriba. En esa pregunta **no se pueden usar comodines**, pero si
  aciertas ganas un bono extra igual al valor base del nivel.
- **Retirarse:** en cualquier pregunta puedes escribir `R` y te llevas lo que llevas ganado.
- **Comodines:** empiezas con uno de cada tipo y solo puedes tener uno de cada uno. Para responder escribes la letra
  directamente; si quieres usar un comodín escribes `0` y se abre el menú de comodines.
- **Tienda:** se abre **una sola vez**, después de responder bien la pregunta 5. Ahí puedes recomprar los comodines que
  ya gastaste, descontando el precio de lo que llevas ganado.

| Comodín            | Qué hace                                                | Precio     |
|--------------------|---------------------------------------------------------|------------|
| Ayuda del público  | Muestra qué porcentaje del público votó por cada opción | $2,000,000 |
| Cambio de pregunta | Cambia la pregunta por otra distinta del mismo nivel    | $3,000,000 |
| 50/50              | Quita dos opciones incorrectas                           | $4,000,000 |

Pueden jugar varios concursantes seguidos: al terminar una partida el juego pregunta si juega otro.
Al final de cada partida se muestra el top 5 de mejores puntajes. Los puntajes se guardan en `puntajes.txt`, así que no se
pierden al cerrar el juego.

## Requisitos

- Java **17** o superior (usa `java -version` para comprobar). Si no lo tienes, se descarga gratis en
  [adoptium.net](https://adoptium.net) (instalar el **JDK**, que trae `java` y `javac`).
- No usa librerías externas.

## Cómo jugar

### Opción 1: descargar y jugar de una (Windows)

1. Descarga el juego: **[TriviaMillonario.zip](https://github.com/alejandromartinez1150597/proyecto_final/raw/main/descargas/TriviaMillonario.zip)**
2. Descomprime el zip.
3. Entra a la carpeta `TriviaMillonario` y dale **doble clic a `jugar.bat`**.

El zip trae el juego ya compilado (`TriviaMillonario.jar`), el banco de preguntas y el `jugar.bat`, que abre el juego
desde esa carpeta para que encuentre las preguntas. Solo necesita tener Java instalado. En otro sistema operativo, desde
esa carpeta se juega con `java -jar TriviaMillonario.jar`.

### Opción 2: compilar desde el código fuente

1. Descarga el repositorio: botón verde **Code → Download ZIP** (y descomprímelo) o
   `git clone https://github.com/alejandromartinez1150597/proyecto_final.git`
2. Abre una terminal (PowerShell o cualquier otra) **dentro de la carpeta del proyecto**.
3. Compila y juega:

```bash
javac -encoding UTF-8 -d bin -sourcepath src src/juego/Main.java
```

```bash
java -cp bin juego.Main
```

En la terminal de Windows las tildes y la ñ se ven bien sin configurar nada, porque `Consola` pasa la consola a UTF-8.

NOTA: El juego tiene que ejecutarse desde la carpeta donde está `banco_preguntas.txt`, porque ahí lee las preguntas y crea
`puntajes.txt` y `bolsa.txt`.


## Estructura del proyecto

```text
src/
├── estructuras/   Nodo, NodoArbol, ListaEnlazada, Pila, Cola y ArbolBinarioBusqueda
├── modelo/        Pregunta, Jugador, Comodin (y sus 3 subclases), EscaleraPremios y Puntaje
└── juego/         Main, Juego, Consola, BancoPreguntas, NivelPreguntas, LectorPreguntas, Ranking, ArchivoPuntajes,
                   Bolsa y EventoPeligroso
banco_preguntas.txt   Las 695 preguntas, una por línea: nivel;enunciado;A;B;C;D;correcta
puntajes.txt          Historial de puntajes guardado automáticamente al terminar cada partida
bolsa.txt             Lo que tiene la bolsa (se crea al jugar)
```

## Dónde se usa cada estructura y por qué

Todas las estructuras las implementamos nosotros, son genéricas y no usan las de `java.util`.

| Estructura | En qué clase vive | Qué guarda | Por qué esta estructura y no otra |
| :--- | :--- | :--- | :--- |
| **Lista enlazada** (`estructuras.ListaEnlazada`) | `modelo.Pregunta` (`opciones`) y `modelo.Jugador` (`comodines`) | Las 4 opciones de texto de cada pregunta y los objetos `Comodin` del inventario del jugador. | Permite eliminar elementos en cualquier posición intermedia sin restricciones de extremo: el `50/50` elimina dos opciones incorrectas por índice (`eliminar(pos)`), y al gastar un comodín específico (`usarYConsumirComodin`) se busca y se borra de la lista para liberar el cupo (solo se puede tener uno de cada tipo). Un arreglo fijo obligaría a mover elementos a mano o dejar huecos `null`. |
| **Pila** (`estructuras.Pila`) | `modelo.EscaleraPremios` (`nivelesSuperados`) | Los números enteros (`Integer`) de cada nivel superado, dejando en la cima el último nivel alcanzado. | Los niveles se ganan uno encima del otro y la cima siempre es el último nivel alcanzado, que es el orden **LIFO** (último en entrar, primero en salir). Al avanzar se apila el nivel (`apilar`) y, si el jugador falla, `perderTodo()` desapila (`desapilar`) desde la cima hasta dejar la pila vacía: el jugador vuelve al nivel 0 y lo que tenía se va a la bolsa. |
| **Cola** (`estructuras.Cola`) | `juego.NivelPreguntas` (`preguntas`), gestionada por `juego.BancoPreguntas` | Las instancias de `Pregunta` correspondientes a cada nivel del 1 al 12. | Permite una rotación cíclica **FIFO** sin perder preguntas: para elegir una al azar en `siguientePregunta`, la cola se rota un número aleatorio de veces (`encolar(desencolar())`), se saca la del frente y se vuelve a encolar al final. Esto también permite que `Cambio de pregunta` entregue otra distinta sin descartar preguntas del banco. |
| **Árbol binario de búsqueda** (`estructuras.ArbolBinarioBusqueda`) | `juego.BancoPreguntas` (`niveles`) y `juego.Ranking` (`arbol`) | En `BancoPreguntas` guarda objetos `NivelPreguntas` ordenados por número de nivel; en `Ranking` guarda objetos `Puntaje` ordenados por premio. | Permite búsqueda rápida y ordenamiento automático sin algoritmos externos. En `BancoPreguntas`, `buscar()` localiza la cola de un nivel por clave; en `Ranking`, cada puntaje entra ordenado y el Top 5 sale directo con un recorrido **inorden inverso** (`derecha -> raíz -> izquierda`) limitado a 5 nodos. Para guardar el ranking en el archivo se usa el recorrido **preorden** (`raíz -> izquierda -> derecha`), así al cargarlo el árbol queda con la misma forma y no se vuelve una lista. |

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
Juego ──> Bolsa (bolsa.txt)
Juego ──> EventoPeligroso
```

### Responsabilidad de cada clase

- **Paquete `estructuras`**
  - `Nodo<T>`: Representa un nodo simple con un dato genérico y la referencia al siguiente nodo.
  - `NodoArbol<T>`: Representa un nodo de árbol binario con un dato genérico y referencias a hijo izquierdo y derecho.
  - `ListaEnlazada<T>`: Implementa una lista simplemente enlazada genérica con inserción al final, lectura y borrado por índice.
  - `Pila<T>`: Implementa una pila genérica LIFO (`apilar`, `desapilar`, `verCima`) con nodos enlazados.
  - `Cola<T>`: Implementa una cola genérica FIFO (`encolar`, `desencolar`, `verFrente`) con punteros al frente y al fin.
  - `ArbolBinarioBusqueda<T>`: Implementa un ABB genérico para tipos `Comparable` con inserción, búsqueda, altura y recorridos inorden inverso y preorden.
- **Paquete `modelo`**
  - `Pregunta`: Guarda el enunciado, nivel, bloque de dificultad, respuesta correcta y lista enlazada de opciones.
  - `Jugador`: Representa al concursante con su nombre, inventario de comodines (máx. 1 por tipo) y escalera de premios.
  - `EscaleraPremios`: Controla el nivel actual, el acumulado, el multiplicador por racha, las compras en tienda y la pérdida de todo al fallar.
  - `Comodin`: Clase abstracta que define nombre, precio, estado de uso y el método `usar` de los comodines.
  - `Comodin5050`: Elimina al azar dos opciones incorrectas de la lista de opciones de la pregunta.
  - `ComodinPublico`: Simula porcentajes de votación del público favoreciendo la respuesta correcta.
  - `ComodinCambioPregunta`: Pide al banco una pregunta distinta del mismo nivel para reemplazar la actual.
  - `Puntaje`: Guarda el nombre y premio final de una partida e implementa `Comparable` para ordenarse en el ranking.
- **Paquete `juego`**
  - `Main`: Punto de entrada que carga `banco_preguntas.txt` una sola vez, pide el nombre (obligatorio) de cada jugador y repite partidas mientras quieran jugar.
  - `Juego`: Controla el ciclo de los 12 niveles, el uso de comodines en ronda, la pregunta de riesgo, la tienda después de la pregunta 5, la bolsa y el cierre con ranking.
  - `Consola`: Encapsula el `Scanner` para leer opciones validadas del teclado e imprimir mensajes en pantalla.
  - `BancoPreguntas`: Organiza las preguntas por nivel en un ABB y las rota aleatoriamente usando colas.
  - `NivelPreguntas`: Agrupa un número de nivel con su cola de preguntas como nodo comparable del banco.
  - `LectorPreguntas`: Lee y valida `banco_preguntas.txt`, mezclando las líneas antes de cargarlas al banco.
  - `Ranking`: Administra los puntajes en un ABB y genera el texto con los mejores resultados.
  - `ArchivoPuntajes`: Lee y escribe los puntajes de forma persistente en el archivo `puntajes.txt`.
  - `Bolsa`: Guarda lo que pierden los jugadores, se lo entrega al que responde las 12 y lo persiste en `bolsa.txt`.
  - `EventoPeligroso`: Decide al azar si sale la pregunta de riesgo y le ofrece al jugador aceptarla o seguir normal.

## Manejo de archivos

- `banco_preguntas.txt`: lo lee `LectorPreguntas` al iniciar el juego, mezcla las líneas con `Collections.shuffle` y las mete en `BancoPreguntas`. Las líneas con errores o fuera de los niveles 1–12 se ignoran.
- `puntajes.txt`: lo lee y lo escribe `ArchivoPuntajes`. Al terminar cada partida se cargan los puntajes que había en el archivo, se agrega el nuevo al `Ranking` y se vuelven a guardar en formato `nombre;premio`, en preorden. Si el archivo no existe, se crea solo.
- `bolsa.txt`: lo lee y lo escribe `Bolsa`. Guarda un solo número, lo que tiene la bolsa. Se actualiza cada vez que alguien falla o gana las 12. Si no existe o está dañado, la bolsa empieza en $0.

## Integrantes y reparto del trabajo

| Apellido    | Usuario de GitHub          | Rol | Clases y partes del código desarrolladas |
|-----------|----------------------------|-----|------------------------------------------|
| Martínez  | @alejandromartinez1150597  | P1  | `Juego` (ciclo de 12 niveles, uso de los 3 comodines en ronda y tienda de comodines), `Jugador` (inventario y compras), `Pregunta` (bloques y categorías), `Consola` y `Main`. |
| Chica     | @Chika002                  | P2  | `Nodo`, `ListaEnlazada`, clase abstracta `Comodin` (con precios), las tres subclases (`Comodin5050`, `ComodinPublico` y `ComodinCambioPregunta`) y `EventoPeligroso` (pregunta de riesgo). |
| Rubiano   | @Arubiano662               | P3  | `Pila`, `Cola`, `EscaleraPremios` (tabla de premios, multiplicador por racha, descuento de compras y pérdida de todo al fallar) y `LectorPreguntas`. |
| Rivera    | @JuanAlejo-Rivera          | P4  | `NodoArbol`, `ArbolBinarioBusqueda` (inorden inverso y preorden), `BancoPreguntas` (rotación aleatoria y consultas por bloque), `NivelPreguntas`, `Ranking`, `Puntaje`, `ArchivoPuntajes` y `Bolsa`. |

## Lo que no alcanzamos a hacer y lo que haríamos distinto

- **Lo que no alcanzamos a hacer:**
  - **Cola de turnos:** varios jugadores pueden jugar seguidos en una misma ejecución, pero se van turnando uno por uno con la pregunta "¿Quiere jugar otro?". No alcanzamos a registrar a todos los jugadores al inicio en una `Cola` de turnos.
- **Lo que haríamos distinto:**
  - **Separar la consola de la lógica del juego:** `Juego` imprime mensajes directamente con `Consola` mientras cambia el estado de `Jugador` y `EscaleraPremios`; separar ambas capas facilitaría pasar el juego a interfaz gráfica sin tocar las reglas.
  - **Evitar repetidos en el ranking:** si una misma persona juega varias veces, hoy queda registrada varias veces en `puntajes.txt`; lo ideal sería actualizar su nodo solo si supera su mejor puntaje.
