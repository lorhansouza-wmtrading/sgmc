package br.com.mam.sgmc.services;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.mam.sgmc.errors.ResourceNotFoundException;
import br.com.mam.sgmc.model.localizacao.Cidade;
import br.com.mam.sgmc.model.localizacao.Pais;
import br.com.mam.sgmc.model.localizacao.Uf;
import br.com.mam.sgmc.repository.CidadeRepository;
import br.com.mam.sgmc.repository.PaisRepository;
import br.com.mam.sgmc.repository.UfRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocalizacaoService {

    private final PaisRepository paisRepository;
    private final UfRepository ufRepository;
    private final CidadeRepository cidadeRepository;

    public List<Pais> listarPaisesComFiltros(String sigla, String nome, String continente) {
        List<Pais> paises = paisRepository.findWithFilters(sigla, nome, continente);
        if (paises.isEmpty()) {
            throw new ResourceNotFoundException("Não foi possível encontrar nenhum país com os filtros informados");
        }
        return paises;
    }

    public List<Uf> listarUfsComFiltros(String sigla, String nome, String regiao, String pais) {
        List<Uf> ufs = ufRepository.findWithFilters(sigla, nome, regiao, pais);
        if (ufs.isEmpty()) {
            throw new ResourceNotFoundException("Não foi possível encontrar nenhuma UF com os filtros informados");
        }
        return ufs;
    }

    public List<Cidade> listarCidadesComFiltros(String ufSigla, String nome, String pais) {
        List<Cidade> cidades = cidadeRepository.findWithFilters(ufSigla, nome, pais);
        if (cidades.isEmpty()) {
            throw new ResourceNotFoundException("Não foi possível encontrar nenhuma cidade com os filtros informados");
        }
        return cidades;
    }

}
