package com.example.demo.dto;
import lombok.*;

@Data

public class UsuarioRequestDTO {
    private String nombre;
    private String apellido;
    private String username;
    private String password;
}