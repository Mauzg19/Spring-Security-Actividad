package com.example.demo.service;

import com.example.demo.model.Usuario;
import com.example.demo.dto.UsuarioResponseDTO;
import com.example.demo.repository.UsuarioRepository;

import java.util.List;
import java.util.stream.Collector;

import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioResponseDTO crearUsuario(Usuario usuario) {
        Usuario guardado = usuarioRepository.save(usuario);

        return new UsuarioResponseDTO(
            guardado.getNombre().toUpperCase(),
            guardado.getApellido().toUpperCase()

        );
    }

    public List<Usuario> obtenerTodos(){
        return usuarioRepository.findAll();
    }

    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow();
    }
}
