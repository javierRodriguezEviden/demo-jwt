package jrc.demo_jwt.Config;

import org.springframework.context.annotation.Bean; // Marca un método como un proveedor de un bean administrado por Spring
import org.springframework.context.annotation.Configuration; // Indica que esta clase contiene configuraciones de Spring
import org.springframework.security.authentication.AuthenticationProvider; // Proveedor de autenticación para manejar la lógica de autenticación
import org.springframework.security.config.annotation.web.builders.HttpSecurity; // Configuración de seguridad web
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; // Habilita la seguridad web en la aplicación
import org.springframework.security.config.http.SessionCreationPolicy; // Configuración de la política de creación de sesiones
import org.springframework.security.web.SecurityFilterChain; // Cadena de filtros de seguridad
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // Filtro de autenticación basado en nombre de usuario y contraseña

import jrc.demo_jwt.jwt.JwtAuthenticationFilter; // Filtro personalizado para manejar la autenticación basada en JWT

import lombok.RequiredArgsConstructor; // Genera un constructor para los campos finales requeridos

@Configuration // Indica que esta clase define configuraciones de Spring
@EnableWebSecurity // Habilita la seguridad web en la aplicación
@RequiredArgsConstructor // Genera un constructor para los campos finales (jwtAuthenticationFilter y authProvider)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter; // Filtro personalizado para manejar JWT
    private final AuthenticationProvider authProvider; // Proveedor de autenticación para manejar la lógica de autenticación

    @Bean // Define un bean administrado por Spring
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        return http
            // Deshabilita la protección CSRF (Cross-Site Request Forgery)
            // Esto es común en aplicaciones RESTful donde no se utilizan cookies para la autenticación
            .csrf(csrf -> csrf.disable())
            
            // Configura las reglas de autorización para las solicitudes HTTP
            .authorizeHttpRequests(authRequest -> 
                authRequest
                    .requestMatchers("/auth/**").permitAll() // Permite el acceso sin autenticación a las rutas que comienzan con "/auth/"
                    .anyRequest().authenticated() // Requiere autenticación para cualquier otra solicitud
            )
            
            // Configura la política de manejo de sesiones
            .sessionManagement(sessionManager -> 
                sessionManager
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Indica que la aplicación no debe mantener estado de sesión
            )
            
            // Configura el proveedor de autenticación
            .authenticationProvider(authProvider)
            
            // Agrega el filtro personalizado para manejar JWT antes del filtro de autenticación predeterminado
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            
            // Construye y devuelve la cadena de filtros de seguridad
            .build();
    }
}