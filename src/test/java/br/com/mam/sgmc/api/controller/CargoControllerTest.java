package br.com.mam.sgmc.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import br.com.mam.sgmc.api.dto.request.CargoRequestDTO;
import br.com.mam.sgmc.model.Cargo;
import br.com.mam.sgmc.services.CargoService;

@WebMvcTest(controllers = CargoController.class, properties = "server.servlet.context-path=")
@Import(br.com.mam.sgmc.config.SecurityConfig.class)
@DisplayName("Testes de Integração - CargoController")
class CargoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private br.com.mam.sgmc.config.SgmcSecurity sgmcSecurity;

    @MockitoBean
    private CargoService cargoService;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Cargo cargo;

    @BeforeEach
    void setUp() {
        cargo = new Cargo();
        cargo.setId(1L);
        cargo.setTitulo("Presidente");
        cargo.setDescricao("Líder do Moto Clube");
    }

    @Test
    @DisplayName("Deve salvar um novo cargo com sucesso")
    void deveSalvarCargoComSucesso() throws Exception {
        CargoRequestDTO requestDTO = new CargoRequestDTO();
        requestDTO.setTitulo("Presidente");
        requestDTO.setDescricao("Líder do Moto Clube");

        when(cargoService.salvarCargo(any(Cargo.class))).thenReturn(cargo);

        mockMvc.perform(post("/cargos")
                .with(jwt().authorities(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_admin")))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Deve listar todos os cargos com sucesso")
    void deveListarCargosComSucesso() throws Exception {
        when(cargoService.listarCargos()).thenReturn(List.of(cargo));

        mockMvc.perform(get("/cargos")
                .with(jwt().authorities(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_membro")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].titulo").value("Presidente"));
    }

    @Test
    @DisplayName("Deve buscar um cargo por ID com sucesso")
    void deveBuscarCargoPorIdComSucesso() throws Exception {
        when(cargoService.buscarPorId(1L)).thenReturn(cargo);

        mockMvc.perform(get("/cargos/1")
                .with(jwt().authorities(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_membro")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.titulo").value("Presidente"));
    }
}
