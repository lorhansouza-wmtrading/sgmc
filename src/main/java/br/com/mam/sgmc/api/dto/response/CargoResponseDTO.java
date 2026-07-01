package br.com.mam.sgmc.api.dto.response;

import br.com.mam.sgmc.model.Cargo;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CargoResponseDTO {
    private Long id;
    private String titulo;
    private String descricao;

    public static CargoResponseDTO toResponseDTO(Cargo cargo) {
        if (cargo == null) return null;
        return CargoResponseDTO.builder()
                .id(cargo.getId())
                .titulo(cargo.getTitulo())
                .descricao(cargo.getDescricao())
                .build();
    }
}
