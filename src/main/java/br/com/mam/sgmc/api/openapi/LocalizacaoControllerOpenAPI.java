package br.com.mam.sgmc.api.openapi;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.mam.sgmc.api.dto.response.CidadeResponseDTO;
import br.com.mam.sgmc.api.dto.response.PaisResponseDTO;
import br.com.mam.sgmc.api.dto.response.UfResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Localização")
public interface LocalizacaoControllerOpenAPI {

    @Operation(summary = "Listar Paises")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paises listados com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paises nao encontrados")
    })
    public ResponseEntity<List<PaisResponseDTO>> listarPaisesComFiltros(
            @RequestParam(required = false) String sigla,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String continente);

    @Operation(summary = "Listar UFs")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "UFs listadas com sucesso"),
            @ApiResponse(responseCode = "404", description = "UFs nao encontradas")
    })
    public ResponseEntity<List<UfResponseDTO>> listarUfsComFiltros(
            @RequestParam(required = false) String sigla,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String regiao,
            @RequestParam(required = false) String pais);

    @Operation(summary = "Listar Cidades")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cidades listadas com sucesso"),
            @ApiResponse(responseCode = "404", description = "Cidades nao encontradas")
    })
    public ResponseEntity<List<CidadeResponseDTO>> listarCidades(
            @RequestParam(required = false) String ufSigla,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String pais);

}
