package br.com.umamanzinha.uma_maozinha.dtos.freelancer;

import br.com.umamanzinha.uma_maozinha.entities.Category;
import br.com.umamanzinha.uma_maozinha.entities.FreelancerProfile;
import br.com.umamanzinha.uma_maozinha.entities.User;
import br.com.umamanzinha.uma_maozinha.enums.CategoryType;

public record FreelancerResponseDTO(
        Long id,
        String title,
        String description,
        UserSimpleResponseDTO user,
        CategorySimpleResponseDTO category,
        Double averageRating
) {
    public record UserSimpleResponseDTO(
            Long id,
            String name
    ) {
        public UserSimpleResponseDTO(User user) {
            this(user.getId(), user.getName());
        }
    }

    public record CategorySimpleResponseDTO(
            Long id,
            CategoryType name
    ) {
        public CategorySimpleResponseDTO(Category category) {
            this(category.getId(), category.getName());
        }
    }

    public FreelancerResponseDTO(FreelancerProfile profile) {
        this(
                profile.getId(),
                profile.getTitle(),
                profile.getDescription(),
                new UserSimpleResponseDTO(profile.getUser()),
                new CategorySimpleResponseDTO(profile.getCategory()),
                profile.getAverageRating()
        );
    }
}
