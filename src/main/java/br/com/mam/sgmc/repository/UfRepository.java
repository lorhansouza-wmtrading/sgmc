package br.com.mam.sgmc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.mam.sgmc.model.localizacao.Uf;

public interface UfRepository extends JpaRepository<Uf, String> {
    Uf findByUfSiglaIgnoreCase(String sigla);
}
