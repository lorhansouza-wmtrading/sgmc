package br.com.mam.sgmc.model;

import java.io.Serializable;

import br.com.mam.sgmc.model.pk.PossePk;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.sql.Date;

@Data
@Entity
@Table(name = "posse")
public class Posse implements Serializable {

    @EmbeddedId
    private PossePk possePk;

    @Column(name = "data_inicio")
    private Date dataInicio;
    @Column(name = "data_fim")
    private Date dataFim;
}
