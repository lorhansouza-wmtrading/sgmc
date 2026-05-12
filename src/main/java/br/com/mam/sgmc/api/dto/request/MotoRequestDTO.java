package br.com.mam.sgmc.api.dto.request;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MotoRequestDTO {

    @NotBlank(message = "Modelo é obrigatório")
    private String modelo;

    @NotBlank(message = "Marca é obrigatória")
    private String marca;

    @NotNull(message = "Ano é obrigatório")
    private Integer ano;

    @NotBlank(message = "Cor é obrigatória")
    private String cor;

    @NotBlank(message = "Placa é obrigatória")
    private String placa;

    @NotNull(message = "Membro ID é obrigatório")
    private Long idMembro;

    private String nomeSeguradora;
    private String tipoSeguro;
    private Instant validadeSeguro;
    private Float valorFranquia;

}
