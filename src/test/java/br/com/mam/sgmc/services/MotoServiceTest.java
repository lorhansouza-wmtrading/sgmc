package br.com.mam.sgmc.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.mam.sgmc.errors.ResourceNotFoundException;
import br.com.mam.sgmc.model.Membro;
import br.com.mam.sgmc.model.moto.CondicaoSeguro;
import br.com.mam.sgmc.model.moto.Marca;
import br.com.mam.sgmc.model.moto.Modelo;
import br.com.mam.sgmc.model.moto.Moto;
import br.com.mam.sgmc.model.moto.Seguro;
import br.com.mam.sgmc.repository.CondicaoSeguroRepository;
import br.com.mam.sgmc.repository.MarcaRepository;
import br.com.mam.sgmc.repository.ModeloRepository;
import br.com.mam.sgmc.repository.MotoRepository;
import br.com.mam.sgmc.repository.SeguroRepository;

@ExtendWith(MockitoExtension.class)
class MotoServiceTest {

    @Mock
    private MotoRepository motoRepository;

    @Mock
    private MembroService membroService;

    @Mock
    private MarcaRepository marcaRepository;

    @Mock
    private ModeloRepository modeloRepository;

    @Mock
    private SeguroRepository seguroRepository;

    @Mock
    private CondicaoSeguroRepository condicaoSeguroRepository;

    @InjectMocks
    private MotoService motoService;

    private Moto moto;
    private Modelo modelo;
    private Marca marca;
    private Seguro seguro;
    private CondicaoSeguro condicaoSeguro;
    private Membro membro;

    @BeforeEach
    void setUp() {
        marca = new Marca();
        marca.setNome("Honda");

        modelo = new Modelo();
        modelo.setNome("CB 500");
        modelo.setMarca(marca);

        condicaoSeguro = new CondicaoSeguro();
        condicaoSeguro.setTipo("Total");

        seguro = new Seguro();
        seguro.setNome("Porto Seguro");
        seguro.setCondicoesSeguro(List.of(condicaoSeguro));

        moto = new Moto();
        moto.setPlaca("ABC1234");
        moto.setAno(2022);
        moto.setCor("Preta");
        moto.setModelo(modelo);
        moto.setSeguro(seguro);

        membro = new Membro();
        membro.setId(1L);
        membro.setNome("Teste");
        membro.setAtivo(0); // ATIVO
    }

    @Test
    @DisplayName("Deve salvar uma moto com sucesso")
    void deveSalvarMotoComSucesso() {
        System.out.println("Executando: Deve salvar uma moto com sucesso");
        when(marcaRepository.findByNome(any())).thenReturn(marca);
        when(modeloRepository.findByNome(any())).thenReturn(modelo);
        when(seguroRepository.findByNome(any())).thenReturn(seguro);
        when(condicaoSeguroRepository.findByTipo(any())).thenReturn(condicaoSeguro);
        when(membroService.buscarPorId(1L)).thenReturn(membro);
        when(motoRepository.save(any(Moto.class))).thenReturn(moto);

        Moto motoSalva = motoService.salvarMoto(moto, 1L);

        assertNotNull(motoSalva);
        assertEquals("ABC1234", motoSalva.getPlaca());
        verify(motoRepository).save(moto);
        System.out.println("Sucesso: Moto com placa " + motoSalva.getPlaca() + " salva.");
    }

    @Test
    @DisplayName("Deve salvar uma moto com sucesso mesmo quando as entidades relacionadas não existem")
    void deveSalvarMotoComSucessoQuandoEntidadesNaoExistem() {
        System.out.println("Executando: Deve salvar uma moto quando entidades não existem");
        when(marcaRepository.findByNome(any())).thenReturn(null);
        when(marcaRepository.save(any())).thenReturn(marca);
        when(modeloRepository.findByNome(any())).thenReturn(null);
        when(modeloRepository.save(any())).thenReturn(modelo);
        when(seguroRepository.findByNome(any())).thenReturn(null);
        when(seguroRepository.save(any())).thenReturn(seguro);
        when(condicaoSeguroRepository.findByTipo(any())).thenReturn(null);
        when(condicaoSeguroRepository.save(any())).thenReturn(condicaoSeguro);
        when(membroService.buscarPorId(1L)).thenReturn(membro);
        when(motoRepository.save(any(Moto.class))).thenReturn(moto);

        Moto motoSalva = motoService.salvarMoto(moto, 1L);

        assertNotNull(motoSalva);
        verify(marcaRepository).save(any());
        verify(modeloRepository).save(any());
        verify(seguroRepository).save(any());
        verify(condicaoSeguroRepository).save(any());
        verify(motoRepository).save(moto);
        System.out.println("Sucesso: Entidades relacionadas criadas e moto salva.");
    }

    @Test
    @DisplayName("Deve buscar moto por placa")
    void deveBuscarPorPlaca() {
        System.out.println("Executando: Deve buscar moto por placa");
        when(motoRepository.findByPlaca("ABC1234")).thenReturn(moto);

        Moto encontrada = motoService.buscarPorPlaca("ABC1234");

        assertNotNull(encontrada);
        assertEquals("ABC1234", encontrada.getPlaca());
        System.out.println("Sucesso: Moto encontrada para a placa ABC1234.");
    }

    @Test
    @DisplayName("Deve lançar exceção ao não encontrar moto por placa")
    void deveLancarExcecaoAoNaoEncontrarPlaca() {
        System.out.println("Executando: Deve lançar exceção ao não encontrar placa");
        when(motoRepository.findByPlaca("XYZ9999")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> motoService.buscarPorPlaca("XYZ9999"));
        System.out.println("Sucesso: Exceção lançada corretamente para placa inexistente.");
    }

    @Test
    @DisplayName("Deve listar todas as motos")
    void deveListarMotos() {
        System.out.println("Executando: Deve listar todas as motos");
        when(motoRepository.findAll()).thenReturn(List.of(moto));

        List<Moto> motos = motoService.listarMotos(null, null, null, null);

        assertNotNull(motos);
        assertEquals(1, motos.size());
        System.out.println("Sucesso: Lista de motos retornada com " + motos.size() + " item(ns).");
    }

    @Test
    @DisplayName("Deve atualizar uma moto com sucesso")
    void deveAtualizarMotoComSucesso() {
        System.out.println("Executando: Deve atualizar uma moto com sucesso");
        when(motoRepository.findByPlaca(any())).thenReturn(moto);
        when(modeloRepository.findByNome(any())).thenReturn(modelo);
        when(marcaRepository.findByNome(any())).thenReturn(marca);
        when(seguroRepository.findByNome(any())).thenReturn(seguro);
        when(condicaoSeguroRepository.findByTipo(any())).thenReturn(condicaoSeguro);
        when(membroService.buscarPorId(1L)).thenReturn(membro);
        when(motoRepository.save(any(Moto.class))).thenReturn(moto);

        Moto motoAtualizada = motoService.atualizarMoto(moto, 1L);

        assertNotNull(motoAtualizada);
        verify(motoRepository).save(any(Moto.class));
        System.out.println("Sucesso: Moto com placa " + motoAtualizada.getPlaca() + " atualizada.");
    }
}
