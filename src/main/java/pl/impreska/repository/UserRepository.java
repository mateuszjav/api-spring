package pl.impreska.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.impreska.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
