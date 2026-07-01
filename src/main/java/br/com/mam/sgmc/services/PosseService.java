package br.com.mam.sgmc.services;

import br.com.mam.sgmc.errors.ResourceNotFoundException;
import br.com.mam.sgmc.model.Cargo;
import br.com.mam.sgmc.model.Membro;
import br.com.mam.sgmc.model.Posse;
import br.com.mam.sgmc.model.pk.PossePk;
import br.com.mam.sgmc.repository.CargoRepository;
import br.com.mam.sgmc.repository.MembroRepository;
import br.com.mam.sgmc.repository.PosseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PosseService {

    private final PosseRepository posseRepository;
    private final MembroRepository membroRepository;
    private final CargoRepository cargoRepository;

    public List<Posse> listarPossesPorMembro(Long idMembro) {
        if (!membroRepository.existsById(idMembro)) {
            throw new ResourceNotFoundException("Membro não encontrado com o ID: " + idMembro);
        }
        return posseRepository.findByPossePkMembroId(idMembro);
    }

    public Posse salvarPosse(Long idMembro, Long idCargo, LocalDate dataInicio, LocalDate dataFim) {
        Membro membro = membroRepository.findById(idMembro)
                .orElseThrow(() -> new ResourceNotFoundException("Membro não encontrado com o ID: " + idMembro));
        
        if (membro.getAtivo() != null && membro.getAtivo() == 0) {
            throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, "Membro inativo não pode ocupar cargos.");
        }

        Cargo cargo = cargoRepository.findById(idCargo)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado com o ID: " + idCargo));

        LocalDate newStart = dataInicio != null ? dataInicio : LocalDate.now();
        LocalDate newEnd = dataFim;

        List<Posse> possesDoCargo = posseRepository.findByPossePkCargoId(idCargo);
        for (Posse posseExistente : possesDoCargo) {
            if (!posseExistente.getPossePk().getMembro().getId().equals(idMembro)) {
                LocalDate startE = posseExistente.getDataInicio().toLocalDate();
                LocalDate endE = posseExistente.getDataFim() != null ? posseExistente.getDataFim().toLocalDate() : null;
                
                boolean startNewBeforeOrEqualEndE = (endE == null || !newStart.isAfter(endE));
                boolean endNewAfterOrEqualStartE = (newEnd == null || !newEnd.isBefore(startE));
                
                if (startNewBeforeOrEqualEndE && endNewAfterOrEqualStartE) {
                    throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.BAD_REQUEST, 
                        "Já existe outro membro ocupando este cargo no período informado.");
                }
            }
        }

        PossePk possePk = new PossePk(cargo, membro);
        Posse posse = new Posse();
        posse.setPossePk(possePk);
        posse.setDataInicio(Date.valueOf(newStart));
        if (newEnd != null) {
            posse.setDataFim(Date.valueOf(newEnd));
        }

        return posseRepository.save(posse);
    }
}
