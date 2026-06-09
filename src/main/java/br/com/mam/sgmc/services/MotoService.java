package br.com.mam.sgmc.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.mam.sgmc.errors.ResourceNotFoundException;
import br.com.mam.sgmc.model.Membro;
import br.com.mam.sgmc.model.moto.CondicaoSeguro;
import br.com.mam.sgmc.model.moto.Marca;
import br.com.mam.sgmc.model.moto.Modelo;
import br.com.mam.sgmc.model.moto.Moto;
import br.com.mam.sgmc.model.moto.Seguro;
import br.com.mam.sgmc.repository.CondicaoSeguroRepository;
import br.com.mam.sgmc.repository.MarcaRepository;
import br.com.mam.sgmc.repository.ModeloRepository;
import br.com.mam.sgmc.repository.MotoRepository;
import br.com.mam.sgmc.repository.SeguroRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MotoService {

    private final MotoRepository motoRepository;
    private final MembroService membroService;
    private final MarcaRepository marcaRepository;
    private final ModeloRepository modeloRepository;
    private final SeguroRepository seguroRepository;
    private final CondicaoSeguroRepository condicaoSeguroRepository;

    public Moto salvarMoto(Moto moto, Long idMembro) {

        if (this.motoRepository.findByPlaca(moto.getPlaca()) != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Placa já cadastrada");
        }

        Marca marca = this.marcaRepository.findByNome(moto.getModelo().getMarca().getNome());
        if (marca == null) {
            marca = this.marcaRepository.save(moto.getModelo().getMarca());
        }

        Modelo modelo = this.modeloRepository.findByNome(moto.getModelo().getNome());
        if (modelo == null) {
            modelo = this.modeloRepository.save(moto.getModelo());
        }

        Seguro seguro = this.seguroRepository.findByNome(moto.getSeguro().getNome());
        if (seguro == null) {
            seguro = this.seguroRepository.save(moto.getSeguro());
        }

        CondicaoSeguro condicaoSeguro = this.condicaoSeguroRepository
                .findByTipo(moto.getSeguro().getCondicaoSeguro().getTipo());
        if (condicaoSeguro == null) {
            condicaoSeguro = this.condicaoSeguroRepository.save(moto.getSeguro().getCondicaoSeguro());
        }

        Membro membro = this.membroService.buscarPorId(idMembro);
        if (membro == null) {
            throw new ResourceNotFoundException("Membro não encontrado");
        }
        if (membro.getAtivo().equals(br.com.mam.sgmc.model.enums.Ativo.INATIVO.getCodigo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Moto não pode ser associada a um membro inativo");
        }
        moto.setMembro(membro);
        return this.motoRepository.save(moto);
    }

    public Moto buscarPorPlaca(String placa) {
        Moto motoEncontrada = this.motoRepository.findByPlaca(placa);
        if (motoEncontrada == null) {
            throw new ResourceNotFoundException("Moto não encontrada");
        }
        return motoEncontrada;
    }

    public List<Moto> listarMotos(Long idMembro, String modelo, String marca, String nomeSeguro) {
        return motoRepository.findWithFilters(idMembro, modelo, marca, nomeSeguro);
    }

    @Transactional
    public Moto atualizarMoto(Moto moto, Long idMembro) {

        Moto motoEncontrada = this.buscarPorPlaca(moto.getPlaca());

        if (motoEncontrada == null) {
            throw new ResourceNotFoundException("Moto não encontrada");
        }

        Modelo modelo = this.modeloRepository.findByNome(moto.getModelo().getNome());
        if (modelo == null) {
            modelo = this.modeloRepository.save(moto.getModelo());
        }

        Marca marca = this.marcaRepository.findByNome(moto.getModelo().getMarca().getNome());
        if (marca == null) {
            marca = this.marcaRepository.save(moto.getModelo().getMarca());
        }

        Seguro seguro = this.seguroRepository.findByNome(moto.getSeguro().getNome());
        if (seguro == null) {
            seguro = this.seguroRepository.save(moto.getSeguro());
        }

        CondicaoSeguro condicaoSeguro = this.condicaoSeguroRepository
                .findByTipo(moto.getSeguro().getCondicaoSeguro().getTipo());
        if (condicaoSeguro == null) {
            condicaoSeguro = this.condicaoSeguroRepository.save(moto.getSeguro().getCondicaoSeguro());
        }

        Membro membro = this.membroService.buscarPorId(idMembro);
        if (membro == null) {
            throw new ResourceNotFoundException(
                    "Membro não encontrado! Moto não pode ser associada a um membro que não existe.");
        }
        if (membro.getAtivo().equals(br.com.mam.sgmc.model.enums.Ativo.INATIVO.getCodigo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Moto não pode ser associada a um membro inativo");
        }

        motoEncontrada.setAno(moto.getAno());
        motoEncontrada.setCor(moto.getCor());
        motoEncontrada.setSeguro(seguro);
        motoEncontrada.setModelo(modelo);
        motoEncontrada.setMembro(membro);

        return this.motoRepository.save(motoEncontrada);
    }

    @Transactional
    public void deletarMoto(Long idMembro, String placa) {
        Moto motoEncontrada = this.buscarPorPlaca(placa);
        if (motoEncontrada == null) {
            throw new ResourceNotFoundException("Moto não encontrada");
        }
        if (!motoEncontrada.getMembro().getId().equals(idMembro)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Moto não pertence ao membro");
        }

        this.motoRepository.deleteByPlaca(placa);
    }
}
