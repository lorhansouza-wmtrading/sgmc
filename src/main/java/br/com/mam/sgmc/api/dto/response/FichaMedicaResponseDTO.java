package br.com.mam.sgmc.api.dto.response;

import br.com.mam.sgmc.model.FichaMedica;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FichaMedicaResponseDTO {
    private String nomePlanoSaude;
    private String numeroCarteira;
    private String tipoSanguineo;
    private String alergias;
    private String medicamentos;
    private String condicoesMedicas;
    private String observacoes;

    public static FichaMedicaResponseDTO toResponseDTO(FichaMedica fichaMedica) {
        return new FichaMedicaResponseDTO(
            fichaMedica.getNomePlanoSaude(), 
            fichaMedica.getNumeroCarteira(), 
            fichaMedica.getTipoSanguineo(), 
            fichaMedica.getAlergias(), 
            fichaMedica.getMedicamentos(), 
            fichaMedica.getCondicoesMedicas(), 
            fichaMedica.getObservacoes());
    }
}
