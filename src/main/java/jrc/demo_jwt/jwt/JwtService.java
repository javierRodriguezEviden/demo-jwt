package jrc.demo_jwt.jwt;

import java.security.Key; // Representa una clave criptográfica
import java.util.Date; // Manejo de fechas
import java.util.HashMap; // Implementación de un mapa hash
import java.util.Map; // Interfaz para mapas
import java.util.function.Function; // Interfaz funcional para manejar funciones

import org.springframework.security.core.userdetails.UserDetails; // Representa los detalles de un usuario
import org.springframework.stereotype.Service; // Marca esta clase como un servicio gestionado por Spring

import io.jsonwebtoken.Claims; // Representa los claims (información) dentro de un JWT
import io.jsonwebtoken.Jwts; // Clase principal para construir y analizar JWTs
import io.jsonwebtoken.SignatureAlgorithm; // Algoritmos de firma para JWT
import io.jsonwebtoken.io.Decoders; // Decodificador para claves en formato Base64
import io.jsonwebtoken.security.Keys; // Utilidad para generar claves criptográficas

@Service // Marca esta clase como un servicio gestionado por Spring
public class JwtService {

    private static final String SECRET_KEY = "tQSx2ohpLmteo4wlHrslwTdnDMr01ZzXXNr24KicXvQAyoOghR7ITUdFrS0EvK8E";
    // Clave secreta utilizada para firmar y verificar los JWTs

    // Genera un token JWT para un usuario
    public String getToken(UserDetails user) {
        return getToken(new HashMap<>(), user); // Llama al método sobrecargado con un mapa vacío de claims adicionales
    }

    // Genera un token JWT con claims adicionales
    private String getToken(Map<String, Object> extraClaims, UserDetails user) {
        return Jwts
            .builder()
            .setClaims(extraClaims) // Establece los claims adicionales
            .setSubject(user.getUsername()) // Establece el nombre de usuario como sujeto del token
            .setIssuedAt(new Date(System.currentTimeMillis())) // Fecha de emisión del token
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // Fecha de expiración (24 minutos)
            .signWith(getKey(), SignatureAlgorithm.HS256) // Firma el token con la clave secreta y el algoritmo HS256
            .compact(); // Construye el token
    }

    // Obtiene la clave criptográfica a partir de la clave secreta
    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); // Decodifica la clave secreta en formato Base64
        return Keys.hmacShaKeyFor(keyBytes); // Genera una clave HMAC-SHA a partir de los bytes decodificados
    }

    // Extrae el nombre de usuario del token
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject); // Obtiene el claim "subject" del token
    }

    // Verifica si un token es válido para un usuario
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token); // Extrae el nombre de usuario del token
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); // Verifica que el nombre de usuario coincida y que el token no haya expirado
    }

    // Obtiene todos los claims del token
    private Claims getAllClaims(String token) {
        return Jwts
            .parserBuilder()
            .setSigningKey(getKey()) // Establece la clave de firma para verificar el token
            .build()
            .parseClaimsJws(token) // Analiza el token y obtiene los claims
            .getBody();
    }

    // Obtiene un claim específico del token utilizando un resolver
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token); // Obtiene todos los claims
        return claimsResolver.apply(claims); // Aplica el resolver para obtener el claim deseado
    }

    // Obtiene la fecha de expiración del token
    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration); // Obtiene el claim "expiration"
    }

    // Verifica si el token ha expirado
    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date()); // Compara la fecha de expiración con la fecha actual
    }
}
