# 📚 Documentación Completa - Aplicación Spring Boot de Gestión de Usuarios

## 📋 Índice
1. [Estructura del Proyecto](#estructura)
2. [Clase Principal](#clase-principal)
3. [Configuración de Seguridad](#configuración-seguridad)
4. [Controladores](#controladores)
5. [Servicios](#servicios)
6. [Repositorios](#repositorios)
7. [Modelos y DTOs](#modelos-dtos)
8. [Configuración](#configuración)

---

## 🏗️ Estructura del Proyecto<a id="estructura"></a>

```
src/main/
├── java/com/example/demo/
│   ├── DemoApplication.java (Clase principal)
│   ├── config/
│   │   └── SecurityConfig.java (Configuración de seguridad)
│   ├── controller/
│   │   ├── UsuarioController.java (Endpoints de usuarios)
│   │   └── ParamsController.java (Endpoint de parámetros)
│   ├── service/
│   │   └── UsuarioService.java (Lógica de negocio)
│   ├── repository/
│   │   └── UsuarioRepository.java (Acceso a la base de datos)
│   ├── model/
│   │   └── Usuario.java (Entidad de BD)
│   └── dto/
│       ├── UsuarioRequestDTO.java (Datos de entrada)
│       └── UsuarioResponseDTO.java (Datos de salida)
└── resources/
    └── application.properties (Configuración de la aplicación)
```

---

## 🚀 Clase Principal<a id="clase-principal"></a>

### `DemoApplication.java`

```java
package com.example.demo;
```
**Línea 1:** Define el paquete raíz de la aplicación. Todos los componentes del proyecto están bajo este paquete principal.

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
```
**Líneas 3-4:** Importa las clases necesarias de Spring Boot para ejecutar la aplicación.

```java
@SpringBootApplication
```
**Línea 6:** Anotación de Spring Boot que combina:
- `@Configuration`: Marca la clase como configuración
- `@EnableAutoConfiguration`: Habilita la configuración automática
- `@ComponentScan`: Escanea componentes en el paquete y subpaquetes

```java
public class DemoApplication {
```
**Línea 7:** Define la clase principal de la aplicación.

```java
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
```
**Línea 9-11:** 
- `main()` es el punto de entrada de la aplicación
- `SpringApplication.run()` inicia el servidor Spring Boot
- `DemoApplication.class` indica que esta es la clase de configuración principal
- `args` son los argumentos de línea de comandos

---

## 🔐 Configuración de Seguridad<a id="configuración-seguridad"></a>

### `SecurityConfig.java`

```java
package com.example.demo.config;
```
**Línea 1:** Paquete para componentes de configuración.

```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
```
**Líneas 3-5:** Importan anotaciones y clases para configurar Spring Security.

```java
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
```
**Líneas 7-9:** Importan clases para definir reglas de autorización HTTP.

```java
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
```
**Líneas 11-12:** Importan clases para gestionar usuarios en memoria.

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
```
**Líneas 14-16:** 
- `@Configuration`: Marca la clase como configuración de Spring
- `@EnableWebSecurity`: Activa la seguridad web de Spring Security
- La clase define todas las reglas de autenticación y autorización

```java
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
```
**Línea 18-19:**
- `@Bean`: Indica que este método retorna un objeto que debe ser gestionado por Spring
- `SecurityFilterChain` es la cadena de filtros que valida las peticiones HTTP

```java
        http
            .csrf(csrf -> csrf.disable())
```
**Línea 22:**
- `csrf()` accede a la configuración CSRF (Cross-Site Request Forgery)
- `.disable()` desactiva la protección CSRF (necesario para API REST sin sesiones)
- La función lambda `csrf -> csrf.disable()` es la sintaxis moderna de Spring Security 6

```java
            .authorizeHttpRequests(auth -> auth
```
**Línea 23:** Inicia la configuración de autorización de peticiones HTTP.

```java
                .requestMatchers(HttpMethod.GET, "/api/usuarios").permitAll()
```
**Línea 25:**
- `requestMatchers()` especifica qué peticiones cumplan la regla
- `HttpMethod.GET` especifica que solo GET
- `"/api/usuarios"` especifica la ruta exacta
- `.permitAll()` permite acceso sin autenticación

```java
                .requestMatchers(HttpMethod.GET, "/api/params").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
```
**Líneas 26-27:** Permiten acceso sin autenticación a params y a la consola H2.

```java
                .requestMatchers(HttpMethod.POST, "/api/usuarios").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/usuarios/**").authenticated()
```
**Líneas 29-30:**
- `authenticated()` requiere que el usuario esté autenticado
- POST a `/api/usuarios` (crear) requiere autenticación
- GET a `/api/usuarios/**` (obtener por ID) requiere autenticación
- `/**` es un patrón que acepta cualquier ruta bajo `/api/usuarios/`

```java
                .anyRequest().authenticated()
            )
```
**Línea 32:** Cualquier otra petición requiere autenticación.

```java
            .httpBasic(basic -> {})
```
**Línea 33:** Activa la autenticación HTTP Basic (usuario:contraseña en Base64).

```java
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));
```
**Línea 34:** Desactiva restricción de frameOptions para permitir H2 console en iframe. Necesario para acceder a `/h2-console`.

```java
        return http.build();
    }
```
**Línea 36-37:** Construye y retorna la cadena de filtros de seguridad.

```java
    @Bean
    public UserDetailsService users() {
```
**Línea 39-40:** Define el servicio que gestiona los detalles del usuario.

```java
        UserDetails user = User
                .withUsername("admin")
                .password("{noop}1234")
                .roles("USER")
                .build();
```
**Líneas 41-45:**
- `User.withUsername("admin")`: Crea usuario con nombre "admin"
- `.password("{noop}1234")`: Define contraseña "1234". `{noop}` significa sin encriptación (solo para desarrollo)
- `.roles("USER")`: Asigna rol "USER" al usuario
- `.build()`: Construye el objeto UserDetails

```java
        return new InMemoryUserDetailsManager(user);
    }
}
```
**Línea 46-47:** Crea un gestor en memoria que almacena el usuario. En desarrollo es suficiente; en producción usaría BD.

---

## 🎮 Controladores<a id="controladores"></a>

### `UsuarioController.java`

```java
package com.example.demo.controller;

import java.util.List;
import com.example.demo.dto.UsuarioResponseDTO;
import com.example.demo.model.Usuario;
import com.example.demo.service.UsuarioService;
import com.example.demo.dto.UsuarioRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
```
**Líneas 1-9:** Importaciones necesarias para el controlador y sus dependencias.

```java
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
```
**Líneas 11-13:**
- `@RestController`: Marca la clase como controlador REST que retorna JSON automáticamente
- `@RequestMapping("/api/usuarios")`: Establece la ruta base para todos los métodos
- La clase contiene los endpoints que manejan las peticiones de usuarios

```java
    @Autowired
    private UsuarioService service;
```
**Líneas 15-16:**
- `@Autowired`: Spring inyecta automáticamente una instancia de UsuarioService
- `service` es el objeto que accede a la lógica de negocio
- Esto logra desacoplamiento: el controlador no crea el servicio, lo recibe

```java
    @PostMapping
    public UsuarioResponseDTO crearUsuario(@RequestBody UsuarioRequestDTO dto) {
        return service.crearUsuario(dto);
    }
```
**Líneas 18-21:**
- `@PostMapping`: Mapea peticiones POST a esta dirección
- `@RequestBody` extrae el JSON del cuerpo de la petición y lo convierte a `UsuarioRequestDTO`
- `crearUsuario()` llama al servicio para guardar el usuario
- Retorna solo nombre y apellido en mayúsculas (`UsuarioResponseDTO`)
- **Requisito:** Protegido por autenticación (configurado en SecurityConfig)

```java
    @GetMapping
    public List<Usuario> obtenerTodos() {
        return service.obtenerTodos();
    }
```
**Líneas 24-27:**
- `@GetMapping`: Mapea peticiones GET a `/api/usuarios`
- Retorna lista de todos los usuarios
- **Requisito:** Acceso sin autenticación (configurado en SecurityConfig)

```java
    @GetMapping("/{id}")
    public Usuario obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }
```
**Líneas 30-33:**
- `@GetMapping("/{id}")`: La ruta es `/api/usuarios/{id}` (ejemplo: `/api/usuarios/1`)
- `@PathVariable Long id`: Extrae el parámetro `id` de la URL y lo convierte a Long
- **Requisito:** Requiere autenticación (configurado en SecurityConfig)

---

### `ParamsController.java`

```java
package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.*;
```
**Líneas 1-5:** Importaciones para el controlador de parámetros.

```java
@RestController
@RequestMapping("/api")
public class ParamsController {
```
**Líneas 7-9:** Define controlador REST con ruta base `/api`.

```java
    @GetMapping("/params")
    public Map<String, String> obtenerNombreCompleto(
            @RequestParam String nombre,
            @RequestParam String apellido) {
```
**Líneas 11-14:**
- `@GetMapping("/params")`: Mapea GET a `/api/params`
- `@RequestParam`: Extrae parámetros de la URL (ejemplo: `?nombre=Juan&apellido=Pérez`)
- El método recibe dos parámetros de la URL como Strings

```java
        Map<String, String> response = new HashMap<>();
        response.put("nombreCompleto", nombre + " " + apellido);
        return response;
    }
}
```
**Líneas 15-18:**
- `HashMap` es un diccionario clave-valor
- `.put()` agrega la clave "nombreCompleto" con valor nombre + espacio + apellido
- Retorna JSON: `{"nombreCompleto":"Juan Pérez"}`
- **Requisito:** Acceso sin autenticación

---

## 🔧 Servicios<a id="servicios"></a>

### `UsuarioService.java`

```java
package com.example.demo.service;

import com.example.demo.model.Usuario;
import com.example.demo.dto.UsuarioResponseDTO;
import com.example.demo.dto.UsuarioRequestDTO;
import com.example.demo.repository.UsuarioRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
```
**Líneas 1-9:** Importaciones necesarias para el servicio.

```java
@Service
public class UsuarioService {
```
**Líneas 11-12:**
- `@Service`: Marca la clase como servicio (componente de lógica de negocio)
- Spring la detecta automáticamente y la registra como bean
- La separa de controladores y repositorios

```java
    @Autowired
    private UsuarioRepository repository;
```
**Líneas 14-15:** Spring inyecta automáticamente el repositorio para acceder a la BD.

```java
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO dto) {
```
**Línea 17:** Método que crea un nuevo usuario. Recibe DTO con datos de entrada.

```java
        Usuario usuario = new Usuario();
```
**Línea 18:** Crea un objeto Usuario vacío (entidad de BD).

```java
        usuario.setNombre(dto.getNombre().toUpperCase());
        usuario.setApellido(dto.getApellido().toUpperCase());
```
**Líneas 19-20:**
- Lee nombre y apellido del DTO
- `.toUpperCase()` convierte a mayúsculas (REQUISITO)
- Los asigna al objeto usuario

```java
        usuario.setUsername(dto.getUsername());
        usuario.setPassword(dto.getPassword());
```
**Líneas 21-22:** Asigna username y password tal como vienen (sin modificar).

```java
        repository.save(usuario);
```
**Línea 24:** Guarda el usuario en la BD. El ID se genera automáticamente.

```java
        return new UsuarioResponseDTO(
                usuario.getNombre(),
                usuario.getApellido()
        );
```
**Líneas 26-29:**
- Crea un DTO de respuesta con solo nombre y apellido
- NO incluye username ni password (seguridad)
- Los datos están en mayúsculas (ya se pusieron así arriba)

```java
    public List<Usuario> obtenerTodos() {
        return repository.findAll();
    }
```
**Líneas 32-34:**
- Método simple que obtiene todos los usuarios de la BD
- `findAll()` es método heredado de JpaRepository

```java
    public Usuario obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
```
**Líneas 37-39:**
- Obtiene usuario por ID
- `.findById()` retorna Optional (puede o no existir)
- `.orElseThrow()` lanza excepción si no existe
- El mensaje de error es "Usuario no encontrado"

---

## 💾 Repositorios<a id="repositorios"></a>

### `UsuarioRepository.java`

```java
package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Usuario;
import java.util.Optional;
```
**Líneas 1-5:** Importaciones para el repositorio.

```java
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
```
**Línea 7:** 
- `interface`: Define una interfaz (contrato sin implementación)
- `JpaRepository<Usuario, Long>`: Hereda de JpaRepository. Proporciona automáticamente:
  - `save()`: Guardar entidad
  - `findAll()`: Obtener todas
  - `findById()`: Obtener por ID
  - `delete()`: Eliminar
  - Y muchas más...
- `<Usuario, Long>`: Trabaja con entidad Usuario, las claves primarias son Long

```java
    Optional<Usuario> findByUsername(String username);
```
**Línea 8:**
- Método personalizado (Spring genera automáticamente la implementación)
- `Optional`: Devuelve Usuario si existe, o vacío si no
- Busca usuario por su username único
- La nomenclatura `findByUsername` sigue una convención que Spring entiende

---

## 📦 Modelos y DTOs<a id="modelos-dtos"></a>

### `Usuario.java` (Modelo)

```java
package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;
```
**Líneas 1-4:**
- `jakarta.persistence.*`: Importa anotaciones JPA para mapear a BD
- `lombok.*`: Importa anotaciones de Lombok que generan código automáticamente

```java
@Entity
@Table(name = "usuarios")
public class Usuario {
```
**Líneas 6-8:**
- `@Entity`: Marca que esta clase es una entidad de BD (tabla)
- `@Table(name = "usuarios")`: Especifica que la tabla en BD se llama "usuarios"

```java
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
```
**Líneas 10-12:**
- `@Id`: Marca como clave primaria
- `@GeneratedValue`: El ID se genera automáticamente
- `IDENTITY`: Usa autoincremento de la BD (1, 2, 3, ...)
- El tipo es `Long` para números grandes

```java
    private String nombre;
    private String apellido;
```
**Líneas 14-15:** Campos simples que se mapean a columnas de BD automáticamente.

```java
    @Column(name = "username")
    private String username;
```
**Líneas 17-18:**
- `@Column(name = "username")`: Especifica que la columna en BD se llama "username"
- Evita usar "user" que es palabra reservada en H2

```java
    private String password;
```
**Línea 19:** Campo para la contraseña.

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
```
**Línas en la clase (reales 6):** Anotaciones de Lombok que generan automáticamente:
- `@Data`: getter, setter, toString, equals, hashCode
- `@NoArgsConstructor`: constructor sin parámetros
- `@AllArgsConstructor`: constructor con todos los parámetros

---

### `UsuarioRequestDTO.java` (DTO de Entrada)

```java
package com.example.demo.dto;
import lombok.*;

@Data
public class UsuarioRequestDTO {
    private String nombre;
    private String apellido;
    private String username;
    private String password;
}
```
**Explicación:**
- DTO = Data Transfer Object (transporta datos entre capas)
- `@Data`: Genera getter, setter automáticamente
- Representa los datos que envía el cliente en la petición POST
- Spring automáticamente convierte JSON a este objeto

**Ejemplo JSON que recibe:**
```json
{
    "nombre": "Juan",
    "apellido": "Pérez",
    "username": "juan123",
    "password": "pass123"
}
```

---

### `UsuarioResponseDTO.java` (DTO de Respuesta)

```java
package com.example.demo.dto;
import lombok.*;

@Data
@AllArgsConstructor
public class UsuarioResponseDTO {
    private String nombre;
    private String apellido;
}
```
**Explicación:**
- DTO para la respuesta que envía el servidor
- Solo contiene nombre y apellido (no incluye username ni password por seguridad)
- `@AllArgsConstructor`: Constructor necesario para crear el objeto en el servicio
- Los datos están en mayúsculas

**Ejemplo JSON que retorna:**
```json
{
    "nombre": "JUAN",
    "apellido": "PÉREZ"
}
```

---

## ⚙️ Configuración<a id="configuración"></a>

### `application.properties`

```properties
server.port=9000
```
**Línea 1:** El servidor corre en puerto 9000 (REQUISITO).

```properties
spring.datasource.url=jdbc:h2:mem:testdb
```
**Línea 3:** URL de conexión a H2. `mem:testdb` significa BD en memoria llamada "testdb".

```properties
spring.datasource.driver-class-name=org.h2.Driver
```
**Línea 4:** Especifica el driver JDBC de H2.

```properties
spring.datasource.username=sa
spring.datasource.password=
```
**Líneas 5-6:** Credenciales de BD. Usuario "sa" (System Administrator), sin contraseña.

```properties
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```
**Línea 8:** Especifica a Hibernate que use el dialecto SQL de H2.

```properties
spring.jpa.hibernate.ddl-auto=update
```
**Línea 9:** `update` crea/actualiza tablas automáticamente al iniciar. Otras opciones:
- `create`: Crea tablas siempre (borra datos previos)
- `validate`: Solo valida estructura
- `none`: No hace nada

```properties
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```
**Líneas 11-12:** 
- Habilita la consola web de H2
- Accesible en `http://localhost:9000/h2-console`
- Útil para inspeccionar BD durante desarrollo

---

## 🔄 Flujo de una Petición POST (Crear Usuario)

```
1. Cliente envía: POST /api/usuarios
   {
       "nombre": "juan",
       "apellido": "pérez",
       "username": "juan123",
       "password": "pass123"
   }

2. Spring Security valida autenticación (usuario: admin, pass: 1234)

3. UsuarioController.crearUsuario():
   - Recibe UsuarioRequestDTO

4. UsuarioService.crearUsuario():
   - Crea objeto Usuario
   - Convierte nombre y apellido a MAYÚSCULAS
   - Asigna username y password sin cambios
   - Guarda en BD mediante repository.save()

5. BD genera ID automáticamente (ejemplo: 1)

6. Servicio retorna UsuarioResponseDTO con:
   {
       "nombre": "JUAN",
       "apellido": "PÉREZ"
   }

7. Spring convierte a JSON y retorna al cliente
```

---

## 📡 Ejemplo de Uso con CURL

```bash
# 1. Crear usuario (requiere autenticación)
curl -X POST http://localhost:9000/api/usuarios \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic YWRtaW46MTIzNA==" \
  -d '{
    "nombre": "Juan",
    "apellido": "Pérez",
    "username": "juan123",
    "password": "pass123"
  }'

# 2. Obtener todos (sin autenticación)
curl http://localhost:9000/api/usuarios

# 3. Obtener por ID (requiere autenticación)
curl -H "Authorization: Basic YWRtaW46MTIzNA==" \
  http://localhost:9000/api/usuarios/1

# 4. Parámetros (sin autenticación)
curl "http://localhost:9000/api/params?nombre=Juan&apellido=Pérez"
```

---

## 🏗️ Arquitectura de Capas

```
┌─────────────────────────────┐
│    REST Controller          │ (UsuarioController, ParamsController)
│  ↓ Maneja peticiones HTTP   │
├─────────────────────────────┤
│    Service                  │ (UsuarioService)
│  ↓ Lógica de negocio        │
├─────────────────────────────┤
│    Repository               │ (UsuarioRepository)
│  ↓ Acceso a datos           │
├─────────────────────────────┤
│    Base de Datos            │ (H2 en memoria)
└─────────────────────────────┘
```

**Beneficios:**
- Separación de responsabilidades
- Fácil de testear (cada capa por separado)
- Código mantenible y escalable
- Desacoplamiento entre capas

---

**Fin de la Documentación**
