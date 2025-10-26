# Avance de Proyecto Final - HomeFitPoints

## Descripción General

Esta aplicación Android, desarrollada como parte del **Módulo 7**, permite a los usuarios visualizar una lista de ejercicios obtenidos desde un servicio web, ver los detalles de cada ejercicio y marcar aquellos que han completado, llevando un registro de su progreso mediante un sistema de puntos.

---

## Funcionalidades Implementadas

- **Visualización de Lista de Ejercicios:**  
  Muestra una lista de ejercicios disponibles, obtenida desde una API simulada.

- **Vista de Detalle:**  
  Permite al usuario seleccionar un ejercicio para ver información detallada: descripción, tipo, repeticiones, dificultad, calorías estimadas y puntos otorgados.

- **Marcado de Ejercicios Completados:**  
  En la pantalla de detalle, el usuario puede marcar un ejercicio como completado.

- **Seguimiento de Progreso (Local):**
    - Guarda localmente qué ejercicios han sido completados.
    - Acumula los puntos de los ejercicios completados.
    - Muestra una marca visual (✅) junto a los ejercicios completados en la lista principal.

- **Navegación:**  
  Flujo entre la pantalla de lista y la de detalle.

- **Manejo de Errores de Conexión:**  
  Muestra una pantalla específica si no se puede conectar con la API.

---

## Conceptos del Módulo Aplicados

### Persistencia de Información (Preferences DataStore)

- Se utiliza **Preferences DataStore** para almacenar de forma persistente el progreso del usuario.
- Datos guardados:
    - `Set<String>` con los IDs de los ejercicios completados.
    - `Int` con el puntaje total acumulado.
- La lógica de acceso a DataStore está encapsulada en la clase `UserPreferencesRepository`, siguiendo el principio de **separación de responsabilidades**.

---

### Uso de Backend Web (Retrofit)

- La aplicación se conecta a una **API web simulada (Apiary)** para obtener la información de los ejercicios.
- Se utiliza **Retrofit** para:
    - Definir la interfaz de la API (`ExerciseApi`).
    - Realizar llamadas GET para lista y detalle.
    - Deserializar respuestas JSON en objetos Kotlin (`ExerciseDto`, `ExerciseDetailDto`).
- Implementación de **OkHttpClient** con `HttpLoggingInterceptor` para depuración de llamadas.
- La lógica de datos remotos se centraliza en `ExerciseRepository`.

---

### Arquitectura MVVM (Model-View-ViewModel)

- Se adopta la arquitectura **MVVM** para mantener una clara separación de responsabilidades.
- Cada pantalla principal (`ExerciseListFragment`, `ExerciseDetailFragment`) tiene su propio **ViewModel**.
- Los **ViewModels** interactúan con los repositorios (`ExerciseRepository` y `UserPreferencesRepository`) para obtener y preparar los datos.
- Las vistas (**Fragments**) solo observan los datos y actualizan la UI sin lógica de negocio.
- Se implementa una **ViewModelFactory** para inyección de dependencias.

---

### Programación Reactiva (LiveData)

- La comunicación entre ViewModels y Fragments se realiza mediante **LiveData**.
- Los ViewModels exponen estados de UI:
    - `Loading`
    - `Success` (con datos)
    - `Error`
- Los Fragments observan los cambios usando `viewLifecycleOwner` para actualizar la interfaz de manera segura y eficiente.

---

### Esquema de Navegación (Navigation Component)

- Navegación entre fragments implementada con **Navigation Component**.
- Flujo definido en `nav_graph.xml`.
- Uso de **Safe Args** para pasar argumentos entre fragments de forma **type-safe**.

---

## Recursos y UI

- **Sin texto hardcodeado:** todo el texto se encuentra en `res/values/strings.xml`.
- **Imágenes:** uso de la librería **Glide** para cargar imágenes desde URLs.
- **Interfaz:** basada en **Material Design** y **ConstraintLayout**, utilizando componentes como:
    - `RecyclerView`
    - `CardView`
    - `ProgressBar`
    - `ImageButton`

---

## Alcances Logrados

- Consumo correcto de datos desde una API externa.
- Flujo de navegación entre lista y detalle funcional.
- Marcado de ejercicios como completados.
- Persistencia local del progreso con DataStore.
- Indicación visual de ejercicios completados.
- Arquitectura basada en **MVVM** y programación reactiva.
- Manejo básico de errores de conexión.

---

## Características Previstas / Mejoras Futuras

- **Pantalla de Progreso:**  
  `ProgresoFragment` con resumen visual del puntaje total y ejercicios completados.

- **Pantalla de Perfil:**  
  `PerfilFragment` para ver/editar nombre del usuario y preferencias (tema claro/oscuro).

- **Mejoras UI/UX:**
    - Añadir animaciones o feedback visual al marcar ejercicios.
    - Considerar la implementación de `BottomNavigationView`.
    - Añadir mejoras visuales en la implementacion de cada interfaz.

---
---

*Proyecto desarrollado por **Fabian Armenta** como parte del Avance del proyecto final del Diplomado en Desarrollo de Aplicaciones Móviles.*
