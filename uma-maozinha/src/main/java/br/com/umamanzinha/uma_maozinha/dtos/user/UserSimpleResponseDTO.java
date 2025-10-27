package br.com.umamanzinha.uma_maozinha.dtos.user;

import br.com.umamanzinha.uma_maozinha.entities.User;

public record UserSimpleResponseDTO(
        Long id,
        String name
) {
    public UserSimpleResponseDTO(User user) {
        this(user.getId(), user.getName());
    }
}