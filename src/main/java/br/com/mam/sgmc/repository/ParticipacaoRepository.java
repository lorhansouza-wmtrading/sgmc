package br.com.mam.sgmc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.mam.sgmc.model.Participacao;
import br.com.mam.sgmc.model.pk.ParticipacaoPk;

@Repository
public interface ParticipacaoRepository extends JpaRepository<Participacao, ParticipacaoPk> {
}
