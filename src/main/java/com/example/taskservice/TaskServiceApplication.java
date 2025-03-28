package com.example.taskservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.example.taskservice") // 🔥 Escanea todos los paquetes del proyecto
@EnableJpaRepositories("com.example.taskservice.repository") // 🔥 Activa los repositorios JPA
@EntityScan("com.example.taskservice.model")  // 🔥 Escanea las entidades en el paquete model
@EnableScheduling
public class TaskServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaskServiceApplication.class, args);
    }
}
