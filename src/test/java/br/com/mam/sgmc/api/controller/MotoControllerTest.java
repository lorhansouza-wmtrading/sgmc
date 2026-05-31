package br.com.mam.sgmc.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
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

@SpringBootTest
@Transactional
@DisplayName("MotoController Comprehensive Tests")
class MotoControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private MotoService motoService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private MotoRequestDTO motoRequestDTO;
    private Moto moto;
    private Membro membro;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        motoRequestDTO = new MotoRequestDTO();
        motoRequestDTO.setPlaca("ABC1234");
        motoRequestDTO.setModelo("CB 500");
        motoRequestDTO.setMarca("Honda");
        motoRequestDTO.setAno(2022);
        motoRequestDTO.setCor("Preta");
        motoRequestDTO.setIdMembro(1L);
        motoRequestDTO.setNomeSeguradora("Porto Seguro");
        motoRequestDTO.setTipoSeguro("Total");
        motoRequestDTO.setValidadeSeguro(Instant.now());
        motoRequestDTO.setValorFranquia(1000.0f);

        membro = new Membro();
        membro.setId(1L);
        membro.setNome("Teste");
        membro.setAtivo(Ativo.ATIVO.getCodigo());

        Marca marca = new Marca();
        marca.setNome("Honda");

        Modelo modelo = new Modelo();
        modelo.setNome("CB 500");
        modelo.setMarca(marca);

        CondicaoSeguro condicaoSeguro = new CondicaoSeguro();
        condicaoSeguro.setTipo("Total");
        condicaoSeguro.setValidadeFim(Instant.now());

        Seguro seguro = new Seguro();
        seguro.setNome("Porto Seguro");
        seguro.setCondicoesSeguro(List.of(condicaoSeguro));

        moto = new Moto();
        moto.setPlaca("ABC1234");
        moto.setAno(2022);
        moto.setCor("Preta");
        moto.setModelo(modelo);
        moto.setSeguro(seguro);
        moto.setMembro(membro);
    }

    @Nested
    @DisplayName("1. Criação de Moto (POST /motos)")
    class CriacaoMoto {

        @Test
        @DisplayName("Deve criar moto com todos os campos válidos")
        void deveCriarMotoComSucesso() throws Exception {
            System.out.println("Executando: POST /motos - Sucesso");
            when(motoService.salvarMoto(any(Moto.class), eq(1L))).thenReturn(moto);

            mockMvc.perform(post("/motos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(motoRequestDTO)))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location"));
        }

        @Test
        @DisplayName("Deve retornar 400 ao tentar criar moto sem placa (NotBlank)")
        void deveRetornar400AoCriarMotoSemPlaca() throws Exception {
            System.out.println("Executando: POST /motos - Placa ausente");
            motoRequestDTO.setPlaca(null);

            mockMvc.perform(post("/motos")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(motoRequestDTO)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Deve retornar 409 ao tentar criar moto com placa duplicada")
        void deveRetornar409AoCriarMotoComPlacaDuplicada() throws Exception {
            System.out.println("Executando: POST /motos - Placa duplicada");
            when(motoService.salvarMoto(any(Moto.class), eq(1L)))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Placa já cadastrada"));

            mockMvc.perform(post("/motos")
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

            mockMvc.perform(get("/motos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].placa").value("ABC1234"))
                    .andExpect(jsonPath("$[0].modelo").value("CB 500"));
        }

        @Test
        @DisplayName("Deve filtrar motos por membro")
        void deveFiltrarPorMembro() throws Exception {
            System.out.println("Executando: GET /motos?idMembro=1 - Sucesso");
            when(motoService.listarMotos(eq(1L), any(), any(), any())).thenReturn(List.of(moto));

            mockMvc.perform(get("/motos?idMembro=1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$[0].idMembro").value(1L));
        }

        @Test
        @DisplayName("Deve filtrar motos por marca")
        void deveFiltrarPorMarca() throws Exception {
            System.out.println("Executando: GET /motos?marca=Honda - Sucesso");
            when(motoService.listarMotos(any(), any(), eq("Honda"), any())).thenReturn(List.of(moto));

            mockMvc.perform(get("/motos?marca=Honda"))
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

            mockMvc.perform(get("/motos/ABC1234"))
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

            mockMvc.perform(get("/motos/XYZ9999"))
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
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(motoRequestDTO)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Deve retornar 409 ao tentar transferir para membro inativo")
        void deveRetornar409AoTransferirMembroInativo() throws Exception {
            System.out.println("Executando: PUT /motos/ABC1234 - Transferência Membro Inativo");
            when(motoService.atualizarMoto(any(Moto.class), eq(1L)))
                .thenThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Moto não pode ser associada a um membro inativo"));

            mockMvc.perform(put("/motos/ABC1234")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(motoRequestDTO)))
                    .andExpect(status().isConflict());
        }
    }
}
