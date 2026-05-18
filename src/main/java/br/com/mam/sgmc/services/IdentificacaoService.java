package br.com.mam.sgmc.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.mam.sgmc.errors.ResourceNotFoundException;
import br.com.mam.sgmc.model.Identificacao;
import br.com.mam.sgmc.repository.IdentificacaoRepository;
import br.com.mam.sgmc.repository.MembroRepository;
import br.com.mam.sgmc.repository.PaisRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IdentificacaoService {

    private final IdentificacaoRepository identificacaoRepository;
    private final PaisRepository paisRepository;
    private final MembroRepository membroRepository;

    public Identificacao salvar(Identificacao identificacao, String paisSigla, Long idMembro) {
        if ("BR".equalsIgnoreCase(paisSigla) && !"CPF".equalsIgnoreCase(identificacao.getTipo())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Para o Brasil, o tipo de identificação deve ser CPF.");
        }

        identificacao.setPais(paisRepository.findById(paisSigla)
                .orElseThrow(() -> new ResourceNotFoundException("País não encontrado com sigla: " + paisSigla)));
        
        identificacao.setMembro(membroRepository.findById(idMembro)
                .orElseThrow(() -> new ResourceNotFoundException("Membro não encontrado com ID: " + idMembro)));

        return identificacaoRepository.save(identificacao);
    }

    public List<Identificacao> listarTodos() {
        return identificacaoRepository.findAll();
    }

    public Identificacao buscarPorId(Long id) {
        return identificacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Identificação não encontrada com ID: " + id));
    }

    public void deletar(Long id) {
        Identificacao identificacao = buscarPorId(id);
        identificacaoRepository.delete(identificacao);
    }
}
