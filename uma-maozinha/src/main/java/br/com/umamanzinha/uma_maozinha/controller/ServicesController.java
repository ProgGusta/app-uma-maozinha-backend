package br.com.umamanzinha.uma_maozinha.controller;

import br.com.umamanzinha.uma_maozinha.config.JwtUserData;
import br.com.umamanzinha.uma_maozinha.dtos.services.ServicesRequestDTO;
import br.com.umamanzinha.uma_maozinha.dtos.services.ServicesResponseDTO;
import br.com.umamanzinha.uma_maozinha.services.ServicesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/services")
public class ServicesController {

    private final ServicesService servicesService;

    public ServicesController(ServicesService servicesService) {
        this.servicesService = servicesService;
    }

    @PostMapping("/create/{freelancerProfileId}")
    public ResponseEntity<ServicesResponseDTO> createService(
            @RequestBody ServicesRequestDTO servicesDTO,
            @PathVariable Long freelancerProfileId,
            @AuthenticationPrincipal JwtUserData data) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(servicesService.createService(servicesDTO, freelancerProfileId, data.id()));
    }


    @PatchMapping("/{serviceId}/confirm")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<ServicesResponseDTO> confirmService(
            @PathVariable Long serviceId,
            @AuthenticationPrincipal JwtUserData data) {

        return ResponseEntity.ok(servicesService.confirmService(serviceId, data.id()));
    }

    @PatchMapping("/{serviceId}/cancel")
    @PreAuthorize("hasAnyRole('USER','FREELANCER')")
    public ResponseEntity<ServicesResponseDTO> cancelService(
            @PathVariable Long serviceId,
            @AuthenticationPrincipal JwtUserData data) {

        return ResponseEntity.ok(servicesService.cancelService(serviceId, data.id()));
    }

    @PatchMapping("/{serviceId}/complete")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<ServicesResponseDTO> completeService(
            @PathVariable Long serviceId,
            @AuthenticationPrincipal JwtUserData data) {

        return ResponseEntity.ok(servicesService.completeService(serviceId, data.id()));
    }

    @PatchMapping("/{serviceId}/in-progress")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<ServicesResponseDTO> changeStatusToInProgress(
            @PathVariable Long serviceId,
            @AuthenticationPrincipal JwtUserData data) {

        return ResponseEntity.ok(servicesService.changeStatusToInProgress(serviceId, data.id()));
    }
}
