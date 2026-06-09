package br.com.mam.sgmc.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.mam.sgmc.model.Cargo;
import br.com.mam.sgmc.repository.CargoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - CargoService")
class CargoServiceTest {

    @Mock
    private CargoRepository cargoRepository;

    @InjectMocks
    private CargoService cargoService;

    private Cargo cargo;

    @BeforeEach
    void setUp() {
        cargo = new Cargo();
        cargo.setId(1L);
        cargo.setTitulo("Presidente");
        cargo.setDescricao("Líder do moto clube");
    }

    @Test
    @DisplayName("Deve salvar um cargo com sucesso")
    void deveSalvarCargoComSucesso() {
        when(cargoRepository.findByTitulo("Presidente")).thenReturn(null);
        when(cargoRepository.save(any(Cargo.class))).thenReturn(cargo);

        Cargo cargoSalvo = cargoService.salvarCargo(cargo);

        assertNotNull(cargoSalvo);
        assertEquals("Presidente", cargoSalvo.getTitulo());
        assertEquals("Líder do moto clube", cargoSalvo.getDescricao());
        verify(cargoRepository).save(cargo);
    }

    @Test
    @DisplayName("Deve lançar exceção ao salvar cargo com título duplicado")
    void deveLancarExcecaoAoSalvarCargoComTituloDuplicado() {
        when(cargoRepository.findByTitulo("Presidente")).thenReturn(cargo);

        assertThrows(IllegalArgumentException.class, () ->
            cargoService.salvarCargo(cargo)
        );
        verify(cargoRepository, never()).save(any());
    }
}
