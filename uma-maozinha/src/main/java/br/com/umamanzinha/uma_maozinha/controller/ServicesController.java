package br.com.umamanzinha.uma_maozinha.controller;

import br.com.umamanzinha.uma_maozinha.dtos.ServicesDTO;
import br.com.umamanzinha.uma_maozinha.services.ServicesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/{freelancerProfileId}/services")
public class ServicesController {
    private final ServicesService servicesService;

    public ServicesController(ServicesService servicesService) {
        this.servicesService = servicesService;
    }

    @PostMapping("/create")
    public ResponseEntity<ServicesDTO> createService(@RequestBody ServicesDTO servicesDTO, @PathVariable Long freelancerProfileId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicesService.createService(servicesDTO, freelancerProfileId));
    }

    @PatchMapping("/{serviceId}/confirm")
    public ResponseEntity<ServicesDTO> confirmService(@PathVariable Long freelancerProfileId, @PathVariable Long serviceId) {
        return ResponseEntity.ok(servicesService.confirmService(serviceId, freelancerProfileId));
    }

    @PatchMapping("/{serviceId}/cancel")
    public ResponseEntity<ServicesDTO> cancelService(@PathVariable Long freelancerProfileId,@PathVariable Long serviceId) {
        return ResponseEntity.ok(servicesService.cancelService(serviceId, freelancerProfileId));
    }

    @PatchMapping("/{serviceId}/complete")
    public ResponseEntity<ServicesDTO> completeService(@PathVariable Long freelancerProfileId,@PathVariable Long serviceId) {
        return ResponseEntity.ok(servicesService.completeService(serviceId, freelancerProfileId));
    }

    @PatchMapping("/{serviceId}/in-progress")
    public ResponseEntity<ServicesDTO> changeStatusToInProgress(@PathVariable Long freelancerProfileId,@PathVariable Long serviceId) {
        return ResponseEntity.ok(servicesService.changeStatusToInProgress(serviceId, freelancerProfileId));
    }




}
