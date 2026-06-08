package br.com.mam.sgmc.services;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.mam.sgmc.errors.ResourceNotFoundException;
import br.com.mam.sgmc.model.Sede;
import br.com.mam.sgmc.model.localizacao.Cidade;
import br.com.mam.sgmc.model.localizacao.Pais;
import br.com.mam.sgmc.model.localizacao.Uf;
import br.com.mam.sgmc.repository.CidadeRepository;
import br.com.mam.sgmc.repository.PaisRepository;
import br.com.mam.sgmc.repository.SedeRepository;
import br.com.mam.sgmc.repository.UfRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SedeService {

    private final SedeRepository sedeRepository;
    private final CidadeRepository cidadeRepository;
    private final PaisRepository paisRepository;
    private final UfRepository ufRepository;

    public Sede salvarSede(Sede sede, String nomeCidade, String ufSigla, String nomePais) {
        Cidade cidadeEncontrada = this.cidadeRepository.findByNome(nomeCidade);
        if (cidadeEncontrada == null) {
            Uf ufEncontrada = this.ufRepository.findByUfSiglaIgnoreCase(ufSigla);
            if (ufEncontrada == null) {
                Pais paisEncontrado = this.paisRepository.findByNome(nomePais);
                if (paisEncontrado == null) {
                    throw new ResourceNotFoundException("Pais não encontrado");
                }
                
            }
        }
        sede.setCidade(cidadeEncontrada);
        return this.sedeRepository.save(sede);
    }

    public Sede buscarPorId(Long id) {
        return this.sedeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sede não encontrada"));
    }

    public List<Sede> listarSedesComFiltros(String nome, String cidade, String ufSigla, String pais) {
        return this.sedeRepository.findWithFilters(nome, cidade, ufSigla, pais);
    }

    public Sede atualizarSede(Long id, Sede sede) {
        Sede sedeBanco = this.sedeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Este id da sede não existe!"));

        sedeBanco.setNome(sede.getNome());
        sedeBanco.setEndereco(sede.getEndereco());
        sedeBanco.setBairro(sede.getBairro());
        sedeBanco.setNumero(sede.getNumero());
        sedeBanco.setCodigoPostal(sede.getCodigoPostal());
        sedeBanco.setAtiva(sede.getAtiva());

        sedeBanco.setCidade(sede.getCidade());
        sedeBanco.getCidade().setUf(sede.getCidade().getUf());
        sedeBanco.getCidade().getUf().setPais(sede.getCidade().getUf().getPais());

        return this.sedeRepository.save(sedeBanco);
    }

    public void inativarSede(Long id) {
        Sede sede = this.buscarPorId(id);
        sede.setAtiva(false);
        this.sedeRepository.save(sede);
    }

}
