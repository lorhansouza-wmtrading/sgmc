package br.com.mam.sgmc.api.dto.response;

import br.com.mam.sgmc.model.localizacao.Pais;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaisResponseDTO {
    
    private String sigla;
    private String nome;
    private String continente;

    public static PaisResponseDTO toResponseDTO(Pais pais) {
        return new PaisResponseDTO(
            pais.getSigla(),
            pais.getNome(),
            pais.getContinente()
        );
    }
}
