# Backend Shopersa

API REST para el sistema de e-commerce Shopersa desarrollada con Spring Boot.

## 🚀 Tecnologías

- **Java 17**
- **Spring Boot 3.2.5**
- **Spring Security** (JWT Authentication)
- **Spring Data JPA**
- **MySQL 8**
- **Maven**
- **Swagger/OpenAPI 3**

## 📋 Prerrequisitos

- Java 17 o superior
- Maven 3.6+
- MySQL 8.0+
- IDE (IntelliJ IDEA, Eclipse, VS Code)

## ⚙️ Configuración

### 1. Base de Datos

Crear la base de datos en MySQL:

```sql
CREATE DATABASE bdshopersa;
```

### 2. Variables de Entorno

Copiar el archivo de ejemplo y configurar las variables:

```bash
cp .env.example .env
```

Editar el archivo `.env` con tus configuraciones:

```properties
# Configuración de Base de Datos
DB_URL=jdbc:mysql://localhost:3306/bdshopersa?serverTimezone=UTC&useSSL=false
DB_USERNAME=root
DB_PASSWORD=tu_password

# Configuración del Seed de Datos
SEED_ENABLED=true
SEED_ADMIN_EMAIL=admin@shopersa.cl
SEED_ADMIN_PASSWORD=admin123
```

### 3. Configuración de application.properties

El archivo `src/main/resources/application.properties` contiene la configuración principal:

```properties
# Base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/bdshopersa?serverTimezone=UTC&useSSL=false
spring.datasource.username=root
spring.datasource.password=

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Uploads
spring.servlet.multipart.max-file-size=10MB
app.uploads.products-dir=uploads/products

# Seed de datos
app.seed.enabled=true
app.seed.admin.email=admin@shopersa.cl
app.seed.admin.password=admin123
```

## 🏃‍♂️ Ejecución

### Desarrollo

```bash
# Compilar y ejecutar
mvn spring-boot:run

# O usando el wrapper
./mvnw spring-boot:run
```

### Producción

```bash
# Compilar
mvn clean package

# Ejecutar JAR
java -jar target/backend_shopersa-0.0.1-SNAPSHOT.jar
```

La aplicación estará disponible en: `http://localhost:8080`

## 📚 Documentación API

Una vez iniciada la aplicación, la documentación Swagger estará disponible en:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## 🔐 Autenticación

### Usuario Administrador por Defecto

El sistema crea automáticamente un usuario administrador:

- **Email**: `admin@shopersa.cl`
- **Password**: `admin123`
- **Rol**: `ADMIN`

### Endpoints de Autenticación

```http
POST /api/auth/login
POST /api/auth/register
GET /api/auth/me
```

### Uso del JWT

Incluir el token en el header de las peticiones:

```http
Authorization: Bearer <tu_jwt_token>
```

## 🗂️ Estructura del Proyecto

```
src/main/java/com/shopersa/backend_shopersa/
├── Config/                 # Configuraciones
│   ├── SecurityConfig.java
│   ├── DataSeeder.java
│   └── OpenApiConfig.java
├── MS_Auth/               # Módulo de Autenticación
├── MS_Usuario/            # Módulo de Usuarios
├── MS_Producto/           # Módulo de Productos
├── MS_Categoria/          # Módulo de Categorías
├── MS_Venta/             # Módulo de Ventas
├── MS_Envio/             # Módulo de Envíos
└── MS_Permisos/          # Módulo de Permisos
```

## 🛡️ Roles y Permisos

### Roles Disponibles

- **ADMIN**: Acceso completo al sistema
- **CLIENTE**: Acceso limitado (compras, perfil)

### Endpoints por Rol

#### Públicos
- `GET /api/productos` - Listar productos
- `GET /api/productos/{id}` - Ver producto
- `POST /api/auth/login` - Iniciar sesión
- `POST /api/auth/register` - Registrarse

#### Solo ADMIN
- `POST /api/productos` - Crear producto
- `PUT /api/productos/{id}` - Actualizar producto
- `DELETE /api/productos/{id}` - Eliminar producto
- `POST /api/productos/{id}/imagenes` - Subir imágenes
- `DELETE /api/productos/{id}/imagenes` - Eliminar imagen
- `PATCH /api/productos/{id}/imagenes/reordenar` - Reordenar imágenes
- `GET /api/usuarios` - Listar usuarios
- `GET /api/ventas` - Ver todas las ventas

#### Solo CLIENTE
- `POST /api/ventas` - Crear venta
- `GET /api/ventas/mis-compras` - Ver mis compras
- `POST /api/envios` - Crear envío
- `GET /api/envios/mis-envios` - Ver mis envíos

## 📁 Gestión de Archivos

### Subida de Imágenes de Productos

```http
POST /api/productos/{id}/imagenes
Content-Type: multipart/form-data

files: [archivo1.jpg, archivo2.png, archivo3.webp]
```

### Configuración de Uploads

```properties
# Tamaño máximo de archivos
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# Directorio de almacenamiento
app.uploads.products-dir=uploads/products
```

## 🧪 Testing

```bash
# Ejecutar todos los tests
mvn test

# Ejecutar tests con cobertura
mvn test jacoco:report
```

## 🔧 Configuraciones Adicionales

### Seed de Datos

El sistema incluye un `CommandLineRunner` que inicializa:

- Permisos básicos (Admin, Cliente)
- Usuario administrador por defecto

Para deshabilitar el seed:

```properties
app.seed.enabled=false
```

### CORS

Configurado para permitir requests desde:

```java
@CrossOrigin(origins = "http://localhost:5173")
```

### Logging

```properties
# Nivel de logging para Spring Security
logging.level.org.springframework.security=DEBUG

# Mostrar SQL queries
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

## 🚨 Solución de Problemas

### Error de Conexión a Base de Datos

1. Verificar que MySQL esté ejecutándose
2. Confirmar credenciales en `application.properties`
3. Verificar que la base de datos `bdshopersa` exista

### Error de Permisos JWT

1. Verificar que el token sea válido
2. Confirmar que el usuario tenga el rol correcto
3. Revisar la configuración de Spring Security

### Error de Subida de Archivos

1. Verificar permisos de escritura en el directorio `uploads/`
2. Confirmar límites de tamaño de archivo
3. Verificar formato de archivo permitido

## 📝 Contribución

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## 📄 Licencia

Este proyecto está bajo la Licencia MIT - ver el archivo [LICENSE](LICENSE) para detalles.

## 👥 Equipo

- **Backend Developer**: [Tu Nombre]
- **Frontend Developer**: [Nombre del Frontend Dev]

## 📞 Soporte

Para soporte técnico, contactar a: `soporte@shopersa.cl`