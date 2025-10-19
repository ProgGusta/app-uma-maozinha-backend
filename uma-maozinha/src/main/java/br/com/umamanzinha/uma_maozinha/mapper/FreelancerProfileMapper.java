package br.com.umamanzinha.uma_maozinha.mapper;

import br.com.umamanzinha.uma_maozinha.dtos.freelancer.FreelancerRequestDTO;
import br.com.umamanzinha.uma_maozinha.entities.Category;
import br.com.umamanzinha.uma_maozinha.entities.FreelancerProfile;
import br.com.umamanzinha.uma_maozinha.entities.User;
import org.springframework.stereotype.Component;

@Component
public class FreelancerProfileMapper {
    public static FreelancerProfile toEntity (FreelancerRequestDTO dto,
                                              User user,
                                              Category category) {
        FreelancerProfile freelancerProfile = new FreelancerProfile();
        freelancerProfile.setTitle(dto.title());
        freelancerProfile.setDescription(dto.description());
        freelancerProfile.setUser(user);
        freelancerProfile.setCategory(category);

        return freelancerProfile;
    }
}
