package br.com.mam.sgmc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.mam.sgmc.model.moto.Seguro;

public interface SeguroRepository extends JpaRepository<Seguro, Long> {
    Seguro findByNome(String nome);
}
