package br.com.mam.sgmc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.mam.sgmc.model.Identificacao;

public interface IdentificacaoRepository extends JpaRepository<Identificacao, Long> {
    Identificacao findByIdentidade(String identidade);
}
