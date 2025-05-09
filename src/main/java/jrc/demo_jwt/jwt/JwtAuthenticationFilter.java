package jrc.demo_jwt.jwt;

import java.io.IOException;
/* import java.net.http.HttpHeaders; */
import org.springframework.http.HttpHeaders; // Clase para manejar encabezados HTTP
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken; // Representa un token de autenticación basado en nombre de usuario y contraseña
import org.springframework.security.core.context.SecurityContextHolder; // Contexto de seguridad para almacenar información de autenticación
import org.springframework.security.core.userdetails.UserDetails; // Representa los detalles de un usuario
import org.springframework.security.core.userdetails.UserDetailsService; // Servicio para cargar detalles de usuario
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource; // Fuente para construir detalles de autenticación web
import org.springframework.stereotype.Component; // Marca esta clase como un componente de Spring
import org.springframework.util.StringUtils; // Utilidad para trabajar con cadenas
import org.springframework.web.filter.OncePerRequestFilter; // Filtro que se ejecuta una vez por solicitud

import jakarta.servlet.FilterChain; // Representa la cadena de filtros
import jakarta.servlet.ServletException; // Excepción lanzada por un servlet
import jakarta.servlet.http.HttpServletRequest; // Representa una solicitud HTTP
import jakarta.servlet.http.HttpServletResponse; // Representa una respuesta HTTP
import lombok.RequiredArgsConstructor; // Genera un constructor con los campos finales requeridos

@Component // Marca esta clase como un componente gestionado por Spring
@RequiredArgsConstructor // Genera un constructor para los campos finales (jwtService y userDetailsService)
public class JwtAuthenticationFilter extends OncePerRequestFilter { // Extiende OncePerRequestFilter para crear un filtro personalizado

    private final JwtService jwtService; // Servicio para manejar operaciones relacionadas con JWT
    private final UserDetailsService userDetailsService; // Servicio para cargar detalles de usuario

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extrae el token JWT de la solicitud
        final String token = getTokenFromRequest(request);
        final String username;

        // Si no hay token, continúa con el siguiente filtro
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Obtiene el nombre de usuario del token
        username = jwtService.getUsernameFromToken(token);

        // Si el nombre de usuario no es nulo y no hay autenticación en el contexto de seguridad
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Carga los detalles del usuario desde el servicio
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Verifica si el token es válido
            if (jwtService.isTokenValid(token, userDetails)) {
                // Crea un token de autenticación basado en los detalles del usuario
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                // Establece detalles adicionales de autenticación
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Establece el token de autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continúa con el siguiente filtro en la cadena
        filterChain.doFilter(request, response);
    }

    // Método para extraer el token JWT del encabezado de autorización de la solicitud
    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION); // Obtiene el encabezado "Authorization"

        // Verifica si el encabezado tiene texto y comienza con "Bearer"
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer")) {
            return authHeader.substring(7); // Devuelve el token sin el prefijo "Bearer "
        }
        return null; // Si no cumple las condiciones, devuelve null
    }
}