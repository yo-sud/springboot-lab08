# Laboratorio API - Spring Boot 3 + Java 17

API REST para gestión de productos desarrollada con Spring Boot 3.2+, Java 17, JPA/Hibernate y MySQL.

## 📋 Requisitos Previos

- **Java 17** o superior
- **Maven 3.8+** (o usar el wrapper `mvnw`)
- **MySQL** (XAMPP recomendado) ejecutándose en puerto 3306
- Base de datos `laboratorio_api` creada (se crea automáticamente con `ddl-auto=update`)

## 🚀 Ejecución

### Opción 1: Con Maven instalado globalmente
```bash
mvn spring-boot:run
```

### Opción 2: Con Maven Wrapper (si está disponible)
```bash
./mvnw spring-boot:run    # Linux/Mac
mvnw.cmd spring-boot:run  # Windows
```

La aplicación se iniciará en: **http://localhost:8080**

## ⚙️ Configuración de Base de Datos (XAMPP)

1. Inicia **Apache** y **MySQL** en XAMPP Control Panel
2. Abre phpMyAdmin (http://localhost/phpmyadmin)
3. La base de datos `laboratorio_api` se creará automáticamente al iniciar la app
4. O crea manualmente:
   ```sql
   CREATE DATABASE laboratorio_api CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

### Credenciales por defecto (XAMPP)
- Usuario: `root`
- Contraseña: *(vacía)*

Si tu configuración es diferente, modifica `src/main/resources/application.properties`:
```properties
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
```

## 📚 Endpoints de la API

Base URL: `http://localhost:8080/api/productos`

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/productos` | Listar todos los productos |
| POST | `/api/productos` | Crear nuevo producto |
| GET | `/api/productos/{id}` | Obtener producto por ID |
| PUT | `/api/productos/{id}` | Actualizar producto por ID |
| DELETE | `/api/productos/{id}` | Eliminar producto por ID |
| GET | `/api/productos/buscar?nombre=xxx` | Buscar productos por nombre |

## 📝 Ejemplos de Uso

### Crear producto (POST)
```bash
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Laptop HP",
    "precio": 1200.50,
    "stock": 10,
    "categoria": "Electrónica"
  }'
```

### Listar productos (GET)
```bash
curl http://localhost:8080/api/productos
```

### Obtener por ID (GET)
```bash
curl http://localhost:8080/api/productos/1
```

### Actualizar producto (PUT)
```bash
curl -X PUT http://localhost:8080/api/productos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Laptop HP Actualizada",
    "precio": 1150.00,
    "stock": 8,
    "categoria": "Electrónica"
  }'
```

### Buscar por nombre (GET)
```bash
curl "http://localhost:8080/api/productos/buscar?nombre=laptop"
```

### Eliminar producto (DELETE)
```bash
curl -X DELETE http://localhost:8080/api/productos/1
```

## ✅ Validaciones

El DTO `ProductoDTO` incluye validaciones:
- **nombre**: `@NotBlank` - No puede estar vacío
- **precio**: `@Positive` - Debe ser mayor a 0
- **stock**: `@Min(0)` - No puede ser negativo
- **categoria**: `@NotBlank` - No puede estar vacía

Respuesta de error de validación (400):
```json
{
  "nombre": "El nombre es obligatorio",
  "precio": "El precio debe ser mayor a cero"
}
```

## 📁 Estructura del Proyecto

```
src/main/java/com/tecsup/
├── controller/
│   └── ProductoController.java
├── dto/
│   └── ProductoDTO.java
├── exception/
│   ├── EntityNotFoundException.java
│   └── GlobalExceptionHandler.java
├── model/
│   └── Producto.java
├── repository/
│   └── ProductoRepository.java
├── service/
│   ├── ProductoService.java
│   └── impl/
│       └── ProductoServiceImpl.java
└── LaboratorioApiApplication.java
```

## 🛠️ Tecnologías Utilizadas

- **Spring Boot 3.2.5**
- **Spring Web** - API REST
- **Spring Data JPA** - Persistencia
- **Spring Validation** - Validación de DTOs
- **MySQL Connector/J** - Driver MySQL
- **Lombok** - Reducción de boilerplate
- **Hibernate** - ORM

## 📦 Compilación y Empaquetado

```bash
# Compilar y crear JAR
mvn clean package

# Ejecutar JAR generado
java -jar target/laboratorio-api-1.0.0.jar
```

## 🧪 Pruebas

```bash
# Ejecutar tests
mvn test
```

---

**Desarrollado para fines educativos - Laboratorio Spring Boot**