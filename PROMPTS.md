# PROMPTS.md

Registro de las consultas hechas a la IA durante la **Fase 2** del laboratorio (rama `mejora-ia`).

- **Proyecto:** Dragon Ball – Catálogo de personajes
- **Integrante:** José Rojas Condor
- **Herramienta usada:** Claude (Anthropic)

---

## Consulta 1

- **Fecha:** 08/11/2026
- **Herramienta:** Claude
- **Prompt:** "Te paso capturas de todas las pantallas de mi app. En el login casi no se nota lo que escribo, la letra sale muy clara. ¿Qué otros problemas visuales ves?"
- **Qué respondió (resumen):** Explicó que el login no tenía un `Surface`, entonces con el celular en modo oscuro el texto salía claro sobre el fondo blanco de la ventana. Además encontró que el título de la lista se partía en dos líneas cuando el admin tiene tres íconos y que las tarjetas del panel Admin tenían distinta altura.
- **Qué usamos / qué cambiamos y por qué:** Envolví el login en un `Surface` para que use el fondo del tema, acorté el título a "Dragon Ball" y usé `IntrinsicSize.Min` para que las dos tarjetas del panel tengan la misma altura. No había notado los dos últimos problemas porque solo probaba con el usuario normal.
- **Qué aprendimos:** Que en Compose el texto toma su color del tema, pero si la pantalla no pinta su propio fondo se ve el de la ventana. Por eso conviene que cada pantalla tenga un `Scaffold` o un `Surface`.

## Consulta 2

- **Fecha:** 08/11/2026
- **Herramienta:** Claude
- **Prompt:** "En la barra de arriba del celular sale una mancha gris cuando llega la notificación, ¿por qué pasa?"
- **Qué respondió (resumen):** El ícono pequeño de una notificación tiene que ser de un solo color y con fondo transparente. Yo estaba usando el ícono de la app, que tiene colores, y Android lo pinta como una mancha. Propuso crear un vector de una campana.
- **Qué usamos / qué cambiamos y por qué:** Agregué `res/drawable/ic_notificacion.xml` (una campana blanca) y lo puse en `setSmallIcon` del `NotificadorAndroid`.
- **Qué aprendimos:** Que Android solo usa la forma (el canal alfa) del ícono de la barra de estado, por eso no sirve cualquier imagen.

## Consulta 3

- **Fecha:** 09/11/2026
- **Herramienta:** Claude
- **Prompt:** "Quiero agregar un buscador por nombre en la lista sin romper que Room sea la fuente de verdad. ¿Dónde debería ir la lógica?"
- **Qué respondió (resumen):** Recomendó guardar el texto de búsqueda en el `ListaViewModel` como un `StateFlow` y filtrar ahí la lista que llega de Room, en vez de hacer una consulta nueva o filtrar en la pantalla. También sugirió no filtrar mientras el estado es Cargando o Error para no tapar esos mensajes.
- **Qué usamos / qué cambiamos y por qué:** Agregué `busqueda`, `buscar()` y `mostrarFiltrada()` al ViewModel y un `OutlinedTextField` con lupa y botón para borrar en la pantalla. La pantalla solo llama a `viewModel.buscar(texto)`.
- **Qué aprendimos:** Que si el texto vive en el ViewModel no se pierde al girar la pantalla, y que el buscador funciona sin internet porque busca en lo que ya está guardado.

## Consulta 4

- **Fecha:** 09/11/2026
- **Herramienta:** Claude
- **Prompt:** "Ahora quiero filtrar por raza con chips y que se pueda combinar con el buscador."
- **Qué respondió (resumen):** Propuso sacar la lista de razas de los mismos personajes guardados (sin repetir y ordenadas), guardar la raza elegida en el ViewModel y aplicar los dos filtros juntos en `mostrarFiltrada()`. Para la pantalla sugirió una fila de `FilterChip` dentro de un `LazyRow`.
- **Qué usamos / qué cambiamos y por qué:** Lo implementé así y agregué el chip "Todos" para quitar el filtro. Si el filtro no encuentra nada, la pantalla muestra "No se encontraron personajes con ese filtro".
- **Qué aprendimos:** Que sacar las razas de los datos es mejor que escribirlas a mano, porque si la API agrega una raza nueva aparece sola.

## Consulta 5

- **Fecha:** 09/11/2026
- **Herramienta:** Claude
- **Prompt:** "¿Cómo hago las 3 pruebas unitarias del ListaViewModel con un repositorio falso como pide el laboratorio?"
- **Qué respondió (resumen):** Explicó que como `ItemRepository` y `Notificador` son interfaces, se pueden reemplazar por clases falsas que no usan internet ni el celular. Propuso probar los tres caminos de `refrescar()`: con internet, sin internet sin datos y sin internet con datos guardados. También indicó que hay que cambiar `Dispatchers.Main` por un `UnconfinedTestDispatcher`, porque en las pruebas no existe el hilo principal de Android.
- **Qué usamos / qué cambiamos y por qué:** Creé `RepositorioFalso`, `NotificadorFalso` y `ListaViewModelTest` con las tres pruebas. Las ejecuté con `gradlew testDebugUnitTest` y pasaron las tres (100%).
- **Qué aprendimos:** Recién entendí para qué sirve que el Repository sea una interface: el ViewModel no sabe si le pasan el real o el falso, y así se puede probar sin depender de internet.

## Consulta 6

- **Fecha:** 09/11/2026
- **Herramienta:** Claude
- **Prompt:** "Quiero darle un mejor estilo visual a la app, con colores de Dragon Ball y los títulos más marcados."
- **Qué respondió (resumen):** Notó que el tema de la plantilla usaba colores dinámicos (`dynamicColor`), que toman los colores del fondo de pantalla del celular. Propuso quitarlos y definir una paleta propia en `Color.kt` y `Theme.kt`, poner los títulos en negrita en `Type.kt` y darle a la barra superior el color principal.
- **Qué usamos / qué cambiamos y por qué:** Definí una paleta naranja, azul y amarillo para modo claro y oscuro, puse los títulos en negrita y creé `coloresBarra()` en `components/BarraSuperior.kt` para no repetir los colores en las cuatro pantallas.
- **Qué aprendimos:** Que cambiando solo el tema cambia toda la app, porque los componentes de Material 3 toman los colores de `MaterialTheme`. No tuve que tocar cada botón.

---

## Reflexión

La IA me ayudó sobre todo a encontrar problemas que yo no veía, como el login en modo oscuro o el ícono de la notificación, y a entender el porqué de cada cambio. No copié las respuestas sin más: probé cada cambio en mi celular antes de hacer el commit y en algunos casos ajusté cosas, como el texto de los mensajes o qué filtrar.

Lo que más me sirvió fue la parte de las pruebas, porque me hizo entender en la práctica por qué el laboratorio pide que el Repository y el Notificador sean interfaces. También aprendí que hay que revisar lo que propone la IA: por ejemplo, el buscador al principio tapaba el mensaje de error y hubo que agregar una condición para que solo filtre cuando ya hay una lista.
