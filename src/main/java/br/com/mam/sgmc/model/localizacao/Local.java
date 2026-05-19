package br.com.mam.sgmc.model.localizacao;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import br.com.mam.sgmc.model.Evento;
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
@Table(name = "local")
@Data
public class Local {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_local")
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
    private int capacidade;
    @Column(name = "contato", length = 15)
    private String contato;
    
    @ManyToOne
    @JoinColumn(name = "id_cidade")
    private Cidade cidade;

    @OneToMany(mappedBy = "local")
    @JsonManagedReference
    private List<Evento> eventos;
}
