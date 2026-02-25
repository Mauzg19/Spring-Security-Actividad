package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ParamsController {

    @GetMapping("/params")
    public Map<String, String> obtenerNombreCompleto(
            @RequestParam String nombre,
            @RequestParam String apellido) {

        Map<String, String> response = new HashMap<>();
        response.put("nombreCompleto", nombre + " " + apellido);
        return response;
    }
}
