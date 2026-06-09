package br.com.mam.sgmc.model;

import java.sql.Date;

import br.com.mam.sgmc.model.moto.Moto;
import br.com.mam.sgmc.model.pk.InscricaoPk;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "inscricao")
@Data
public class Inscricao {
    
    @EmbeddedId
    private InscricaoPk pk;

    @Column(name = "data_inscricao")
    private Date dataInscricao;

    @ManyToOne
    @JoinColumn(name = "moto_placa", referencedColumnName = "placa", nullable = true)
    private Moto moto;
}
