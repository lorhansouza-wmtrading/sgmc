package br.com.mam.sgmc.api.openapi;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.mam.sgmc.api.dto.request.MembroRequestDTO;
import br.com.mam.sgmc.api.dto.response.MembroResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Membros", description = "API de gerenciamento de membros")
public interface MembroControllerOpenAPI {

    @Operation(summary = "Lista todos os membros", description = "Retorna uma lista de todos os membros cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membros listados com sucesso")
    })
    ResponseEntity<List<MembroResponseDTO>> listarMembros(@RequestParam(required = false) Integer ativo);

    @Operation(summary = "Busca um membro por ID", description = "Retorna um membro pelo seu identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membro encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Membro não encontrado")
    })
    ResponseEntity<MembroResponseDTO> buscarPorId(@PathVariable Long id);

    @Operation(summary = "Atualiza um membro", description = "Atualiza os dados de um membro existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Membro atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Membro não encontrado"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    ResponseEntity<MembroResponseDTO> atualizarMembro(@PathVariable Long id, @RequestBody @Valid MembroRequestDTO membroDTO);

    @Operation(summary = "Cria um novo membro", description = "Adiciona um novo membro ao sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Membro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    ResponseEntity<String> criarMembro(@RequestBody @Valid MembroRequestDTO membroDTO);

    @Operation(summary = "Inativa um membro", description = "Desativa um membro, marcando-o como inativo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Membro inativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Membro não encontrado")
    })
    ResponseEntity<Void> inativarMembro(@PathVariable Long id);
}
