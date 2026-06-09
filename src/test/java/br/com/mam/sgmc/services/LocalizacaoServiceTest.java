package br.com.mam.sgmc.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.mam.sgmc.errors.ResourceNotFoundException;
import br.com.mam.sgmc.model.localizacao.Cidade;
import br.com.mam.sgmc.model.localizacao.Pais;
import br.com.mam.sgmc.model.localizacao.Uf;
import br.com.mam.sgmc.repository.CidadeRepository;
import br.com.mam.sgmc.repository.PaisRepository;
import br.com.mam.sgmc.repository.UfRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - LocalizacaoService")
class LocalizacaoServiceTest {

    @Mock
    private PaisRepository paisRepository;

    @Mock
    private UfRepository ufRepository;

    @Mock
    private CidadeRepository cidadeRepository;

    @InjectMocks
    private LocalizacaoService localizacaoService;

    private Pais pais;
    private Uf uf;
    private Cidade cidade;

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
    }

    // ========== listarPaisesComFiltros ==========

    @Test
    @DisplayName("Deve listar países sem filtros")
    void deveListarPaisesSemFiltros() {
        when(paisRepository.findWithFilters(null, null, null)).thenReturn(List.of(pais));

        List<Pais> paises = localizacaoService.listarPaisesComFiltros(null, null, null);

        assertNotNull(paises);
        assertEquals(1, paises.size());
        assertEquals("Brasil", paises.get(0).getNome());
    }

    @Test
    @DisplayName("Deve listar países com filtro por sigla")
    void deveListarPaisesComFiltroPorSigla() {
        when(paisRepository.findWithFilters("BR", null, null)).thenReturn(List.of(pais));

        List<Pais> paises = localizacaoService.listarPaisesComFiltros("BR", null, null);

        assertEquals(1, paises.size());
        assertEquals("BR", paises.get(0).getSigla());
    }

    @Test
    @DisplayName("Deve listar países com filtro por continente")
    void deveListarPaisesComFiltroPorContinente() {
        when(paisRepository.findWithFilters(null, null, "América do Sul")).thenReturn(List.of(pais));

        List<Pais> paises = localizacaoService.listarPaisesComFiltros(null, null, "América do Sul");

        assertEquals(1, paises.size());
    }

    @Test
    @DisplayName("Deve lançar exceção quando nenhum país for encontrado")
    void deveLancarExcecaoQuandoNenhumPaisEncontrado() {
        when(paisRepository.findWithFilters("XX", null, null)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () ->
            localizacaoService.listarPaisesComFiltros("XX", null, null)
        );
    }

    // ========== listarUfsComFiltros ==========

    @Test
    @DisplayName("Deve listar UFs sem filtros")
    void deveListarUfsSemFiltros() {
        when(ufRepository.findWithFilters(null, null, null, null)).thenReturn(List.of(uf));

        List<Uf> ufs = localizacaoService.listarUfsComFiltros(null, null, null, null);

        assertNotNull(ufs);
        assertEquals(1, ufs.size());
        assertEquals("São Paulo", ufs.get(0).getNome());
    }

    @Test
    @DisplayName("Deve listar UFs com filtro por sigla")
    void deveListarUfsComFiltroPorSigla() {
        when(ufRepository.findWithFilters("SP", null, null, null)).thenReturn(List.of(uf));

        List<Uf> ufs = localizacaoService.listarUfsComFiltros("SP", null, null, null);

        assertEquals(1, ufs.size());
        assertEquals("SP", ufs.get(0).getUfSigla());
    }

    @Test
    @DisplayName("Deve listar UFs com filtro por região")
    void deveListarUfsComFiltroPorRegiao() {
        when(ufRepository.findWithFilters(null, null, "Sudeste", null)).thenReturn(List.of(uf));

        List<Uf> ufs = localizacaoService.listarUfsComFiltros(null, null, "Sudeste", null);

        assertEquals(1, ufs.size());
    }

    @Test
    @DisplayName("Deve lançar exceção quando nenhuma UF for encontrada")
    void deveLancarExcecaoQuandoNenhumaUfEncontrada() {
        when(ufRepository.findWithFilters("XX", null, null, null)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () ->
            localizacaoService.listarUfsComFiltros("XX", null, null, null)
        );
    }

    // ========== listarCidadesComFiltros ==========

    @Test
    @DisplayName("Deve listar cidades sem filtros")
    void deveListarCidadesSemFiltros() {
        when(cidadeRepository.findWithFilters(null, null, null)).thenReturn(List.of(cidade));

        List<Cidade> cidades = localizacaoService.listarCidadesComFiltros(null, null, null);

        assertNotNull(cidades);
        assertEquals(1, cidades.size());
        assertEquals("São Paulo", cidades.get(0).getNome());
    }

    @Test
    @DisplayName("Deve listar cidades com filtro por UF")
    void deveListarCidadesComFiltroPorUf() {
        when(cidadeRepository.findWithFilters("SP", null, null)).thenReturn(List.of(cidade));

        List<Cidade> cidades = localizacaoService.listarCidadesComFiltros("SP", null, null);

        assertEquals(1, cidades.size());
    }

    @Test
    @DisplayName("Deve listar cidades com filtro por nome")
    void deveListarCidadesComFiltroPorNome() {
        when(cidadeRepository.findWithFilters(null, "São Paulo", null)).thenReturn(List.of(cidade));

        List<Cidade> cidades = localizacaoService.listarCidadesComFiltros(null, "São Paulo", null);

        assertEquals(1, cidades.size());
        assertEquals("São Paulo", cidades.get(0).getNome());
    }

    @Test
    @DisplayName("Deve lançar exceção quando nenhuma cidade for encontrada")
    void deveLancarExcecaoQuandoNenhumaCidadeEncontrada() {
        when(cidadeRepository.findWithFilters(null, "Inexistente", null)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () ->
            localizacaoService.listarCidadesComFiltros(null, "Inexistente", null)
        );
    }
}
