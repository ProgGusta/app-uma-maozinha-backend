package br.com.umamanzinha.uma_maozinha.repository;

import br.com.umamanzinha.uma_maozinha.entities.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
    List<Phone> findByUserId(Long userId);
}
