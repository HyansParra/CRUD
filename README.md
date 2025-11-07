# CaféFidelidad QR (Aplicación Android)

Este es un proyecto académico que consiste en una aplicación Android para **"CaféFidelidad QR"**, un sistema de fidelización desarrollado para una cafetería. La aplicación fue creada en **Java** usando Android Studio y gestiona todos sus datos a través de una base de datos local **SQLite**.

## Enlace de la Presentación (Evaluación)

Para la evaluación de la Unidad II, puedes encontrar la presentación de PowerPoint (Semana 12) en la siguiente carpeta compartida:

* **[Ver Presentación Final (Semana 12) en OneDrive](https://alumnossantotomas-my.sharepoint.com/:f:/g/personal/h_parra8_alumnos_santotomas_cl/Etzq3a42JZdKnXufIgazC6wBT7-xgn3fJzdqur4I49ExAA?e=gKju8v)**

---

## Funcionalidades Principales

La aplicación implementa un sistema de autenticación que diferencia claramente entre usuarios **normales** y **administradores**, ofreciendo características específicas para cada rol.

### Para Clientes (Rol: "normal")

* **Acceso y Registro:** Implementación de las pantallas de Inicio de Sesión y Registro para nuevos clientes.
* **Navegación:** Pantalla principal de bienvenida con acceso directo a las secciones clave.
* **Catálogo:** Visualización completa de los productos y cafés disponibles.
* **Mi Perfil:**
    * Muestra los datos del perfil y los puntos acumulados.
    * Botón **"Mi QR"** para generar un código QR único (para identificarse o acumular puntos).
    * **CRUD de Reseñas:** Permite la gestión completa de las opiniones del usuario sobre los productos.
        * **Crear (C):** Añadir una nueva reseña, incluyendo una calificación por estrellas y un comentario.
        * **Leer (R):** Ver el historial de todas las reseñas propias.
        * **Actualizar (U):** Modificar la calificación o el comentario de una reseña ya existente.
        * **Eliminar (D):** Borrar una reseña de forma definitiva.
    * Opción de Cerrar Sesión.

### Para Administradores (Rol: "administrador")

El rol de Administrador incluye todas las funciones de Cliente, además de herramientas de gestión:

* **CRUD de Usuarios:** Acceso (desde el perfil) para crear, leer, actualizar y eliminar cuentas de usuario.
* **CRUD de Catálogo:** Acceso (desde el catálogo) para añadir, editar y eliminar productos.
* **Información Gerencial:** Acceso rápido (desde el perfil) a secciones de **Reglas** y **Beneficios** de la cafetería.

## Estructura de Datos (SQLite)

La persistencia de datos se maneja localmente con la clase `SQLiteOpenHelper` (`cafeteria.db`). Las tablas clave son:

* `Usuarios`: Almacena las credenciales de acceso y el tipo de rol (`normal` / `administrador`).
* `Clientes`: Contiene información de perfil, incluyendo la gestión de **puntos**.
* `Productos`: Almacena el catálogo de cafés con su información.
* `Reseñas`: Registra las opiniones de los usuarios, vinculando un `idUsuario` con un `idProducto` y la calificación.

## Cómo Ejecutar la Aplicación

1.  **Clonar el repositorio:**
    ```bash
    git clone [URL_DE_TU_REPOSITORIO]
    ```
2.  **Abrir en Android Studio:**
    * Seleccionar **"Open an existing project"** y dirigirse a la carpeta clonada.
3.  **Sincronizar Gradle:** Esperar a la sincronización de dependencias (necesarias para la librería de QR, `zxing-android-embedded`).
4.  **Ejecutar:** Iniciar la app en un emulador o dispositivo físico.

## Credenciales de Prueba

La base de datos se inicializa con los siguientes usuarios:

* **Admin:** `Usuario: admin` | `Contraseña: admin123`
* **Normal:** `Usuario: usuario` | `Contraseña: usuario123`