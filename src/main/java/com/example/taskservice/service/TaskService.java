package com.example.taskservice.service;

import com.example.taskservice.model.Task;
import com.example.taskservice.model.TaskStatus;
import com.example.taskservice.repository.TaskRepository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
    	return taskRepository.findAll(Sort.by(Sort.Direction.ASC, "id")); // Ordena por ID ascendente
    }

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task createTask(Task task) {
        // Ensure 'completed' is false if not explicitly set to COMPLETADA
        if (task.getStatus() != TaskStatus.COMPLETADA) {
            task.setCompleted(false);
        }
        task.setStartDate(LocalDateTime.now());

        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask) {
        return taskRepository.findById(id)
                .map(task -> {
                    // ¡Verifica el estado DENTRO del map!
                    if (task.getStatus() == TaskStatus.COMPLETADA || task.getStatus() == TaskStatus.CANCELADA) {
                        throw new IllegalStateException("No se puede actualizar una tarea COMPLETADA o CANCELADA.");
                    }
	                 // Validar startDate
                    if (updatedTask.getStartDate() != null && !Objects.equals(updatedTask.getStartDate(), task.getStartDate())) {
                        throw new IllegalStateException("No se puede modificar el campo startDate.");
                    }
                    //Validar completed
                    if (updatedTask.isCompleted() && !Objects.equals(updatedTask.isCompleted(),task.isCompleted() )) {
                        throw new IllegalStateException("No se puede modificar el campo completed.");
                    }
                    // Actualiza solo los campos NO NULOS:
                    if (updatedTask.getTitle() != null) {
                        task.setTitle(updatedTask.getTitle());
                    }
                    if (updatedTask.getDescription() != null) {
                        task.setDescription(updatedTask.getDescription());
                    }
                    if (updatedTask.getStatus() != null) {
                        task.setStatus(updatedTask.getStatus());
                    }
                    if (updatedTask.getDueDate() != null) {
                        task.setDueDate(updatedTask.getDueDate());
                    }
                    if (updatedTask.getAssignedTo() != null) {
                        task.setAssignedTo(updatedTask.getAssignedTo());
                    }

                    return taskRepository.save(task); // Guarda los cambios
                })
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada con id: " + id)); // Lanza excepción si no se encuentra
    }

    
    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
    
    public Task assignTaskToUser(Long taskId, String username) {
        return taskRepository.findById(taskId)
            .map(task -> {
                task.setAssignedTo(username); // Usar el nombre de usuario en String
                return taskRepository.save(task);
            })
            .orElse(null);
    }
    
    public List<Task> getTasksByStatus(TaskStatus status) {
        return taskRepository.findByStatus(status);
    }


}
