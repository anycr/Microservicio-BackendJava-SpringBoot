package com.example.taskservice.repository;

import com.example.taskservice.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List; 

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

	List<Task> findAllByOrderByIdAsc(); // Método para ordenar por ID ascendente
    // (Opcional) Puedes añadir métodos personalizados aquí, si los necesitas.
    // Spring Data JPA generará la implementación automáticamente.
}