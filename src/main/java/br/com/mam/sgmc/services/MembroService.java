package br.com.mam.sgmc.services;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.mam.sgmc.errors.ResourceNotFoundException;
import br.com.mam.sgmc.model.Identificacao;
import br.com.mam.sgmc.model.Membro;
import br.com.mam.sgmc.model.enums.Ativo;
import br.com.mam.sgmc.model.Cargo;
import br.com.mam.sgmc.model.Posse;
import br.com.mam.sgmc.model.pk.PossePk;
import br.com.mam.sgmc.repository.CargoRepository;
import br.com.mam.sgmc.repository.PosseRepository;
import br.com.mam.sgmc.repository.FichaMedicaRepository;
import br.com.mam.sgmc.repository.SedeRepository;
import br.com.mam.sgmc.repository.MembroRepository;
import br.com.mam.sgmc.repository.PaisRepository;
import br.com.mam.sgmc.repository.IdentificacaoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembroService {
    private final MembroRepository membroRepository;
    private final FichaMedicaRepository fichaMedicaRepository;
    private final SedeRepository sedeRepository;
    private final PaisRepository paisRepository;
    private final CargoRepository cargoRepository;
    private final PosseRepository posseRepository;
    private final IdentificacaoRepository identificacaoRepository;

    public Membro salvarMembro(Membro membro, Long idCargo, Long idSede, String paisSigla) {
        if (membro.getIdentidade() != null && membro.getIdentidade().getIdentidade() != null) {
            Identificacao identExistente = identificacaoRepository.findByIdentidade(membro.getIdentidade().getIdentidade());
            if (identExistente != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe um membro cadastrado com este documento/CPF.");
            }
        }
        

        
        if (idSede != null) {
            membro.setSede(sedeRepository.findById(idSede)
                .orElseThrow(() -> new ResourceNotFoundException("Sede não encontrada com ID: " + idSede)));
        }

        if (membro.getIdentidade() != null && paisSigla != null) {
            if ("BR".equalsIgnoreCase(paisSigla) && !"CPF".equalsIgnoreCase(membro.getIdentidade().getTipo())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Para o Brasil, o tipo de identificação deve ser CPF.");
            }
            membro.getIdentidade().setPais(paisRepository.findById(paisSigla)
                .orElseThrow(() -> new ResourceNotFoundException("País não encontrado com sigla: " + paisSigla)));
        }
        Membro membroBanco = this.membroRepository.save(membro);
        if(membro.getFichaMedica() != null){
            membro.getFichaMedica().setMembro(membroBanco);
            membroBanco.setFichaMedica(this.fichaMedicaRepository.save(membro.getFichaMedica()));
        }

        if (idCargo != null) {
            Cargo cargo = cargoRepository.findById(idCargo)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado com ID: " + idCargo));
            
            PossePk possePk = new PossePk(cargo, membroBanco);
            Posse posse = new Posse();
            posse.setPossePk(possePk);
            posse.setDataInicio(membroBanco.getDataAdmissao() != null ? membroBanco.getDataAdmissao() : Date.valueOf(LocalDate.now()));
            
            this.posseRepository.save(posse);
        }

        return membroBanco;
    }

    public Membro atualizarMembro(Membro membro, Long id, Long idCargo, Long idSede, String paisSigla) {
        Membro membroExistente = membroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membro não encontrado com ID: " + id));
        
        atualizarDadosBasicos(membroExistente, membro);
        atualizarSede(membroExistente, idSede);
        atualizarIdentidade(membroExistente, membro, paisSigla);

        membroExistente = this.membroRepository.save(membroExistente);

        atualizarFichaMedica(membroExistente, membro);
        atualizarPosse(membroExistente, idCargo);

        return membroExistente;
    }

    private void atualizarDadosBasicos(Membro membroExistente, Membro membro) {
        membroExistente.setNome(membro.getNome());
        membroExistente.setApelido(membro.getApelido());
        membroExistente.setSexo(membro.getSexo());
        membroExistente.setEmail(membro.getEmail());
        membroExistente.setTelefone(membro.getTelefone());
        membroExistente.setDataNascimento(membro.getDataNascimento());
        membroExistente.setNacionalidade(membro.getNacionalidade());
        membroExistente.setNaturalidade(membro.getNaturalidade());
        membroExistente.setEstadoCivil(membro.getEstadoCivil());
        membroExistente.setEhBatizado(membro.getEhBatizado());
        membroExistente.setTemEscudo(membro.getTemEscudo());
        membroExistente.setTamanhoCamisa(membro.getTamanhoCamisa());
        membroExistente.setDataAdmissao(membro.getDataAdmissao());
        
        if (membro.getAtivo() != null && !membro.getAtivo().equals(membroExistente.getAtivo())) {
            membroExistente.setAtivo(membro.getAtivo().equals(Ativo.INATIVO.getCodigo()) 
                    ? Ativo.INATIVO.getCodigo() 
                    : Ativo.ATIVO.getCodigo());
        }
    }

    private void atualizarSede(Membro membroExistente, Long idSede) {
        if (idSede != null) {
            membroExistente.setSede(sedeRepository.findById(idSede)
                .orElseThrow(() -> new ResourceNotFoundException("Sede não encontrada com ID: " + idSede)));
        }
    }

    private void atualizarIdentidade(Membro membroExistente, Membro membro, String paisSigla) {
        if (membro.getIdentidade() != null && paisSigla != null) {
            if ("BR".equalsIgnoreCase(paisSigla) && !"CPF".equalsIgnoreCase(membro.getIdentidade().getTipo())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Para o Brasil, o tipo de identificação deve ser CPF.");
            }
            
            if (membro.getIdentidade().getIdentidade() != null) {
                Identificacao identExistente = identificacaoRepository.findByIdentidade(membro.getIdentidade().getIdentidade());
                if (identExistente != null && (membroExistente.getIdentidade() == null || !identExistente.getIdIdentificacao().equals(membroExistente.getIdentidade().getIdIdentificacao()))) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe um membro cadastrado com este documento/CPF.");
                }
            }
            
            Identificacao identidade = membro.getIdentidade();
            identidade.setPais(paisRepository.findById(paisSigla)
                .orElseThrow(() -> new ResourceNotFoundException("País não encontrado com sigla: " + paisSigla)));
            
            if (membroExistente.getIdentidade() != null) {
                identidade.setIdIdentificacao(membroExistente.getIdentidade().getIdIdentificacao());
            }
            identidade.setMembro(membroExistente);
            membroExistente.setIdentidade(identidade);
        }
    }

    private void atualizarFichaMedica(Membro membroExistente, Membro membro) {
        if (membro.getFichaMedica() != null) {
            membro.getFichaMedica().setMembro(membroExistente);
            membroExistente.setFichaMedica(this.fichaMedicaRepository.save(membro.getFichaMedica()));
        }
    }

    private void atualizarPosse(Membro membroExistente, Long idCargo) {
        if (idCargo != null) {
            Cargo cargo = cargoRepository.findById(idCargo)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado com ID: " + idCargo));
            
            PossePk possePk = new PossePk(cargo, membroExistente);
            if (!this.posseRepository.existsById(possePk)) {
                Posse posse = new Posse();
                posse.setPossePk(possePk);
                posse.setDataInicio(Date.valueOf(LocalDate.now()));
                this.posseRepository.save(posse);
            }
        }
    }

    public List<Membro> listarMembros(Integer ativo) {
        List<Membro> membrosBanco;
        if (ativo != null) {
            membrosBanco = this.membroRepository.findByAtivo(ativo);
            for (Membro membro : membrosBanco) {
                membro.setIdade(this.calcularIdade(membro));
            }
            return membrosBanco;
        }
        membrosBanco = this.membroRepository.findAll();
        for (Membro membro : membrosBanco) {
            membro.setIdade(this.calcularIdade(membro));
        }
        return membrosBanco;
    }

    public Membro buscarPorId(Long id) {
        Membro membro = this.membroRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Este id do membro não existe!"));
        membro.setIdade(this.calcularIdade(membro));
        return membro;
    }

    public void inativarMembro(Long id) {
        Membro membro = this.buscarPorId(id);
        membro.setAtivo(Ativo.INATIVO.getCodigo());
        this.membroRepository.save(membro);
    }

    public Integer calcularIdade(Membro membro) {
        if (membro.getDataNascimento() == null) {
            return null;
        }
        LocalDate hoje = LocalDate.now();
        LocalDate dataNascimento = membro.getDataNascimento().toLocalDate();
        Integer idade = hoje.getYear() - dataNascimento.getYear();
        if (hoje.getMonthValue() < dataNascimento.getMonthValue() || 
            (hoje.getMonthValue() == dataNascimento.getMonthValue() && hoje.getDayOfMonth() < dataNascimento.getDayOfMonth())) {
            idade--;
        }
        return idade;
    }
}
