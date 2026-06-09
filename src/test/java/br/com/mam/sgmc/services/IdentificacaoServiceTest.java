package br.com.mam.sgmc.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import br.com.mam.sgmc.errors.ResourceNotFoundException;
import br.com.mam.sgmc.model.Identificacao;
import br.com.mam.sgmc.model.Membro;
import br.com.mam.sgmc.model.localizacao.Pais;
import br.com.mam.sgmc.repository.IdentificacaoRepository;
import br.com.mam.sgmc.repository.MembroRepository;
import br.com.mam.sgmc.repository.PaisRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - IdentificacaoService")
class IdentificacaoServiceTest {

    @Mock
    private IdentificacaoRepository identificacaoRepository;

    @Mock
    private PaisRepository paisRepository;

    @Mock
    private MembroRepository membroRepository;

    @InjectMocks
    private IdentificacaoService identificacaoService;

    private Identificacao identificacao;
    private Pais pais;
    private Membro membro;

    @BeforeEach
    void setUp() {
        pais = new Pais();
        pais.setSigla("BR");
        pais.setNome("Brasil");

        membro = new Membro();
        membro.setId(1L);
        membro.setNome("João Silva");

        identificacao = new Identificacao();
        identificacao.setIdIdentificacao(1L);
        identificacao.setTipo("CPF");
        identificacao.setIdentidade("12345678909");
        identificacao.setEmissor("SSP");
        identificacao.setDataEmissao(Date.valueOf(LocalDate.of(2010, 1, 1)));
    }

    // ========== salvar ==========

    @Test
    @DisplayName("Deve salvar identificação com sucesso")
    void deveSalvarIdentificacaoComSucesso() {
        when(paisRepository.findById("BR")).thenReturn(Optional.of(pais));
        when(membroRepository.findById(1L)).thenReturn(Optional.of(membro));
        when(identificacaoRepository.save(any(Identificacao.class))).thenReturn(identificacao);

        Identificacao salva = identificacaoService.salvar(identificacao, "BR", 1L);

        assertNotNull(salva);
        assertEquals("CPF", salva.getTipo());
        assertEquals("12345678909", salva.getIdentidade());
        verify(identificacaoRepository).save(any(Identificacao.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar identificação no Brasil sem CPF")
    void deveLancarExcecaoAoSalvarIdentificacaoNoBrasilSemCPF() {
        identificacao.setTipo("RG");

        assertThrows(ResponseStatusException.class, () ->
            identificacaoService.salvar(identificacao, "BR", 1L)
        );
        verify(identificacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve salvar identificação fora do Brasil com tipo diferente de CPF")
    void deveSalvarIdentificacaoForaDoBrasilComTipoDiferente() {
        Pais paisEstrangeiro = new Pais();
        paisEstrangeiro.setSigla("US");
        paisEstrangeiro.setNome("Estados Unidos");

        identificacao.setTipo("PASSPORT");

        when(paisRepository.findById("US")).thenReturn(Optional.of(paisEstrangeiro));
        when(membroRepository.findById(1L)).thenReturn(Optional.of(membro));
        when(identificacaoRepository.save(any(Identificacao.class))).thenReturn(identificacao);

        Identificacao salva = identificacaoService.salvar(identificacao, "US", 1L);

        assertNotNull(salva);
        assertEquals("PASSPORT", salva.getTipo());
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar identificação com país inexistente")
    void deveLancarExcecaoAoSalvarIdentificacaoComPaisInexistente() {
        when(paisRepository.findById("XX")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
            identificacaoService.salvar(identificacao, "XX", 1L)
        );
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar identificação com membro inexistente")
    void deveLancarExcecaoAoSalvarIdentificacaoComMembroInexistente() {
        when(paisRepository.findById("BR")).thenReturn(Optional.of(pais));
        when(membroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
            identificacaoService.salvar(identificacao, "BR", 99L)
        );
    }

    // ========== listarTodos ==========

    @Test
    @DisplayName("Deve listar todas as identificações")
    void deveListarTodasIdentificacoes() {
        when(identificacaoRepository.findAll()).thenReturn(List.of(identificacao));

        List<Identificacao> identificacoes = identificacaoService.listarTodos();

        assertNotNull(identificacoes);
        assertEquals(1, identificacoes.size());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há identificações")
    void deveRetornarListaVaziaQuandoNaoHaIdentificacoes() {
        when(identificacaoRepository.findAll()).thenReturn(List.of());

        List<Identificacao> identificacoes = identificacaoService.listarTodos();

        assertNotNull(identificacoes);
        assertTrue(identificacoes.isEmpty());
    }

    // ========== buscarPorId ==========

    @Test
    @DisplayName("Deve buscar identificação por ID com sucesso")
    void deveBuscarIdentificacaoPorIdComSucesso() {
        when(identificacaoRepository.findById(1L)).thenReturn(Optional.of(identificacao));

        Identificacao encontrada = identificacaoService.buscarPorId(1L);

        assertNotNull(encontrada);
        assertEquals(1L, encontrada.getIdIdentificacao());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar identificação por ID inexistente")
    void deveLancarExcecaoAoBuscarIdentificacaoPorIdInexistente() {
        when(identificacaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
            identificacaoService.buscarPorId(99L)
        );
    }

    // ========== deletar ==========

    @Test
    @DisplayName("Deve deletar identificação com sucesso")
    void deveDeletarIdentificacaoComSucesso() {
        when(identificacaoRepository.findById(1L)).thenReturn(Optional.of(identificacao));
        doNothing().when(identificacaoRepository).delete(identificacao);

        assertDoesNotThrow(() -> identificacaoService.deletar(1L));

        verify(identificacaoRepository).delete(identificacao);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar identificação com ID inexistente")
    void deveLancarExcecaoAoDeletarIdentificacaoComIdInexistente() {
        when(identificacaoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
            identificacaoService.deletar(99L)
        );
    }
}
