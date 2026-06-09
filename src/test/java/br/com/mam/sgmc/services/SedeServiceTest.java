package br.com.mam.sgmc.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
import br.com.mam.sgmc.model.Sede;
import br.com.mam.sgmc.model.localizacao.Cidade;
import br.com.mam.sgmc.model.localizacao.Pais;
import br.com.mam.sgmc.model.localizacao.Uf;
import br.com.mam.sgmc.repository.CidadeRepository;
import br.com.mam.sgmc.repository.PaisRepository;
import br.com.mam.sgmc.repository.SedeRepository;
import br.com.mam.sgmc.repository.UfRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - SedeService")
class SedeServiceTest {

    @Mock
    private SedeRepository sedeRepository;

    @Mock
    private CidadeRepository cidadeRepository;

    @Mock
    private PaisRepository paisRepository;

    @Mock
    private UfRepository ufRepository;

    @InjectMocks
    private SedeService sedeService;

    private Sede sede;
    private Cidade cidade;
    private Uf uf;
    private Pais pais;

    @BeforeEach
    void setUp() {
        pais = new Pais();
        pais.setSigla("BR");
        pais.setNome("Brasil");
        pais.setContinente("América do Sul");

        uf = new Uf();
        uf.setUfSigla("SP");
        uf.setNome("São Paulo");
        uf.setRegiao("Sudeste");
        uf.setPais(pais);

        cidade = new Cidade();
        cidade.setId(1L);
        cidade.setNome("São Paulo");
        cidade.setUf(uf);

        sede = new Sede();
        sede.setId(1L);
        sede.setNome("Sede Central");
        sede.setEndereco("Rua Principal, 100");
        sede.setBairro("Centro");
        sede.setNumero("100");
        sede.setCodigoPostal("01001-000");
        sede.setAtiva(true);
        sede.setCidade(cidade);
    }

    // ========== salvarSede ==========

    @Test
    @DisplayName("Deve salvar uma sede com sucesso quando a cidade existe")
    void deveSalvarSedeComSucessoQuandoCidadeExiste() {
        when(cidadeRepository.findByNome("São Paulo")).thenReturn(cidade);
        when(sedeRepository.save(any(Sede.class))).thenReturn(sede);

        Sede sedeSalva = sedeService.salvarSede(sede, "São Paulo", "SP", "Brasil");

        assertNotNull(sedeSalva);
        assertEquals("Sede Central", sedeSalva.getNome());
        assertEquals(cidade, sedeSalva.getCidade());
        verify(sedeRepository).save(sede);
    }

    @Test
    @DisplayName("Deve salvar sede quando cidade não existe mas UF existe")
    void deveSalvarSedeQuandoCidadeNaoExisteMasUfExiste() {
        when(cidadeRepository.findByNome("Campinas")).thenReturn(null);
        when(ufRepository.findByUfSiglaIgnoreCase("SP")).thenReturn(uf);
        when(sedeRepository.save(any(Sede.class))).thenReturn(sede);

        Sede sedeSalva = sedeService.salvarSede(sede, "Campinas", "SP", "Brasil");

        assertNotNull(sedeSalva);
        verify(sedeRepository).save(sede);
    }

    @Test
    @DisplayName("Deve salvar sede quando cidade e UF não existem mas País existe")
    void deveSalvarSedeQuandoCidadeEUfNaoExistemMasPaisExiste() {
        when(cidadeRepository.findByNome("Campinas")).thenReturn(null);
        when(ufRepository.findByUfSiglaIgnoreCase("SP")).thenReturn(null);
        when(paisRepository.findByNome("Brasil")).thenReturn(pais);
        when(sedeRepository.save(any(Sede.class))).thenReturn(sede);

        Sede sedeSalva = sedeService.salvarSede(sede, "Campinas", "SP", "Brasil");

        assertNotNull(sedeSalva);
        verify(sedeRepository).save(sede);
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar sede quando o país não existe")
    void deveLancarExcecaoAoSalvarSedeQuandoPaisNaoExiste() {
        when(cidadeRepository.findByNome("Campinas")).thenReturn(null);
        when(ufRepository.findByUfSiglaIgnoreCase("XX")).thenReturn(null);
        when(paisRepository.findByNome("Inexistente")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () ->
            sedeService.salvarSede(sede, "Campinas", "XX", "Inexistente")
        );
    }

    // ========== buscarPorId ==========

    @Test
    @DisplayName("Deve buscar sede por ID com sucesso")
    void deveBuscarSedePorIdComSucesso() {
        when(sedeRepository.findById(1L)).thenReturn(Optional.of(sede));

        Sede encontrada = sedeService.buscarPorId(1L);

        assertNotNull(encontrada);
        assertEquals(1L, encontrada.getId());
        assertEquals("Sede Central", encontrada.getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar sede por ID inexistente")
    void deveLancarExcecaoAoBuscarSedePorIdInexistente() {
        when(sedeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
            sedeService.buscarPorId(99L)
        );
    }

    // ========== listarSedesComFiltros ==========

    @Test
    @DisplayName("Deve listar sedes sem filtros")
    void deveListarSedesSemFiltros() {
        when(sedeRepository.findWithFilters(null, null, null, null)).thenReturn(List.of(sede));

        List<Sede> sedes = sedeService.listarSedesComFiltros(null, null, null, null);

        assertNotNull(sedes);
        assertEquals(1, sedes.size());
        assertEquals("Sede Central", sedes.get(0).getNome());
    }

    @Test
    @DisplayName("Deve listar sedes com filtro por nome")
    void deveListarSedesComFiltroPorNome() {
        when(sedeRepository.findWithFilters("Sede Central", null, null, null)).thenReturn(List.of(sede));

        List<Sede> sedes = sedeService.listarSedesComFiltros("Sede Central", null, null, null);

        assertNotNull(sedes);
        assertEquals(1, sedes.size());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nenhuma sede for encontrada")
    void deveRetornarListaVaziaQuandoNenhumaSedeEncontrada() {
        when(sedeRepository.findWithFilters("Inexistente", null, null, null)).thenReturn(List.of());

        List<Sede> sedes = sedeService.listarSedesComFiltros("Inexistente", null, null, null);

        assertNotNull(sedes);
        assertTrue(sedes.isEmpty());
    }

    // ========== atualizarSede ==========

    @Test
    @DisplayName("Deve atualizar uma sede com sucesso")
    void deveAtualizarSedeComSucesso() {
        Sede sedeAtualizada = new Sede();
        sedeAtualizada.setNome("Sede Atualizada");
        sedeAtualizada.setEndereco("Rua Nova, 200");
        sedeAtualizada.setBairro("Bairro Novo");
        sedeAtualizada.setNumero("200");
        sedeAtualizada.setCodigoPostal("02002-000");
        sedeAtualizada.setAtiva(true);
        sedeAtualizada.setCidade(cidade);

        when(sedeRepository.findById(1L)).thenReturn(Optional.of(sede));
        when(sedeRepository.save(any(Sede.class))).thenReturn(sede);

        Sede resultado = sedeService.atualizarSede(1L, sedeAtualizada);

        assertNotNull(resultado);
        assertEquals("Sede Atualizada", resultado.getNome());
        assertEquals("Rua Nova, 200", resultado.getEndereco());
        verify(sedeRepository).save(any(Sede.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar sede com ID inexistente")
    void deveLancarExcecaoAoAtualizarSedeComIdInexistente() {
        when(sedeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
            sedeService.atualizarSede(99L, sede)
        );
    }

    // ========== inativarSede ==========

    @Test
    @DisplayName("Deve inativar sede com sucesso")
    void deveInativarSedeComSucesso() {
        when(sedeRepository.findById(1L)).thenReturn(Optional.of(sede));
        when(sedeRepository.save(any(Sede.class))).thenReturn(sede);

        sedeService.inativarSede(1L);

        assertFalse(sede.getAtiva());
        verify(sedeRepository).save(sede);
    }

    @Test
    @DisplayName("Deve lançar exceção ao inativar sede com ID inexistente")
    void deveLancarExcecaoAoInativarSedeComIdInexistente() {
        when(sedeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
            sedeService.inativarSede(99L)
        );
    }
}
