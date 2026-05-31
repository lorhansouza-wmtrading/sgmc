package br.com.mam.sgmc.model;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.mam.sgmc.model.localizacao.Local;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "evento")
@Data
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Long id;
    @Column(name = "nome", length = 150)
    private String nome;
    @Column(name = "descricao", length = 255)
    private String descricao;
    @Column(name = "data_inicio")
    private Date dataInicio;
    @Column(name = "data_fim")
    private Date dataFim;
    private float valor;

    @ManyToOne
    @JoinColumn(name = "id_local")
    private Local local;

    @OneToMany(mappedBy = "pk.evento")
    @JsonManagedReference
    private List<Participacao> participacoes;
}
