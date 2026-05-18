package br.com.mam.sgmc.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import br.com.mam.sgmc.errors.ResourceNotFoundException;
import br.com.mam.sgmc.model.Cargo;
import br.com.mam.sgmc.model.Identificacao;
import br.com.mam.sgmc.model.Membro;
import br.com.mam.sgmc.model.Sede;
import br.com.mam.sgmc.model.enums.Ativo;
import br.com.mam.sgmc.model.localizacao.Pais;
import br.com.mam.sgmc.repository.CargoRepository;
import br.com.mam.sgmc.repository.MembroRepository;
import br.com.mam.sgmc.repository.PaisRepository;
import br.com.mam.sgmc.repository.SedeRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("MembroService Unit Tests")
class MembroServiceTest {

    @Mock
    private MembroRepository membroRepository;

    @Mock
    private CargoRepository cargoRepository;

    @Mock
    private SedeRepository sedeRepository;

    @Mock
    private PaisRepository paisRepository;

    @InjectMocks
    private MembroService membroService;

    private Membro membro;
    private Cargo cargo;
    private Sede sede;
    private Pais pais;

    @BeforeEach
    void setUp() {
        cargo = new Cargo();
        cargo.setId(1L);
        cargo.setTitulo("Presidente");

        sede = new Sede();
        sede.setId(1L);
        sede.setNome("Sede Central");

        pais = new Pais();
        pais.setSigla("BR");
        pais.setNome("Brasil");

        membro = new Membro();
        membro.setId(1L);
        membro.setNome("João Silva");
        membro.setApelido("João");
        membro.setSexo("M");
        membro.setEmail("joao@email.com");
        membro.setTelefone("11999999999");
        membro.setDataNascimento(Date.valueOf(LocalDate.of(1990, 1, 1)));
        membro.setEhBatizado(0); // BATIZADO
        membro.setTemEscudo(0); // TEM_ESCUDO
        membro.setAtivo(0); // ATIVO
        membro.setTamanhoCamisa("G");
        membro.setDataAdmissao(Date.valueOf(LocalDate.now()));
        
        Identificacao identidade = new Identificacao();
        identidade.setTipo("CPF");
        identidade.setIdentidade("12345678909");
        identidade.setEmissor("SSP");
        identidade.setDataEmissao(Date.valueOf(LocalDate.of(2010, 1, 1)));
        identidade.setPais(pais);
        membro.setIdentidade(identidade);
    }

    @Test
    @DisplayName("Deve salvar um membro com sucesso")
    void deveSalvarMembroComSucesso() {
        when(membroRepository.findByNome(any())).thenReturn(null);
        when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo));
        when(sedeRepository.findById(1L)).thenReturn(Optional.of(sede));
        when(paisRepository.findById("BR")).thenReturn(Optional.of(pais));
        when(membroRepository.save(any(Membro.class))).thenReturn(membro);

        Membro salvo = membroService.salvarMembro(membro, 1L, 1L, "BR");

        assertNotNull(salvo);
        assertEquals("João Silva", salvo.getNome());
        verify(membroRepository).save(any(Membro.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar membro com nome existente")
    void deveLancarExcecaoAoSalvarMembroComNomeExistente() {
        when(membroRepository.findByNome("João Silva")).thenReturn(membro);

        assertThrows(ResponseStatusException.class, () -> 
            membroService.salvarMembro(membro, 1L, 1L, "BR")
        );
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar membro com cargo inexistente")
    void deveLancarExcecaoAoSalvarMembroComCargoInexistente() {
        when(membroRepository.findByNome(any())).thenReturn(null);
        when(cargoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            membroService.salvarMembro(membro, 1L, 1L, "BR")
        );
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar membro com sede inexistente")
    void deveLancarExcecaoAoSalvarMembroComSedeInexistente() {
        when(membroRepository.findByNome(any())).thenReturn(null);
        when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo));
        when(sedeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            membroService.salvarMembro(membro, 1L, 1L, "BR")
        );
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar membro no Brasil sem CPF")
    void deveLancarExcecaoAoSalvarMembroNoBrasilSemCPF() {
        when(membroRepository.findByNome(any())).thenReturn(null);
        membro.getIdentidade().setTipo("RG");

        assertThrows(ResponseStatusException.class, () -> 
            membroService.salvarMembro(membro, null, null, "BR")
        );
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar membro com país inexistente")
    void deveLancarExcecaoAoSalvarMembroComPaisInexistente() {
        when(membroRepository.findByNome(any())).thenReturn(null);
        when(paisRepository.findById("BR")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            membroService.salvarMembro(membro, null, null, "BR")
        );
    }

    @Test
    @DisplayName("Deve atualizar um membro com sucesso")
    void deveAtualizarMembroComSucesso() {
        when(membroRepository.findById(1L)).thenReturn(Optional.of(membro));
        when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo));
        when(sedeRepository.findById(1L)).thenReturn(Optional.of(sede));
        when(paisRepository.findById("BR")).thenReturn(Optional.of(pais));
        when(membroRepository.save(any(Membro.class))).thenReturn(membro);

        Membro atualizado = membroService.atualizarMembro(membro, 1L, 1L, 1L, "BR");

        assertNotNull(atualizado);
        verify(membroRepository).save(any(Membro.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar membro inexistente")
    void deveLancarExcecaoAoAtualizarMembroInexistente() {
        when(membroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            membroService.atualizarMembro(membro, 99L, 1L, 1L, "BR")
        );
    }

    @Test
    @DisplayName("Deve listar todos os membros sem filtro")
    void deveListarMembrosSemFiltro() {
        when(membroRepository.findAll()).thenReturn(List.of(membro));

        List<Membro> membros = membroService.listarMembros(null);

        assertNotNull(membros);
        assertEquals(1, membros.size());
        assertNotNull(membros.get(0).getIdade());
    }

    @Test
    @DisplayName("Deve listar membros filtrados por status ativo")
    void deveListarMembrosFiltradosPorAtivo() {
        when(membroRepository.findByAtivo(0)).thenReturn(List.of(membro));

        List<Membro> membros = membroService.listarMembros(0);

        assertNotNull(membros);
        assertEquals(1, membros.size());
    }

    @Test
    @DisplayName("Deve buscar membro por ID com sucesso")
    void deveBuscarMembroPorIdComSucesso() {
        when(membroRepository.findById(1L)).thenReturn(Optional.of(membro));

        Membro encontrado = membroService.buscarPorId(1L);

        assertNotNull(encontrado);
        assertEquals(1L, encontrado.getId());
        assertNotNull(encontrado.getIdade());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar membro por ID inexistente")
    void deveLancarExcecaoAoBuscarMembroPorIdInexistente() {
        when(membroRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            membroService.buscarPorId(99L)
        );
    }

    @Test
    @DisplayName("Deve inativar membro com sucesso")
    void deveInativarMembroComSucesso() {
        when(membroRepository.findById(1L)).thenReturn(Optional.of(membro));
        when(membroRepository.save(any(Membro.class))).thenReturn(membro);

        membroService.inativarMembro(1L);

        assertEquals(Ativo.INATIVO.getCodigo(), membro.getAtivo());
        verify(membroRepository).save(membro);
    }

    @Test
    @DisplayName("Deve calcular idade com sucesso")
    void deveCalcularIdadeComSucesso() {
        membro.setDataNascimento(Date.valueOf(LocalDate.now().minusYears(30)));
        Integer idade = membroService.calcularIdade(membro);
        assertEquals(30, idade);
    }
}
