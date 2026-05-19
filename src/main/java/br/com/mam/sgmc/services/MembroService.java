package br.com.mam.sgmc.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.mam.sgmc.errors.ResourceNotFoundException;
import br.com.mam.sgmc.model.Identificacao;
import br.com.mam.sgmc.model.Membro;
import br.com.mam.sgmc.model.enums.Ativo;
import br.com.mam.sgmc.repository.CargoRepository;
import br.com.mam.sgmc.repository.FichaMedicaRepository;
import br.com.mam.sgmc.repository.SedeRepository;
import br.com.mam.sgmc.repository.MembroRepository;
import br.com.mam.sgmc.repository.PaisRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembroService {
    private final MembroRepository membroRepository;
    private final FichaMedicaRepository fichaMedicaRepository;
    private final CargoRepository cargoRepository;
    private final SedeRepository sedeRepository;
    private final PaisRepository paisRepository;

    public Membro salvarMembro(Membro membro, Long idCargo, Long idSede, String paisSigla) {
        Membro membroBanco = new Membro();
        if (this.membroRepository.findByNome(membro.getNome()) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este nome já existe.");
        }
        
        if (idCargo != null) {
            membro.setCargo(cargoRepository.findById(idCargo)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado com ID: " + idCargo)));
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
        membroBanco = this.membroRepository.save(membro);
        if(membro.getFichaMedica() != null){
            membro.getFichaMedica().setMembro(membroBanco);
            membroBanco.setFichaMedica(this.fichaMedicaRepository.save(membro.getFichaMedica()));
        }
        return membroBanco;
    }

    public Membro atualizarMembro(Membro membro, Long id, Long idCargo, Long idSede, String paisSigla) {
        Membro membroExistente = membroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membro não encontrado com ID: " + id));
        
        membroExistente.setNome(membro.getNome());
        membroExistente.setApelido(membro.getApelido());
        membroExistente.setSexo(membro.getSexo());
        membroExistente.setEmail(membro.getEmail());
        membroExistente.setTelefone(membro.getTelefone());
        membroExistente.setDataNascimento(membro.getDataNascimento());
        membroExistente.setNacionalidade(membro.getNacionalidade());
        membroExistente.setNaturalidade(membro.getNaturalidade());
        membroExistente.setEhBatizado(membro.getEhBatizado());
        membroExistente.setTemEscudo(membro.getTemEscudo());
        
        if (membro.getAtivo() != null && !membro.getAtivo().equals(membroExistente.getAtivo())) {
            if (membro.getAtivo().equals(Ativo.INATIVO.getCodigo())) {
                membroExistente.setAtivo(Ativo.INATIVO.getCodigo());
            } else {
                membroExistente.setAtivo(Ativo.ATIVO.getCodigo());
            }
        }
        
        membroExistente.setTamanhoCamisa(membro.getTamanhoCamisa());
        membroExistente.setDataAdmissao(membro.getDataAdmissao());

        if (idCargo != null) {
            membroExistente.setCargo(cargoRepository.findById(idCargo)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado com ID: " + idCargo)));
        } else {
            membroExistente.setCargo(null);
        }
        
        if (idSede != null) {
            membroExistente.setSede(sedeRepository.findById(idSede)
                .orElseThrow(() -> new ResourceNotFoundException("Sede não encontrada com ID: " + idSede)));
        }

        if (membro.getIdentidade() != null && paisSigla != null) {
            if ("BR".equalsIgnoreCase(paisSigla) && !"CPF".equalsIgnoreCase(membro.getIdentidade().getTipo())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Para o Brasil, o tipo de identificação deve ser CPF.");
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

        membroExistente = this.membroRepository.save(membroExistente);
        if(membro.getFichaMedica() != null){
            membro.getFichaMedica().setMembro(membroExistente);
            membroExistente.setFichaMedica(this.fichaMedicaRepository.save(membro.getFichaMedica()));
        }

        return membroExistente;
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
