package br.com.mam.sgmc.api.dto.response;

import java.sql.Date;

import br.com.mam.sgmc.model.Identificacao;
import lombok.Data;

@Data
public class IdentificacaoResponseDTO {
    private String tipo;
    private String identidade;
    private String emissor;
    private Date dataEmissao;
    private String paisSigla;

    public static IdentificacaoResponseDTO toResponseDTO(Identificacao identificacao) {
        IdentificacaoResponseDTO dto = new IdentificacaoResponseDTO();
        dto.setTipo(identificacao.getTipo());
        dto.setIdentidade(identificacao.getIdentidade());
        dto.setEmissor(identificacao.getEmissor());
        dto.setDataEmissao(identificacao.getDataEmissao());
        dto.setPaisSigla(identificacao.getPais() != null ? identificacao.getPais().getSigla() : null);
        return dto;
    }
}
