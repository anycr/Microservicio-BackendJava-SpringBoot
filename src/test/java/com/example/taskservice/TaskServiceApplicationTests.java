package com.example.taskservice;

import com.example.taskservice.model.Task; // Asegúrate de que los imports son correctos
import com.example.taskservice.model.TaskStatus;
import com.example.taskservice.service.TaskService; // Asegúrate de que los imports son correctos
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class TaskServiceApplicationTests {
  @Autowired
  private TaskService taskService;

  @Test
  void testCreateTask() {
    Task task = new Task(); // Crea una instancia de Task
    task.setTitle("Prueba");  //Ahora sí usa los métodos setTitle, setDescription, etc.
    task.setDescription("Descripción");
    task.setStatus(TaskStatus.PENDIENTE);
    task.setCompleted(false);
    task.setDueDate(LocalDateTime.now());
    task.setAssignedTo("correo");

    Task savedTask = taskService.createTask(task); // Guarda la tarea
    assertNotNull(savedTask.getId()); // Verifica que se generó un ID
  }
}