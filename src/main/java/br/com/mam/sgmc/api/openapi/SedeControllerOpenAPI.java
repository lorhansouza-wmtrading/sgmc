package br.com.mam.sgmc.api.openapi;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.mam.sgmc.api.dto.request.SedeRequestDTO;
import br.com.mam.sgmc.model.Sede;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Sedes", description = "API de gerenciamento de sedes")
public interface SedeControllerOpenAPI {

    @Operation(summary = "Lista todas as sedes", description = "Retorna uma lista de todas as sedes cadastradas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sedes listadas com sucesso")
    })
    public ResponseEntity<Page<Sede>> listarSedesComFiltros(
            String nome, String cidade, String uf, String pais, Pageable pageable);

    @Operation(summary = "Busca uma sede por ID", description = "Retorna uma sede pelo seu identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sede encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Sede não encontrada")
    })
    public ResponseEntity<Sede> buscarPorId(Long id);

    @Operation(summary = "Cria uma nova sede", description = "Adiciona uma nova sede ao sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sede criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    public ResponseEntity<Sede> salvarSede(SedeRequestDTO sedeRequestDTO);

    @Operation(summary = "Atualiza uma sede", description = "Atualiza os dados de uma sede existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sede atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Sede não encontrada"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    public ResponseEntity<Sede> atualizarSede(Long id, SedeRequestDTO sedeRequestDTO);

    @Operation(summary = "Inativa uma sede", description = "Desativa uma sede, marcando-a como inativa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sede inativada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Sede não encontrada")
    })
    public ResponseEntity<Void> inativarSede(@PathVariable Long id);
}
