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
    private String nacionalidade;
    private String naturalidade;
    private Integer idade;
    private Boolean ehBatizado;
    private Boolean temEscudo;
    private Boolean ativo;
    private String tamanhoCamisa;
    private Date dataAdmissao;
    private Long idCargo;
    private Long idSede;
    private IdentificacaoResponseDTO identidade;
    private FichaMedicaResponseDTO fichaMedica;

    public static MembroResponseDTO toResponseDTO(Membro membro) {
        return new MembroResponseDTO(
                membro.getId(),
                membro.getNome(),
                membro.getApelido(),
                membro.getSexo(),
                membro.getEmail(),
                membro.getTelefone(),
                membro.getDataNascimento(),
                membro.getNacionalidade(),
                membro.getNaturalidade(),
                membro.getIdade(),
                getBooleanFromInt(membro.getEhBatizado()),
                getBooleanFromInt(membro.getTemEscudo()),
                getBooleanFromInt(membro.getAtivo()),
                membro.getTamanhoCamisa(),
                membro.getDataAdmissao(),
                membro.getCargo() != null ? membro.getCargo().getId() : null,
                membro.getSede() != null ? membro.getSede().getId() : null,
                membro.getIdentidade() != null ? IdentificacaoResponseDTO.toResponseDTO(membro.getIdentidade()) : null,
                membro.getFichaMedica() != null ? FichaMedicaResponseDTO.toResponseDTO(membro.getFichaMedica()) : null);
    }

    private static Boolean getBooleanFromInt(Integer value) {
        if (value == null)
            return Boolean.FALSE;
        return value == 0 ? Boolean.TRUE : Boolean.FALSE;
    }
}
