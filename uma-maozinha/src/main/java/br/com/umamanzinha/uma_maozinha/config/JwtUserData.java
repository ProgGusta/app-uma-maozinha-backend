package br.com.umamanzinha.uma_maozinha.config;

import lombok.Builder;

@Builder
public record JwtUserData(Long id, String name,String email) {
}
