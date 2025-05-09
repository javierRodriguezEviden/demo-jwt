package jrc.demo_jwt.User;

import java.util.Collection; // Representa una colección de elementos
import java.util.List; // Implementación de una lista

import org.springframework.security.core.GrantedAuthority; // Representa una autoridad otorgada a un usuario
import org.springframework.security.core.authority.SimpleGrantedAuthority; // Implementación simple de GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails; // Interfaz que define los detalles de un usuario

import jakarta.persistence.Column; // Marca un campo como una columna en la base de datos
import jakarta.persistence.Entity; // Marca esta clase como una entidad JPA
import jakarta.persistence.EnumType; // Define cómo se almacenan los valores de un enum en la base de datos
import jakarta.persistence.Enumerated; // Marca un campo como un enum persistente
import jakarta.persistence.GeneratedValue; // Indica que el valor de este campo será generado automáticamente
import jakarta.persistence.Id; // Marca un campo como la clave primaria
import jakarta.persistence.Table; // Define la tabla asociada a esta entidad
import jakarta.persistence.UniqueConstraint; // Define restricciones únicas en la tabla
import lombok.AllArgsConstructor; // Genera un constructor con todos los campos
import lombok.Builder; // Proporciona un patrón de construcción para la clase
import lombok.Data; // Genera getters, setters, equals, hashCode y toString
import lombok.NoArgsConstructor; // Genera un constructor sin argumentos

@Data // Genera automáticamente getters, setters, equals, hashCode y toString
@Builder // Permite construir instancias de esta clase utilizando el patrón Builder
@AllArgsConstructor // Genera un constructor con todos los campos como parámetros
@NoArgsConstructor // Genera un constructor sin argumentos
@Entity // Marca esta clase como una entidad JPA
@Table(name="user", uniqueConstraints = {@UniqueConstraint(columnNames = {"username"})}) 
// Define la tabla "user" en la base de datos y asegura que el campo "username" sea único
public class User implements UserDetails { // Implementa la interfaz UserDetails para integrarse con Spring Security

    @Id // Marca este campo como la clave primaria
    @GeneratedValue // Indica que el valor será generado automáticamente
    Integer id; // Identificador único del usuario

    @Column(nullable = false) // Define que este campo no puede ser nulo en la base de datos
    String username; // Nombre de usuario

    String lastname; // Apellido del usuario
    String firstname; // Nombre del usuario
    String country; // País del usuario
    String password; // Contraseña del usuario

    @Enumerated(EnumType.STRING) // Almacena el valor del enum como una cadena en la base de datos
    Role role; // Rol del usuario (por ejemplo, ADMIN o USER)

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Devuelve una lista de autoridades basadas en el rol del usuario
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    // Otros métodos de la interfaz UserDetails (como isAccountNonExpired, isAccountNonLocked, etc.)
    // no están implementados aquí, pero pueden ser agregados si es necesario.
}
