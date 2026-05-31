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

@Tag(name = "Motos", description = "API de gerenciamento de motos")
public interface MotoControllerOpenAPI {

    @Operation(summary = "Salvar moto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Moto salva com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Membro não encontrado"),
            @ApiResponse(responseCode = "409", description = "Placa já cadastrada")
    })
    public ResponseEntity<Void> criarMoto(@RequestBody MotoRequestDTO motoRequestDTO);

    @Operation(summary = "Listar motos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de motos"),
    })
    public List<MotoResponseDTO> listarMotos(
            @RequestParam(required = false) Long idMembro,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String seguro
    );

    @Operation(summary = "Atualizar moto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Moto atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Moto não encontrada (placa errada) ou membro não encontrado"),
            @ApiResponse(responseCode = "409", description = "Moto não pode ser associada a um membro inativo")
    })
    public ResponseEntity<MotoResponseDTO> atualizarMoto(
            @PathVariable String placa,
            @RequestBody MotoRequestDTO motoRequestDTO);
}
