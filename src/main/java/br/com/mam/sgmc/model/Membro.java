package br.com.mam.sgmc.model;

import java.sql.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import br.com.mam.sgmc.model.moto.Moto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Entity
@Table(name = "membro")
@Data
public class Membro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_membro")
    private Long id;
    @Column(name = "nome", length = 200)
    private String nome;
    @Column(name = "apelido", length = 45)
    private String apelido;
    @Column(name = "sexo", length = 1)
    private String sexo;
    @Column(name = "email", length = 254)
    private String email;
    @Column(name = "telefone", length = 15) // até 15 caracteres para incluir código do país, DDD e número (Padrão Internacional E.164)
    private String telefone;
    @Column(name = "data_nascimento")
    private Date dataNascimento;
    @Column(name = "nacionalidade", length = 50)
    private String nacionalidade;
    @Column(name = "naturalidade", length = 50)
    private String naturalidade;
    @Transient
    private Integer idade;
    @Column(name = "batizado")
    private Integer ehBatizado;
    @Column(name = "tem_escudo")
    private Integer temEscudo;
    @Column(name = "ativo")
    private Integer ativo;
    @Column(name = "tamanho_camisa", length = 20)
    private String tamanhoCamisa;
    @Column(name = "data_admissao")
    private Date dataAdmissao;

    @ManyToOne
    @JoinColumn(name = "id_cargo")
    private Cargo cargo;

    @ManyToOne
    @JoinColumn(name = "id_sede")
    private Sede sede;

    @OneToMany(mappedBy = "membro", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Moto> motos;

    @OneToOne(mappedBy = "membro", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Identificacao identidade;
}
