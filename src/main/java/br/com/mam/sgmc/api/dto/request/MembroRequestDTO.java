package br.com.mam.sgmc.api.dto.request;

import java.io.Serializable;
import java.time.LocalDate;

import br.com.mam.sgmc.model.enums.Ativo;
import br.com.mam.sgmc.model.enums.Batizado;
import br.com.mam.sgmc.model.enums.Escudo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MembroRequestDTO implements Serializable {
    
    @NotBlank(message = "Nome do membro não preenchido.")
    private String nome;
    private String apelido;
    @NotBlank(message = "Sexo do membro não preenchido.")
    private String sexo;
    @NotBlank(message = "Email do membro não preenchido.")
    private String email;
    @NotBlank(message = "Telefone do membro não preenchido.")
    private String telefone;
    @NotNull(message = "Data de nascimento do membro não preenchida.")
    @Past(message = "Data de nascimento inválida.")
    private LocalDate dataNascimento;
    @NotBlank(message = "Nacionalidade do membro não preenchida.")
    private String nacionalidade;
    @NotBlank(message = "Naturalidade do membro não preenchida.")
    private String naturalidade;
    @NotNull(message = "Batizado do membro não preenchido.")
    private Boolean ehBatizado;
    @NotNull(message = "Escudo do membro não preenchido.")
    private Boolean temEscudo;
    @NotNull(message = "Ativo do membro não preenchido.")
    private Boolean ativo;
    @NotBlank(message = "Tamanho da camisa do membro não preenchido.")
    private String tamanhoCamisa;
    @NotNull(message = "Data de admissão do membro não preenchida.")
    private LocalDate dataAdmissao;
    private Long idCargo;
    private Long idSede;
    private IdentificacaoRequestDTO identidade;
    // private List<Moto> motos;

    public Integer getCodigoBatizado() {
        if (Boolean.TRUE.equals(this.ehBatizado)) {
            return Batizado.BATIZADO.getCodigo();
        } else {
            return Batizado.NAO_BATIZADO.getCodigo();
        }
    }

    public Integer getCodigoEscudo() {
        if (Boolean.TRUE.equals(this.temEscudo)) {
            return Escudo.TEM_ESCUDO.getCodigo();
        } else {
            return Escudo.NAO_TEM_ESCUDO.getCodigo();
        }
    }

    public Integer getCodigoAtivo() {
        if (Boolean.TRUE.equals(this.ativo)) {
            return Ativo.ATIVO.getCodigo();
        } else {
            return Ativo.INATIVO.getCodigo();
        }
    }

}
