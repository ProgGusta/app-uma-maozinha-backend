package br.com.umamanzinha.uma_maozinha.services;

import br.com.umamanzinha.uma_maozinha.dtos.services.ServicesRequestDTO;
import br.com.umamanzinha.uma_maozinha.dtos.services.ServicesResponseDTO;
import br.com.umamanzinha.uma_maozinha.dtos.services.ServicesWaitDTO;
import br.com.umamanzinha.uma_maozinha.entities.FreelancerProfile;
import br.com.umamanzinha.uma_maozinha.entities.Services;
import br.com.umamanzinha.uma_maozinha.entities.User;
import br.com.umamanzinha.uma_maozinha.enums.ServiceStatus;
import br.com.umamanzinha.uma_maozinha.exceptions.BusinessRuleException;
import br.com.umamanzinha.uma_maozinha.exceptions.ForbiddenException;
import br.com.umamanzinha.uma_maozinha.exceptions.ResourceNotFoundException;
import br.com.umamanzinha.uma_maozinha.exceptions.UnauthorizedActionException;
import br.com.umamanzinha.uma_maozinha.mapper.ServicesMapper;
import br.com.umamanzinha.uma_maozinha.repository.FreelancerProfileRepository;
import br.com.umamanzinha.uma_maozinha.repository.ServicesRepository;
import br.com.umamanzinha.uma_maozinha.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public ServicesResponseDTO createService(ServicesRequestDTO servicesDTO, Long freelancerProfileId, Long authUserId) {
        if (!servicesDTO.userId().equals(authUserId)) {
            throw new ForbiddenException("You cannot create a service for another user!");
        }
        FreelancerProfile freelancerProfile = freelancerProfileRepository.findById(freelancerProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("FreelancerProfile not found"));

        User user = userRepository.findById(servicesDTO.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Services services = ServicesMapper.toEntity(servicesDTO, freelancerProfile, user);
        services.setStatus(ServiceStatus.PENDING);
        servicesRepository.save(services);

        return ServicesMapper.toDTO(services);
    }
    @Transactional
    public List<ServicesResponseDTO> getAllServicesByUserId(Long userId, Long authUserId) {
        if (!userId.equals(authUserId)) {
            throw new ForbiddenException("You cannot access services of another user!");
        }
        List<Services> servicesList = servicesRepository.findByUserId(userId);
        return servicesList.stream()
                .map(ServicesMapper::toDTO).toList();
    }
    @Transactional
    public List<ServicesResponseDTO> getAllServicesByFreelancerId(Long freelancerProfileId, Long authUserId) {
        FreelancerProfile profile = freelancerProfileRepository.findById(freelancerProfileId)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer profile not found with ID: " + freelancerProfileId));

        if (!profile.getUser().getId().equals(authUserId)) {
            throw new ForbiddenException("You cannot access services of another freelancer profile!");
        }

        List<Services> servicesList = servicesRepository.findByFreelancerProfileId(freelancerProfileId);
        return servicesList.stream()
                .map(ServicesMapper::toDTO).toList();
    }

    public ServicesResponseDTO confirmService(Long serviceId,  Long authUserId){
        Services service =  servicesRepository.findById(serviceId).orElseThrow(
                () -> new ResourceNotFoundException("Service not found"));

        if( !service.getFreelancerProfile().getUser().getId().equals(authUserId))
            throw new UnauthorizedActionException("You cannot update another freelancer service status!");

        if(!service.getStatus().equals(ServiceStatus.PENDING)){
            throw new BusinessRuleException("You cannot confirm this service");
        }
        service.setStatus(ServiceStatus.CONFIRMED);

        return ServicesMapper.toDTO(servicesRepository.save(service));

    }

    public ServicesResponseDTO waitService(Long serviceId, Long authUserId, ServicesWaitDTO dto){
        Services service =  servicesRepository.findById(serviceId).orElseThrow(
                () -> new ResourceNotFoundException("Service not found"));

        if(!service.getFreelancerProfile().getUser().getId().equals(authUserId))
            throw new UnauthorizedActionException("You cannot update another freelancer service status!");

        if(!service.getStatus().equals(ServiceStatus.PENDING)){
            throw new BusinessRuleException("You cannot update this service");
        }

        if(dto.price()!=null)
            service.setPrice(dto.price());

        if(dto.description()!=null)
            service.setDescription(dto.description());


        service.setStatus(ServiceStatus.WAITING_USER);

        return ServicesMapper.toDTO(servicesRepository.save(service));

    }
    public ServicesResponseDTO acceptService(Long serviceId, Long authUserId){
        Services service =  servicesRepository.findById(serviceId).orElseThrow(
                () -> new ResourceNotFoundException("Service not found"));

        if(!service.getUser().getId().equals(authUserId))
            throw new UnauthorizedActionException("You cannot update another user service status!");

        if(!service.getStatus().equals(ServiceStatus.WAITING_USER)){
            throw new BusinessRuleException("You cannot accept this service");
        }
        service.setStatus(ServiceStatus.PENDING);

        return ServicesMapper.toDTO(servicesRepository.save(service));

    }

    public ServicesResponseDTO cancelService(Long serviceId, Long authUserId) {
        Services service = servicesRepository.findById(serviceId)
                .orElseThrow(() -> new ResourceNotFoundException("Service not found"));

        boolean isUser = service.getUser().getId().equals(authUserId);
        boolean isFreelancer = service.getFreelancerProfile().getUser().getId().equals(authUserId);

        if (!isUser && !isFreelancer) {
            throw new UnauthorizedActionException("You cannot cancel a service that is not yours!");
        }

        if (service.getStatus().equals(ServiceStatus.COMPLETED)) {
            throw new BusinessRuleException("You cannot cancel a completed service.");
        }

        service.setStatus(ServiceStatus.CANCELLED);
        return ServicesMapper.toDTO(servicesRepository.save(service));
    }

    public ServicesResponseDTO completeService(Long serviceId, Long authUserId){
        Services service =  servicesRepository.findById(serviceId).orElseThrow(
                () -> new ResourceNotFoundException("Service not found"));

        if(!service.getFreelancerProfile().getUser().getId().equals(authUserId))
            throw new UnauthorizedActionException("You cannot update another freelancer service status!");


        service.setStatus(ServiceStatus.COMPLETED);

        return ServicesMapper.toDTO(servicesRepository.save(service));

    }
    public ServicesResponseDTO changeStatusToInProgress(Long serviceId, Long authUserId){
        Services service =  servicesRepository.findById(serviceId).orElseThrow(
                () -> new ResourceNotFoundException("Service not found"));

        if(!service.getFreelancerProfile().getUser().getId().equals(authUserId))
            throw new UnauthorizedActionException("You cannot update another freelancer service status!");

        if(!service.getStatus().equals(ServiceStatus.CONFIRMED)){
            throw new BusinessRuleException("You cannot change this service status to In Progress");
        }
        service.setStatus(ServiceStatus.IN_PROGRESS);


        return ServicesMapper.toDTO(servicesRepository.save(service));

    }
}
