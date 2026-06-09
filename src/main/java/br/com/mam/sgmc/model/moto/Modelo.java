package br.com.mam.sgmc.model.moto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "modelo")
@Data
public class Modelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo")
    private Long id;
    @Column(name = "nome", length = 70)
    private String nome;
    private int cilindrada;

    @ManyToOne
    @JoinColumn(name = "id_marca", nullable = false)
    private Marca marca;
}
