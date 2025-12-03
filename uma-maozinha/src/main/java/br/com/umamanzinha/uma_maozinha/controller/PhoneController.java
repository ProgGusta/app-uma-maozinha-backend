package br.com.umamanzinha.uma_maozinha.controller;

import br.com.umamanzinha.uma_maozinha.config.JwtUserData;
import br.com.umamanzinha.uma_maozinha.dtos.PhoneDTO;
import br.com.umamanzinha.uma_maozinha.exceptions.ForbiddenException;
import br.com.umamanzinha.uma_maozinha.services.PhoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/phone")
@Tag(name = "Telefones", description = "Operações relacionadas a telefones de usuários")
public class PhoneController {
    private final PhoneService phoneService;

    public PhoneController(PhoneService phoneService) {
        this.phoneService = phoneService;
    }

    @Operation(summary = "Adicionar telefone ao usuário", description = "Adiciona um novo telefone ao usuário especificado. Apenas o próprio usuário pode adicionar telefones ao seu perfil.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Telefone criado com sucesso", content = @Content(schema = @Schema(implementation = PhoneDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Ação proibida: usuário não pode adicionar telefone para outro usuário", content = @Content)
    })
    @PostMapping("/{userId}/add")
    public ResponseEntity<PhoneDTO> addPhone(
            @Parameter(description = "ID do usuário", required = true, example = "1") @PathVariable Long userId,
            @Parameter(description = "Dados do telefone", required = true) @Valid @RequestBody PhoneDTO phoneDTO,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data) {

        if (!data.id().equals(userId)) {
            throw new ForbiddenException("You cannot add a phone to another user!");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(phoneService.addPhoneToUser(userId, phoneDTO));
    }

    @Operation(summary = "Atualizar telefone", description = "Atualiza os dados de um telefone existente. Apenas o dono do telefone pode realizar a atualização.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Telefone atualizado com sucesso", content = @Content(schema = @Schema(implementation = PhoneDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
        @ApiResponse(responseCode = "404", description = "Telefone não encontrado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Ação proibida: usuário não pode atualizar telefone de outro usuário", content = @Content)
    })
    @PutMapping("/{phoneId}")
    public ResponseEntity<PhoneDTO> updatePhone(
            @Parameter(description = "ID do telefone", required = true, example = "10") @PathVariable Long phoneId,
            @Parameter(description = "Dados do telefone", required = true) @Valid @RequestBody PhoneDTO phoneDTO,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data) {

        return ResponseEntity.ok(phoneService.updatePhone(phoneId, phoneDTO, data.id()));
    }

    @Operation(summary = "Listar todos os telefones do usuário", description = "Retorna todos os telefones cadastrados para o usuário especificado. Apenas o próprio usuário pode acessar seus telefones.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de telefones retornada com sucesso", content = @Content(schema = @Schema(implementation = PhoneDTO.class))),
        @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Ação proibida: usuário não pode acessar telefones de outro usuário", content = @Content)
    })
    @GetMapping("/{userId}/listAll")
    public ResponseEntity<List<PhoneDTO>> listAllPhones(
            @Parameter(description = "ID do usuário", required = true, example = "1") @PathVariable Long userId,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data) {

        if (!data.id().equals(userId)) {
            throw new ForbiddenException("You cannot access phones from another user!");
        }

        return ResponseEntity.ok(phoneService.getPhonesByUserId(userId));
    }

    @Operation(summary = "Excluir telefone", description = "Remove um telefone do usuário. Apenas o dono do telefone pode realizar a exclusão.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Telefone excluído com sucesso", content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
        @ApiResponse(responseCode = "404", description = "Telefone não encontrado", content = @Content),
        @ApiResponse(responseCode = "403", description = "Ação proibida: usuário não pode excluir telefone de outro usuário", content = @Content)
    })
    @DeleteMapping("/{phoneId}")
    public ResponseEntity<Void> deletePhone(
            @Parameter(description = "ID do telefone", required = true, example = "10") @PathVariable Long phoneId,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data) {

        phoneService.deletePhone(phoneId, data.id());
        return ResponseEntity.noContent().build();
    }
}
