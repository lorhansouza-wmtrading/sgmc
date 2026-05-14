package br.com.mam.sgmc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.mam.sgmc.model.localizacao.Cidade;

public interface CidadeRepository extends JpaRepository<Cidade, Long> {
    Cidade findByNome(String nome);

    @Query("SELECT c FROM Cidade c" + 
            " JOIN  c.uf u" + 
            " JOIN  u.pais p" + 
            " WHERE " + 
            "(:ufSigla IS NULL OR u.ufSigla = :ufSigla) AND " +
            "(:nome IS NULL OR c.nome = :nome) AND " +
            "(:pais IS NULL OR p.nome = :pais)")
    List<Cidade> findWithFilters(
        String ufSigla,
        String nome,
        String pais
    );

}
