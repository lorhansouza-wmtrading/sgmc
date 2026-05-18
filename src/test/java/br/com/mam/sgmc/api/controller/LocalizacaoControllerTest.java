package br.com.mam.sgmc.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import br.com.mam.sgmc.errors.ResourceNotFoundException;
import br.com.mam.sgmc.model.localizacao.Cidade;
import br.com.mam.sgmc.model.localizacao.Pais;
import br.com.mam.sgmc.model.localizacao.Uf;
import br.com.mam.sgmc.services.LocalizacaoService;

@SpringBootTest
@Transactional
@DisplayName("LocalizacaoController Comprehensive Tests")
class LocalizacaoControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private LocalizacaoService localizacaoService;

    private Pais pais;
    private Uf uf;
    private Cidade cidade;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

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

    @Nested
    @DisplayName("1. Listagem de Países (GET /localizacao/paises)")
    class ListagemPaises {

        @Test
        @DisplayName("Deve listar países com sucesso")
        void deveListarPaises() throws Exception {
            when(localizacaoService.listarPaisesComFiltros(any(), any(), any())).thenReturn(List.of(pais));

            mockMvc.perform(get("/localizacao/paises"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].sigla").value("BR"))
                    .andExpect(jsonPath("$[0].nome").value("Brasil"))
                    .andExpect(jsonPath("$[0].continente").value("América do Sul"));
        }

        @Test
        @DisplayName("Deve retornar 404 quando nenhum país for encontrado")
        void deveRetornar404QuandoNenhumPaisEncontrado() throws Exception {
            when(localizacaoService.listarPaisesComFiltros(any(), any(), any()))
                .thenThrow(new ResourceNotFoundException("Não foi possível encontrar nenhum país com os filtros informados"));

            mockMvc.perform(get("/localizacao/paises"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("2. Listagem de UFs (GET /localizacao/ufs)")
    class ListagemUfs {

        @Test
        @DisplayName("Deve listar UFs com sucesso")
        void deveListarUfs() throws Exception {
            when(localizacaoService.listarUfsComFiltros(any(), any(), any(), any())).thenReturn(List.of(uf));

            mockMvc.perform(get("/localizacao/ufs"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].sigla").value("SP"))
                    .andExpect(jsonPath("$[0].nome").value("São Paulo"))
                    .andExpect(jsonPath("$[0].regiaoNome").value("Sudeste"))
                    .andExpect(jsonPath("$[0].paisNome").value("Brasil"));
        }

        @Test
        @DisplayName("Deve retornar 404 quando nenhuma UF for encontrada")
        void deveRetornar404QuandoNenhumaUfEncontrada() throws Exception {
            when(localizacaoService.listarUfsComFiltros(any(), any(), any(), any()))
                .thenThrow(new ResourceNotFoundException("Não foi possível encontrar nenhuma UF com os filtros informados"));

            mockMvc.perform(get("/localizacao/ufs"))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("3. Listagem de Cidades (GET /localizacao/cidades)")
    class ListagemCidades {

        @Test
        @DisplayName("Deve listar cidades com sucesso")
        void deveListarCidades() throws Exception {
            when(localizacaoService.listarCidadesComFiltros(any(), any(), any())).thenReturn(List.of(cidade));

            mockMvc.perform(get("/localizacao/cidades"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].nome").value("São Paulo"))
                    .andExpect(jsonPath("$[0].ufSigla").value("SP"))
                    .andExpect(jsonPath("$[0].ufNome").value("São Paulo"))
                    .andExpect(jsonPath("$[0].paisNome").value("Brasil"));
        }

        @Test
        @DisplayName("Deve retornar 404 quando nenhuma cidade for encontrada")
        void deveRetornar404QuandoNenhumaCidadeEncontrada() throws Exception {
            when(localizacaoService.listarCidadesComFiltros(any(), any(), any()))
                .thenThrow(new ResourceNotFoundException("Não foi possível encontrar nenhuma cidade com os filtros informados"));

            mockMvc.perform(get("/localizacao/cidades"))
                    .andExpect(status().isNotFound());
        }
    }
}
