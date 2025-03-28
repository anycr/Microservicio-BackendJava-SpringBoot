package com.example.taskservice.service;

import com.example.taskservice.model.Task;
import com.example.taskservice.model.TaskStatus;
import com.example.taskservice.repository.TaskRepository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
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
        
        task.setStartDate(LocalDateTime.now());
        
        // Validar que dueDate no sea antes que startDate
        if (task.getDueDate() != null && task.getStartDate() != null &&
                task.getDueDate().isBefore(task.getStartDate())) { // Compara LocalDate/LocalDateTime
                throw new IllegalStateException("La fecha de vencimiento no puede ser anterior a la fecha de inicio.");
            }
        
        // Si no es COMPLETADA, asegurar que completed sea false
        if (task.getStatus() != TaskStatus.COMPLETADA) {
            task.setCompleted(false);
        }

        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask) {
        return taskRepository.findById(id)
                .map(task -> {
                    // Si la tarea ya está COMPLETADA o CANCELADA, no se puede modificar
                    if (task.getStatus() == TaskStatus.COMPLETADA || task.getStatus() == TaskStatus.CANCELADA) {
                        throw new IllegalStateException("No se puede actualizar una tarea COMPLETADA o CANCELADA.");
                    }

                    // Validar que no se modifique el campo startDate
                    if (updatedTask.getStartDate() != null && 
                        !Objects.equals(updatedTask.getStartDate(), task.getStartDate())) {
                        throw new IllegalStateException("No se puede modificar el campo startDate.");
                    }

                    // Validar que no se intente modificar completed manualmente
                    if (task.getStatus() != TaskStatus.COMPLETADA && task.isCompleted() ) {
                        throw new IllegalStateException("No se puede modificar el campo completed manualmente.");
                    }
                    
                    // Validar que dueDate no sea antes que startDate
                    if (updatedTask.getDueDate() != null && 
                            updatedTask.getDueDate().isBefore(task.getStartDate())) {
                            throw new IllegalArgumentException("La fecha de vencimiento no puede ser anterior a la fecha de inicio.");
                        }

                    // Actualiza solo los campos NO NULOS
                    if (updatedTask.getTitle() != null) {
                        task.setTitle(updatedTask.getTitle());
                    }
                    if (updatedTask.getDescription() != null) {
                        task.setDescription(updatedTask.getDescription());
                    }
                    if (updatedTask.getStatus() != null) {
                        task.setStatus(updatedTask.getStatus());

                        // Si el estado es COMPLETADA, marcar completed = true automáticamente
                        if (updatedTask.getStatus() == TaskStatus.COMPLETADA) {
                            task.setCompleted(true);
                        }
                    }
                    if (updatedTask.getDueDate() != null) {
                        task.setDueDate(updatedTask.getDueDate());
                    }
                    if (updatedTask.getAssignedTo() != null) {
                        task.setAssignedTo(updatedTask.getAssignedTo());
                    }

                    return taskRepository.save(task); // Guarda los cambios
                })
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada con id: " + id));
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

    // Verifica y cancela tareas cada hora
    @Scheduled(cron = "0 0 * * * *")
    public void checkForExpiredTasks() {
        List<Task> tasks = taskRepository.findAll();

        tasks.forEach(task -> {
            if (task.getDueDate() != null && 
                task.getDueDate().toLocalDate().isBefore(LocalDateTime.now().toLocalDate()) && 
                task.getStatus() != TaskStatus.CANCELADA) {

                task.setStatus(TaskStatus.CANCELADA);
                taskRepository.save(task);
                System.out.println("\u23F0 Tarea " + task.getId() + " fue cancelada autom\u00e1ticamente.");
            }
        });
    }

}
