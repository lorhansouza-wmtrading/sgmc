package br.com.mam.sgmc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.mam.sgmc.model.localizacao.Uf;

public interface UfRepository extends JpaRepository<Uf, String> {
    Uf findByUfSiglaIgnoreCase(String sigla);

    @Query("SELECT u FROM Uf u" + 
            "LEFT JOIN u.pais p " +
            "WHERE " +
            "(:sigla IS NULL OR u.ufSigla = :sigla) AND " +
            "(:nome IS NULL OR u.nome = :nome) AND " +
            "(:regiao IS NULL OR u.regiao = :regiao) AND " +
            "(:pais IS NULL OR p.nome = :pais)")
    List<Uf> findWithFilters(
        String sigla, 
        String nome, 
        String regiao, 
        String pais
    );
}
