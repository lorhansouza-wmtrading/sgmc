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
        Cargo cargo = cargoRepository.findById(idCargo)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado com o ID: " + idCargo));

        PossePk possePk = new PossePk(cargo, membro);
        Posse posse = new Posse();
        posse.setPossePk(possePk);
        posse.setDataInicio(dataInicio != null ? Date.valueOf(dataInicio) : Date.valueOf(LocalDate.now()));
        if (dataFim != null) {
            posse.setDataFim(Date.valueOf(dataFim));
        }

        return posseRepository.save(posse);
    }
}
