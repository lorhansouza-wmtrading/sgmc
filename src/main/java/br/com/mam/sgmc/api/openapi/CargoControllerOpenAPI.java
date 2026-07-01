package br.com.mam.sgmc.api.openapi;

import br.com.mam.sgmc.api.dto.request.CargoRequestDTO;
import br.com.mam.sgmc.api.dto.response.CargoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Cargos", description = "API de gerenciamento de cargos estatutários do motoclube")
public interface CargoControllerOpenAPI {

    @Operation(summary = "Cria um novo cargo", description = "Adiciona um novo cargo/patente ao catálogo do sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cargo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida ou cargo já existente")
    })
    ResponseEntity<Void> salvarCargo(@RequestBody @Valid CargoRequestDTO cargoRequestDTO);

    @Operation(summary = "Lista todos os cargos", description = "Retorna uma lista de todos os cargos cadastrados no motoclube.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cargos listados com sucesso")
    })
    ResponseEntity<List<CargoResponseDTO>> listarCargos();

    @Operation(summary = "Busca um cargo por ID", description = "Retorna os detalhes de um cargo específico pelo seu ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cargo encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cargo não encontrado")
    })
    ResponseEntity<CargoResponseDTO> buscarPorId(@PathVariable Long id);
}
