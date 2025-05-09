package jrc.demo_jwt.Auth;

import org.springframework.security.authentication.AuthenticationManager; // Maneja la autenticación de usuarios
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Representa un token de autenticación basado en nombre de usuario y contraseña
import org.springframework.security.core.userdetails.UserDetails; // Representa los detalles de un usuario
import org.springframework.security.crypto.password.PasswordEncoder; // Proporciona métodos para codificar contraseñas
import org.springframework.stereotype.Service; // Marca esta clase como un servicio de Spring

import jrc.demo_jwt.User.Role; // Enum que define los roles de usuario
import jrc.demo_jwt.User.User; // Clase que representa a un usuario
import jrc.demo_jwt.User.UserRepository; // Repositorio para interactuar con la base de datos de usuarios
import jrc.demo_jwt.jwt.JwtService; // Servicio para manejar operaciones relacionadas con JWT
import lombok.RequiredArgsConstructor; // Genera un constructor para los campos finales requeridos

@Service // Marca esta clase como un servicio gestionado por Spring
@RequiredArgsConstructor // Genera un constructor para inicializar los campos finales
public class AuthService {

    private final UserRepository userRepository; // Repositorio para acceder a los datos de los usuarios
    private final JwtService jwtService; // Servicio para generar y validar tokens JWT
    private final PasswordEncoder passwordEncoder; // Codificador de contraseñas
    private final AuthenticationManager authenticationManager; // Maneja la autenticación de usuarios

    // Método para manejar el inicio de sesión
    public AuthResponse login(LoginRquest request) {
        // Autentica al usuario utilizando el nombre de usuario y la contraseña proporcionados
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Busca al usuario en el repositorio por su nombre de usuario
        UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow();

        // Genera un token JWT para el usuario autenticado
        String token = jwtService.getToken(user);

        // Devuelve una respuesta con el token generado
        return AuthResponse.builder()
            .token(token)
            .build();
    }

    // Método para manejar el registro de nuevos usuarios
    public AuthResponse register(RegisterRequest request) {
        // Crea un nuevo usuario con los datos proporcionados en la solicitud
        User user = User.builder()
            .username(request.getUsername()) // Establece el nombre de usuario
            .password(passwordEncoder.encode(request.getPassword())) // Codifica la contraseña antes de guardarla
            .firstname(request.getFirstname()) // Establece el nombre
            .lastname(request.lastname) // Establece el apellido
            .country(request.getCountry()) // Establece el país
            .role(Role.USER) // Asigna el rol de usuario por defecto
            .build();

        // Guarda el usuario en la base de datos
        userRepository.save(user);

        // Genera un token JWT para el usuario registrado
        return AuthResponse.builder()
            .token(jwtService.getToken(user))
            .build();
    }

}
