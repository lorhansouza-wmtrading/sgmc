package br.com.mam.sgmc.model.moto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "seguro")
@Data
public class Seguro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idseguro")
    private Long id;
    @Column(name = "nome", length = 255)
    private String nome;

    @jakarta.persistence.ManyToOne
    @jakarta.persistence.JoinColumn(name = "id_condicao_seguro", referencedColumnName = "idcondicao_seguro", nullable = false)
    private CondicaoSeguro condicaoSeguro;
}
