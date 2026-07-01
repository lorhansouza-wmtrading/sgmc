package br.com.mam.sgmc.api.dto.response;

import br.com.mam.sgmc.model.Posse;
import lombok.Builder;
import lombok.Data;
import java.sql.Date;

@Data
@Builder
public class PosseResponseDTO {
    private Long idMembro;
    private String nomeMembro;
    private CargoResponseDTO cargo;
    private Date dataInicio;
    private Date dataFim;

    public static PosseResponseDTO toResponseDTO(Posse posse) {
        if (posse == null) return null;
        return PosseResponseDTO.builder()
                .idMembro(posse.getPossePk().getMembro().getId())
                .nomeMembro(posse.getPossePk().getMembro().getNome())
                .cargo(CargoResponseDTO.toResponseDTO(posse.getPossePk().getCargo()))
                .dataInicio(posse.getDataInicio())
                .dataFim(posse.getDataFim())
                .build();
    }
}
