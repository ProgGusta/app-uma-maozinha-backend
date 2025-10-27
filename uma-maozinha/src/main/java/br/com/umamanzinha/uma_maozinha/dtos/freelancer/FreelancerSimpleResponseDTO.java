package br.com.umamanzinha.uma_maozinha.dtos.freelancer;

import br.com.umamanzinha.uma_maozinha.dtos.user.UserSimpleResponseDTO;
import br.com.umamanzinha.uma_maozinha.entities.FreelancerProfile;

public record FreelancerSimpleResponseDTO(
        Long id,
        String title,
        UserSimpleResponseDTO user
) {
    public FreelancerSimpleResponseDTO(FreelancerProfile profile) {
        this(
                profile.getId(),
                profile.getTitle(),
                new UserSimpleResponseDTO(profile.getUser())
        );
    }
}
