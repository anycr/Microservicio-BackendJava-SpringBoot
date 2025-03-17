package com.example.taskservice.model;

import jakarta.persistence.*;

@Entity  // 🔥 Esto marca la clase como una entidad JPA
@Table(name = "users")  // Opcional: Especifica el nombre de la tabla en la BD
public class User {

    @Id  // 🔥 Clave primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Generación automática del ID
    private Long id;  // Asegúrate de tener un ID en tu entidad

    @Column(unique = true, nullable = false)  // Nombre de usuario único y no nulo
    private String username;

    @Column(nullable = false)
    private String password;

    // 🔹 Constructor vacío requerido por JPA
    public User() {
    }

    // 🔹 Constructor con parámetros
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // 🔹 Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
