package br.com.mam.sgmc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.mam.sgmc.model.Cargo;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long> {
}
