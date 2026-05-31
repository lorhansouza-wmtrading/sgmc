package br.com.mam.sgmc.model.localizacao;

import java.util.List;

import br.com.mam.sgmc.model.Identificacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "pais")
@Data
public class Pais {
    @Id
    @Column(name = "pais_sigla", length = 2)
    private String sigla;
    @Column(name = "nome", length = 150)
    private String nome;
    @Column(name = "continente", length = 50)
    private String continente;

    @OneToMany(mappedBy = "pais")
    private List<Uf> ufs;

    @OneToMany(mappedBy = "pais")
    private List<Identificacao> identificacoes;
}
