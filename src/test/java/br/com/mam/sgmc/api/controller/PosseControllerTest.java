package br.com.mam.sgmc.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;
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

import br.com.mam.sgmc.api.dto.request.PosseRequestDTO;
import br.com.mam.sgmc.model.Cargo;
import br.com.mam.sgmc.model.Membro;
import br.com.mam.sgmc.model.Posse;
import br.com.mam.sgmc.model.pk.PossePk;
import br.com.mam.sgmc.services.PosseService;

@WebMvcTest(controllers = PosseController.class, properties = "server.servlet.context-path=")
@Import(br.com.mam.sgmc.config.SecurityConfig.class)
@DisplayName("Testes de Integração - PosseController")
class PosseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private br.com.mam.sgmc.config.SgmcSecurity sgmcSecurity;

    @MockitoBean
    private PosseService posseService;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Posse posse;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        when(sgmcSecurity.isSelf(any())).thenReturn(true);

        Membro membro = new Membro();
        membro.setId(1L);
        membro.setNome("João Silva");

        Cargo cargo = new Cargo();
        cargo.setId(1L);
        cargo.setTitulo("Presidente");
        cargo.setDescricao("Líder do Moto Clube");

        PossePk possePk = new PossePk(cargo, membro);

        posse = new Posse();
        posse.setPossePk(possePk);
        posse.setDataInicio(Date.valueOf(LocalDate.now()));
    }

    @Test
    @DisplayName("Deve registrar uma nova posse com sucesso")
    void deveRegistrarPosseComSucesso() throws Exception {
        PosseRequestDTO requestDTO = new PosseRequestDTO();
        requestDTO.setIdCargo(1L);
        requestDTO.setDataInicio(LocalDate.now());

        when(posseService.salvarPosse(eq(1L), eq(1L), any(LocalDate.class), any())).thenReturn(posse);

        mockMvc.perform(post("/membros/1/posses")
                .with(jwt().authorities(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_admin")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve listar posses do membro com sucesso")
    void deveListarPossesComSucesso() throws Exception {
        when(posseService.listarPossesPorMembro(1L)).thenReturn(List.of(posse));

        mockMvc.perform(get("/membros/1/posses")
                .with(jwt().authorities(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_admin")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idMembro").value(1L))
                .andExpect(jsonPath("$[0].nomeMembro").value("João Silva"))
                .andExpect(jsonPath("$[0].cargo.titulo").value("Presidente"));
    }
}
