package br.com.mam.sgmc.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.mam.sgmc.errors.ResourceNotFoundException;
import br.com.mam.sgmc.model.Evento;
import br.com.mam.sgmc.model.localizacao.Local;
import br.com.mam.sgmc.repository.EventoRepository;
import br.com.mam.sgmc.repository.InscricaoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - EventoService")
class EventoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private LocalService localService;

    @Mock
    private InscricaoRepository inscricaoRepository;

    @InjectMocks
    private EventoService eventoService;

    private Evento evento;
    private Local local;

    @BeforeEach
    void setUp() {
        local = new Local();
        local.setId(1L);
        local.setNome("Sede Campestre");
        local.setEndereco("Rua das Flores");
        local.setBairro("Centro");
        local.setNumero("123");
        local.setCodigoPostal("12345-678");
        local.setCapacidade(100);

        evento = new Evento();
        evento.setId(1L);
        evento.setNome("Encontro de Motos");
        evento.setDescricao("Encontro anual");
        evento.setDataInicio(Instant.parse("2026-12-01T10:00:00Z"));
        evento.setDataFim(Instant.parse("2026-12-01T18:00:00Z"));
        evento.setValor(50.0f);
        evento.setLocal(local);
    }

    @Test
    @DisplayName("Deve listar todos os eventos")
    void deveListarEventos() {
        when(eventoRepository.findAll()).thenReturn(List.of(evento));

        List<Evento> eventos = eventoService.listarEventos();

        assertNotNull(eventos);
        assertEquals(1, eventos.size());
        assertEquals("Encontro de Motos", eventos.get(0).getNome());
        verify(eventoRepository).findAll();
    }

    @Test
    @DisplayName("Deve buscar evento por ID com sucesso")
    void deveBuscarEventoPorIdComSucesso() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));

        Evento encontrado = eventoService.buscarPorId(1L);

        assertNotNull(encontrado);
        assertEquals(1L, encontrado.getId());
        assertEquals("Encontro de Motos", encontrado.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar evento por ID inexistente")
    void deveLancarExcecaoAoBuscarEventoPorIdInexistente() {
        when(eventoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> eventoService.buscarPorId(99L));
    }

    @Test
    @DisplayName("Deve criar um evento com sucesso")
    void deveCriarEventoComSucesso() {
        when(localService.criarLocal(any(Local.class))).thenReturn(local);
        when(eventoRepository.save(any(Evento.class))).thenReturn(evento);

        Evento criado = eventoService.criarEvento(evento);

        assertNotNull(criado);
        assertEquals("Encontro de Motos", criado.getNome());
        verify(localService).criarLocal(local);
        verify(eventoRepository).save(evento);
    }

    @Test
    @DisplayName("Deve atualizar um evento com sucesso")
    void deveAtualizarEventoComSucesso() {
        Evento eventoAtualizadoInfo = new Evento();
        eventoAtualizadoInfo.setNome("Encontro de Motos Atualizado");
        eventoAtualizadoInfo.setDescricao("Nova descrição");
        eventoAtualizadoInfo.setDataInicio(evento.getDataInicio());
        eventoAtualizadoInfo.setDataFim(evento.getDataFim());
        eventoAtualizadoInfo.setValor(60.0f);
        eventoAtualizadoInfo.setLocal(local);

        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        when(localService.criarLocal(any(Local.class))).thenReturn(local);
        when(eventoRepository.save(any(Evento.class))).thenReturn(evento);

        Evento atualizado = eventoService.atualizarEvento(1L, eventoAtualizadoInfo);

        assertNotNull(atualizado);
        assertEquals("Encontro de Motos Atualizado", atualizado.getNome());
        assertEquals("Nova descrição", atualizado.getDescricao());
        assertEquals(60.0f, atualizado.getValor());
        verify(eventoRepository).save(evento);
    }

    @Test
    @DisplayName("Deve deletar um evento com sucesso")
    void deveDeletarEventoComSucesso() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento));
        doNothing().when(eventoRepository).delete(evento);

        assertDoesNotThrow(() -> eventoService.deletarEvento(1L));

        verify(eventoRepository).delete(evento);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar evento com ID inexistente")
    void deveLancarExcecaoAoAtualizarEventoComIdInexistente() {
        when(eventoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
            eventoService.atualizarEvento(99L, evento)
        );
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar evento com ID inexistente")
    void deveLancarExcecaoAoDeletarEventoComIdInexistente() {
        when(eventoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
            eventoService.deletarEvento(99L)
        );
    }

    @Test
    @DisplayName("Deve inscrever membros em um evento com sucesso")
    void deveInscreverMembrosComSucesso() {
        br.com.mam.sgmc.model.Membro membro = new br.com.mam.sgmc.model.Membro();
        membro.setId(1L);
        membro.setNome("João");

        br.com.mam.sgmc.model.pk.InscricaoPk pk = new br.com.mam.sgmc.model.pk.InscricaoPk(evento, membro);
        br.com.mam.sgmc.model.Inscricao inscricao = new br.com.mam.sgmc.model.Inscricao();
        inscricao.setPk(pk);
        inscricao.setDataInscricao(new java.sql.Date(System.currentTimeMillis()));

        List<br.com.mam.sgmc.model.Inscricao> inscricoes = List.of(inscricao);

        when(inscricaoRepository.saveAll(inscricoes)).thenReturn(inscricoes);

        List<br.com.mam.sgmc.model.Inscricao> resultado = eventoService.inscreverMembros(inscricoes);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(inscricaoRepository).saveAll(inscricoes);
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao listar eventos sem dados")
    void deveRetornarListaVaziaAoListarEventosSemDados() {
        when(eventoRepository.findAll()).thenReturn(List.of());

        List<Evento> eventos = eventoService.listarEventos();

        assertNotNull(eventos);
        assertTrue(eventos.isEmpty());
    }
}

