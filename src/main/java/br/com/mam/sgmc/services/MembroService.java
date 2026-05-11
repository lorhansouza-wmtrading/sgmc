package br.com.mam.sgmc.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.mam.sgmc.errors.ResourceNotFoundException;
import br.com.mam.sgmc.model.Membro;
import br.com.mam.sgmc.model.enums.Ativo;
import br.com.mam.sgmc.repository.CargoRepository;
import br.com.mam.sgmc.repository.SedeRepository;
import br.com.mam.sgmc.repository.MembroRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MembroService {
    private final MembroRepository membroRepository;
    private final CargoRepository cargoRepository;
    private final SedeRepository sedeRepository;

    public Membro salvarMembro(Membro membro, Long idCargo, Long idSede) {
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

        return membroRepository.save(membro);
    }

    public Membro atualizarMembro(Membro membro, Long id, Long idCargo, Long idSede) {
        Membro membroExistente = membroRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Membro não encontrado com ID: " + id));
        
        membroExistente.setNome(membro.getNome());
        membroExistente.setApelido(membro.getApelido());
        membroExistente.setSexo(membro.getSexo());
        membroExistente.setEmail(membro.getEmail());
        membroExistente.setTelefone(membro.getTelefone());
        membroExistente.setDataNascimento(membro.getDataNascimento());
        membroExistente.setEhBatizado(membro.getEhBatizado());
        membroExistente.setTemEscudo(membro.getTemEscudo());
        
        // Inactivation logic
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
        } else {
            membroExistente.setSede(null); //OLHAR CASO DE USO DE MEMBRO SEM SEDE
        }

        return this.membroRepository.save(membroExistente);
    }

    public List<Membro> listarMembros(Integer ativo) { //OLHAR CASO DE USO DE LISTA DE MEMBROS ATIVOS E INATIVOS POR FILTRO E ORDENAÇÃO DE DATA DE ADMISSÃO
        if (ativo != null) {
            return this.membroRepository.findByAtivo(ativo);
        }
        return this.membroRepository.findAll();
    }

    public Membro buscarPorId(Long id) {
        return this.membroRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Este id do membro não existe!"));
    }

    public void inativarMembro(Long id) {
        Membro membro = this.buscarPorId(id);
        membro.setAtivo(Ativo.INATIVO.getCodigo());
        this.membroRepository.save(membro);
    }
}
