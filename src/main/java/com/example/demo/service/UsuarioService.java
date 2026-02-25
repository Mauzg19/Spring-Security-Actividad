package com.example.demo.service;

import com.example.demo.model.Usuario;
import com.example.demo.dto.UsuarioResponseDTO;
import com.example.demo.dto.UsuarioRequestDTO;
import com.example.demo.repository.UsuarioRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre().toUpperCase());
        usuario.setApellido(dto.getApellido().toUpperCase());
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(dto.getPassword());

        repository.save(usuario);

        return new UsuarioResponseDTO(
                usuario.getNombre(),
                usuario.getApellido()
        );
    }

    public List<Usuario> obtenerTodos() {
        return repository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}
