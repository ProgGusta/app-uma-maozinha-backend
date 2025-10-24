package br.com.umamanzinha.uma_maozinha.repository;

import br.com.umamanzinha.uma_maozinha.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
