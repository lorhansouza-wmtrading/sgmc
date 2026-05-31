package br.com.mam.sgmc.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "ficha_medica")
@Data
public class FichaMedica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ficha_medica")
    private Long id;

    @Column(name = "nome_plano", length = 100)
    private String nomePlanoSaude;
    @Column(name = "carteira_saude", length = 45)
    private String numeroCarteira;
    @Column(name = "tipo_sanguineo", length = 5)
    private String tipoSanguineo;
    @Column(name = "alergias", length = 255)
    private String alergias;
    @Column(name = "medicamentos_continuos", length = 255)
    private String medicamentos;
    @Column(name = "condicoes_medicas", length = 255)
    private String condicoesMedicas;
    @Column(name = "observacoes", length = 255)
    private String observacoes;

    @OneToOne
    @JoinColumn(name = "id_membro")
    private Membro membro;
}
