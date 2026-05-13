package br.com.mam.sgmc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.mam.sgmc.model.localizacao.Cidade;

public interface CidadeRepository extends JpaRepository<Cidade, Long> {
    Cidade findByNome(String nome);

}
