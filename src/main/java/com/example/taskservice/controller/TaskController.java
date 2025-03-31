package com.example.taskservice.controller;

import com.example.taskservice.model.Task;
import com.example.taskservice.model.TaskStatus;
import com.example.taskservice.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasks")
@PreAuthorize("hasAuthority('USER')") // Solo usuarios autenticados pueden acceder
@CrossOrigin(origins = "http://localhost:4200")
public class TaskController {

    private  TaskService taskService;

    public TaskController(TaskService taskService){
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Optional<Task> task = taskService.getTaskById(id);
        return task.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Task>> getTasksByStatus(@PathVariable String status) {
        try {
            TaskStatus taskStatus = TaskStatus.valueOf(status.toUpperCase()); // Convertir el String a Enum
            List<Task> tasks = taskService.getTasksByStatus(taskStatus);
            return tasks.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(tasks);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // Si el estado no existe, devuelve 400
        }
    }

    @GetMapping("/assigned/{username}")
    public ResponseEntity<List<Task>> getTasksByAssignedUser(@PathVariable String username) {
        try {
            List<Task> tasks = taskService.getTasksByAssignedUser(username);
            if (tasks.isEmpty()) {
                // Puedes devolver 204 No Content si prefieres, o 200 OK con lista vacía
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            System.err.println("Error al buscar tareas por usuario asignado: " + e.getMessage());
            // Considera devolver un error más específico si es necesario
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task) { // Cambia a ResponseEntity<?>
        System.out.println("Datos recibidos en createTask (backend): " + task);
        try {
            Task newTask = taskService.createTask(task);
            System.out.println("Tarea creada: "+ newTask);
            return new ResponseEntity<>(newTask, HttpStatus.CREATED); // 201 Created con la tarea
        } catch (IllegalStateException e) { // Captura la excepción de validación (u otras)
            // Devuelve el mensaje específico de la excepción
            Map<String, String> errorResponse = Map.of("message", e.getMessage());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); // 400 Bad Request
        } catch (Exception e) {
            // Manejo genérico para otros errores inesperados
            System.err.println("Error interno al crear la tarea: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> errorResponse = Map.of("message", "Error interno del servidor al crear la tarea.");
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error
        }
    }
     
     @PostMapping("/{id}/assign")
     public ResponseEntity<?> assignTask(@PathVariable Long id, @RequestParam String username) { // Cambia a ResponseEntity<?>
    	    try {
    	        System.out.println("Recibida solicitud para asignar tarea ID: " + id + " a usuario: " + username); // Log
    	        Task updatedTask = taskService.assignTaskToUser(id, username);
    	        return ResponseEntity.ok(updatedTask); // 200 OK con la tarea actualizada
    	    } catch (IllegalArgumentException e) {
    	        // Maneja 'Tarea no encontrada'
    	        System.err.println("Error al asignar - Tarea no encontrada: " + e.getMessage()); // Log de error
    	        Map<String, String> errorResponse = Map.of("message", e.getMessage());
    	        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND); // 404
    	    } catch (IllegalStateException e) {
    	        // Maneja errores de validación (estado no válido)
    	        System.err.println("Error al asignar - Estado inválido: " + e.getMessage()); // Log de error
    	        Map<String, String> errorResponse = Map.of("message", e.getMessage());
    	        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); // 400 con el mensaje específico
    	    } catch (Exception e) {
    	        // Manejo genérico
    	        System.err.println("Error interno al asignar tarea: " + e.getMessage());
    	        e.printStackTrace();
    	        Map<String, String> errorResponse = Map.of("message", "Error interno del servidor al asignar la tarea.");
    	        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR); // 500
    	    }
    	}
     

     @PutMapping("/{id}")
     public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) { // Cambia el tipo de retorno a ResponseEntity<?>
         try {
             Task updated = taskService.updateTask(id, updatedTask);
             return new ResponseEntity<>(updated, HttpStatus.OK); // Devuelve la tarea actualizada
         } catch (IllegalArgumentException e) {
             // Maneja 'Tarea no encontrada'
             Map<String, String> errorResponse = Map.of("message", e.getMessage()); // Crea un mapa para el mensaje
             return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND); // Devuelve 404 con el mensaje
         } catch (IllegalStateException e) {
             // Maneja errores de validación (como fecha inválida o estado inválido)
             Map<String, String> errorResponse = Map.of("message", e.getMessage()); // ¡Incluye el mensaje de la excepción!
             return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST); // Devuelve 400 con el mensaje
         } catch (Exception e) {
             // Manejo genérico para otros errores inesperados
             System.err.println("Error interno al actualizar la tarea: " + e.getMessage());
             e.printStackTrace();
             Map<String, String> errorResponse = Map.of("message", "Error interno del servidor al actualizar la tarea.");
             return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR); // Devuelve 500
         }
     }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}