package com.example.taskservice.service;

import com.example.taskservice.model.Task;
import com.example.taskservice.model.TaskStatus;
import com.example.taskservice.repository.TaskRepository;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
        task.setDueDate(LocalDateTime.now());

        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask) {
        return taskRepository.findById(id).map(task -> {
            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setStatus(updatedTask.getStatus());

            // Si la tarea se marca como COMPLETADA, aseguramos que completed sea true
            if (updatedTask.getStatus() == TaskStatus.COMPLETADA) {
                task.setCompleted(true);
            }

            return taskRepository.save(task);
        }).orElse(null);
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
