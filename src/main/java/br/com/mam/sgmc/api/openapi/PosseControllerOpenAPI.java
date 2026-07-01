package br.com.mam.sgmc.api.openapi;

import br.com.mam.sgmc.api.dto.request.PosseRequestDTO;
import br.com.mam.sgmc.api.dto.response.PosseResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.security.core.parameters.P;

@Tag(name = "Posses", description = "API de gerenciamento do histórico de mandatos (posses) de cargos dos membros")
public interface PosseControllerOpenAPI {

    @Operation(summary = "Lista o histórico de posses de um membro", description = "Retorna todas as posses de cargos que um determinado membro já ocupou ou ocupa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Posses listadas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Membro não encontrado")
    })
    ResponseEntity<List<PosseResponseDTO>> listarPossesPorMembro(@P("id") @PathVariable("id") Long id);

    @Operation(summary = "Registra uma nova posse de cargo para o membro", description = "Atribui um cargo a um membro, definindo a data de início e fim da posse.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Posse registrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Membro ou cargo não encontrado")
    })
    ResponseEntity<Void> registrarPosse(@PathVariable("id") Long id, @RequestBody @Valid PosseRequestDTO posseRequestDTO);
}
