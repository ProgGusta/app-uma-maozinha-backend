package br.com.umamanzinha.uma_maozinha.controller;

import br.com.umamanzinha.uma_maozinha.config.JwtUserData;
import br.com.umamanzinha.uma_maozinha.dtos.AddressDTO;
import br.com.umamanzinha.uma_maozinha.services.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.List;
// Swagger/OpenAPI imports
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/adress")
@Tag(name = "Endereços", description = "Operações relacionadas a endereços de usuários")
public class AdressController {
    private final AddressService addressService;

    public AdressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @Operation(summary = "Adicionar endereço ao usuário", description = "Adiciona um novo endereço ao usuário especificado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Endereço criado com sucesso", content = @Content(schema = @Schema(implementation = AddressDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content)
    })
    @PostMapping("/{userId}/add")
    public ResponseEntity<AddressDTO> addAddress(
            @Parameter(description = "ID do usuário", required = true) @PathVariable Long userId,
            @Parameter(description = "Dados do endereço", required = true) @Valid @RequestBody AddressDTO addressDTO,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data){
        return ResponseEntity.status(HttpStatus.CREATED).body(addressService.addAddressToUser(userId,addressDTO, data.id()));
    }

    @Operation(summary = "Atualizar endereço", description = "Atualiza os dados de um endereço existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Endereço atualizado com sucesso", content = @Content(schema = @Schema(implementation = AddressDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
        @ApiResponse(responseCode = "404", description = "Endereço não encontrado", content = @Content)
    })
    @PutMapping("/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(
            @Parameter(description = "ID do endereço", required = true) @PathVariable Long addressId,
            @Parameter(description = "Dados do endereço", required = true) @Valid @RequestBody AddressDTO addressDTO,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data){
        return ResponseEntity.ok(addressService.updateAddress(addressId,addressDTO, data.id()));
    }

    @Operation(summary = "Listar todos os endereços do usuário", description = "Retorna todos os endereços cadastrados para o usuário especificado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de endereços retornada com sucesso", content = @Content(schema = @Schema(implementation = AddressDTO.class))),
        @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content)
    })
    @GetMapping("/{userId}/listAll")
    public ResponseEntity<List<AddressDTO>> listAllAddresses(
            @Parameter(description = "ID do usuário", required = true) @PathVariable Long userId,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data){
        return ResponseEntity.ok(addressService.getAddressesByUserId(userId, data.id()));
    }

    @Operation(summary = "Excluir endereço", description = "Remove um endereço do usuário.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Endereço excluído com sucesso", content = @Content),
        @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content),
        @ApiResponse(responseCode = "404", description = "Endereço não encontrado", content = @Content)
    })
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @Parameter(description = "ID do endereço", required = true) @PathVariable Long addressId,
            @Parameter(hidden = true) @AuthenticationPrincipal JwtUserData data){
        addressService.deleteAddress(addressId,data.id());
        return ResponseEntity.noContent().build();
    }
}
