package br.com.mam.sgmc.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.mam.sgmc.api.dto.request.MotoRequestDTO;
import br.com.mam.sgmc.api.dto.response.MotoResponseDTO;
import br.com.mam.sgmc.model.moto.CondicaoSeguro;
import br.com.mam.sgmc.model.moto.Marca;
import br.com.mam.sgmc.model.moto.Modelo;
import br.com.mam.sgmc.model.moto.Moto;
import br.com.mam.sgmc.model.moto.Seguro;
import br.com.mam.sgmc.services.MotoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import jakarta.validation.Valid;



@RestController
@RequestMapping(value = "/motos")
@RequiredArgsConstructor
public class MotoController {

    private final MotoService motoService;

    @PostMapping
    public ResponseEntity<Void> criarMoto(@RequestBody @Valid MotoRequestDTO motoRequestDTO) {
        
        Moto moto = new Moto();
        Modelo modelo = new Modelo();
        Marca marca = new Marca();
        Seguro seguro = new Seguro();
        CondicaoSeguro condicaoSeguro = new CondicaoSeguro();
        
        condicaoSeguro.setTipo(motoRequestDTO.getTipoSeguro());
        condicaoSeguro.setValidadeFim(motoRequestDTO.getValidadeSeguro());
        condicaoSeguro.setValorFranquia(motoRequestDTO.getValorFranquia());
        
        seguro.setNome(motoRequestDTO.getNomeSeguradora());
        seguro.setCondicoesSeguro(List.of(condicaoSeguro));
        
        marca.setNome(motoRequestDTO.getMarca());
        modelo.setMarca(marca);
        modelo.setNome(motoRequestDTO.getModelo());
        
        moto.setModelo(modelo);
        if (motoRequestDTO.getAno() != null) {
            moto.setAno(motoRequestDTO.getAno());
        }
        moto.setCor(motoRequestDTO.getCor());
        moto.setPlaca(motoRequestDTO.getPlaca());
        moto.setSeguro(seguro);

        Moto motoSalva = this.motoService.salvarMoto(moto, motoRequestDTO.getIdMembro());

        String location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(motoSalva.getPlaca()).toUriString();
                
        return ResponseEntity.status(HttpStatus.CREATED).header("Location", location).build();
    }

    @GetMapping
    public List<MotoResponseDTO> listarMotos(
            @RequestParam(required = false) Long idMembro,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String seguro) {
        return motoService.listarMotos(idMembro, modelo, marca, seguro).stream().map(MotoResponseDTO::toResponseDTO).toList();
    }

    @GetMapping("/{placa}")
    public ResponseEntity<MotoResponseDTO> buscarPorPlaca(@PathVariable String placa) {
        Moto moto = motoService.buscarPorPlaca(placa);
        return ResponseEntity.ok(MotoResponseDTO.toResponseDTO(moto));
    }

    @PutMapping("/{placa}")
    public ResponseEntity<MotoResponseDTO> atualizarMoto(@PathVariable String placa, @RequestBody MotoRequestDTO motoRequestDTO) {
        
        Moto moto = new Moto();
        Modelo modelo = new Modelo();
        Marca marca = new Marca();
        Seguro seguro = new Seguro();
        CondicaoSeguro condicaoSeguro = new CondicaoSeguro();
        
        condicaoSeguro.setTipo(motoRequestDTO.getTipoSeguro());
        condicaoSeguro.setValidadeFim(motoRequestDTO.getValidadeSeguro());
        condicaoSeguro.setValorFranquia(motoRequestDTO.getValorFranquia());
        
        seguro.setNome(motoRequestDTO.getNomeSeguradora());
        seguro.setCondicoesSeguro(List.of(condicaoSeguro));
        
        marca.setNome(motoRequestDTO.getMarca());
        modelo.setMarca(marca);
        modelo.setNome(motoRequestDTO.getModelo());
        
        moto.setModelo(modelo);
        if (motoRequestDTO.getAno() != null) {
            moto.setAno(motoRequestDTO.getAno());
        }
        moto.setCor(motoRequestDTO.getCor());
        moto.setPlaca(motoRequestDTO.getPlaca());
        moto.setSeguro(seguro);

        Moto motoAtualizada = this.motoService.atualizarMoto(moto, motoRequestDTO.getIdMembro());
        MotoResponseDTO motoResponseDTO = MotoResponseDTO.toResponseDTO(motoAtualizada);
        return ResponseEntity.status(HttpStatus.OK).body(motoResponseDTO);
    }
}