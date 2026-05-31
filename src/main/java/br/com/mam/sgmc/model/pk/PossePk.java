package br.com.mam.sgmc.model.pk;


import java.io.Serializable;

import br.com.mam.sgmc.model.Cargo;
import br.com.mam.sgmc.model.Membro;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class PossePk implements Serializable {

    @ManyToOne
    @JoinColumn(name = "id_cargo", referencedColumnName = "id_cargo")
    private Cargo cargo;

    @ManyToOne
    @JoinColumn(name = "id_membro", referencedColumnName = "id_membro")
    private Membro membro;
}
