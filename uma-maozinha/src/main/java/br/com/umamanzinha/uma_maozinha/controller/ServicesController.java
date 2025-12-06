package br.com.umamanzinha.uma_maozinha.controller;

import br.com.umamanzinha.uma_maozinha.config.JwtUserData;
import br.com.umamanzinha.uma_maozinha.dtos.services.ServicesRequestDTO;
import br.com.umamanzinha.uma_maozinha.dtos.services.ServicesResponseDTO;
import br.com.umamanzinha.uma_maozinha.dtos.services.ServicesWaitDTO;
import br.com.umamanzinha.uma_maozinha.services.ServicesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/services")
@Tag(name = "Serviços", description = "Endpoints para criação e gerenciamento de serviços. Regras de negócio: apenas usuários e freelancers autorizados podem criar, confirmar, cancelar, completar ou alterar o status dos serviços. Cada ação possui validações específicas de status e permissões.")
public class ServicesController {

    private final ServicesService servicesService;

    public ServicesController(ServicesService servicesService) {
        this.servicesService = servicesService;
    }

    @Operation(summary = "Criar serviço", description = "Cria um novo serviço para um perfil de freelancer. Apenas o próprio usuário pode criar serviços para si. O serviço é criado com status PENDING.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Serviço criado com sucesso", content = @Content(schema = @Schema(implementation = ServicesResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou regras de negócio violadas", content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Ação proibida: usuário não pode criar serviço para outro usuário", content = @Content),
        @ApiResponse(responseCode = "404", description = "Perfil de freelancer ou usuário não encontrado", content = @Content)
    })
    @PostMapping("/create/{freelancerProfileId}")
    public ResponseEntity<ServicesResponseDTO> createService(
            @Parameter(description = "Dados do serviço", required = true) @RequestBody ServicesRequestDTO servicesDTO,
            @Parameter(description = "ID do perfil de freelancer", required = true, example = "1") @PathVariable Long freelancerProfileId,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(servicesService.createService(servicesDTO, freelancerProfileId, data.id()));
    }


    @Operation(summary = "Confirmar serviço", description = "Confirma o serviço, alterando o status para CONFIRMED. Apenas o freelancer dono do serviço pode confirmar. Só é possível confirmar serviços com status PENDING.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Serviço confirmado com sucesso", content = @Content(schema = @Schema(implementation = ServicesResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Regras de negócio violadas (status diferente de PENDING)", content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Ação proibida: usuário não pode confirmar serviço de outro freelancer", content = @Content),
        @ApiResponse(responseCode = "404", description = "Serviço não encontrado", content = @Content)
    })
    @PatchMapping("/{serviceId}/confirm")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<ServicesResponseDTO> confirmService(
            @Parameter(description = "ID do serviço", required = true, example = "10") @PathVariable Long serviceId,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data) {

        return ResponseEntity.ok(servicesService.confirmService(serviceId, data.id()));
    }

    @Operation(summary = "Cancelar serviço", description = "Cancela o serviço, alterando o status para CANCELLED. Apenas o usuário ou freelancer dono do serviço pode cancelar. Não é possível cancelar serviços com status COMPLETED.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Serviço cancelado com sucesso", content = @Content(schema = @Schema(implementation = ServicesResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Regras de negócio violadas (serviço já concluído)", content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Ação proibida: usuário não pode cancelar serviço de outro usuário/freelancer", content = @Content),
        @ApiResponse(responseCode = "404", description = "Serviço não encontrado", content = @Content)
    })
    @PatchMapping("/{serviceId}/cancel")
    @PreAuthorize("hasAnyRole('USER','FREELANCER')")
    public ResponseEntity<ServicesResponseDTO> cancelService(
            @Parameter(description = "ID do serviço", required = true, example = "10") @PathVariable Long serviceId,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data) {

        return ResponseEntity.ok(servicesService.cancelService(serviceId, data.id()));
    }

    @Operation(summary = "Completar serviço", description = "Marca o serviço como COMPLETED. Apenas o freelancer dono do serviço pode completar. Não há restrição de status para completar, mas o serviço deve pertencer ao freelancer autenticado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Serviço marcado como concluído", content = @Content(schema = @Schema(implementation = ServicesResponseDTO.class))),
        @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Ação proibida: usuário não pode completar serviço de outro freelancer", content = @Content),
        @ApiResponse(responseCode = "404", description = "Serviço não encontrado", content = @Content)
    })
    @PatchMapping("/{serviceId}/complete")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<ServicesResponseDTO> completeService(
            @Parameter(description = "ID do serviço", required = true, example = "10") @PathVariable Long serviceId,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data) {

        return ResponseEntity.ok(servicesService.completeService(serviceId, data.id()));
    }

    @Operation(summary = "Alterar status do serviço para IN_PROGRESS", description = "Altera o status do serviço para IN_PROGRESS. Apenas o freelancer dono do serviço pode realizar esta ação. Só é possível alterar para IN_PROGRESS se o status atual for CONFIRMED.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status alterado para IN_PROGRESS", content = @Content(schema = @Schema(implementation = ServicesResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Regras de negócio violadas (status diferente de CONFIRMED)", content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Ação proibida: usuário não pode alterar status de serviço de outro freelancer", content = @Content),
        @ApiResponse(responseCode = "404", description = "Serviço não encontrado", content = @Content)
    })
    @PatchMapping("/{serviceId}/in-progress")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<ServicesResponseDTO> changeStatusToInProgress(
            @Parameter(description = "ID do serviço", required = true, example = "10") @PathVariable Long serviceId,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data) {

        return ResponseEntity.ok(servicesService.changeStatusToInProgress(serviceId, data.id()));
    }

    @Operation(
            summary = "Aceitar serviço após revisão (retorna para PENDING)",
            description = "O usuário aceita o serviço após o freelancer colocar em WAITING_USER. "
                    + "Apenas o usuário dono do serviço pode aceitar. Só é possível aceitar quando o status atual for WAITING_USER."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Serviço aceito com sucesso (status PENDING)",
                    content = @Content(schema = @Schema(implementation = ServicesResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Regras de negócio violadas (status diferente de WAITING_USER)", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Ação proibida: usuário não pode aceitar serviço de outro usuário", content = @Content),
            @ApiResponse(responseCode = "404", description = "Serviço não encontrado", content = @Content)
    })
    @PatchMapping("/{serviceId}/accept")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ServicesResponseDTO> acceptService(
            @Parameter(description = "ID do serviço", required = true, example = "10")
            @PathVariable Long serviceId,

            @Parameter(hidden = true)
            @AuthenticationPrincipal JwtUserData data
    ) {
        return ResponseEntity.ok(
                servicesService.acceptService(serviceId, data.id())
        );
    }
    @Operation(
            summary = "Colocar serviço em espera (WAITING_USER)",
            description = "O freelancer altera o serviço para WAITING_USER, podendo atualizar preço e descrição. "
                    + "Apenas o freelancer dono do serviço pode executar esta ação. Só é possível realizar esta ação se o serviço estiver com status PENDING."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Serviço atualizado para WAITING_USER",
                    content = @Content(schema = @Schema(implementation = ServicesResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Regras de negócio violadas (status diferente de PENDING)", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
            @ApiResponse(responseCode = "403", description = "Ação proibida: freelancer não pode alterar serviço de outro freelancer", content = @Content),
            @ApiResponse(responseCode = "404", description = "Serviço não encontrado", content = @Content)
    })
    @PatchMapping("/{serviceId}/wait")
    @PreAuthorize("hasRole('FREELANCER')")
    public ResponseEntity<ServicesResponseDTO> waitService(
            @Parameter(description = "ID do serviço", required = true, example = "10")
            @PathVariable Long serviceId,

            @Parameter(description = "Dados opcionais para atualização do serviço (preço/descrição)")
            @RequestBody ServicesWaitDTO dto,

            @Parameter(hidden = true)
            @AuthenticationPrincipal JwtUserData data
    ) {
        return ResponseEntity.ok(
                servicesService.waitService(serviceId, data.id(), dto)
        );
    }

}
