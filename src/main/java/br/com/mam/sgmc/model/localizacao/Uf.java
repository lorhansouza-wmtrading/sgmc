package br.com.mam.sgmc.model.localizacao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "estado_provincia")
@Data
public class Uf {
    @Id
    @Column(name = "estado_sigla", length = 2)
    private String ufSigla;
    @Column(name = "nome", length = 255)
    private String nome;
    @Column(name = "regiao", length = 255)
    private String regiao;

    @OneToOne
    @JoinColumn(name = "pais_sigla", referencedColumnName = "pais_sigla")
    private Pais pais;
}