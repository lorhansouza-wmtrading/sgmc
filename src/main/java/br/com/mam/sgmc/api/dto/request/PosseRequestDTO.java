package br.com.mam.sgmc.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PosseRequestDTO {
    
    @NotNull(message = "O ID do cargo é obrigatório")
    private Long idCargo;

    private LocalDate dataInicio;

    private LocalDate dataFim;
}
