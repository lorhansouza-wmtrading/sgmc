package br.com.mam.sgmc.api.controller;

import br.com.mam.sgmc.api.dto.request.PosseRequestDTO;
import br.com.mam.sgmc.api.dto.response.PosseResponseDTO;
import br.com.mam.sgmc.api.openapi.PosseControllerOpenAPI;
import br.com.mam.sgmc.model.Posse;
import br.com.mam.sgmc.services.PosseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PosseController implements PosseControllerOpenAPI {

    private final PosseService posseService;

    @Override
    @PreAuthorize("hasAnyRole('admin', 'diretoria') or @sgmcSecurity.isSelf(#id)")
    @GetMapping("/membros/{id}/posses")
    public ResponseEntity<List<PosseResponseDTO>> listarPossesPorMembro(@PathVariable("id") Long id) {
        List<PosseResponseDTO> response = posseService.listarPossesPorMembro(id).stream()
                .map(PosseResponseDTO::toResponseDTO)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Override
    @PreAuthorize("hasAnyRole('admin', 'diretoria')")
    @PostMapping("/membros/{id}/posses")
    public ResponseEntity<Void> registrarPosse(@PathVariable("id") Long id, @RequestBody @Valid PosseRequestDTO posseRequestDTO) {
        posseService.salvarPosse(
                id,
                posseRequestDTO.getIdCargo(),
                posseRequestDTO.getDataInicio(),
                posseRequestDTO.getDataFim()
        );
        String location = ServletUriComponentsBuilder.fromCurrentRequest().path("")
                .buildAndExpand().toUriString();
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, location).build();
    }
}
