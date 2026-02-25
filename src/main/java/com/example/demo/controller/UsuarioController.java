package com.example.demo.controller;

import java.util.List;

import com.example.demo.dto.UsuarioResponseDTO;
import com.example.demo.model.Usuario;
import com.example.demo.service.UsuarioService;
import com.example.demo.dto.UsuarioRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    // 1. Crear usuario (CON autenticación)
    @PostMapping
    public UsuarioResponseDTO crearUsuario(@RequestBody UsuarioRequestDTO dto) {
        return service.crearUsuario(dto);
    }

    // 2. Obtener todos (SIN autenticación)
    @GetMapping
    public List<Usuario> obtenerTodos() {
        return service.obtenerTodos();
    }

    // 3. Obtener por id (CON autenticación)
    @GetMapping("/{id}")
    public Usuario obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }
}