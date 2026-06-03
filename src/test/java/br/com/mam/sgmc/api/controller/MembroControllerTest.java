package br.com.mam.sgmc.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.mam.sgmc.api.dto.request.FichaMedicaRequestDTO;
import br.com.mam.sgmc.api.dto.request.IdentificacaoRequestDTO;
import br.com.mam.sgmc.api.dto.request.MembroRequestDTO;
import br.com.mam.sgmc.model.FichaMedica;
import br.com.mam.sgmc.model.Membro;
import br.com.mam.sgmc.model.enums.Ativo;
import br.com.mam.sgmc.services.MembroService;

@WebMvcTest(MembroController.class)
@Import(br.com.mam.sgmc.config.SecurityConfig.class)
@DisplayName("Testes de Integração - MembroController")
class MembroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MembroService membroService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private MembroRequestDTO membroRequestDTO;
    private Membro membro;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        membroRequestDTO = new MembroRequestDTO();
        membroRequestDTO.setNome("João Silva");
        membroRequestDTO.setApelido("João");
        membroRequestDTO.setSexo("M");
        membroRequestDTO.setEmail("joao@email.com");
        membroRequestDTO.setTelefone("11999999999");
        membroRequestDTO.setDataNascimento(LocalDate.of(1990, 1, 1));
        membroRequestDTO.setNacionalidade("Brasileira");
        membroRequestDTO.setNaturalidade("São Paulo");
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

        FichaMedicaRequestDTO fichaMedicaDTO = new FichaMedicaRequestDTO();
        fichaMedicaDTO.setNomePlanoSaude("Unimed");
        fichaMedicaDTO.setNumeroCarteira("123456789");
        fichaMedicaDTO.setTipoSanguineo("O+");
        fichaMedicaDTO.setAlergias("Nenhuma");
        fichaMedicaDTO.setMedicamentos("Nenhum");
        fichaMedicaDTO.setCondicoesMedicas("Nenhuma");
        fichaMedicaDTO.setObservacoes("Saudável");
        membroRequestDTO.setFichaMedica(fichaMedicaDTO);

        membro = new Membro();
        membro.setId(1L);
        membro.setNome(membroRequestDTO.getNome());
        membro.setAtivo(Ativo.ATIVO.getCodigo());
        membro.setEhBatizado(0);
        membro.setTemEscudo(0);

        FichaMedica fichaMedica = new FichaMedica();
        fichaMedica.setId(1L);
        fichaMedica.setNomePlanoSaude("Unimed");
        fichaMedica.setNumeroCarteira("123456789");
        fichaMedica.setTipoSanguineo("O+");
        fichaMedica.setAlergias("Nenhuma");
        fichaMedica.setMedicamentos("Nenhum");
        fichaMedica.setCondicoesMedicas("Nenhuma");
        fichaMedica.setObservacoes("Saudável");
        fichaMedica.setMembro(membro);
        membro.setFichaMedica(fichaMedica);
    }

    @Test
    @DisplayName("Deve criar um membro com sucesso")
    void deveCriarMembroComSucesso() throws Exception {
        when(membroService.salvarMembro(any(Membro.class), any(), any(), any())).thenReturn(membro);

        mockMvc.perform(post("/membros")
                .with(jwt().authorities(() -> "ROLE_PRESIDENT"))
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
                .with(jwt().authorities(() -> "ROLE_PRESIDENT"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(membroRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar criar membro sem ficha médica")
    void deveRetornar400AoCriarMembroSemFichaMedica() throws Exception {
        membroRequestDTO.setFichaMedica(null);

        mockMvc.perform(post("/membros")
                .with(jwt().authorities(() -> "ROLE_PRESIDENT"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(membroRequestDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve listar todos os membros")
    void deveListarMembros() throws Exception {
        when(membroService.listarMembros(null)).thenReturn(List.of(membro));

        mockMvc.perform(get("/membros")
                .with(jwt().authorities(() -> "ROLE_PRESIDENT")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                .andExpect(jsonPath("$[0].ativo").value(true));
    }

    @Test
    @DisplayName("Deve listar apenas membros ativos")
    void deveListarMembrosAtivos() throws Exception {
        when(membroService.listarMembros(0)).thenReturn(List.of(membro));

        mockMvc.perform(get("/membros?ativo=0")
                .with(jwt().authorities(() -> "ROLE_PRESIDENT")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ativo").value(true));
    }

    @Test
    @DisplayName("Deve buscar membro por ID")
    void deveBuscarMembroPorId() throws Exception {
        when(membroService.buscarPorId(1L)).thenReturn(membro);

        mockMvc.perform(get("/membros/1")
                .with(jwt().authorities(() -> "ROLE_PRESIDENT")))
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
                .with(jwt().authorities(() -> "ROLE_PRESIDENT"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(membroRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ativo").value(false));
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar ID inexistente")
    void deveRetornar404AoBuscarIdInexistente() throws Exception {
        when(membroService.buscarPorId(99L)).thenThrow(new br.com.mam.sgmc.errors.ResourceNotFoundException("Não encontrado"));

        mockMvc.perform(get("/membros/99")
                .with(jwt().authorities(() -> "ROLE_PRESIDENT")))
                .andExpect(status().isNotFound());
    }
}
