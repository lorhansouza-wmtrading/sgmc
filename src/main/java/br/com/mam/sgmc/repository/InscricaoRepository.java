package br.com.mam.sgmc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.mam.sgmc.model.Inscricao;
import br.com.mam.sgmc.model.pk.InscricaoPk;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, InscricaoPk> {
}
