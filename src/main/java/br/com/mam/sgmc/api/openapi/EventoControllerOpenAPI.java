package br.com.mam.sgmc.api.openapi;

import java.util.List;

import org.springframework.http.ResponseEntity;

import br.com.mam.sgmc.api.dto.request.EventoRequestDTO;
import br.com.mam.sgmc.api.dto.request.InscricaoRequestDTO;
import br.com.mam.sgmc.api.dto.response.EventoResponseDTO;
import br.com.mam.sgmc.api.dto.response.InscricaoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Eventos", description = "API de gerenciamento de eventos")
public interface EventoControllerOpenAPI {

    @Operation(summary = "Lista todos os eventos", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{}"))),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{}")))
    })
    ResponseEntity<List<EventoResponseDTO>> listarEventos();

    @Operation(summary = "Busca um evento por id", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{}"))),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{}")))
    })
    ResponseEntity<EventoResponseDTO> buscarPorId(Long id);

    @Operation(summary = "Cria um evento", responses = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{}"))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{}")))
    })
    ResponseEntity<EventoResponseDTO> criarEvento(@Valid EventoRequestDTO evento);

    @Operation(summary = "Atualiza um evento", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{}"))),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{}"))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{}")))
    })
    ResponseEntity<EventoResponseDTO> atualizarEvento(Long id, @Valid EventoRequestDTO evento);

    @Operation(summary = "Deleta um evento", responses = {
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{}"))),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{}")))
    })
    ResponseEntity<Void> deletarEvento(Long id);

    @Operation(summary = "Inscreve um ou mais membros (com ou sem suas motos) em um evento", responses = {
            @ApiResponse(responseCode = "201", description = "Created", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "[]"))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{}"))),
            @ApiResponse(responseCode = "404", description = "Não encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{}")))
    })
    ResponseEntity<List<InscricaoResponseDTO>> inscreverMembros(Long id, @Valid List<InscricaoRequestDTO> inscricoes);

    @Operation(summary = "Lista todos os membros inscritos em um evento", responses = {
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "[]"))),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{}")))
    })
    ResponseEntity<List<InscricaoResponseDTO>> listarInscritos(Long id);
}
