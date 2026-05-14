package br.com.mam.sgmc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.mam.sgmc.model.localizacao.Pais;

public interface PaisRepository extends JpaRepository<Pais, String> {

    Pais findByNome(String nome);

    Pais findBySigla(String sigla);

    @Query("SELECT p FROM Pais p WHERE " +
            "(:sigla IS NULL OR p.sigla = :sigla) AND " +
            "(:nome IS NULL OR p.nome = :nome) AND " +
            "(:continente IS NULL OR p.continente = :continente)")
    List<Pais> findWithFilters(
        String sigla, 
        String nome, 
        String continente
    );

}
