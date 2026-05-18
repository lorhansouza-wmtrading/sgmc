package br.com.mam.sgmc.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.mam.sgmc.api.dto.request.IdentificacaoRequestDTO;
import br.com.mam.sgmc.api.dto.request.MembroRequestDTO;
import br.com.mam.sgmc.model.Membro;
import br.com.mam.sgmc.model.enums.Ativo;
import br.com.mam.sgmc.services.MembroService;

@SpringBootTest
@Transactional
@DisplayName("MembroController Integration Tests")
class MembroControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private MembroService membroService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private MembroRequestDTO membroRequestDTO;
    private Membro membro;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        membroRequestDTO = new MembroRequestDTO();
        membroRequestDTO.setNome("João Silva");
        membroRequestDTO.setApelido("João");
        membroRequestDTO.setSexo("M");
        membroRequestDTO.setEmail("joao@email.com");
        membroRequestDTO.setTelefone("11999999999");
        membroRequestDTO.setDataNascimento(LocalDate.of(1990, 1, 1));
        membroRequestDTO.setEhBatizado(true);
        membroRequestDTO.setTemEscudo(true);
        membroRequestDTO.setAtivo(true);
        membroRequestDTO.setTamanhoCamisa("G");
        membroRequestDTO.setDataAdmissao(LocalDate.now());

        IdentificacaoRequestDTO identidadeDTO = new IdentificacaoRequestDTO();
        identidadeDTO.setTipo("CPF");
        identidadeDTO.setIdentidade("12345678909");
        identidadeDTO.setEmissor("SSP");
        identidadeDTO.setDataEmissao(LocalDate.of(2010, 1, 1));
        identidadeDTO.setPaisSigla("BR");
        membroRequestDTO.setIdentidade(identidadeDTO);

        membro = new Membro();
        membro.setId(1L);
        membro.setNome(membroRequestDTO.getNome());
        membro.setAtivo(Ativo.ATIVO.getCodigo());
        membro.setEhBatizado(0);
        membro.setTemEscudo(0);
    }

    @Test
    @DisplayName("Deve criar um membro com sucesso")
    void deveCriarMembroComSucesso() throws Exception {
        when(membroService.salvarMembro(any(Membro.class), any(), any(), any())).thenReturn(membro);

        mockMvc.perform(post("/membros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(membroRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar criar membro sem nome")
    void deveRetornar400AoCriarMembroSemNome() throws Exception {
        membroRequestDTO.setNome(null);

        mockMvc.perform(post("/membros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(membroRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve listar todos os membros")
    void deveListarMembros() throws Exception {
        when(membroService.listarMembros(null)).thenReturn(List.of(membro));

        mockMvc.perform(get("/membros"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                .andExpect(jsonPath("$[0].ativo").value(true));
    }

    @Test
    @DisplayName("Deve listar apenas membros ativos")
    void deveListarMembrosAtivos() throws Exception {
        when(membroService.listarMembros(0)).thenReturn(List.of(membro));

        mockMvc.perform(get("/membros?ativo=0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ativo").value(true));
    }

    @Test
    @DisplayName("Deve buscar membro por ID")
    void deveBuscarMembroPorId() throws Exception {
        when(membroService.buscarPorId(1L)).thenReturn(membro);

        mockMvc.perform(get("/membros/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    @DisplayName("Deve inativar um membro via PUT")
    void deveInativarMembroViaPut() throws Exception {
        membroRequestDTO.setAtivo(false);
        
        membro.setAtivo(Ativo.INATIVO.getCodigo());

        when(membroService.atualizarMembro(any(Membro.class), eq(1L), any(), any(), any())).thenReturn(membro);

        mockMvc.perform(put("/membros/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(membroRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ativo").value(false));
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar ID inexistente")
    void deveRetornar404AoBuscarIdInexistente() throws Exception {
        when(membroService.buscarPorId(99L)).thenThrow(new br.com.mam.sgmc.errors.ResourceNotFoundException("Não encontrado"));

        mockMvc.perform(get("/membros/99"))
                .andExpect(status().isNotFound());
    }
}
