package br.com.mam.sgmc.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import br.com.mam.sgmc.model.Cargo;
import br.com.mam.sgmc.model.Membro;
import br.com.mam.sgmc.model.Posse;
import br.com.mam.sgmc.model.pk.PossePk;

@SpringBootTest
@Transactional
@DisplayName("PosseRepository Integration Tests")
class PosseRepositoryTest {

    @org.springframework.test.context.bean.override.mockito.MockitoBean
    private org.springframework.web.client.RestClient keycloakRestClient;

    @Autowired
    private PosseRepository posseRepository;

    @Autowired
    private MembroRepository membroRepository;

    @Autowired
    private CargoRepository cargoRepository;

    @Test
    @DisplayName("Deve persistir e buscar uma posse com chave composta com sucesso")
    void devePersistirEBuscarPosseComSucesso() {
        // 1. Criar e persistir um Cargo
        Cargo cargo = new Cargo();
        cargo.setTitulo("Diretor de Eventos");
        cargo.setDescricao("Responsável pela organização de eventos e passeios");
        cargo = cargoRepository.save(cargo);

        // 2. Criar e persistir um Membro
        Membro membro = new Membro();
        membro.setNome("Roberto Silva");
        membro.setApelido("Beto");
        membro.setSexo("M");
        membro.setEmail("beto@email.com");
        membro.setTelefone("11988887777");
        membro.setDataNascimento(Date.valueOf(LocalDate.of(1985, 6, 15)));
        membro.setEhBatizado(0);
        membro.setTemEscudo(0);
        membro.setAtivo(0);
        membro.setTamanhoCamisa("GG");
        membro.setDataAdmissao(Date.valueOf(LocalDate.now()));
        membro.setNacionalidade("Brasileira");
        membro.setNaturalidade("Rio de Janeiro");
        membro = membroRepository.save(membro);

        // 3. Criar a chave composta PossePk
        PossePk possePk = new PossePk();
        possePk.setCargo(cargo);
        possePk.setMembro(membro);

        // 4. Criar e persistir a Posse
        Posse posse = new Posse();
        posse.setPossePk(possePk);
        posse.setDataInicio(Date.valueOf(LocalDate.now()));
        posse.setDataFim(Date.valueOf(LocalDate.now().plusYears(1)));

        Posse posseSalva = posseRepository.save(posse);
        assertNotNull(posseSalva);
        assertEquals(cargo.getId(), posseSalva.getPossePk().getCargo().getId());
        assertEquals(membro.getId(), posseSalva.getPossePk().getMembro().getId());

        // 5. Buscar a posse pelo ID composto
        Optional<Posse> posseBuscada = posseRepository.findById(possePk);
        assertTrue(posseBuscada.isPresent());
        assertEquals(posse.getDataInicio(), posseBuscada.get().getDataInicio());
        assertEquals(posse.getDataFim(), posseBuscada.get().getDataFim());
    }
}
