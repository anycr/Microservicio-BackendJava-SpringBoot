package com.example.taskservice.controller;

import com.example.taskservice.model.Task;
import com.example.taskservice.model.TaskStatus;
import com.example.taskservice.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

     @PostMapping
     public ResponseEntity<Task> createTask(@RequestBody Task task){
        Task newTask = taskService.createTask(task);
        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
     }
     
     @PostMapping("/{id}/assign")
     public ResponseEntity<Task> assignTask(@PathVariable Long id, @RequestParam String username) {
         Task updatedTask = taskService.assignTaskToUser(id, username);
         return updatedTask != null ? ResponseEntity.ok(updatedTask) : ResponseEntity.notFound().build();
     }
     

     @PutMapping("/{id}")
     public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task updatedTask) { //Devuelve un objeto Task, no String
         try {
             Task updated = taskService.updateTask(id, updatedTask);
             return new ResponseEntity<>(updated, HttpStatus.OK); //Devuelve la tarea
         } catch (IllegalArgumentException e) {
             return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); //Devuelve 404 si no la encuentra
         } catch (IllegalStateException e) {
             return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST); //Devuelve 400 si no se puede modificar.
         }
     }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}