package br.com.umamanzinha.uma_maozinha.repository;

import br.com.umamanzinha.uma_maozinha.entities.FreelancerProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FreelancerProfileRepository extends JpaRepository<FreelancerProfile,Long> {
    List<FreelancerProfile> findByUserId(Long userId);
}
