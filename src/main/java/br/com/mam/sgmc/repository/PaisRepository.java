package br.com.mam.sgmc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.mam.sgmc.model.localizacao.Pais;

public interface PaisRepository extends JpaRepository<Pais, String> {

    Pais findByNome(String nome);

    Pais findBySigla(String sigla);

}
