package br.com.mam.sgmc.api.dto.request;

import br.com.mam.sgmc.model.Cargo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CargoRequestDTO {

    @NotBlank(message = "O título do cargo é obrigatório")
    @Size(max = 100, message = "O título não deve exceder 100 caracteres")
    private String titulo;

    @Size(max = 255, message = "A descrição não deve exceder 255 caracteres")
    private String descricao;

    public static Cargo toCargo(CargoRequestDTO dto) {
        Cargo cargo = new Cargo();
        cargo.setTitulo(dto.getTitulo());
        cargo.setDescricao(dto.getDescricao());
        return cargo;
    }
}
