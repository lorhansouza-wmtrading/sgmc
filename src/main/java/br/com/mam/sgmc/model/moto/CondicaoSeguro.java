package br.com.mam.sgmc.model.moto;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "condicao_seguro")
@Data
public class CondicaoSeguro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcondicao_seguro")
    private Long id;
    @Column(name = "tipo", length = 45)
    private String tipo;
    @Column(name = "validade_fim")
    private Instant validadeFim;
    @Column(name = "valor")
    private Float valorFranquia;

    @jakarta.persistence.OneToMany(mappedBy = "condicaoSeguro")
    private java.util.List<Seguro> seguros;
}
