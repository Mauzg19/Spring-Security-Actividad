package com.example.demo.controller;

import java.util.Map;



import com.example.demo.dto.UsuarioResponseDTO;
import com.example.demo.model.Usuario;
import com.example.demo.service.UsuarioService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    
    @PostMapping
    public UsuarioResponseDTO crearUsuario(@RequestBody Usuario usuario) {
        return usuarioService.crearUsuario(usuario);
    }

   
    @GetMapping
    public List<Usuario> obtenerTodos() {
        return usuarioService.obtenerTodos();
    }

    
    @GetMapping("/{id}")
    public Usuario obtenerPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id);
    }

    
    @GetMapping("/params")
    public Map<String, String> obtenerPorParametros(
            @RequestParam String nombre,
            @RequestParam String apellido) {

        Map<String, String> response = new HashMap<>();
        response.put("nombreCompleto", nombre + " " + apellido);
        return response;
    }
}

