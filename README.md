# Mínimo 1 DSA - Sistema de Gestión de Flota de Drones

## 1. Estado Actual del Proyecto
El proyecto ha sido completado en su totalidad, cumpliendo con los requisitos especificados:
- **Parte I**: Esquema de estructuras de datos y análisis de complejidad temporal Big O.
- **Parte II**: Interfaz `DronesManager`, Fachada Singleton `DronesManagerImpl` con trazas Log4j (INFO, ERROR y FATAL) y tests unitarios `DronesManagerTest` en JUnit.
- **Parte III**: API REST `DronesService` desarrollada con Jersey/Grizzly, integrada con Swagger UI y testeada en Postman.

### Especificación del Servicio REST (`DronesService`)
El servicio REST está implementado en la clase `DronesService` mediante Jersey (JAX-RS) y mapeado bajo la ruta base `/dsawm/drones/`. Utiliza la fachada Singleton `DronesManagerImpl` para la ejecución de la lógica de negocio.

| Operación | Método HTTP | Ruta (Path) | Cuerpo de Petición (Request Body) | Respuesta (Response Body) | Códigos HTTP |
| :--- | :---: | :--- | :--- | :--- | :---: |
| **Añadir Dron** | `POST` | `/addDron` | `{"id":"D1", "nombre":"Mavic 3", "fabricante":"DJI", "modelo":"Pro"}` | Objeto `Dron` en JSON | `201 Created` / `400 Bad Request` |
| **Añadir Piloto** | `POST` | `/addPiloto` | `{"id":"P1", "nombre":"Carlos", "apellidos":"Sáez"}` | Objeto `Piloto` en JSON | `201 Created` / `400 Bad Request` |
| **Crear Plan Vuelo** | `POST` | `/planVuelo` | `{"idDron":"D1", "idPiloto":"P1", "fecha":"2026-11-05T10:00", "tiempoEstimado":2.5, "posiciones":[...]}` | Objeto `PlanVuelo` en JSON | `201 Created` / `400 Bad Request` |
| **Guardar en Almacén** | `POST` | `/almacen/{idDron}` | N/A | Objeto `Dron` actualizado (`enAlmacen: true`) | `200 OK` / `404 Not Found` |
| **Reparar Dron (LIFO)** | `POST` | `/almacen/reparar` | N/A | Objeto `Dron` desapilado (`enAlmacen: false`) | `200 OK` / `404 Not Found` |
| **Listar Drones (Horas)** | `GET` | `/drones/horas` | N/A | Lista `List<Dron>` ordenada desc. por horas | `200 OK` |
| **Listar Pilotos (Horas)** | `GET` | `/pilotos/horas` | N/A | Lista `List<Piloto>` ordenada desc. por horas | `200 OK` |

### Manejo de Ciclos y Serialización JSON
Para asegurar la serialización y deserialización correcta en JSON con Jackson y cumplir los requerimientos de la asignatura:
- **Evitación de relaciones circulares:** `PlanVuelo` almacena identificadores planos (`idDron` e `idPiloto` de tipo `String`) en lugar de contener referencias a las instancias completas de las clases `Dron` y `Piloto`.
- **Prevención de recursión infinita:** Se evita la anidación bidireccional (`Dron` $\rightarrow$ `PlanVuelo` $\rightarrow$ `Dron`), impidiendo errores de tipo `StackOverflowError` o fallos en el parser JSON.

## 2. Estructuras de Datos Elegidas y Justificación
- **`drones` (`HashMap<String, Dron>`)**: Búsqueda e inserción directa en $O(1)$ mediante el identificador del dron.
- **`pilots` (`HashMap<String, Piloto>`)**: Búsqueda e inserción directa en $O(1)$ mediante el identificador del piloto.
- **`almacen` (`Stack<Dron>` / LIFO)**: Pila para el mantenimiento de drones. Al entrar por el fondo, el último dron ingresado queda situado más cerca de la puerta, por lo que las reparaciones siguen un orden estricto LIFO (*Last In, First Out*).
- **`planesDeVuelo` (`LinkedList<PlanVuelo>`)**: Cada `Dron` y `Piloto` incluye una lista enlazada interna con su historial de vuelos.
- **`posiciones` (`LinkedList<Posicion>`)**: Colección ordenada de coordenadas (latitud/longitud) para cada ruta de vuelo.

## 3. Errores y Problemas Encontrados
- **Bloqueo del puerto HTTP**: Conflicto `ProcessingException` al intentar arrancar `Main.java` con el puerto `8080` ocupado por una instancia previa. Solucionado finalizando los procesos activos en segundo plano.
- **Ubicación de recursos**: Configuración del archivo `log4j.properties` para la resolución adecuada de appenders de consola.

## 4. Funcionalidades Pendientes
- Todas las operaciones obligatorias solicitadas en el enunciado han sido implementadas y validadas correctamente.