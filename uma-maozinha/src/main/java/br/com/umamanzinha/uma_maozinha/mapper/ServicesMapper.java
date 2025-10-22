package br.com.umamanzinha.uma_maozinha.mapper;

import br.com.umamanzinha.uma_maozinha.dtos.ServicesDTO;
import br.com.umamanzinha.uma_maozinha.entities.FreelancerProfile;
import br.com.umamanzinha.uma_maozinha.entities.Services;
import br.com.umamanzinha.uma_maozinha.entities.User;
import org.springframework.stereotype.Component;

@Component
public class ServicesMapper {
    public static Services toEntity (ServicesDTO servicesDTO, FreelancerProfile freelancerProfile, User user) {
        Services services  = new Services ();
        services.setStatus(servicesDTO.status());
        services.setPrice(servicesDTO.price());
        services.setLocation(servicesDTO.location());
        services.setDescription(servicesDTO.description());
        services.setFreelancerProfile(freelancerProfile);
        services.setUser(user);

        return services;
    }
    public static ServicesDTO toDTO (Services services) {
        return new ServicesDTO(
                services.getId(),
                services.getStatus(),
                services.getPrice(),
                services.getLocation(),
                services.getDescription(),
                services.getCreatedAt().toString(),
                services.getFreelancerProfile().getId(),
                services.getUser().getId()
        );
    }

}
