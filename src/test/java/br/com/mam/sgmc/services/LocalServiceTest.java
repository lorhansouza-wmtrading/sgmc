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
import org.springframework.web.server.ResponseStatusException;

import br.com.mam.sgmc.model.localizacao.Local;
import br.com.mam.sgmc.repository.LocalRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários - LocalService")
class LocalServiceTest {

    @Mock
    private LocalRepository localRepository;

    @InjectMocks
    private LocalService localService;

    private Local local;

    @BeforeEach
    void setUp() {
        local = new Local();
        local.setId(1L);
        local.setNome("Sede Campestre");
        local.setEndereco("Rua das Flores");
        local.setBairro("Centro");
        local.setNumero("123");
        local.setCodigoPostal("12345-678");
        local.setCapacidade(100);
        local.setContato("11988888888");
    }

    @Test
    @DisplayName("Deve criar um local com sucesso")
    void deveCriarLocalComSucesso() {
        when(localRepository.findByCodigoPostal("12345-678")).thenReturn(null);
        when(localRepository.save(any(Local.class))).thenReturn(local);

        Local localCriado = localService.criarLocal(local);

        assertNotNull(localCriado);
        assertEquals("Sede Campestre", localCriado.getNome());
        assertEquals("12345-678", localCriado.getCodigoPostal());
        verify(localRepository).save(local);
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar local com código postal duplicado")
    void deveLancarExcecaoAoCriarLocalComCodigoPostalDuplicado() {
        when(localRepository.findByCodigoPostal("12345-678")).thenReturn(local);

        assertThrows(ResponseStatusException.class, () ->
            localService.criarLocal(local)
        );
        verify(localRepository, never()).save(any());
    }
}
