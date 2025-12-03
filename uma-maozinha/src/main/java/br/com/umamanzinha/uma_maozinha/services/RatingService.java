package br.com.umamanzinha.uma_maozinha.services;

import br.com.umamanzinha.uma_maozinha.dtos.rating.RatingRequestDTO;
import br.com.umamanzinha.uma_maozinha.dtos.rating.RatingResponseDTO;
import br.com.umamanzinha.uma_maozinha.entities.FreelancerProfile;
import br.com.umamanzinha.uma_maozinha.entities.Ratings;
import br.com.umamanzinha.uma_maozinha.entities.Services;
import br.com.umamanzinha.uma_maozinha.enums.ServiceStatus;
import br.com.umamanzinha.uma_maozinha.exceptions.BusinessRuleException;
import br.com.umamanzinha.uma_maozinha.exceptions.ForbiddenException;
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
    public RatingResponseDTO createRating(RatingRequestDTO ratingRequestDTO, Long serviceId, Long authUserId) {
        Services services = servicesRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        if (!services.getUser().getId().equals(authUserId)) {
            throw new ForbiddenException("You cannot rate a service that is not yours!");
        }

        if (ratingRepository.existsByServicesId(serviceId)) {
            throw new BusinessRuleException("Rating for this service already exists");
        }

        if (!services.getStatus().equals(ServiceStatus.COMPLETED)) {
            throw new BusinessRuleException("You can only rate a completed service!");
        }

        Ratings rating = RatingMapper.toEntity(ratingRequestDTO);

        rating.setServices(services);
        rating.setFreelancerProfile(services.getFreelancerProfile());

        Ratings saveRating = ratingRepository.save(rating);

        calculateRating(services.getFreelancerProfile());

        return RatingMapper.toDTO(saveRating);

    }

    @Transactional
    public RatingResponseDTO updateRating(Long ratingId, RatingRequestDTO ratingRequestDTO,  Long authUserId) {
        Ratings rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found"));

        if (!rating.getServices().getUser().getId().equals(authUserId)) {
            throw new ForbiddenException("You cannot rate a service that is not yours!");
        }

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
    public void deleteRating(Long ratingId, Long authUserId) {
        Ratings rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new ResourceNotFoundException("Rating not found"));

        if (!rating.getServices().getUser().getId().equals(authUserId)) {
            throw new ForbiddenException("You cannot delete a rating of a service that is not yours!");
        }

        ratingRepository.delete(rating);

        calculateRating(rating.getFreelancerProfile());

    }

    @Transactional(readOnly = true)
    public List<RatingResponseDTO> getAllRatingsByFreelancerProfileId(Long freelancerProfileId) {
        List<Ratings> ratings = ratingRepository.findByFreelancerProfile_Id(freelancerProfileId);
        return ratings.stream()
                .map(RatingMapper::toDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RatingResponseDTO> getAllRatingsByUserId(Long userId, Long authUserId) {
        if (!userId.equals(authUserId)) {
            throw new ForbiddenException("You cannot access ratings of another user!");
        }
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
