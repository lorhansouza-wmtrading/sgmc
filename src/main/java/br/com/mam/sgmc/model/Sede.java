package br.com.mam.sgmc.model;

import br.com.mam.sgmc.model.localizacao.Cidade;
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
@Table(name = "sede")
@Data
public class Sede {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sede")
    private Long id;
    @Column(name = "nome", length = 255)
    private String nome;
    @Column(name = "endereco", length = 255)
    private String endereco;
    @Column(name = "bairro", length = 150)
    private String bairro;
    @Column(name = "numero", length = 6)
    private String numero;
    @Column(name = "codigo_postal", length = 10)
    private String codigoPostal;
    @Column(name = "ativa")
    private Boolean ativa;
    
    @ManyToOne
    @JoinColumn(name = "id_cidade")
    private Cidade cidade;
}
