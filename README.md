# Gestión de Tareas con Spring Boot:
Este microservicio de gestión de tareas con Spring Boot y PostgreSQL proporciona una API REST funcional que permite manejar tareas fácilmente. Puedes extenderlo con autenticación, mensajería y más.

### 📌 ️ Tecnologías Utilizadas:
```
Java 21
Spring Boot 3
Spring Data JPA (para la interacción con PostgreSQL)
Spring Web (para la API REST)
Docker (para contenerizar la aplicación y la base de datos)
JWT  para autenticación
```

### 📌 Estructura del Proyecto:
```
📂 task-service
┣ 📂 src/main/java/com/example/taskservice
┃ ┣ 📂 config (Configuraciones generales)
┃ ┃ ┗ CorsConfig.java (Configuración de CORS)
┃ ┣ 📂 controller (Controladores)
┃ ┃ ┣ AuthController.java (Controlador para sutenticación)
┃ ┃ ┗ TaskController.java (Controlador REST)
┃ ┣ 📂 model (Modelos de entidad)
┃ ┃ ┣ Task.java (Modelo de Datos de tareas)
┃ ┃ ┣ TaskStatus.java (Enumeración para el estado de la tarea)
┃ ┃ ┗ User.java (Modelo de Datos de los usuarios)
┃ ┣ 📂 repository (Repositorios JPA)
┃ ┃ ┣ TaskRepository.java (Repositorios para Tareas)
┃ ┃ ┗ UserRepository.java (Repositorios para Usuarios)
┃ ┣ 📂 security (Configuración de seguridad)
┃ ┃ ┣ CustomUserDetailsService.java (Autenticar usuarios desde la base de datos)
┃ ┃ ┣ JwtAuthenticationFilter.java (Filtro para extraer el token JWT de la solicitud HTTP, lo valida y lo usa para autenticar al usuario)
┃ ┃ ┣ JwtUtil.java (Manejo de tokens JWT)
┃ ┃ ┗ SecurityConfig.java (Configuración de Spring Security)
┃ ┣ 📂 service (Lógica de negocio)
┃ ┃ ┣ TaskService.java (Servicio para manejar las tareas)
┃ ┃ ┗ UserService.java (Servicio para manejar usuarios)
┃ ┣ 📂 utils (Java independiente para generar una clave segura en Base64)
┃ ┃ ┗ KeyGeneratorUtil.java
┃ ┗ TaskServiceApplication.java (Clase principal de Spring Boot)
┣ 📄 application.properties
┣ 📄 Dockerfile
┣ 📄 docker-compose.yml
┣ 📄 pom.xml
```

## Funcionalidades del Microservicio
### 📌 Endpoints principales (REST API)
```
Método	 Endpoint		      Descripción
POST	/auth/register	              Registrar un usuario en la base de datos
POST	/auth/login		      Iniciar sesión para obtener el token JWT
POST	/api/tasks/		      Crear una nueva tarea
POST	/api/tasks/{id}/assign	      Asignar una tarea a un usuario
GET	/api/tasks/		      Obtener todas las tareas
GET	/api/tasks/{id}	              Obtener una tarea por ID
GET	/api/tasks/assigned/{user}    Obtener las tareas por usuarios asignados
GET	/api/tasks/status/{status}    Consultar tareas por Status
PUT	/api/tasks/{id}	              Actualizar una tarea
DELETE	/api/tasks/{id}		      Eliminar una tarea
```
### 📌 Modelo de Datos (Ejemplo en JSON)
Crear tarea
```
{
  "title": "Tarea de prueba",
  "description": "Descripción de la tarea de prueba",
  "status": "PENDIENTE",
  "dueDate": "2025-03-28T12:30:00",
  "assignedTo": "user_123"
}
```
## Desplegar con Docker
### 📌 Comandos para ejecutar:
✅ 1- Descargar repositorio
✅ 2- Abre la terminal y ve a la carpeta del proyecto:
```
cd /ruta/del/proyecto
```
✅ 3- Limpien and construye el projecto (Maven)
```
mvn clean install
```
✅ 4- Construye la imagen Docker
```
docker-compose up --build
```
✅ 5- Inicie el contenedor:
```
docker-compose up --build #Iniciar los contenedores
docker-compose up -d --build #Segundo plano
```
