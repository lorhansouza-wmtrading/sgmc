package br.com.mam.sgmc.services;

import org.springframework.stereotype.Service;

import br.com.mam.sgmc.model.Cargo;
import br.com.mam.sgmc.repository.CargoRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import br.com.mam.sgmc.errors.ResourceNotFoundException;

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

    public List<Cargo> listarCargos() {
        return cargoRepository.findAll();
    }

    public Cargo buscarPorId(Long id) {
        return cargoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cargo não encontrado com o ID: " + id));
    }
}
