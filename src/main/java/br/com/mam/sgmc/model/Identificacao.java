package br.com.mam.sgmc.model;

import java.sql.Date;

import br.com.mam.sgmc.model.localizacao.Pais;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "identificacao")
@Data
public class Identificacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_identificacao")
    private Long idIdentificacao;
    @Column(name = "tipo", length = 7)
    private String tipo;
    @Column(name = "identidade", length = 45)
    private String identidade;
    @Column(name = "emissor", length = 150)
    private String emissor;
    @Column(name = "data_emissao")
    private Date dataEmissao;

    @ManyToOne
    @JoinColumn(name = "pais_sigla")
    private Pais pais;

    @OneToOne(mappedBy = "identidade")
    private Membro membro;
}
