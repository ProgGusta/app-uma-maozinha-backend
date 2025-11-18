package br.com.umamanzinha.uma_maozinha.dtos.login;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(@NotBlank String email, @NotBlank String password) {
}
