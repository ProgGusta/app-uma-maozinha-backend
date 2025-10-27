package br.com.umamanzinha.uma_maozinha.mapper;

import br.com.umamanzinha.uma_maozinha.dtos.services.ServicesRequestDTO;
import br.com.umamanzinha.uma_maozinha.dtos.services.ServicesResponseDTO;
import br.com.umamanzinha.uma_maozinha.dtos.freelancer.FreelancerSimpleResponseDTO;
import br.com.umamanzinha.uma_maozinha.dtos.user.UserSimpleResponseDTO;
import br.com.umamanzinha.uma_maozinha.entities.FreelancerProfile;
import br.com.umamanzinha.uma_maozinha.entities.Services;
import br.com.umamanzinha.uma_maozinha.entities.User;
import org.springframework.stereotype.Component;

@Component
public class ServicesMapper {
    public static Services toEntity (ServicesRequestDTO servicesDTO, FreelancerProfile freelancerProfile, User user) {
        Services services  = new Services ();
        services.setPrice(servicesDTO.price());
        services.setLocation(servicesDTO.location());
        services.setDescription(servicesDTO.description());
        services.setFreelancerProfile(freelancerProfile);
        services.setUser(user);

        return services;
    }
    public static ServicesResponseDTO toDTO (Services services) {
        return new ServicesResponseDTO(
                services.getId(),
                services.getStatus(),
                services.getPrice(),
                services.getLocation(),
                services.getDescription(),
                services.getCreatedAt().toString(),
                new FreelancerSimpleResponseDTO(services.getFreelancerProfile()),
                new UserSimpleResponseDTO(services.getUser())
        );
    }

}
