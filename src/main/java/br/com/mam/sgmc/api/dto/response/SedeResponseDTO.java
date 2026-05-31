package br.com.mam.sgmc.api.dto.response;

import br.com.mam.sgmc.model.Sede;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SedeResponseDTO {
    private Long id;
    private String nome;
    private String endereco;
    private String bairro;
    private String numero;
    private String codigoPostal;
    private Boolean ativa;
    private String cidade;
    private String estado;
    private String pais;

    public static SedeResponseDTO toResponseDTO(Sede sede){
        return new SedeResponseDTO(
                sede.getId(),
                sede.getNome(),
                sede.getEndereco(),
                sede.getBairro(),
                sede.getNumero(),
                sede.getCodigoPostal(),
                sede.getAtiva(),
                sede.getCidade().getNome(),
                sede.getCidade().getUf().getNome(),
                sede.getCidade().getUf().getPais().getNome()
        );
    }

}
