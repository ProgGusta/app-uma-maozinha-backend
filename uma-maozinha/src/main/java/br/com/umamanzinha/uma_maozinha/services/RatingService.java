package br.com.umamanzinha.uma_maozinha.services;

import br.com.umamanzinha.uma_maozinha.dtos.rating.RatingRequestDTO;
import br.com.umamanzinha.uma_maozinha.dtos.rating.RatingResponseDTO;
import br.com.umamanzinha.uma_maozinha.entities.FreelancerProfile;
import br.com.umamanzinha.uma_maozinha.entities.Ratings;
import br.com.umamanzinha.uma_maozinha.entities.Services;
import br.com.umamanzinha.uma_maozinha.exceptions.ResourceNotFoundException;
import br.com.umamanzinha.uma_maozinha.mapper.RatingMapper;
import br.com.umamanzinha.uma_maozinha.repository.FreelancerProfileRepository;
import br.com.umamanzinha.uma_maozinha.repository.RatingRepository;
import br.com.umamanzinha.uma_maozinha.repository.ServicesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;
    private final ServicesRepository servicesRepository;
    private final FreelancerProfileRepository freelancerProfileRepository;

    public RatingService(RatingRepository ratingRepository, ServicesRepository servicesRepository, FreelancerProfileRepository freelancerProfileRepository) {
        this.ratingRepository = ratingRepository;
        this.servicesRepository = servicesRepository;
        this.freelancerProfileRepository = freelancerProfileRepository;
    }
    @Transactional
    public RatingResponseDTO createRating(RatingRequestDTO ratingRequestDTO, Long serviceId) {
        Services service = servicesRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        Ratings rating = new Ratings();
        rating.setScore(ratingRequestDTO.score());
        rating.setComment(ratingRequestDTO.comment());
        rating.setServices(service);
        rating.setFreelancerProfile(service.getFreelancerProfile());

        Ratings saveRating = ratingRepository.save(rating);

        calculateRating(service.getFreelancerProfile());

        return RatingMapper.toDTO(saveRating);

    }


    @Transactional
    public RatingResponseDTO updateRating(Long ratingId, RatingRequestDTO ratingRequestDTO) {
        Ratings rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found"));

        if (ratingRequestDTO.score() != null) {
            rating.setScore(ratingRequestDTO.score());
            calculateRating(rating.getFreelancerProfile());
        }

        if (ratingRequestDTO.comment() != null)
            rating.setComment(ratingRequestDTO.comment());

        Ratings updatedRating = ratingRepository.save(rating);


        return RatingMapper.toDTO(updatedRating);
    }

    @Transactional
    public void deleteRating(Long ratingId) {
        Ratings rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found"));

        ratingRepository.delete(rating);

        calculateRating(rating.getFreelancerProfile());

    }
    @Transactional
    public List<RatingResponseDTO> getAllRatingsByFreelancerProfileId(Long freelancerProfileId) {
        List<Ratings> ratings = ratingRepository.findByFreelancerProfile_Id(freelancerProfileId);
        return ratings.stream()
                .map(RatingMapper::toDTO)
                .toList();
    }

    @Transactional
    public List<RatingResponseDTO> getAllRatingsByUserId(Long userId) {
        List<Ratings> ratings = ratingRepository.findByServices_User_Id(userId);
        return ratings.stream()
                .map(RatingMapper::toDTO)
                .toList();
    }

    private void calculateRating(FreelancerProfile freelancerProfile) {
        List<Ratings> ratings = ratingRepository.findByFreelancerProfile_Id(freelancerProfile.getId());
        Double averageRating = ratings.stream()
                .mapToDouble(Ratings::getScore)
                .average()
                .orElse(0.0);
        freelancerProfile.setAverageRating(averageRating);
        freelancerProfileRepository.save(freelancerProfile);
    }
}
