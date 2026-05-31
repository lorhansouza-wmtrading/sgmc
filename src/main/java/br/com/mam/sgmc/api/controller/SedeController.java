package br.com.mam.sgmc.api.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.mam.sgmc.api.dto.request.SedeRequestDTO;
import br.com.mam.sgmc.api.dto.response.SedeResponseDTO;
import br.com.mam.sgmc.model.Sede;
import br.com.mam.sgmc.services.SedeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/sedes")
@RequiredArgsConstructor
public class SedeController {

    private final SedeService sedeService;

    @PostMapping
    public ResponseEntity<Void> salvarSede(@RequestBody SedeRequestDTO sedeRequestDTO) {
        Sede sedeSalva = sedeService.salvarSede(
            SedeRequestDTO.toSede(sedeRequestDTO),
            sedeRequestDTO.getCidade(), 
            sedeRequestDTO.getUfSigla(), 
            sedeRequestDTO.getPais()
        );
        String location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(sedeSalva.getId()).toUriString();
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, location).build();
    }

    @GetMapping("/buscar/{id}")
    public ResponseEntity<SedeResponseDTO> buscarPorId(@PathVariable Long id) {
        Sede sede = this.sedeService.buscarPorId(id);
        SedeResponseDTO response = SedeResponseDTO.toResponseDTO(sede);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<SedeResponseDTO>> listarSedesComFiltros(
        @RequestParam(required = false) String nome,
        @RequestParam(required = false) String cidade,
        @RequestParam(required = false) String ufSigla,
        @RequestParam(required = false) String pais) {

        List<SedeResponseDTO> response = this.sedeService.listarSedesComFiltros(nome, cidade, ufSigla, pais)
            .stream()
            .map(SedeResponseDTO::toResponseDTO)
            .toList();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SedeResponseDTO> atualizarSede(@PathVariable Long id, @RequestBody @Valid SedeRequestDTO sedeRequestDTO) {
        Sede sede = SedeRequestDTO.toSede(sedeRequestDTO);
        Sede sedeSalva = this.sedeService.atualizarSede(id, sede);
        SedeResponseDTO response = SedeResponseDTO.toResponseDTO(sedeSalva);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/{id}/inativar")
    public ResponseEntity<Void> inativarSede(@PathVariable Long id) {
        this.sedeService.inativarSede(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
