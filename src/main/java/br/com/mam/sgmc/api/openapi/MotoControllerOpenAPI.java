package br.com.mam.sgmc.api.openapi;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.mam.sgmc.api.dto.request.MotoRequestDTO;
import br.com.mam.sgmc.api.dto.response.MotoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Motos", description = "API de gerenciamento de motos")
public interface MotoControllerOpenAPI {

    @Operation(summary = "Cria uma nova moto", description = "Adiciona uma nova moto ao sistema associada a um membro.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Moto criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Membro não encontrado"),
            @ApiResponse(responseCode = "409", description = "Placa já cadastrada")
    })
    ResponseEntity<Void> criarMoto(@RequestBody @Valid MotoRequestDTO motoRequestDTO);

    @Operation(summary = "Lista todas as motos", description = "Retorna uma lista de todas as motos cadastradas, com suporte a filtros.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Motos listadas com sucesso")
    })
    List<MotoResponseDTO> listarMotos(
            @RequestParam(required = false) Long idMembro,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String seguro);

    @Operation(summary = "Busca uma moto por placa", description = "Retorna os detalhes de uma moto pelo seu número de placa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Moto encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Moto não encontrada")
    })
    ResponseEntity<MotoResponseDTO> buscarPorPlaca(@PathVariable String placa);

    @Operation(summary = "Atualiza uma moto", description = "Atualiza os dados de uma moto existente pelo seu número de placa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Moto atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Moto não encontrada ou membro não encontrado"),
            @ApiResponse(responseCode = "409", description = "Moto não pode ser associada a um membro inativo")
    })
    ResponseEntity<MotoResponseDTO> atualizarMoto(
            @PathVariable String placa,
            @RequestBody @Valid MotoRequestDTO motoRequestDTO);

    @Operation(summary = "Deleta uma moto", description = "Remove uma moto do sistema associada a um membro.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Moto removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Moto não encontrada"),
            @ApiResponse(responseCode = "409", description = "Moto não pertence ao membro informado")
    })
    ResponseEntity<Void> deletarMoto(@PathVariable Long idMembro, @PathVariable String placa);
}
