package br.com.mam.sgmc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.mam.sgmc.model.Posse;
import br.com.mam.sgmc.model.pk.PossePk;

import java.util.List;

public interface PosseRepository extends JpaRepository<Posse, PossePk> {
    List<Posse> findByPossePkMembroId(Long idMembro);
    List<Posse> findByPossePkCargoId(Long idCargo);
}
