package br.com.umamanzinha.uma_maozinha.dtos;

public record PhoneDTO(
    String number,
    Boolean isWhatsApp,
    String description
) {
}
