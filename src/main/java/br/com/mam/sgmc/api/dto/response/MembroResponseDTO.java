package br.com.mam.sgmc.api.dto.response;

import java.io.Serializable;
import java.sql.Date;

import br.com.mam.sgmc.model.Membro;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MembroResponseDTO implements Serializable {
    private Long id;
    private String nome;
    private String apelido;
    private String sexo;
    private String email;
    private String telefone;
    private Date dataNascimento;
    private Boolean ehBatizado;
    private Boolean temEscudo;
    private Boolean ativo;
    private String tamanhoCamisa;
    private Date dataAdmissao;
    private Long idCargo;
    private Long idSede;

    public static MembroResponseDTO toResponseDTO(Membro membro){
        MembroResponseDTO dto = new MembroResponseDTO();
        dto.setId(membro.getId());
        dto.setNome(membro.getNome());
        dto.setApelido(membro.getApelido());
        dto.setSexo(membro.getSexo());
        dto.setEmail(membro.getEmail());
        dto.setTelefone(membro.getTelefone());
        dto.setDataNascimento(membro.getDataNascimento());
        dto.setEhBatizado(getBooleanFromInt(membro.getEhBatizado()));
        dto.setTemEscudo(getBooleanFromInt(membro.getTemEscudo()));
        dto.setAtivo(getBooleanFromInt(membro.getAtivo()));
        dto.setTamanhoCamisa(membro.getTamanhoCamisa());
        dto.setDataAdmissao(membro.getDataAdmissao());
        dto.setIdCargo(membro.getCargo() != null ? membro.getCargo().getId() : null);
        dto.setIdSede(membro.getSede() != null ? membro.getSede().getId() : null);
        return dto;
    }

    private static Boolean getBooleanFromInt(Integer value){
        if (value == null) return false;
        return value == 0 ? true : false;
    }
}
