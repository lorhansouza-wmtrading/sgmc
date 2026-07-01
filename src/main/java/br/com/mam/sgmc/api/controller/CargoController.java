package br.com.mam.sgmc.api.controller;

import br.com.mam.sgmc.api.dto.request.CargoRequestDTO;
import br.com.mam.sgmc.api.dto.response.CargoResponseDTO;
import br.com.mam.sgmc.api.openapi.CargoControllerOpenAPI;
import br.com.mam.sgmc.model.Cargo;
import br.com.mam.sgmc.services.CargoService;
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
@RequestMapping("/cargos")
@RequiredArgsConstructor
public class CargoController implements CargoControllerOpenAPI {

    private final CargoService cargoService;

    @Override
    @PreAuthorize("hasAnyRole('admin', 'diretoria')")
    @PostMapping
    public ResponseEntity<Void> salvarCargo(@RequestBody @Valid CargoRequestDTO cargoRequestDTO) {
        Cargo cargoSalvo = cargoService.salvarCargo(CargoRequestDTO.toCargo(cargoRequestDTO));
        String location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(cargoSalvo.getId()).toUriString();
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, location).build();
    }

    @Override
    @PreAuthorize("hasAnyRole('admin', 'diretoria', 'membro')")
    @GetMapping
    public ResponseEntity<List<CargoResponseDTO>> listarCargos() {
        List<CargoResponseDTO> cargos = cargoService.listarCargos().stream()
                .map(CargoResponseDTO::toResponseDTO)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(cargos);
    }

    @Override
    @PreAuthorize("hasAnyRole('admin', 'diretoria', 'membro')")
    @GetMapping("/{id}")
    public ResponseEntity<CargoResponseDTO> buscarPorId(@PathVariable Long id) {
        Cargo cargo = cargoService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(CargoResponseDTO.toResponseDTO(cargo));
    }
}
