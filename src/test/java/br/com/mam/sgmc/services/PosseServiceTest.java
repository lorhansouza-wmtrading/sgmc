package br.com.mam.sgmc.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import br.com.mam.sgmc.errors.ResourceNotFoundException;
import br.com.mam.sgmc.model.Cargo;
import br.com.mam.sgmc.model.Membro;
import br.com.mam.sgmc.model.Posse;
import br.com.mam.sgmc.model.pk.PossePk;
import br.com.mam.sgmc.repository.CargoRepository;
import br.com.mam.sgmc.repository.MembroRepository;
import br.com.mam.sgmc.repository.PosseRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - PosseService")
class PosseServiceTest {

    @Mock
    private PosseRepository posseRepository;

    @Mock
    private MembroRepository membroRepository;

    @Mock
    private CargoRepository cargoRepository;

    @InjectMocks
    private PosseService posseService;

    private Membro membro;
    private Cargo cargo;
    private Posse posse;

    @BeforeEach
    void setUp() {
        membro = new Membro();
        membro.setId(1L);
        membro.setNome("João Silva");

        cargo = new Cargo();
        cargo.setId(1L);
        cargo.setTitulo("Presidente");

        PossePk pk = new PossePk(cargo, membro);
        posse = new Posse();
        posse.setPossePk(pk);
    }

    @Test
    @DisplayName("Deve listar posses por membro com sucesso")
    void deveListarPossesPorMembroComSucesso() {
        when(membroRepository.existsById(1L)).thenReturn(true);
        when(posseRepository.findByPossePkMembroId(1L)).thenReturn(List.of(posse));

        List<Posse> result = posseService.listarPossesPorMembro(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Presidente", result.get(0).getPossePk().getCargo().getTitulo());
    }

    @Test
    @DisplayName("Deve lançar exceção ao listar posses de membro inexistente")
    void deveLancarExcecaoAoListarPossesDeMembroInexistente() {
        when(membroRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> posseService.listarPossesPorMembro(1L));
    }

    @Test
    @DisplayName("Deve salvar uma posse com sucesso")
    void deveSalvarPosseComSucesso() {
        membro.setAtivo(1); // Ativo
        when(membroRepository.findById(1L)).thenReturn(Optional.of(membro));
        when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo));
        when(posseRepository.findByPossePkCargoId(1L)).thenReturn(List.of());
        when(posseRepository.save(any(Posse.class))).thenReturn(posse);

        Posse result = posseService.salvarPosse(1L, 1L, LocalDate.now(), null);

        assertNotNull(result);
        verify(posseRepository).save(any(Posse.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar posse de membro inativo")
    void deveLancarExcecaoAoSalvarPosseDeMembroInativo() {
        membro.setAtivo(0); // Inativo
        when(membroRepository.findById(1L)).thenReturn(Optional.of(membro));

        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> 
            posseService.salvarPosse(1L, 1L, LocalDate.now(), null)
        );
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar posse com cargo sobreposto")
    void deveLancarExcecaoAoSalvarPosseComCargoSobreposto() {
        membro.setAtivo(1);
        when(membroRepository.findById(1L)).thenReturn(Optional.of(membro));
        when(cargoRepository.findById(1L)).thenReturn(Optional.of(cargo));

        Membro outroMembro = new Membro();
        outroMembro.setId(2L);
        PossePk outroPk = new PossePk(cargo, outroMembro);
        Posse posseExistente = new Posse();
        posseExistente.setPossePk(outroPk);
        posseExistente.setDataInicio(java.sql.Date.valueOf(LocalDate.now().minusDays(5)));
        posseExistente.setDataFim(java.sql.Date.valueOf(LocalDate.now().plusDays(5)));

        when(posseRepository.findByPossePkCargoId(1L)).thenReturn(List.of(posseExistente));

        assertThrows(org.springframework.web.server.ResponseStatusException.class, () -> 
            posseService.salvarPosse(1L, 1L, LocalDate.now(), null)
        );
    }
}
