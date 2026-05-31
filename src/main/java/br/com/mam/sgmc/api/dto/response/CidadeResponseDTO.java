package br.com.mam.sgmc.api.dto.response;

import br.com.mam.sgmc.model.localizacao.Cidade;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CidadeResponseDTO {
    private Long id;
    private String nome;
    private String ufSigla;
    private String ufNome;
    private String paisNome;

    public static CidadeResponseDTO toResponseDTO(Cidade cidade) {
        return new CidadeResponseDTO(
            cidade.getId(),
            cidade.getNome(),
            cidade.getUf().getUfSigla(),
            cidade.getUf().getNome(),
            cidade.getUf().getPais().getNome()
        );
    }
}
