package br.com.mam.sgmc.services;

import org.springframework.stereotype.Service;

import br.com.mam.sgmc.model.Cargo;
import br.com.mam.sgmc.repository.CargoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CargoService {

    private final CargoRepository cargoRepository;

    public Cargo salvarCargo(Cargo cargo) {
        Cargo cargoExistente = cargoRepository.findByTitulo(cargo.getTitulo());
        if(cargoExistente != null){
            throw new IllegalArgumentException("Cargo " + cargo.getTitulo() + " já existe");
        }
        return cargoRepository.save(cargo);
    }
}
