package br.com.mam.sgmc.api.dto.response;

import java.sql.Date;

import br.com.mam.sgmc.model.Inscricao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InscricaoResponseDTO {
    private Long idEvento;
    private String nomeEvento;
    private Long idMembro;
    private String nomeMembro;
    private String placaMoto;
    private String modeloMoto;
    private Date dataInscricao;

    public static InscricaoResponseDTO toResponseDTO(Inscricao inscricao) {
        return new InscricaoResponseDTO(
            inscricao.getPk().getEvento().getId(),
            inscricao.getPk().getEvento().getNome(),
            inscricao.getPk().getMembro().getId(),
            inscricao.getPk().getMembro().getNome(),
            inscricao.getMoto() != null ? inscricao.getMoto().getPlaca() : null,
            inscricao.getMoto() != null && inscricao.getMoto().getModelo() != null ? inscricao.getMoto().getModelo().getNome() : null,
            inscricao.getDataInscricao()
        );
    }
}
