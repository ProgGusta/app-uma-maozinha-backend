package br.com.umamanzinha.uma_maozinha.controller;

import br.com.umamanzinha.uma_maozinha.dtos.ServicesDTO;
import br.com.umamanzinha.uma_maozinha.services.ServicesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/services")
public class ServicesController {
    private final ServicesService servicesService;

    public ServicesController(ServicesService servicesService) {
        this.servicesService = servicesService;
    }

    @PostMapping("/create/{freelancerProfileId}")
    public ResponseEntity<ServicesDTO> createService(@RequestBody ServicesDTO servicesDTO, @PathVariable Long freelancerProfileId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicesService.createService(servicesDTO, freelancerProfileId));
    }


}
