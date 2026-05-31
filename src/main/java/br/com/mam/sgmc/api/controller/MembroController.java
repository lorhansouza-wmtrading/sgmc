package br.com.mam.sgmc.api.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.mam.sgmc.api.dto.request.FichaMedicaRequestDTO;
import br.com.mam.sgmc.api.dto.request.MembroRequestDTO;
import br.com.mam.sgmc.api.dto.response.MembroResponseDTO;
import br.com.mam.sgmc.api.openapi.MembroControllerOpenAPI;
import br.com.mam.sgmc.errors.ResourceNotFoundException;
import br.com.mam.sgmc.model.Identificacao;
import br.com.mam.sgmc.model.Membro;
import br.com.mam.sgmc.model.localizacao.Pais;
import br.com.mam.sgmc.services.MembroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/membros")
@RequiredArgsConstructor
public class MembroController implements MembroControllerOpenAPI {

    private final MembroService membroService;

    @PostMapping
    public ResponseEntity<String> criarMembro(@RequestBody @Valid MembroRequestDTO membroDTO)
            throws ResourceNotFoundException {
        Membro membro = new Membro();

        membro.setNome(membroDTO.getNome());
        membro.setApelido(membroDTO.getApelido());
        membro.setSexo(membroDTO.getSexo());
        membro.setEmail(membroDTO.getEmail());
        membro.setTelefone(membroDTO.getTelefone());
        membro.setDataNascimento(Date.valueOf(membroDTO.getDataNascimento()));
        membro.setNacionalidade(membroDTO.getNacionalidade());
        membro.setNaturalidade(membroDTO.getNaturalidade());
        membro.setEhBatizado(membroDTO.getCodigoBatizado());
        membro.setTemEscudo(membroDTO.getCodigoEscudo());
        membro.setAtivo(membroDTO.getCodigoAtivo());
        membro.setTamanhoCamisa(membroDTO.getTamanhoCamisa());
        membro.setDataAdmissao(Date.valueOf(membroDTO.getDataAdmissao()));
        
        if (membroDTO.getIdentidade() != null) {
            membro.setIdentidade(new Identificacao());
            membro.getIdentidade().setTipo(membroDTO.getIdentidade().getTipo());
            membro.getIdentidade().setIdentidade(membroDTO.getIdentidade().getIdentidade());
            membro.getIdentidade().setEmissor(membroDTO.getIdentidade().getEmissor());
            membro.getIdentidade().setDataEmissao(Date.valueOf(membroDTO.getIdentidade().getDataEmissao()));
            membro.getIdentidade().setMembro(membro);
            membro.getIdentidade().setPais(new Pais());
            membro.getIdentidade().getPais().setSigla(membroDTO.getIdentidade().getPaisSigla());
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Identificação não preenchida");
        }
        
        if (membroDTO.getFichaMedica() != null){
            membro.setFichaMedica(FichaMedicaRequestDTO.paraEntidade(membroDTO.getFichaMedica()));
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ficha médica não preenchida");
        }

        membro = this.membroService.salvarMembro(membro, membroDTO.getIdCargo(), membroDTO.getIdSede(), 
                membroDTO.getIdentidade().getPaisSigla());

        String location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(membro.getId()).toUriString();
        return ResponseEntity.status(HttpStatus.CREATED).header(HttpHeaders.LOCATION, location).build();
    }

    @GetMapping
    public ResponseEntity<List<MembroResponseDTO>> listarMembros(@RequestParam(required = false) Integer ativo) {
        List<Membro> membros = membroService.listarMembros(ativo);
        List<MembroResponseDTO> responses = membros.stream()
                .map(MembroResponseDTO::toResponseDTO)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MembroResponseDTO> buscarPorId(@PathVariable Long id) {
        Membro membro = membroService.buscarPorId(id);
        return ResponseEntity.status(HttpStatus.OK).body(MembroResponseDTO.toResponseDTO(membro));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MembroResponseDTO> atualizarMembro(@PathVariable Long id, @RequestBody @Valid MembroRequestDTO membroDTO) throws ResourceNotFoundException{
        Membro membro = new Membro();
        membro.setNome(membroDTO.getNome());
        membro.setApelido(membroDTO.getApelido());
        membro.setSexo(membroDTO.getSexo());
        membro.setEmail(membroDTO.getEmail());
        membro.setTelefone(membroDTO.getTelefone());
        membro.setDataNascimento(Date.valueOf(membroDTO.getDataNascimento()));
        membro.setNacionalidade(membroDTO.getNacionalidade());
        membro.setNaturalidade(membroDTO.getNaturalidade());
        membro.setEhBatizado(membroDTO.getCodigoBatizado());
        membro.setTemEscudo(membroDTO.getCodigoEscudo());
        membro.setAtivo(membroDTO.getCodigoAtivo());
        membro.setTamanhoCamisa(membroDTO.getTamanhoCamisa());
        membro.setDataAdmissao(Date.valueOf(membroDTO.getDataAdmissao()));

        if (membroDTO.getIdentidade() != null) {
            membro.setIdentidade(new Identificacao());
            membro.getIdentidade().setTipo(membroDTO.getIdentidade().getTipo());
            membro.getIdentidade().setIdentidade(membroDTO.getIdentidade().getIdentidade());
            membro.getIdentidade().setEmissor(membroDTO.getIdentidade().getEmissor());
            membro.getIdentidade().setDataEmissao(Date.valueOf(membroDTO.getIdentidade().getDataEmissao()));
            membro.getIdentidade().setMembro(membro);
            membro.getIdentidade().setPais(new Pais());
            membro.getIdentidade().getPais().setSigla(membroDTO.getIdentidade().getPaisSigla());
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Identificação não preenchida");
        }

        if (membroDTO.getFichaMedica() != null){
            membro.setFichaMedica(FichaMedicaRequestDTO.paraEntidade(membroDTO.getFichaMedica()));
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ficha médica não preenchida");
        }

        membro = this.membroService.atualizarMembro(membro, id, membroDTO.getIdCargo(), membroDTO.getIdSede(),
                membro.getIdentidade().getPais().getSigla());
        return ResponseEntity.status(HttpStatus.OK).body(MembroResponseDTO.toResponseDTO(membro));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativarMembro(@PathVariable Long id) {
        this.membroService.inativarMembro(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
