package com.example.taskservice.controller;

import com.example.taskservice.model.User;  // Importar la clase User
import com.example.taskservice.security.JwtUtil;
import com.example.taskservice.service.UserService;
import org.springframework.http.HttpStatus; // Importa HttpStatus
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder; //Para el 201

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    // Constructor explícito para la inyección de dependencias
    public AuthController(UserService userService, JwtUtil jwtUtil, 
                          AuthenticationManager authenticationManager, 
                          UserDetailsService userDetailsService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) { // Usa ResponseEntity<?>
        User registeredUser = userService.saveUser(user);

        // Construye la URI del nuevo recurso (buena práctica para 201 Created)
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(registeredUser.getId())
            .toUri();

        // Opción 1: Solo el ID
        // return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", registeredUser.getId()));

        // Opción 2: ID y username (más informativo)
          return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", registeredUser.getId(), "username", registeredUser.getUsername()));

        //Con la URI
        //return ResponseEntity.created(location).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody User user) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtUtil.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(Map.of("token", token));
    }
}