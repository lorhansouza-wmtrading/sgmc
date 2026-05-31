package br.com.mam.sgmc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.mam.sgmc.model.FichaMedica;

@Repository
public interface FichaMedicaRepository extends JpaRepository<FichaMedica, Long> {

}
