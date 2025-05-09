package jrc.demo_jwt.Config;

import org.springframework.context.annotation.Bean; // Marca un método como proveedor de un bean gestionado por Spring
import org.springframework.context.annotation.Configuration; // Indica que esta clase contiene configuraciones de Spring
import org.springframework.security.authentication.AuthenticationManager; // Maneja la autenticación de usuarios
import org.springframework.security.authentication.AuthenticationProvider; // Proveedor de autenticación para manejar la lógica de autenticación
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // Proveedor de autenticación basado en DAO
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration; // Configuración de autenticación
import org.springframework.security.core.userdetails.UserDetailsService; // Servicio para cargar detalles de usuario
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Excepción lanzada cuando no se encuentra un usuario
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // Implementación de codificación de contraseñas con BCrypt
import org.springframework.security.crypto.password.PasswordEncoder; // Interfaz para codificar contraseñas

import jrc.demo_jwt.User.UserRepository; // Repositorio para interactuar con la base de datos de usuarios
import lombok.RequiredArgsConstructor; // Genera un constructor para los campos finales requeridos

@Configuration // Marca esta clase como una clase de configuración de Spring
@RequiredArgsConstructor // Genera un constructor para inicializar los campos finales
public class ApplicationConfig {

    private final UserRepository userRepository; // Repositorio para acceder a los datos de los usuarios

    @Bean // Define un bean gestionado por Spring
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        // Proporciona el AuthenticationManager desde la configuración de autenticación
        return config.getAuthenticationManager();
    }

    @Bean // Define un bean gestionado por Spring
    public AuthenticationProvider authenticationProvider() {
        // Configura un proveedor de autenticación basado en DAO
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailService()); // Establece el servicio de detalles de usuario
        authenticationProvider.setPasswordEncoder(passwordEncoder()); // Establece el codificador de contraseñas
        return authenticationProvider;
    }

    @Bean // Define un bean gestionado por Spring
    public PasswordEncoder passwordEncoder() {
        // Proporciona una implementación de codificación de contraseñas con BCrypt
        return new BCryptPasswordEncoder();
    }

    @Bean // Define un bean gestionado por Spring
    public UserDetailsService userDetailService() {
        // Proporciona un servicio para cargar detalles de usuario desde el repositorio
        return username -> userRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found")); // Lanza una excepción si el usuario no existe
    }
}
