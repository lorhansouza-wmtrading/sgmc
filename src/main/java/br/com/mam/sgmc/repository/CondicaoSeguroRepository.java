package br.com.mam.sgmc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.mam.sgmc.model.moto.CondicaoSeguro;

public interface CondicaoSeguroRepository extends JpaRepository<CondicaoSeguro, Long> {
    CondicaoSeguro findByTipo(String tipo);
}
