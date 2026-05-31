package br.com.mam.sgmc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.mam.sgmc.model.moto.Moto;

public interface MotoRepository extends JpaRepository<Moto, String> {
    Moto findByPlaca(String placa);
}
