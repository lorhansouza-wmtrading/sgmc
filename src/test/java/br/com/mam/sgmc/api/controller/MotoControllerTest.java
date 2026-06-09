package br.com.mam.sgmc.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import br.com.mam.sgmc.config.SecurityConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.com.mam.sgmc.api.dto.request.MotoRequestDTO;
import br.com.mam.sgmc.model.Membro;
import br.com.mam.sgmc.model.enums.Ativo;
import br.com.mam.sgmc.model.moto.CondicaoSeguro;
import br.com.mam.sgmc.model.moto.Marca;
import br.com.mam.sgmc.model.moto.Modelo;
import br.com.mam.sgmc.model.moto.Moto;
import br.com.mam.sgmc.model.moto.Seguro;
import br.com.mam.sgmc.services.MotoService;

@WebMvcTest(MotoController.class)
@Import(SecurityConfig.class)
@DisplayName("MotoController Comprehensive Tests")
class MotoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MotoService motoService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private MotoRequestDTO motoRequestDTO;
    private Moto moto;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        motoRequestDTO = new MotoRequestDTO();
        motoRequestDTO.setPlaca("ABC1234");
        motoRequestDTO.setModelo("CB 500");
        motoRequestDTO.setMarca("Honda");
        motoRequestDTO.setCor("Preta");
        motoRequestDTO.setAno(2022);
        motoRequestDTO.setIdMembro(1L);
        motoRequestDTO.setNomeSeguradora("Porto Seguro");
        motoRequestDTO.setTipoSeguro("Completo");
        motoRequestDTO.setValidadeSeguro(Instant.parse("2026-12-31T23:59:59Z"));
        motoRequestDTO.setValorFranquia(1500.0f);

        Membro membro = new Membro();
        membro.setId(1L);
        membro.setNome("João Silva");
        membro.setAtivo(Ativo.ATIVO.getCodigo());

        Marca marca = new Marca();
        marca.setId(1L);
        marca.setNome("Honda");

        Modelo modelo = new Modelo();
        modelo.setId(1L);
        modelo.setNome("CB 500");
        modelo.setCilindrada(500);
        modelo.setMarca(marca);

        CondicaoSeguro condicaoSeguro = new CondicaoSeguro();
        condicaoSeguro.setId(1L);
        condicaoSeguro.setTipo("Completo");
        condicaoSeguro.setValidadeFim(Instant.parse("2026-12-31T23:59:59Z"));
        condicaoSeguro.setValorFranquia(1500.0f);

        Seguro seguro = new Seguro();
        seguro.setId(1L);
        seguro.setNome("Porto Seguro");
        seguro.setCondicaoSeguro(condicaoSeguro);
        condicaoSeguro.setSeguros(List.of(seguro));

        moto = new Moto();
        moto.setPlaca("ABC1234");
        moto.setModelo(modelo);
        moto.setCor("Preta");
        moto.setAno(2022);
        moto.setMembro(membro);
        moto.setSeguro(seguro);
    }

    @Nested
    @DisplayName("1. Criação de Motos (POST /motos)")
    class CriacaoMoto {

        @Test
        @DisplayName("Deve criar uma moto com sucesso")
        void deveCriarMotoComSucesso() throws Exception {
            System.out.println("Executando: POST /motos - Sucesso");
            when(motoService.salvarMoto(any(Moto.class), eq(1L))).thenReturn(moto);

            mockMvc.perform(post("/motos")
                    .with(jwt().authorities(() -> "ROLE_PRESIDENT"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(motoRequestDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location"));
        }

        @Test
        @DisplayName("Deve retornar 400 ao criar moto sem placa")
        void deveRetornar400AoCriarMotoSemPlaca() throws Exception {
            System.out.println("Executando: POST /motos - Sem placa");
            motoRequestDTO.setPlaca(null);

            mockMvc.perform(post("/motos")
                    .with(jwt().authorities(() -> "ROLE_PRESIDENT"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(motoRequestDTO)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 409 ao criar moto com placa duplicada")
        void deveRetornar409AoCriarMotoComPlacaDuplicada() throws Exception {
            System.out.println("Executando: POST /motos - Placa duplicada");
            when(motoService.salvarMoto(any(Moto.class), eq(1L)))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Moto com essa placa já existe"));

            mockMvc.perform(post("/motos")
                    .with(jwt().authorities(() -> "ROLE_PRESIDENT"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(motoRequestDTO)))
                    .andExpect(status().isConflict());
        }

        @Test
        @DisplayName("Deve retornar 409 ao associar a membro inativo")
        void deveRetornar409AoAssociarMembroInativo() throws Exception {
            System.out.println("Executando: POST /motos - Membro inativo");
            when(motoService.salvarMoto(any(Moto.class), eq(1L)))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Moto não pode ser associada a um membro inativo"));

            mockMvc.perform(post("/motos")
                    .with(jwt().authorities(() -> "ROLE_PRESIDENT"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(motoRequestDTO)))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("2. Listagem de Motos (GET /motos)")
    class ListagemMotos {

        @Test
        @DisplayName("Deve listar todas as motos")
        void deveListarMotos() throws Exception {
            System.out.println("Executando: GET /motos - Listar todas");
            when(motoService.listarMotos(any(), any(), any(), any())).thenReturn(List.of(moto));

            mockMvc.perform(get("/motos")
                .with(jwt().authorities(() -> "ROLE_PRESIDENT")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].placa").value("ABC1234"))
                .andExpect(jsonPath("$[0].modelo").value("CB 500"));
        }

        @Test
        @DisplayName("Deve filtrar motos por membro")
        void deveFiltrarPorMembro() throws Exception {
            System.out.println("Executando: GET /motos?idMembro=1 - Sucesso");
            when(motoService.listarMotos(eq(1L), any(), any(), any())).thenReturn(List.of(moto));

            mockMvc.perform(get("/motos?idMembro=1")
                .with(jwt().authorities(() -> "ROLE_PRESIDENT")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idMembro").value(1));
        }

        @Test
        @DisplayName("Deve filtrar motos por marca")
        void deveFiltrarPorMarca() throws Exception {
            System.out.println("Executando: GET /motos?marca=Honda - Sucesso");
            when(motoService.listarMotos(any(), any(), eq("Honda"), any())).thenReturn(List.of(moto));

            mockMvc.perform(get("/motos?marca=Honda")
                .with(jwt().authorities(() -> "ROLE_PRESIDENT")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].marca").value("Honda"));
        }
    }

    @Nested
    @DisplayName("3. Busca de Moto por Placa")
    class BuscaMoto {

        @Test
        @DisplayName("Deve buscar moto existente por placa")
        void deveBuscarMotoPorPlaca() throws Exception {
            System.out.println("Executando: GET /motos/ABC1234 - Sucesso");
            when(motoService.buscarPorPlaca("ABC1234")).thenReturn(moto);

            mockMvc.perform(get("/motos/ABC1234")
                .with(jwt().authorities(() -> "ROLE_PRESIDENT")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.placa").value("ABC1234"))
                .andExpect(jsonPath("$.modelo").value("CB 500"));
        }

        @Test
        @DisplayName("Deve retornar 404 ao buscar placa inexistente")
        void deveRetornar404AoBuscarPlacaInexistente() throws Exception {
            System.out.println("Executando: GET /motos/XYZ9999 - Não encontrado");
            when(motoService.buscarPorPlaca("XYZ9999"))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Moto não encontrada"));

            mockMvc.perform(get("/motos/XYZ9999")
                .with(jwt().authorities(() -> "ROLE_PRESIDENT")))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @DisplayName("4. Atualização de Moto (PUT /motos/{placa})")
    class AtualizacaoMoto {

        @Test
        @DisplayName("Deve atualizar uma moto com sucesso")
        void deveAtualizarMotoComSucesso() throws Exception {
            System.out.println("Executando: PUT /motos/ABC1234 - Sucesso");
            when(motoService.atualizarMoto(any(Moto.class), eq(1L))).thenReturn(moto);

            mockMvc.perform(put("/motos/ABC1234")
                    .with(jwt().authorities(() -> "ROLE_PRESIDENT"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(motoRequestDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.placa").value("ABC1234"));
        }

        @Test
        @DisplayName("Deve retornar 404 ao tentar atualizar para membro inexistente")
        void deveRetornar404AoAtualizarMembroInexistente() throws Exception {
            System.out.println("Executando: PUT /motos/ABC1234 - Membro inexistente");
            when(motoService.atualizarMoto(any(Moto.class), eq(1L)))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Membro não encontrado"));

            mockMvc.perform(put("/motos/ABC1234")
                    .with(jwt().authorities(() -> "ROLE_PRESIDENT"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(motoRequestDTO)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Deve retornar 409 ao tentar transferir para membro inativo")
        void deveRetornar409AoTransferirMembroInativo() throws Exception {
            when(motoService.atualizarMoto(any(Moto.class), eq(1L)))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Moto não pode ser associada a um membro inativo"));

            mockMvc.perform(put("/motos/ABC1234")
                    .with(jwt().authorities(() -> "ROLE_PRESIDENT"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(motoRequestDTO)))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("5. Deleção de Moto (DELETE /motos/{idMembro}/{placa})")
    class DelecaoMoto {

        @Test
        @DisplayName("Deve deletar uma moto com sucesso")
        void deveDeletarMotoComSucesso() throws Exception {
            org.mockito.Mockito.doNothing().when(motoService).deletarMoto(1L, "ABC1234");

            mockMvc.perform(delete("/motos/1/ABC1234")
                    .with(jwt().authorities(() -> "ROLE_PRESIDENT")))
                    .andExpect(status().isNoContent());
        }

        @Test
        @DisplayName("Deve retornar 404 ao deletar moto com placa inexistente")
        void deveRetornar404AoDeletarMotoComPlacaInexistente() throws Exception {
            org.mockito.Mockito.doThrow(new br.com.mam.sgmc.errors.ResourceNotFoundException("Moto não encontrada"))
                .when(motoService).deletarMoto(1L, "XYZ0000");

            mockMvc.perform(delete("/motos/1/XYZ0000")
                    .with(jwt().authorities(() -> "ROLE_PRESIDENT")))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Deve retornar 409 ao deletar moto que não pertence ao membro")
        void deveRetornar409AoDeletarMotoQueNaoPertenceAoMembro() throws Exception {
            org.mockito.Mockito.doThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Moto não pertence ao membro"))
                .when(motoService).deletarMoto(99L, "ABC1234");

            mockMvc.perform(delete("/motos/99/ABC1234")
                    .with(jwt().authorities(() -> "ROLE_PRESIDENT")))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("6. Testes de Segurança")
    class SegurancaMoto {

        @Test
        @DisplayName("Deve retornar 401 ao acessar sem autenticação")
        void deveRetornar401SemAutenticacao() throws Exception {
            mockMvc.perform(get("/motos"))
                    .andExpect(status().isUnauthorized());
        }
    }
}
