package jrc.demo_jwt.Auth;

import org.springframework.web.bind.annotation.RequestMapping; // Anotación para mapear rutas a nivel de clase
import org.springframework.web.bind.annotation.RestController; // Marca esta clase como un controlador REST
import lombok.RequiredArgsConstructor; // Genera un constructor para los campos finales requeridos
import org.springframework.http.ResponseEntity; // Representa una respuesta HTTP completa
import org.springframework.web.bind.annotation.PostMapping; // Anotación para mapear solicitudes POST
import org.springframework.web.bind.annotation.RequestBody; // Anotación para vincular el cuerpo de la solicitud a un objeto Java

@RestController // Indica que esta clase es un controlador REST
@RequestMapping("/auth") // Define la ruta base para todos los endpoints de este controlador
@RequiredArgsConstructor // Genera un constructor para inicializar los campos finales (authService)
public class AuthController {

    private final AuthService authService; // Servicio que contiene la lógica de autenticación y registro

    @PostMapping(value = "login") // Mapea solicitudes POST a "/auth/login"
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRquest request) {
        // Llama al servicio de autenticación para manejar el inicio de sesión
        // Devuelve una respuesta HTTP con el resultado del inicio de sesión
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping(value = "register") // Mapea solicitudes POST a "/auth/register"
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        // Llama al servicio de autenticación para manejar el registro
        // Devuelve una respuesta HTTP con el resultado del registro
        return ResponseEntity.ok(authService.register(request));
    }

}
