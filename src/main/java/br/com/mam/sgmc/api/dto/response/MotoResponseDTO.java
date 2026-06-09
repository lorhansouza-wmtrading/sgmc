package br.com.mam.sgmc.api.dto.response;

import java.time.Instant;

import br.com.mam.sgmc.model.moto.Moto;
import lombok.Data;

@Data
public class MotoResponseDTO {
    private String modelo;
    private String marca;
    private int ano;
    private String cor;
    private String placa;
    private String tipoSeguro;
    private String nomeSeguro;
    private Instant validadeSeguro;
    private Long idMembro;
    private String nomeMembro;

    public static MotoResponseDTO toResponseDTO(Moto moto) {
        MotoResponseDTO response = new MotoResponseDTO();
        response.setModelo(moto.getModelo().getNome());
        response.setMarca(moto.getModelo().getMarca().getNome());
        response.setAno(moto.getAno());
        response.setCor(moto.getCor());
        response.setPlaca(moto.getPlaca());
        response.setIdMembro(moto.getMembro().getId());
        response.setNomeMembro(moto.getMembro().getNome());
        response.setTipoSeguro(moto.getSeguro().getCondicaoSeguro().getTipo());
        response.setNomeSeguro(moto.getSeguro().getNome());
        response.setValidadeSeguro(moto.getSeguro().getCondicaoSeguro().getValidadeFim());
        return response;
    }
}
