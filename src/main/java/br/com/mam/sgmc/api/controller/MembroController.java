package br.com.mam.sgmc.api.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.mam.sgmc.api.dto.request.MembroRequestDTO;
import br.com.mam.sgmc.api.dto.response.MembroResponseDTO;
import br.com.mam.sgmc.model.Membro;
import br.com.mam.sgmc.services.MembroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/membros")
@RequiredArgsConstructor
public class MembroController {

    private final MembroService membroService;

    @PostMapping
    public ResponseEntity<?> criarMembro(@RequestBody MembroRequestDTO membroDTO) throws ResponseStatusException {
        Membro membro = new Membro();

        membro.setNome(membroDTO.getNome());
        membro.setApelido(membroDTO.getApelido());
        membro.setSexo(membroDTO.getSexo());
        membro.setEmail(membroDTO.getEmail());
        membro.setTelefone(membroDTO.getTelefone());
        membro.setDataNascimento(Date.valueOf(membroDTO.getDataNascimento()));
        membro.setEhBatizado(membroDTO.getCodigoBatizado());
        membro.setTemEscudo(membroDTO.getCodigoEscudo());
        membro.setAtivo(membroDTO.getCodigoAtivo());
        membro.setTamanhoCamisa(membroDTO.getTamanhoCamisa());
        membro.setDataAdmissao(Date.valueOf(membroDTO.getDataAdmissao()));

        membro = this.membroService.salvarMembro(membro);

        String location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(membro.getId()).toUriString();
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, location).build();
    }

    @GetMapping
    public ResponseEntity<List<MembroResponseDTO>> listarMembros() {
        List<Membro> membros = membroService.listarMembros();
        List<MembroResponseDTO> responses = membros.stream()
                .map(MembroResponseDTO::toResponseDTO)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Membro> buscarPorId(@PathVariable Long id) {
        Membro membro = membroService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(membro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Membro> atualizarMembro(@PathVariable Long id, @RequestBody @Valid MembroRequestDTO membroDTO) {
        Membro membro = new Membro();
        membro.setNome(membroDTO.getNome());
        membro.setApelido(membroDTO.getApelido());
        membro.setSexo(membroDTO.getSexo());
        membro.setEmail(membroDTO.getEmail());
        membro.setTelefone(membroDTO.getTelefone());
        membro.setDataNascimento(Date.valueOf(membroDTO.getDataNascimento()));
        membro.setEhBatizado(membroDTO.getCodigoBatizado());
        membro.setTemEscudo(membroDTO.getCodigoEscudo());
        membro.setAtivo(membroDTO.getCodigoAtivo());
        membro.setTamanhoCamisa(membroDTO.getTamanhoCamisa());
        membro.setDataAdmissao(Date.valueOf(membroDTO.getDataAdmissao()));

        membro = this.membroService.atualizarMembro(membro, id);
        return ResponseEntity.status(HttpStatus.OK).body(membro);
    }
}
