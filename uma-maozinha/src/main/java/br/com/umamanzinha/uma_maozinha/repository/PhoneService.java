package br.com.umamanzinha.uma_maozinha.repository;

import br.com.umamanzinha.uma_maozinha.entities.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneService extends JpaRepository<Phone, Long> {
}
