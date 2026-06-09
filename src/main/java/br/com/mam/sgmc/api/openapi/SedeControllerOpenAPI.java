package br.com.mam.sgmc.api.openapi;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.mam.sgmc.api.dto.request.SedeRequestDTO;
import br.com.mam.sgmc.api.dto.response.SedeResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Sedes", description = "API de gerenciamento de sedes")
public interface SedeControllerOpenAPI {

    @Operation(summary = "Cria uma nova sede", description = "Adiciona uma nova sede ao sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sede criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    ResponseEntity<Void> salvarSede(@RequestBody @Valid SedeRequestDTO sedeRequestDTO);

    @Operation(summary = "Busca uma sede por ID", description = "Retorna uma sede pelo seu identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sede encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Sede não encontrada")
    })
    ResponseEntity<SedeResponseDTO> buscarPorId(@PathVariable Long id);

    @Operation(summary = "Lista todas as sedes", description = "Retorna uma lista de todas as sedes cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sedes listadas com sucesso")
    })
    ResponseEntity<List<SedeResponseDTO>> listarSedesComFiltros(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cidade,
            @RequestParam(required = false) String ufSigla,
            @RequestParam(required = false) String pais);

    @Operation(summary = "Atualiza uma sede", description = "Atualiza os dados de uma sede existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sede atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Sede não encontrada"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    ResponseEntity<SedeResponseDTO> atualizarSede(@PathVariable Long id, @RequestBody @Valid SedeRequestDTO sedeRequestDTO);

    @Operation(summary = "Inativa uma sede", description = "Desativa uma sede, marcando-a como inativa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sede inativada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Sede não encontrada")
    })
    ResponseEntity<Void> inativarSede(@PathVariable Long id);
}
