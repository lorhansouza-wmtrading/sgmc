package br.com.mam.sgmc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.mam.sgmc.model.moto.Modelo;

public interface ModeloRepository extends JpaRepository<Modelo, Long> {
    Modelo findByNome(String nome);
}
