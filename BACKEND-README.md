# Pastelería Mil Sabores - Backend API

* Asignatura: Desarrollo de Aplicaciones Móviles.
* Sección: DSY1105-002D
* Profesor: Ronald Villalobos.
* Integrantes: Federico Pereira - Sebastián Robles - Carlos Miranda - Anyara Rosso.

---

## Alcance del Proyecto:

Este proyecto consiste en el desarrollo del backend API REST para la aplicación móvil de e-commerce "Pastelería Mil Sabores".
El backend proporciona todos los servicios necesarios para soportar las operaciones de la aplicación móvil, incluyendo autenticación de usuarios, gestión de productos, carrito de compras, y administración del catálogo.
Esta API está diseñada para ser escalable, segura y eficiente, garantizando una experiencia fluida para los usuarios finales.

---

## Pasos para instalación y ejecución:

### 1. Clona el repositorio

Abre tu terminal y ejecuta el siguiente comando:

```bash
git clone https://github.com/catluvies/dsy1105-pasteleria-mil-sabores.git
cd dsy1105-pasteleria-mil-sabores/backend
```

### 2. Configura las variables de entorno

Crea un archivo `.env` en la raíz del proyecto backend con las siguientes variables:

```env
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=pasteleria_db
DB_USER=postgres
DB_PASSWORD=tu_password

# Server
PORT=8080
HOST=0.0.0.0

# Security
JWT_SECRET=tu_secreto_seguro_aqui
JWT_ISSUER=pasteleria-mil-sabores
JWT_AUDIENCE=pasteleria-api
JWT_REALM=Pasteleria App

# File Upload
UPLOAD_DIR=./uploads
MAX_FILE_SIZE=5242880
```

### 3. Instala las dependencias

Asegúrate de tener instalado:
- **JDK 11** o superior
- **PostgreSQL 14** o superior
- **Gradle 8.0** o superior (opcional, el proyecto incluye Gradle Wrapper)

Sincroniza las dependencias ejecutando:

```bash
./gradlew build
```

### 4. Configura la base de datos

Crea la base de datos en PostgreSQL:

```sql
CREATE DATABASE pasteleria_db;
CREATE USER postgres WITH PASSWORD 'tu_password';
GRANT ALL PRIVILEGES ON DATABASE pasteleria_db TO postgres;
```

Las tablas se crearán automáticamente al iniciar el servidor por primera vez.

### 5. Ejecuta el servidor

Inicia el servidor en modo desarrollo:

```bash
./gradlew run
```

O para producción:

```bash
./gradlew build
java -jar build/libs/pasteleria-backend.jar
```

El servidor estará disponible en: `http://localhost:8080`

### 6. Verifica la instalación

Accede a la ruta de salud del servidor:

```bash
curl http://localhost:8080/health
```

Deberías recibir una respuesta:
```json
{
  "status": "OK",
  "timestamp": "2025-11-16T12:00:00Z"
}
```

---

## Funcionalidades Implementadas:

* **Autenticación JWT**: Sistema completo de registro y login con tokens seguros.
* **Gestión de Usuarios**: CRUD de usuarios con roles (cliente/administrador).
* **Catálogo de Productos**: Endpoints para listar, buscar y filtrar productos.
* **Gestión de Productos**: CRUD completo para administradores.
* **Carrito de Compras**: Endpoints para agregar, actualizar y eliminar items del carrito.
* **Gestión de Imágenes**: Upload y almacenamiento de imágenes de productos.
* **Validación de Datos**: Validación exhaustiva de todas las entradas de usuario.
* **Seguridad**: Hashing de contraseñas con BCrypt y autenticación mediante JWT.
* **Base de Datos**: PostgreSQL con migraciones automáticas.

---

## Endpoints Principales:

### Autenticación

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/register` | Registrar nuevo usuario | No |
| POST | `/api/auth/login` | Iniciar sesión | No |
| GET | `/api/auth/me` | Obtener usuario actual | Sí |
| POST | `/api/auth/refresh` | Renovar token | Sí |

**Ejemplo - Registro:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "usuario@example.com",
    "password": "password123",
    "name": "Juan Pérez",
    "phone": "+56912345678"
  }'
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "email": "usuario@example.com",
    "name": "Juan Pérez",
    "role": "CLIENT"
  }
}
```

---

### Productos

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| GET | `/api/products` | Listar todos los productos | No |
| GET | `/api/products/{id}` | Obtener producto por ID | No |
| GET | `/api/products/search?q={query}` | Buscar productos | No |
| POST | `/api/products` | Crear nuevo producto | Admin |
| PUT | `/api/products/{id}` | Actualizar producto | Admin |
| DELETE | `/api/products/{id}` | Eliminar producto | Admin |

**Ejemplo - Listar productos:**
```bash
curl http://localhost:8080/api/products
```

**Respuesta:**
```json
{
  "products": [
    {
      "id": 1,
      "name": "Torta de Chocolate",
      "description": "Deliciosa torta de chocolate con cobertura de ganache",
      "price": 15000,
      "category": "TORTAS",
      "imageUrl": "/uploads/torta-chocolate.jpg",
      "stock": 10,
      "available": true
    }
  ],
  "total": 1
}
```

**Ejemplo - Crear producto (Admin):**
```bash
curl -X POST http://localhost:8080/api/products \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Cupcake de Vainilla",
    "description": "Cupcake suave de vainilla con buttercream",
    "price": 2500,
    "category": "CUPCAKES",
    "stock": 50
  }'
```

---

### Carrito de Compras

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| GET | `/api/cart` | Obtener carrito del usuario | Sí |
| POST | `/api/cart/items` | Agregar item al carrito | Sí |
| PUT | `/api/cart/items/{id}` | Actualizar cantidad | Sí |
| DELETE | `/api/cart/items/{id}` | Eliminar item del carrito | Sí |
| DELETE | `/api/cart` | Vaciar carrito | Sí |

**Ejemplo - Agregar al carrito:**
```bash
curl -X POST http://localhost:8080/api/cart/items \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "productId": 1,
    "quantity": 2
  }'
```

**Respuesta:**
```json
{
  "cart": {
    "id": 1,
    "userId": 1,
    "items": [
      {
        "id": 1,
        "product": {
          "id": 1,
          "name": "Torta de Chocolate",
          "price": 15000
        },
        "quantity": 2,
        "subtotal": 30000
      }
    ],
    "total": 30000
  }
}
```

---

### Gestión de Imágenes

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| POST | `/api/upload` | Subir imagen | Admin |
| GET | `/uploads/{filename}` | Obtener imagen | No |

**Ejemplo - Subir imagen:**
```bash
curl -X POST http://localhost:8080/api/upload \
  -H "Authorization: Bearer {token}" \
  -F "file=@/path/to/image.jpg"
```

**Respuesta:**
```json
{
  "url": "/uploads/1234567890-image.jpg",
  "filename": "1234567890-image.jpg"
}
```

---

### Administración

| Método | Endpoint | Descripción | Auth |
|--------|----------|-------------|------|
| GET | `/api/admin/users` | Listar todos los usuarios | Admin |
| GET | `/api/admin/stats` | Estadísticas del sistema | Admin |
| PUT | `/api/admin/users/{id}/role` | Cambiar rol de usuario | Admin |

---

## Stack Tecnológico:

*   [![Kotlin][Kotlin-shield]][Kotlin-url]
*   [![Ktor][Ktor-shield]][Ktor-url]
*   [![PostgreSQL][PostgreSQL-shield]][PostgreSQL-url]
*   [![Exposed][Exposed-shield]][Exposed-url]
*   [![JWT][JWT-shield]][JWT-url]
*   [![Koin][Koin-shield]][Koin-url]
*   [![Kotlinx Serialization][Serialization-shield]][Serialization-url]
*   [![Gradle][Gradle-shield]][Gradle-url]

---

## Estructura del Proyecto:

```
backend/
├── src/
│   ├── main/
│   │   ├── kotlin/
│   │   │   └── cl/duoc/pasteleria/
│   │   │       ├── Application.kt          # Punto de entrada
│   │   │       ├── plugins/                # Configuración de plugins
│   │   │       │   ├── Routing.kt
│   │   │       │   ├── Security.kt
│   │   │       │   └── Serialization.kt
│   │   │       ├── routes/                 # Definición de rutas
│   │   │       │   ├── AuthRoutes.kt
│   │   │       │   ├── ProductRoutes.kt
│   │   │       │   └── CartRoutes.kt
│   │   │       ├── models/                 # Modelos de datos
│   │   │       │   ├── User.kt
│   │   │       │   ├── Product.kt
│   │   │       │   └── Cart.kt
│   │   │       ├── database/               # Configuración DB
│   │   │       │   ├── DatabaseFactory.kt
│   │   │       │   └── tables/
│   │   │       ├── repositories/           # Capa de datos
│   │   │       │   ├── UserRepository.kt
│   │   │       │   └── ProductRepository.kt
│   │   │       ├── services/               # Lógica de negocio
│   │   │       │   ├── AuthService.kt
│   │   │       │   └── ProductService.kt
│   │   │       └── utils/                  # Utilidades
│   │   │           ├── JwtConfig.kt
│   │   │           └── HashingUtils.kt
│   │   └── resources/
│   │       └── application.conf            # Configuración
│   └── test/
│       └── kotlin/                         # Tests
├── build.gradle.kts                        # Dependencias
├── .env                                    # Variables de entorno
└── README.md
```

---

## Seguridad:

* **Autenticación JWT**: Tokens seguros con expiración configurable
* **Hashing de Contraseñas**: BCrypt con salt para almacenamiento seguro
* **Validación de Entrada**: Sanitización y validación de todos los datos
* **CORS**: Configurado para permitir solo orígenes autorizados
* **Rate Limiting**: Protección contra ataques de fuerza bruta
* **HTTPS**: Recomendado para producción
* **Variables de Entorno**: Credenciales sensibles nunca en código fuente

---

## Testing:

Ejecutar todos los tests:

```bash
./gradlew test
```

Ejecutar tests con reporte de cobertura:

```bash
./gradlew test jacocoTestReport
```

---

## Despliegue:

### Docker

```bash
# Construir imagen
docker build -t pasteleria-backend .

# Ejecutar contenedor
docker run -p 8080:8080 --env-file .env pasteleria-backend
```

### Docker Compose

```bash
docker-compose up -d
```

---

## Hitos:

* Se implementó la arquitectura base del servidor Ktor
* Se configuró la base de datos PostgreSQL con Exposed
* Se implementó autenticación JWT completa
* Se crearon endpoints CRUD para productos
* Se implementó sistema de carrito de compras
* Se agregó sistema de upload de imágenes
* Se implementaron roles y permisos de usuario

---

<!-- Shields & URLS -->
[Kotlin-shield]: https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white
[Kotlin-url]: https://kotlinlang.org/
[Ktor-shield]: https://img.shields.io/badge/Ktor-087CFA?style=for-the-badge&logo=ktor&logoColor=white
[Ktor-url]: https://ktor.io/
[PostgreSQL-shield]: https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white
[PostgreSQL-url]: https://www.postgresql.org/
[Exposed-shield]: https://img.shields.io/badge/Exposed-000000?style=for-the-badge&logo=jetbrains&logoColor=white
[Exposed-url]: https://github.com/JetBrains/Exposed
[JWT-shield]: https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white
[JWT-url]: https://jwt.io/
[Koin-shield]: https://img.shields.io/badge/Koin-FF9800?style=for-the-badge&logo=kotlin&logoColor=white
[Koin-url]: https://insert-koin.io/
[Serialization-shield]: https://img.shields.io/badge/Kotlinx.Serialization-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white
[Serialization-url]: https://github.com/Kotlin/kotlinx.serialization
[Gradle-shield]: https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white
[Gradle-url]: https://gradle.org/
