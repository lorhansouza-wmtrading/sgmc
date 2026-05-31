package br.com.mam.sgmc.api.dto.response;

import br.com.mam.sgmc.model.localizacao.Uf;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UfResponseDTO {
    private String sigla;
    private String nome;
    private String regiaoNome;
    private String paisNome;

    public static UfResponseDTO toResponseDTO(Uf uf) {
        return new UfResponseDTO(
            uf.getUfSigla(),
            uf.getNome(),
            uf.getRegiao(),
            uf.getPais().getNome()
        );
    }
}
