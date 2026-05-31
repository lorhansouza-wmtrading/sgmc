package br.com.mam.sgmc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.mam.sgmc.model.Sede;

public interface SedeRepository extends JpaRepository<Sede, Long> {
    Sede findByNome(String nome);

    Sede findByCidade(String cidade);

    @Query("""
            SELECT s FROM Sede s
            JOIN s.cidade c
            WHERE (:uf IS NULL OR s.cidade.uf.ufSigla = :uf)

    """)
    List<Sede> findByUf(String uf);

    List<Sede> findByAtiva(Boolean ativa);

    @Query("""
            SELECT s FROM Sede s
            JOIN s.cidade c
            WHERE (:nome IS NULL OR s.nome = :nome)
            AND (:cidade IS NULL OR s.cidade.nome = :cidade)
            AND (:uf IS NULL OR s.cidade.uf.ufSigla = :uf)
            AND (:pais IS NULL OR s.cidade.uf.pais.nome = :pais)
    """)
    List<Sede> findWithFilters(String nome, String cidade, String uf, String pais);
}
