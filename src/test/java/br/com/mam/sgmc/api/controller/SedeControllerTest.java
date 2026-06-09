package br.com.mam.sgmc.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.mam.sgmc.api.dto.request.SedeRequestDTO;
import br.com.mam.sgmc.config.SecurityConfig;
import br.com.mam.sgmc.errors.ResourceNotFoundException;
import br.com.mam.sgmc.model.Sede;
import br.com.mam.sgmc.model.localizacao.Cidade;
import br.com.mam.sgmc.model.localizacao.Pais;
import br.com.mam.sgmc.model.localizacao.Uf;
import br.com.mam.sgmc.services.SedeService;

@WebMvcTest(SedeController.class)
@Import(SecurityConfig.class)
@DisplayName("Testes de Integração - SedeController")
class SedeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SedeService sedeService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private SedeRequestDTO sedeRequestDTO;
    private Sede sede;

    @BeforeEach
    void setUp() {
        sedeRequestDTO = new SedeRequestDTO();
        sedeRequestDTO.setNome("Sede Central");
        sedeRequestDTO.setEndereco("Rua Principal, 100");
        sedeRequestDTO.setBairro("Centro");
        sedeRequestDTO.setNumero("100");
        sedeRequestDTO.setCodigoPostal("01001-000");
        sedeRequestDTO.setAtiva(true);
        sedeRequestDTO.setCidade("São Paulo");
        sedeRequestDTO.setUfSigla("SP");
        sedeRequestDTO.setPais("Brasil");

        Pais pais = new Pais();
        pais.setSigla("BR");
        pais.setNome("Brasil");

        Uf uf = new Uf();
        uf.setUfSigla("SP");
        uf.setNome("São Paulo");
        uf.setPais(pais);

        Cidade cidade = new Cidade();
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

    @Nested
    @DisplayName("1. Criação de Sede (POST /sedes)")
    class CriacaoSede {

        @Test
        @DisplayName("Deve criar uma sede com sucesso")
        void deveCriarSedeComSucesso() throws Exception {
            when(sedeService.salvarSede(any(Sede.class), eq("São Paulo"), eq("SP"), eq("Brasil")))
                .thenReturn(sede);

            mockMvc.perform(post("/sedes")
                    .with(jwt().authorities(() -> "ROLE_PRESIDENT"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(sedeRequestDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location"));
        }

        @Test
        @DisplayName("Deve retornar 400 ao criar sede sem nome")
        void deveRetornar400AoCriarSedeSemNome() throws Exception {
            sedeRequestDTO.setNome(null);

            mockMvc.perform(post("/sedes")
                    .with(jwt().authorities(() -> "ROLE_PRESIDENT"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(sedeRequestDTO)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 400 ao criar sede sem endereço")
        void deveRetornar400AoCriarSedeSemEndereco() throws Exception {
            sedeRequestDTO.setEndereco(null);

            mockMvc.perform(post("/sedes")
                    .with(jwt().authorities(() -> "ROLE_PRESIDENT"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(sedeRequestDTO)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("2. Busca de Sede por ID (GET /sedes/buscar/{id})")
    class BuscaSede {

        @Test
        @DisplayName("Deve buscar sede por ID com sucesso")
        void deveBuscarSedePorIdComSucesso() throws Exception {
            when(sedeService.buscarPorId(1L)).thenReturn(sede);

            mockMvc.perform(get("/sedes/buscar/1")
                    .with(jwt().authorities(() -> "ROLE_PRESIDENT")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nome").value("Sede Central"))
                    .andExpect(jsonPath("$.cidade").value("São Paulo"))
                    .andExpect(jsonPath("$.estado").value("São Paulo"))
                    .andExpect(jsonPath("$.pais").value("Brasil"));
        }

        @Test
        @DisplayName("Deve retornar 404 ao buscar sede com ID inexistente")
        void deveRetornar404AoBuscarSedeComIdInexistente() throws Exception {
            when(sedeService.buscarPorId(99L))
                .thenThrow(new ResourceNotFoundException("Sede não encontrada"));

            mockMvc.perform(get("/sedes/buscar/99")
                    .with(jwt().authorities(() -> "ROLE_PRESIDENT")))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("3. Listagem de Sedes (GET /sedes/listar)")
    class ListagemSedes {

        @Test
        @DisplayName("Deve listar sedes sem filtros")
        void deveListarSedesSemFiltros() throws Exception {
            when(sedeService.listarSedesComFiltros(null, null, null, null))
                .thenReturn(List.of(sede));

            mockMvc.perform(get("/sedes/listar")
                    .with(jwt().authorities(() -> "ROLE_PRESIDENT")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].id").value(1))
                    .andExpect(jsonPath("$[0].nome").value("Sede Central"));
        }

        @Test
        @DisplayName("Deve listar sedes com filtro por nome")
        void deveListarSedesComFiltroPorNome() throws Exception {
            when(sedeService.listarSedesComFiltros(eq("Sede Central"), any(), any(), any()))
                .thenReturn(List.of(sede));

            mockMvc.perform(get("/sedes/listar?nome=Sede Central")
                    .with(jwt().authorities(() -> "ROLE_PRESIDENT")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].nome").value("Sede Central"));
        }

        @Test
        @DisplayName("Deve retornar lista vazia quando nenhuma sede for encontrada")
        void deveRetornarListaVazia() throws Exception {
            when(sedeService.listarSedesComFiltros(any(), any(), any(), any()))
                .thenReturn(List.of());

            mockMvc.perform(get("/sedes/listar?nome=Inexistente")
                    .with(jwt().authorities(() -> "ROLE_PRESIDENT")))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isEmpty());
        }
    }

    @Nested
    @DisplayName("4. Atualização de Sede (PUT /sedes/{id})")
    class AtualizacaoSede {

        @Test
        @DisplayName("Deve atualizar uma sede com sucesso")
        void deveAtualizarSedeComSucesso() throws Exception {
            when(sedeService.atualizarSede(eq(1L), any(Sede.class))).thenReturn(sede);

            mockMvc.perform(put("/sedes/1")
                    .with(jwt().authorities(() -> "ROLE_PRESIDENT"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(sedeRequestDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nome").value("Sede Central"));
        }

        @Test
        @DisplayName("Deve retornar 404 ao atualizar sede com ID inexistente")
        void deveRetornar404AoAtualizarSedeInexistente() throws Exception {
            when(sedeService.atualizarSede(eq(99L), any(Sede.class)))
                .thenThrow(new ResourceNotFoundException("Este id da sede não existe!"));

            mockMvc.perform(put("/sedes/99")
                    .with(jwt().authorities(() -> "ROLE_PRESIDENT"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(sedeRequestDTO)))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("5. Inativação de Sede (PATCH /sedes/{id}/inativar)")
    class InativacaoSede {

        @Test
        @DisplayName("Deve inativar uma sede com sucesso")
        void deveInativarSedeComSucesso() throws Exception {
            doNothing().when(sedeService).inativarSede(1L);

            mockMvc.perform(patch("/sedes/1/inativar")
                    .with(jwt().authorities(() -> "ROLE_PRESIDENT")))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Deve retornar 404 ao inativar sede com ID inexistente")
        void deveRetornar404AoInativarSedeInexistente() throws Exception {
            org.mockito.Mockito.doThrow(new ResourceNotFoundException("Sede não encontrada"))
                .when(sedeService).inativarSede(99L);

            mockMvc.perform(patch("/sedes/99/inativar")
                    .with(jwt().authorities(() -> "ROLE_PRESIDENT")))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("6. Testes de Segurança")
    class SegurancaSede {

        @Test
        @DisplayName("Deve retornar 401 ao acessar sem autenticação")
        void deveRetornar401SemAutenticacao() throws Exception {
            mockMvc.perform(get("/sedes/listar"))
                    .andExpect(status().isUnauthorized());
        }
    }
}
