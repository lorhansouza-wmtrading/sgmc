package br.com.mam.sgmc.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.mam.sgmc.errors.ResourceNotFoundException;
import br.com.mam.sgmc.model.Evento;
import br.com.mam.sgmc.model.Participacao;
import br.com.mam.sgmc.model.localizacao.Local;
import br.com.mam.sgmc.repository.EventoRepository;
import br.com.mam.sgmc.repository.ParticipacaoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventoService {

    private final EventoRepository eventoRepository;
    private final LocalService localService;
    private final ParticipacaoRepository participacaoRepository;

    @Transactional
    public List<Participacao> inscreverMembros(List<Participacao> participacoes) {
        return this.participacaoRepository.saveAll(participacoes);
    }

    public List<Evento> listarEventos(){
        return this.eventoRepository.findAll();
    }

    public Evento buscarPorId(Long id) {
        return this.eventoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado"));
    }

    public Evento criarEvento(Evento evento){
        Local local = this.localService.criarLocal(evento.getLocal());
        evento.setLocal(local);
        return this.eventoRepository.save(evento);
    }

    @Transactional
    public Evento atualizarEvento(Long id, Evento eventoAtualizado) {
        Evento eventoExistente = this.buscarPorId(id);

        eventoExistente.setNome(eventoAtualizado.getNome());
        eventoExistente.setDescricao(eventoAtualizado.getDescricao());
        eventoExistente.setDataInicio(eventoAtualizado.getDataInicio());
        eventoExistente.setDataFim(eventoAtualizado.getDataFim());
        eventoExistente.setValor(eventoAtualizado.getValor());

        if (eventoAtualizado.getLocal() != null) {
            Local local = this.localService.criarLocal(eventoAtualizado.getLocal());
            eventoExistente.setLocal(local);
        }

        return this.eventoRepository.save(eventoExistente);
    }

    public void deletarEvento(Long id) {
        Evento evento = this.buscarPorId(id);
        this.eventoRepository.delete(evento);
    }
}
