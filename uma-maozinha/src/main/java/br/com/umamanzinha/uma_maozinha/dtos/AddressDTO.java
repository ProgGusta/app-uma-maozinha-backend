package br.com.umamanzinha.uma_maozinha.dtos;

public record AddressDTO(
    String street,
    String city,
    String state,
    String zipCode,
    String country
) {
}
