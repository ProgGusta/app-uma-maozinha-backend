package br.com.umamanzinha.uma_maozinha.repository;

import br.com.umamanzinha.uma_maozinha.entities.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    Optional<User> findByEmail(@NotBlank String email);
}
