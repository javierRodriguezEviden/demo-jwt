package jrc.demo_jwt.User;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Integer>{
    Optional<User> findByUsername(String username); // busca un usuario por el nombre de usuario
}
