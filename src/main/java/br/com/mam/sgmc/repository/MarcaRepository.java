package br.com.mam.sgmc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.mam.sgmc.model.moto.Marca;

public interface MarcaRepository extends JpaRepository<Marca, Long> {
    Marca findByNome(String nome);
}
