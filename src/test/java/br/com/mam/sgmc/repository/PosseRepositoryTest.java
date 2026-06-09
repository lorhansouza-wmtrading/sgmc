package br.com.mam.sgmc.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import br.com.mam.sgmc.model.Cargo;
import br.com.mam.sgmc.model.Membro;
import br.com.mam.sgmc.model.Posse;
import br.com.mam.sgmc.model.Identificacao;
import br.com.mam.sgmc.model.Sede;
import br.com.mam.sgmc.model.localizacao.Cidade;
import br.com.mam.sgmc.model.localizacao.Uf;
import br.com.mam.sgmc.model.localizacao.Pais;
import br.com.mam.sgmc.model.pk.PossePk;
import br.com.mam.sgmc.repository.SedeRepository;
import br.com.mam.sgmc.repository.CidadeRepository;
import br.com.mam.sgmc.repository.UfRepository;

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

    @Autowired
    private PaisRepository paisRepository;

    @Autowired
    private UfRepository ufRepository;

    @Autowired
    private CidadeRepository cidadeRepository;

    @Autowired
    private SedeRepository sedeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setupSchema() {
        try {
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
            jdbcTemplate.execute("ALTER TABLE identificacao MODIFY id_identificacao INT AUTO_INCREMENT");
            jdbcTemplate.execute("ALTER TABLE membro MODIFY COLUMN sexo CHAR(1) NOT NULL");
            jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
        } catch (Exception e) {
            // Ignora se for H2 ou outro banco
        }
    }

    @Test
    @DisplayName("Deve persistir e buscar uma posse com chave composta com sucesso")
    void devePersistirEBuscarPosseComSucesso() {
        // 1. Criar e persistir um Cargo
        Cargo cargo = new Cargo();
        cargo.setTitulo("Diretor de Eventos");
        cargo.setDescricao("Responsável pela organização de eventos e passeios");
        cargo = cargoRepository.save(cargo);

        // 2. Criar e persistir um Membro (com Identificação e Sede)
        Pais pais = paisRepository.findById("BR").orElseGet(() -> {
            Pais newPais = new Pais();
            newPais.setSigla("BR");
            newPais.setNome("Brasil");
            newPais.setContinente("América do Sul");
            return paisRepository.save(newPais);
        });

        Uf uf = ufRepository.findById("RJ").orElseGet(() -> {
            Uf newUf = new Uf();
            newUf.setUfSigla("RJ");
            newUf.setNome("Rio de Janeiro");
            newUf.setRegiao("Sudeste");
            newUf.setPais(pais);
            return ufRepository.save(newUf);
        });

        Cidade cidade = cidadeRepository.findWithFilters("RJ", "Rio de Janeiro", null).stream().findFirst().orElseGet(() -> {
            Cidade newCidade = new Cidade();
            newCidade.setNome("Rio de Janeiro");
            newCidade.setUf(uf);
            return cidadeRepository.save(newCidade);
        });

        Sede sede = sedeRepository.findWithFilters("Sede Central", "Rio de Janeiro", "RJ", "Brasil").stream().findFirst().orElseGet(() -> {
            Sede newSede = new Sede();
            newSede.setNome("Sede Central");
            newSede.setEndereco("Rua das Flores");
            newSede.setBairro("Centro");
            newSede.setNumero("123");
            newSede.setCodigoPostal("20000-000");
            newSede.setAtiva(true);
            newSede.setCidade(cidade);
            return sedeRepository.save(newSede);
        });

        Identificacao identificacao = new Identificacao();
        identificacao.setTipo("RG");
        identificacao.setIdentidade("123456789");
        identificacao.setEmissor("SSP");
        identificacao.setDataEmissao(Date.valueOf(LocalDate.now()));
        identificacao.setPais(pais);

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
        membro.setEstadoCivil("Solteiro");
        membro.setNomeContatoEmergencia("Maria Silva");
        membro.setTelefoneContatoEmergencia("11999998888");
        membro.setSede(sede);
        membro.setIdentidade(identificacao);
        identificacao.setMembro(membro);

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
