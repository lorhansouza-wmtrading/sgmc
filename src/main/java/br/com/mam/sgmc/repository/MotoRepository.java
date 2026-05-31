package br.com.mam.sgmc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.mam.sgmc.model.moto.Moto;

public interface MotoRepository extends JpaRepository<Moto, String> {
    Moto findByPlaca(String placa);

    void deleteByPlaca(String placa);

    @Query("SELECT m FROM Moto m " +
            "WHERE (:idMembro IS NULL OR m.membro.id = :idMembro) " +
            "AND (:modelo IS NULL OR m.modelo.nome = :modelo) " +
            "AND (:marca IS NULL OR m.modelo.marca.nome = :marca) " +
            "AND (:seguro IS NULL OR m.seguro.nome = :seguro)")
    List<Moto> findWithFilters(Long idMembro, String modelo, String marca, String seguro);
}
