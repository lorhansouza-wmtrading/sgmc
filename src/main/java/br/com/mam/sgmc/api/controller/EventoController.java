package br.com.mam.sgmc.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mam.sgmc.api.openapi.EventoControllerOpenAPI;
import br.com.mam.sgmc.api.dto.request.EventoRequestDTO;
import br.com.mam.sgmc.api.dto.request.InscricaoRequestDTO;
import br.com.mam.sgmc.api.dto.response.EventoResponseDTO;
import br.com.mam.sgmc.api.dto.response.InscricaoResponseDTO;
import br.com.mam.sgmc.model.Evento;
import br.com.mam.sgmc.model.Membro;
import br.com.mam.sgmc.model.Inscricao;
import br.com.mam.sgmc.model.pk.InscricaoPk;
import br.com.mam.sgmc.model.moto.Moto;
import br.com.mam.sgmc.services.EventoService;
import br.com.mam.sgmc.services.MembroService;
import br.com.mam.sgmc.services.MotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@PreAuthorize("hasAnyRole('PRESIDENT','SECRETARY')")
@RestController
@RequestMapping(value = "/eventos")
@RequiredArgsConstructor
public class EventoController implements EventoControllerOpenAPI {

    private final EventoService eventoService;
    private final MembroService membroService;
    private final MotoService motoService;

    @GetMapping
    public ResponseEntity<List<EventoResponseDTO>> listarEventos() {
        List<Evento> eventos = this.eventoService.listarEventos();
        List<EventoResponseDTO> responses = eventos.stream()
                .map(EventoResponseDTO::toResponseDTO)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoResponseDTO> buscarPorId(@PathVariable Long id) {
        Evento evento = this.eventoService.buscarPorId(id);
        EventoResponseDTO response = EventoResponseDTO.toResponseDTO(evento);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<EventoResponseDTO> criarEvento(@RequestBody @Valid EventoRequestDTO eventoRequestDTO) {
        Evento evento = Evento.fromRequestDTO(eventoRequestDTO);
        EventoResponseDTO eventoCriado = EventoResponseDTO.toResponseDTO(this.eventoService.criarEvento(evento));
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoCriado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoResponseDTO> atualizarEvento(@PathVariable Long id, @RequestBody @Valid EventoRequestDTO eventoRequestDTO) {
        Evento evento = Evento.fromRequestDTO(eventoRequestDTO);
        Evento eventoAtualizado = this.eventoService.atualizarEvento(id, evento);
        EventoResponseDTO response = EventoResponseDTO.toResponseDTO(eventoAtualizado);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEvento(@PathVariable Long id) {
        this.eventoService.deletarEvento(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/inscricoes")
    public ResponseEntity<List<InscricaoResponseDTO>> inscreverMembros(
            @PathVariable Long id,
            @RequestBody @Valid List<InscricaoRequestDTO> inscricoesRequestDTO) {
        
        Evento evento = this.eventoService.buscarPorId(id);
        
        List<Inscricao> inscricoes = inscricoesRequestDTO.stream().map(dto -> {
            Membro membro = this.membroService.buscarPorId(dto.getIdMembro());
            
            Moto moto = null;
            if (dto.getPlacaMoto() != null && !dto.getPlacaMoto().isBlank()) {
                moto = this.motoService.buscarPorPlaca(dto.getPlacaMoto());
                if (moto.getMembro() == null || !moto.getMembro().getId().equals(membro.getId())) {
                    throw new IllegalArgumentException("A moto com placa " + dto.getPlacaMoto() + " não pertence ao membro com ID " + membro.getId());
                }
            }
            
            InscricaoPk pk = new InscricaoPk(evento, membro);
            Inscricao inscricao = new Inscricao();
            inscricao.setPk(pk);
            inscricao.setMoto(moto);
            inscricao.setDataInscricao(new java.sql.Date(System.currentTimeMillis()));
            return inscricao;
        }).toList();
        
        List<Inscricao> inscricoesSalvas = this.eventoService.inscreverMembros(inscricoes);
        List<InscricaoResponseDTO> response = inscricoesSalvas.stream()
                .map(InscricaoResponseDTO::toResponseDTO)
                .toList();
                
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/inscricoes")
    public ResponseEntity<List<InscricaoResponseDTO>> listarInscritos(@PathVariable Long id) {
        Evento evento = this.eventoService.buscarPorId(id);
        List<InscricaoResponseDTO> response = evento.getInscricoes().stream()
                .map(InscricaoResponseDTO::toResponseDTO)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

