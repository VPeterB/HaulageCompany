package hu.bme.aut.haulagecompany.repository;

import hu.bme.aut.haulagecompany.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    User getUserByUsername(String username);
    Optional<User> findByUsername(String username);
}