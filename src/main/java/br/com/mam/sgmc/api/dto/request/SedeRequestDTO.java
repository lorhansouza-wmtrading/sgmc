package br.com.mam.sgmc.api.dto.request;

import java.io.Serializable;

import br.com.mam.sgmc.model.Sede;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SedeRequestDTO implements Serializable {
    @NotBlank(message = "Nome da sede não preenchido.")
    private String nome;
    @NotBlank(message = "Endereço da sede não preenchido.")
    private String endereco;
    @NotBlank(message = "Bairro da sede não preenchido.")
    private String bairro;
    @NotBlank(message = "Número da sede não preenchido.")
    private String numero;
    @NotBlank(message = "Código Postal da sede não preenchido.")
    private String codigoPostal;
    private Boolean ativa;
    @NotBlank(message = "Cidade da sede não preenchido.")
    private String cidade;
    @NotBlank(message = "Estado da sede não preenchido.")
    private String ufSigla;
    @NotBlank(message = "País da sede não preenchido.")
    private String pais;

    public static Sede toSede(SedeRequestDTO sedeRequestDTO) {
        Sede sede = new Sede();
        sede.setNome(sedeRequestDTO.getNome());
        sede.setEndereco(sedeRequestDTO.getEndereco());
        sede.setBairro(sedeRequestDTO.getBairro());
        sede.setNumero(sedeRequestDTO.getNumero());
        sede.setCodigoPostal(sedeRequestDTO.getCodigoPostal());
        sede.setAtiva(sedeRequestDTO.getAtiva());
        return sede;
    }

}
