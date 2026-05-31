package br.com.mam.sgmc.api.dto.request;

import br.com.mam.sgmc.model.FichaMedica;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FichaMedicaRequestDTO {
    private String nomePlanoSaude;
    private String numeroCarteira;
    private String tipoSanguineo;
    private String alergias;
    private String medicamentos;
    private String condicoesMedicas;
    private String observacoes;

    public static FichaMedica paraEntidade(FichaMedicaRequestDTO requestDTO) {
        FichaMedica fichaMedica = new FichaMedica();
        fichaMedica.setNomePlanoSaude(requestDTO.getNomePlanoSaude());
        fichaMedica.setNumeroCarteira(requestDTO.getNumeroCarteira());
        fichaMedica.setTipoSanguineo(requestDTO.getTipoSanguineo());
        fichaMedica.setAlergias(requestDTO.getAlergias());
        fichaMedica.setMedicamentos(requestDTO.getMedicamentos());
        fichaMedica.setCondicoesMedicas(requestDTO.getCondicoesMedicas());
        fichaMedica.setObservacoes(requestDTO.getObservacoes());
        return fichaMedica;
    }
}
