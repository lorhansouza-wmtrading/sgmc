package br.com.mam.sgmc.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.mam.sgmc.api.dto.response.CidadeResponseDTO;
import br.com.mam.sgmc.api.dto.response.PaisResponseDTO;
import br.com.mam.sgmc.api.dto.response.UfResponseDTO;
import br.com.mam.sgmc.services.LocalizacaoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/localizacao")
@RequiredArgsConstructor
public class LocalizacaoController {

    private final LocalizacaoService localizacaoService;

    @GetMapping("/paises")
    public ResponseEntity<List<PaisResponseDTO>> listarPaisesComFiltros(
        @RequestParam(required = false) String sigla,
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) String continente) {
        List<PaisResponseDTO> response = this.localizacaoService.listarPaisesComFiltros(sigla, nome, continente)
            .stream()
            .map(PaisResponseDTO::toResponseDTO)
            .toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/ufs")
    public ResponseEntity<List<UfResponseDTO>> listarUfsComFiltros(
        @RequestParam(required = false) String sigla,
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) String regiao,
        @RequestParam(required = false) String pais) {
        List<UfResponseDTO> response = this.localizacaoService.listarUfsComFiltros(sigla, nome, regiao, pais)
            .stream()
            .map(UfResponseDTO::toResponseDTO)
            .toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/cidades")
    public ResponseEntity<List<CidadeResponseDTO>> listarCidades(
        @RequestParam(required = false) String ufSigla,
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) String pais) {
        List<CidadeResponseDTO> response = this.localizacaoService.listarCidadesComFiltros(ufSigla, nome, pais)
            .stream()
            .map(CidadeResponseDTO::toResponseDTO)
            .toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
