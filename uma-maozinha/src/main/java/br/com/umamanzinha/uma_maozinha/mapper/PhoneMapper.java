package br.com.umamanzinha.uma_maozinha.mapper;

import br.com.umamanzinha.uma_maozinha.dtos.PhoneDTO;
import br.com.umamanzinha.uma_maozinha.entities.Phone;
import org.springframework.stereotype.Component;

@Component
public class PhoneMapper {
    public static Phone toEntity(PhoneDTO dto) {
        Phone phone = new Phone();
        phone.setNumber(dto.number());
        phone.setIsWhatsApp(dto.isWhatsApp());
        phone.setDescription(dto.description());
        return phone;
    }

    public static PhoneDTO toDto(Phone phone) {
        return new PhoneDTO(
            phone.getId(),
            phone.getNumber(),
            phone.getIsWhatsApp(),
            phone.getDescription()
        );
    }
}
