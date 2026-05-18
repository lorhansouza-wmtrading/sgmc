package br.com.mam.sgmc.api.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class IdentificacaoRequestDTO {

    @NotBlank(message = "Tipo de identificação não informado.")
    private String tipo;

    @NotBlank(message = "Número da identidade não informado.")
    private String identidade;

    @NotBlank(message = "Órgão emissor não informado.")
    private String emissor;

    @NotNull(message = "Data de emissão não informada.")
    private LocalDate dataEmissao;

    @NotBlank(message = "Sigla do país não informada.")
    private String paisSigla;
}
