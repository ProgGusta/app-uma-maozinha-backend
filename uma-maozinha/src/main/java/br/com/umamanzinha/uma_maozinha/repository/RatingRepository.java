package br.com.umamanzinha.uma_maozinha.repository;

import br.com.umamanzinha.uma_maozinha.entities.Ratings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Ratings, Long> {
    List<Ratings> findByFreelancerProfile_Id(Long id);
}
