package br.com.mam.sgmc.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.Instant;
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

import br.com.mam.sgmc.api.dto.request.EventoRequestDTO;
import br.com.mam.sgmc.api.dto.request.LocalRequestDTO;
import br.com.mam.sgmc.model.Evento;
import br.com.mam.sgmc.model.localizacao.Local;
import br.com.mam.sgmc.services.EventoService;
import br.com.mam.sgmc.errors.ResourceNotFoundException;

@WebMvcTest(controllers = EventoController.class, properties = "server.servlet.context-path=")
@Import(br.com.mam.sgmc.config.SecurityConfig.class)
@DisplayName("Testes de Integração - EventoController")
class EventoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private br.com.mam.sgmc.config.SgmcSecurity sgmcSecurity;

    @MockitoBean
    private EventoService eventoService;

    @MockitoBean
    private br.com.mam.sgmc.services.MembroService membroService;

    @MockitoBean
    private br.com.mam.sgmc.services.MotoService motoService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private EventoRequestDTO eventoRequestDTO;
    private Evento evento;

    @BeforeEach
    void setUp() {
        org.mockito.Mockito.when(sgmcSecurity.isSelf(org.mockito.ArgumentMatchers.any())).thenReturn(true);
        org.mockito.Mockito.when(sgmcSecurity.isMotoOwner(org.mockito.ArgumentMatchers.any())).thenReturn(true);
        org.mockito.Mockito.when(sgmcSecurity.canInscribe(org.mockito.ArgumentMatchers.any())).thenReturn(true);

        objectMapper.registerModule(new JavaTimeModule());

        LocalRequestDTO localDTO = new LocalRequestDTO();
        localDTO.setNome("Sede Campestre");
        localDTO.setEndereco("Rua das Flores");
        localDTO.setBairro("Centro");
        localDTO.setNumero("123");
        localDTO.setCodigoPostal("12345-678");
        localDTO.setContato("11988888888");
        localDTO.setCapacidade(100);
        localDTO.setCidade("São Paulo");
        localDTO.setProvinciaEstado("SP");
        localDTO.setPais("Brasil");

        eventoRequestDTO = new EventoRequestDTO();
        eventoRequestDTO.setNome("Encontro de Motos");
        eventoRequestDTO.setDescricao("Encontro anual");
        eventoRequestDTO.setDataInicio(Instant.parse("2026-12-01T10:00:00Z"));
        eventoRequestDTO.setDataFim(Instant.parse("2026-12-01T18:00:00Z"));
        eventoRequestDTO.setValor(50.0f);
        eventoRequestDTO.setLocal(localDTO);

        Local local = Local.fromRequestDTO(localDTO);
        local.setId(1L);

        evento = new Evento();
        evento.setId(1L);
        evento.setNome(eventoRequestDTO.getNome());
        evento.setDescricao(eventoRequestDTO.getDescricao());
        evento.setDataInicio(eventoRequestDTO.getDataInicio());
        evento.setDataFim(eventoRequestDTO.getDataFim());
        evento.setValor(eventoRequestDTO.getValor());
        evento.setLocal(local);
    }

    @Test
    @DisplayName("Deve criar um evento com sucesso")
    void deveCriarEventoComSucesso() throws Exception {
        when(eventoService.criarEvento(any(Evento.class))).thenReturn(evento);

        mockMvc.perform(post("/eventos")
                .with(jwt().authorities(() -> "ROLE_admin"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventoRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Encontro de Motos"));
    }

    @Test
    @DisplayName("Deve listar todos os eventos")
    void deveListarEventos() throws Exception {
        when(eventoService.listarEventos()).thenReturn(List.of(evento));

        mockMvc.perform(get("/eventos").with(jwt().authorities(() -> "ROLE_admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nome").value("Encontro de Motos"));
    }

    @Test
    @DisplayName("Deve buscar evento por ID")
    void deveBuscarEventoPorId() throws Exception {
        when(eventoService.buscarPorId(1L)).thenReturn(evento);

        mockMvc.perform(get("/eventos/1").with(jwt().authorities(() -> "ROLE_admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Encontro de Motos"));
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar ID inexistente")
    void deveRetornar404AoBuscarIdInexistente() throws Exception {
        when(eventoService.buscarPorId(99L)).thenThrow(new ResourceNotFoundException("Evento não encontrado"));

        mockMvc.perform(get("/eventos/99").with(jwt().authorities(() -> "ROLE_admin")))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve atualizar um evento com sucesso")
    void deveAtualizarEventoComSucesso() throws Exception {
        when(eventoService.atualizarEvento(eq(1L), any(Evento.class))).thenReturn(evento);

        mockMvc.perform(put("/eventos/1")
                .with(jwt().authorities(() -> "ROLE_admin"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventoRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Encontro de Motos"));
    }

    @Test
    @DisplayName("Deve deletar um evento com sucesso")
    void deveDeletarEventoComSucesso() throws Exception {
        doNothing().when(eventoService).deletarEvento(1L);

        mockMvc.perform(delete("/eventos/1").with(jwt().authorities(() -> "ROLE_admin")))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve listar inscrições de um evento")
    void deveListarInscricoesDeUmEvento() throws Exception {
        br.com.mam.sgmc.model.Membro membroMock = new br.com.mam.sgmc.model.Membro();
        membroMock.setId(10L);
        membroMock.setNome("Membro Teste");
        
        br.com.mam.sgmc.model.pk.InscricaoPk pk = new br.com.mam.sgmc.model.pk.InscricaoPk(evento, membroMock);
        br.com.mam.sgmc.model.Inscricao inscricao = new br.com.mam.sgmc.model.Inscricao();
        inscricao.setPk(pk);
        inscricao.setDataInscricao(new java.sql.Date(System.currentTimeMillis()));
        
        evento.setInscricoes(List.of(inscricao));

        when(eventoService.buscarPorId(1L)).thenReturn(evento);

        mockMvc.perform(get("/eventos/1/inscricoes").with(jwt().authorities(() -> "ROLE_admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idMembro").value(10))
                .andExpect(jsonPath("$[0].nomeMembro").value("Membro Teste"));
    }

    @Test
    @DisplayName("Deve inscrever membro no evento com sucesso")
    void deveInscreverMembroComSucesso() throws Exception {
        br.com.mam.sgmc.model.Membro membroMock = new br.com.mam.sgmc.model.Membro();
        membroMock.setId(10L);
        membroMock.setNome("Membro Teste");
        membroMock.setAtivo(1); // ATIVO

        br.com.mam.sgmc.api.dto.request.InscricaoRequestDTO dto = new br.com.mam.sgmc.api.dto.request.InscricaoRequestDTO();
        dto.setIdMembro(10L);
        dto.setPlacaMoto(null);

        br.com.mam.sgmc.model.pk.InscricaoPk pk = new br.com.mam.sgmc.model.pk.InscricaoPk(evento, membroMock);
        br.com.mam.sgmc.model.Inscricao inscricao = new br.com.mam.sgmc.model.Inscricao();
        inscricao.setPk(pk);
        inscricao.setDataInscricao(new java.sql.Date(System.currentTimeMillis()));

        when(eventoService.buscarPorId(1L)).thenReturn(evento);
        when(membroService.buscarPorId(10L)).thenReturn(membroMock);
        when(eventoService.inscreverMembros(any())).thenReturn(List.of(inscricao));

        mockMvc.perform(post("/eventos/1/inscricoes")
                .with(jwt().authorities(() -> "ROLE_admin"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(dto))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].idMembro").value(10))
                .andExpect(jsonPath("$[0].nomeMembro").value("Membro Teste"));
    }

    @Test
    @DisplayName("Deve retornar 400 ao tentar inscrever membro inativo")
    void deveRetornar400AoInscreverMembroInativo() throws Exception {
        br.com.mam.sgmc.model.Membro membroMock = new br.com.mam.sgmc.model.Membro();
        membroMock.setId(10L);
        membroMock.setNome("Membro Inativo");
        membroMock.setAtivo(0); // INATIVO

        br.com.mam.sgmc.api.dto.request.InscricaoRequestDTO dto = new br.com.mam.sgmc.api.dto.request.InscricaoRequestDTO();
        dto.setIdMembro(10L);
        dto.setPlacaMoto(null);

        when(eventoService.buscarPorId(1L)).thenReturn(evento);
        when(membroService.buscarPorId(10L)).thenReturn(membroMock);

        mockMvc.perform(post("/eventos/1/inscricoes")
                .with(jwt().authorities(() -> "ROLE_admin"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(List.of(dto))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve retornar 401 ao acessar sem autenticação")
    void deveRetornar401SemAutenticacao() throws Exception {
        mockMvc.perform(get("/eventos"))
                .andExpect(status().isUnauthorized());
    }
}
