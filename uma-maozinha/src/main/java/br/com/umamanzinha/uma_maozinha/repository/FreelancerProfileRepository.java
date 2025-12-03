package br.com.umamanzinha.uma_maozinha.repository;

import br.com.umamanzinha.uma_maozinha.entities.FreelancerProfile;
import br.com.umamanzinha.uma_maozinha.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FreelancerProfileRepository extends JpaRepository<FreelancerProfile,Long> {
    List<FreelancerProfile> findByUserId(Long userId);
    Boolean existsByUserId(Long userId);
    User findUserById(Long userId);
}
