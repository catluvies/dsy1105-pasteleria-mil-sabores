# Pastelería Mil Sabores - Aplicación Móvil E-Commerce 🍰

* **Asignatura:** Desarrollo de Aplicaciones Móviles
* **Sección:** DSY1105-002D
* **Profesor:** Ronald Villalobos
* **Integrantes:** Federico Pereira - Sebastián Robles - Carlos Miranda - Anyara Rosso

---

## 📱 Acerca del Proyecto

Este proyecto consiste en el desarrollo de una aplicación móvil de e-commerce para la **Pastelería Mil Sabores**, una empresa con 50 años de trayectoria. Nuestro objetivo es modernizar su sistema de ventas online, ofreciendo una experiencia de compra accesible, intuitiva y que refleje la calidad y tradición de la marca.

La aplicación permite a los usuarios explorar el catálogo de productos, gestionar su carrito de compras, y a los administradores gestionar el inventario completo con sincronización en tiempo real.

---

## 🚀 Instalación y Ejecución

### Requisitos Previos
- Android Studio Hedgehog (2023.1.1) o superior
- JDK 11 o superior
- Dispositivo físico o emulador con Android 7.0 (API 24) o superior

### Pasos de Instalación

1. **Clonar el repositorio**

```bash
git clone https://github.com/catluvies/dsy1105-pasteleria-mil-sabores.git
```

2. **Abrir el proyecto en Android Studio**
    - En la pantalla de bienvenida, selecciona "Open"
    - Navega hasta la carpeta del proyecto clonado y ábrela

3. **Sincronizar dependencias**
    - Espera a que Android Studio descargue automáticamente todas las librerías (Gradle Sync)
    - Este proceso puede tardar unos minutos la primera vez

4. **Ejecutar la aplicación**
    - Selecciona un emulador o conecta un dispositivo Sfísico
    - Presiona el botón **Run** (▶️) en la barra superior

![Botón Run de Android Studio](docs/images/RUNANDROIDSTUDIO.png)

---

## ✅ Hitos Logrados - 2da Evaluación

* **Sistema de Autenticación Completo**: Implementación de registro y login con validación de datos, gestión de sesión de usuario mediante ViewModels.

* **Catálogo de Productos Funcional**: Visualización de productos con navegación a detalle, integración con base de datos local Room, y carga de datos desde archivos de recursos.

* **Carrito de Compras Operativo**: Sistema completo de carrito con persistencia local, agregar/eliminar productos, y cálculo de totales en tiempo real.

* **Panel de Administración**: Vista exclusiva para administradores con gestión completa de productos (crear, editar, eliminar) del catálogo.

* **Integración de Recursos Nativos**: Implementación de acceso a cámara y galería para que el administrador pueda agregar imágenes de productos desde el dispositivo.

* **Arquitectura MVVM Completa**: Separación de capas con ViewModels, Repositories, y DAOs. Implementación de StateFlow para manejo reactivo del estado.

* **Base de Datos Room**: Persistencia local con Room Database para usuarios, productos y carrito de compras.

* **Sistema de Navegación**: Navigation Compose implementado para flujo completo entre todas las pantallas de la aplicación.

* **Perfil de Usuario**: Pantalla de perfil con información del usuario y opciones de configuración.

---

## 🎯 Hitos Logrados - 3ra Evaluación

### Integración con Backend y Microservicios

* **Backend Spring Boot en Producción**: API REST completa desplegada en Railway conectada con Oracle Cloud Database
    - Endpoints CRUD completos para gestión de productos
    - Sincronización bidireccional entre app móvil y backend
    - Manejo de errores y fallback offline-first

* **Firebase Storage para Imágenes**:
    - Upload de imágenes directamente desde la app móvil
    - CDN global para carga rápida de imágenes
    - URLs persistentes almacenadas en backend

* **Arquitectura de Microservicios**:
    - Frontend (Android) ← HTTP → Backend (Spring Boot) ← SQL → Oracle Cloud DB
    - Firebase Storage para assets multimedia
    - Separación de responsabilidades y escalabilidad

### Consumo de API Externa

* **Integración con API de Reseñas**: Consumo de API REST externa para mostrar reseñas de clientes
    - Retrofit configurado con múltiples endpoints
    - Manejo de estados de carga y errores
    - UI reactiva con datos en tiempo real

### Mejoras de UI/UX

* **Splash Screen Personalizado**: Pantalla de bienvenida animada con branding de la pastelería

* **Modo Oscuro Completo**:
    - Toggle dinámico entre tema claro y oscuro
    - Persistencia de preferencia del usuario
    - Colores adaptados según Material Design 3

* **Rediseño Visual**:
    - Interfaz modernizada siguiendo Material Design 3
    - Cards con elevación y esquinas redondeadas
    - Iconografía coherente y badges informativos
    - Empty states con mensajes amigables

### Funcionalidades Avanzadas

* **Flujo de Checkout Completo**:
    - Pantalla de checkout con formulario de datos de envío
    - Validación de campos en tiempo real
    - Confirmación de pedido con resumen detallado

* **Sistema de Sincronización**:
    - Sync automático al abrir la app
    - Manejo offline-first (funciona sin internet)
    - Room como caché local persistente

* **Gestión de Imágenes del Admin**:
    - Captura desde cámara con permisos runtime
    - Selección desde galería
    - Preview antes de subir
    - Upload a Firebase Storage con progress

### Testing y Calidad

* **Pruebas Unitarias con Kotest**:
    - 6 tests unitarios implementados
    - Cobertura de modelos, cálculos y operaciones
    - Enfoque BDD (Behavior Driven Development)
    - MockK para mocking de dependencias

* **APK Firmado para Distribución**:
    - Generación de keystore (.jks)
    - APK release firmado y optimizado
    - Listo para distribución en producción

### Optimizaciones Técnicas

* **Serialización JSON Optimizada**:
    - Uso de @Expose annotations para control granular
    - Configuración de Gson para evitar campos innecesarios
    - Reducción de payload en requests

* **Logging y Debugging**:
    - HttpLoggingInterceptor para monitoreo de requests
    - Logs estructurados en Repository layer
    - Manejo de errores con mensajes descriptivos

* **Configuración de Red**:
    - Timeouts configurados (30s connect/read/write)
    - Manejo de CLEARTEXT traffic para desarrollo
    - SSL/TLS para producción (Railway)

---

## 🛠️ Stack Tecnológico

### Core
*   [![Kotlin][Kotlin-shield]][Kotlin-url] - Lenguaje principal
*   [![Android Studio][Android-Studio-shield]][Android-Studio-url] - IDE de desarrollo

### UI Framework
*   [![Jetpack Compose][Compose-shield]][Compose-url] - UI moderna declarativa
*   [![Material Design 3][Material-3-shield]][Material-3-url] - Sistema de diseño
*   [![Navigation Compose][Navigation-Compose-shield]][Navigation-Compose-url] - Navegación

### Arquitectura
*   [![MVVM][MVVM-shield]][MVVM-url] - Patrón arquitectónico
*   [![Repository Pattern][Repository-shield]][Repository-url] - Capa de datos
*   [![StateFlow][StateFlow-shield]][StateFlow-url] - Manejo de estado reactivo

### Base de Datos
*   [![Room][Room-shield]][Room-url] - Persistencia local SQLite
*   [![Oracle Cloud][Oracle-shield]][Oracle-url] - Base de datos en la nube

### Networking
*   [![Retrofit][Retrofit-shield]][Retrofit-url] - Cliente HTTP REST
*   [![Gson][Gson-shield]][Gson-url] - Serialización JSON
*   [![OkHttp][OkHttp-shield]][OkHttp-url] - HTTP client con interceptors

### Backend & Cloud
*   [![Spring Boot][Spring-shield]][Spring-url] - Backend microservicio
*   [![Railway][Railway-shield]][Railway-url] - Hosting del backend
*   [![Firebase Storage][Firebase-shield]][Firebase-url] - Almacenamiento de imágenes

### Testing
*   [![Kotest][Kotest-shield]][Kotest-url] - Framework de testing
*   [![MockK][MockK-shield]][MockK-url] - Mocking library

### Multimedia
*   [![Coil][Coil-shield]][Coil-url] - Carga de imágenes asíncrona

---

## 🎨 Guía de Estilo Visual

### Paleta de Colores

#### Modo Claro
* **Fondo Principal**: Crema Pastel `#FFF5E1`
* **Botones Primarios**: Rosa Suave `#FFC0CB`
* **Elementos Destacados**: Chocolate `#8B4513`
* **Texto Principal**: Marrón Oscuro `#5D4037`
* **Texto Secundario**: Gris Claro `#B0BEC5`

#### Modo Oscuro
* **Fondo Principal**: Gris Oscuro `#121212`
* **Superficie**: Gris Medio `#1E1E1E`
* **Accentos**: Rosa Pastel con ajuste de luminosidad
* **Texto**: Blanco `#FFFFFF` / Gris Claro `#E0E0E0`

### Tipografía
* **Títulos y Encabezados**: Pacifico (Display)
* **Textos Generales**: Lato (Body)
* **Elementos UI**: Roboto (Sistema)

### Componentes
* **Cards**: Elevación de 4dp, esquinas redondeadas de 12dp
* **Botones**: Esquinas redondeadas de 8dp, altura de 48dp
* **Iconos**: Material Icons Extended

---

## 🔗 API Endpoints

### Base URL
```
https://pasteleria.anyararosso.com/api/
```

### Productos
- `GET /products` - Obtener todos los productos
- `POST /products` - Crear nuevo producto
- `PUT /products/{id}` - Actualizar producto
- `DELETE /products/{id}` - Eliminar producto

---

## 📝 Credenciales de Prueba

### Usuario Regular
- **Email**: `user@test.com`
- **Contraseña**: `123456`

### Administrador
- **Email**: `admin@pasteleria.com`
- **Contraseña**: `admin123`

---

## 🧪 Ejecutar Tests

```bash
./gradlew test
```

Los tests incluyen validación de modelos, cálculos de precios, y operaciones de carrito.

---

## 📦 Generar APK

1. En Android Studio: `Build > Generate Signed Bundle / APK`
2. Seleccionar **APK**
3. Usar el keystore del proyecto (si aplica)
4. Build Type: **Release**
5. El APK se genera en: `app/build/outputs/apk/release/`

---

<!-- Shields & URLS -->
[Kotlin-shield]: https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white
[Kotlin-url]: https://kotlinlang.org/
[Compose-shield]: https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white
[Compose-url]: https://developer.android.com/jetpack/compose
[Navigation-Compose-shield]: https://img.shields.io/badge/Navigation%20Compose-073042?style=for-the-badge&logo=jetpackcompose&logoColor=white
[Navigation-Compose-url]: https://developer.android.com/jetpack/compose/navigation
[StateFlow-shield]: https://img.shields.io/badge/StateFlow-2E8B57?style=for-the-badge&logo=kotlin&logoColor=white
[StateFlow-url]: https://developer.android.com/kotlin/flow/stateflow-and-sharedflow
[Android-Studio-shield]: https://img.shields.io/badge/Android%20Studio-3DDC84?style=for-the-badge&logo=androidstudio&logoColor=white
[Android-Studio-url]: https://developer.android.com/studio
[MVVM-shield]: https://img.shields.io/badge/Arquitectura-MVVM-orange?style=for-the-badge
[MVVM-url]: https://developer.android.com/jetpack/guide
[Room-shield]: https://img.shields.io/badge/Room-DB-A4C639?style=for-the-badge&logo=sqlite&logoColor=white
[Room-url]: https://developer.android.com/jetpack/androidx/releases/room
[Material-3-shield]: https://img.shields.io/badge/Material%20Design%203-757575?style=for-the-badge&logo=materialdesign&logoColor=white
[Material-3-url]: https://m3.material.io/
[Retrofit-shield]: https://img.shields.io/badge/Retrofit-48B983?style=for-the-badge&logo=square&logoColor=white
[Retrofit-url]: https://square.github.io/retrofit/
[Gson-shield]: https://img.shields.io/badge/Gson-59666C?style=for-the-badge&logo=google&logoColor=white
[Gson-url]: https://github.com/google/gson
[OkHttp-shield]: https://img.shields.io/badge/OkHttp-3E4348?style=for-the-badge&logo=square&logoColor=white
[OkHttp-url]: https://square.github.io/okhttp/
[Spring-shield]: https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white
[Spring-url]: https://spring.io/projects/spring-boot
[Railway-shield]: https://img.shields.io/badge/Railway-0B0D0E?style=for-the-badge&logo=railway&logoColor=white
[Railway-url]: https://railway.app/
[Firebase-shield]: https://img.shields.io/badge/Firebase%20Storage-FFCA28?style=for-the-badge&logo=firebase&logoColor=black
[Firebase-url]: https://firebase.google.com/products/storage
[Kotest-shield]: https://img.shields.io/badge/Kotest-6E40C9?style=for-the-badge&logo=kotlin&logoColor=white
[Kotest-url]: https://kotest.io/
[MockK-shield]: https://img.shields.io/badge/MockK-FF6F00?style=for-the-badge&logo=kotlin&logoColor=white
[MockK-url]: https://mockk.io/
[Coil-shield]: https://img.shields.io/badge/Coil-0095D5?style=for-the-badge&logo=kotlin&logoColor=white
[Coil-url]: https://coil-kt.github.io/coil/
[Oracle-shield]: https://img.shields.io/badge/Oracle%20Cloud-F80000?style=for-the-badge&logo=oracle&logoColor=white
[Oracle-url]: https://www.oracle.com/cloud/
[Repository-shield]: https://img.shields.io/badge/Repository-Pattern-blue?style=for-the-badge
[Repository-url]: https://developer.android.com/jetpack/guide#fetch-data