package br.com.umamanzinha.uma_maozinha.services;

import br.com.umamanzinha.uma_maozinha.dtos.freelancer.FreelancerRequestDTO;
import br.com.umamanzinha.uma_maozinha.dtos.freelancer.FreelancerResponseDTO;
import br.com.umamanzinha.uma_maozinha.entities.Category;
import br.com.umamanzinha.uma_maozinha.entities.FreelancerProfile;
import br.com.umamanzinha.uma_maozinha.entities.User;
import br.com.umamanzinha.uma_maozinha.exceptions.ResourceNotFoundException;
import br.com.umamanzinha.uma_maozinha.mapper.FreelancerProfileMapper;
import br.com.umamanzinha.uma_maozinha.repository.CategoryRepository;
import br.com.umamanzinha.uma_maozinha.repository.FreelancerProfileRepository;
import br.com.umamanzinha.uma_maozinha.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FreelancerProfileService {

    private final FreelancerProfileRepository freelancerProfileRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public FreelancerProfileService(FreelancerProfileRepository freelancerProfileRepository, UserRepository userRepository, CategoryRepository categoryRepository) {
        this.freelancerProfileRepository = freelancerProfileRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public FreelancerResponseDTO createFreelancer(Long id, FreelancerRequestDTO dto){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if(!user.getIsFreelancer()){
            user.setIsFreelancer(true);
            userRepository.save(user);
        }

        Category categoryEntity = categoryRepository.findByName(dto.category())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + dto.category()));

        FreelancerProfile freelancerProfile = FreelancerProfileMapper.toEntity(dto, user,  categoryEntity);
        freelancerProfile.setAverageRating(0.0);
        freelancerProfile = freelancerProfileRepository.save(freelancerProfile);

        return  new FreelancerResponseDTO(freelancerProfile);
    }

    @Transactional
    public void deleteProfile(Long profile_id) {
        //TODO: Verificar se usuário está autenticado (quando adicionar o spring security)

        FreelancerProfile profile = freelancerProfileRepository.findById(profile_id)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer profile not found with ID: " + profile_id));

        //TODO: Verificar se o usuário que está tentando apagar o perfil freelancer é o dono dele

        freelancerProfileRepository.delete(profile);

        if(!freelancerProfileRepository.existsByUserId(profile.getUser().getId())) {
            User user = profile.getUser();
            user.setIsFreelancer(false);
            userRepository.save(user);
        }
    }

    @Transactional(readOnly = true)
    public FreelancerResponseDTO getProfileById(Long profile_id) {
        FreelancerProfile profile = freelancerProfileRepository.findById(profile_id)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer profile not found with ID: " + profile_id));

        return new FreelancerResponseDTO(profile);
    }

    @Transactional(readOnly = true)
    public List<FreelancerResponseDTO> getProfilesByUserId(Long user_id) {
        userRepository.findById(user_id)
                .orElseThrow(() -> new ResourceNotFoundException("user not found with ID: " + user_id));

        List<FreelancerProfile> profiles = freelancerProfileRepository.findByUserId(user_id);

        return profiles.stream()
                .map(FreelancerResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public FreelancerResponseDTO updateProfile(Long profile_id, @Valid FreelancerRequestDTO dto) {
        //TODO: Verificar e obter o id do user conectado

        FreelancerProfile profile = freelancerProfileRepository.findById(profile_id)
                .orElseThrow(() -> new ResourceNotFoundException("Freelancer profile not found with ID: " + profile_id));

        //TODO: Verificar se o usuário logado é o dono do perfil

        profile.setTitle(dto.title());
        profile.setDescription(dto.description());

        if (!profile.getCategory().getName().equals(dto.category())) {
            Category newCategory = categoryRepository.findByName(dto.category())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + dto.category()));
            profile.setCategory(newCategory);
        }

        profile = freelancerProfileRepository.save(profile);

        return new FreelancerResponseDTO(profile);
    }
}
