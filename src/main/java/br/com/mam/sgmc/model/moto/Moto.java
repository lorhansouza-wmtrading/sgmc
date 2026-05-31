package br.com.mam.sgmc.model.moto;

import br.com.mam.sgmc.model.Membro;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "moto")
@Data
public class Moto {
    @Id
    @Column(name = "placa", length = 7)
    private String placa;
    @Column(name = "ano")
    private int ano;
    @Column(name = "cor")
    private String cor;

    @OneToOne
    @JoinColumn(name = "id_seguro")
    private Seguro seguro;

    @ManyToOne
    @JoinColumn(name = "id_membro")
    private Membro membro;

    @ManyToOne
    @JoinColumn(name = "id_modelo", nullable = false)
    private Modelo modelo;
}
