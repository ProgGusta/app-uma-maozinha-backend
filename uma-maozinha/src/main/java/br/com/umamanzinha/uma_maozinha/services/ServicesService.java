package br.com.umamanzinha.uma_maozinha.services;

import br.com.umamanzinha.uma_maozinha.dtos.ServicesDTO;
import br.com.umamanzinha.uma_maozinha.entities.FreelancerProfile;
import br.com.umamanzinha.uma_maozinha.entities.Services;
import br.com.umamanzinha.uma_maozinha.entities.User;
import br.com.umamanzinha.uma_maozinha.enums.ServiceStatus;
import br.com.umamanzinha.uma_maozinha.exceptions.ResourceNotFoundException;
import br.com.umamanzinha.uma_maozinha.mapper.ServicesMapper;
import br.com.umamanzinha.uma_maozinha.repository.FreelancerProfileRepository;
import br.com.umamanzinha.uma_maozinha.repository.ServicesRepository;
import br.com.umamanzinha.uma_maozinha.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicesService {
    private final ServicesRepository servicesRepository;
    private final FreelancerProfileRepository freelancerProfileRepository;
    private final UserRepository userRepository;

    public ServicesService(ServicesRepository servicesRepository, FreelancerProfileRepository freelancerProfileRepository, UserRepository userRepository) {
        this.servicesRepository = servicesRepository;
        this.freelancerProfileRepository = freelancerProfileRepository;
        this.userRepository = userRepository;
    }

    public ServicesDTO createService(ServicesDTO servicesDTO, Long freelancerProfileId) {
        FreelancerProfile freelancerProfile = freelancerProfileRepository.findById(freelancerProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("FreelancerProfile not found"));

        User user = userRepository.findById(servicesDTO.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Services services = ServicesMapper.toEntity(servicesDTO, freelancerProfile, user);
        services.setStatus(ServiceStatus.PENDING);
        servicesRepository.save(services);

        return ServicesMapper.toDTO(services);
    }
}
