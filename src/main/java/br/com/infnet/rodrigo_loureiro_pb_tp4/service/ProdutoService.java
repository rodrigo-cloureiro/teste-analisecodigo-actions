package br.com.infnet.rodrigo_loureiro_pb_tp4.service;

import br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto.ProdutoDTO;
import br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto.ProdutoRequestDTO;

import java.util.List;
import java.util.UUID;

public interface ProdutoService {
    List<ProdutoDTO> listar();

    ProdutoDTO buscarPorId(UUID id);

    List<ProdutoDTO> buscarPorNome(String nome);

    ProdutoDTO salvar(ProdutoRequestDTO request);

    ProdutoDTO editar(UUID id, ProdutoRequestDTO request);

    void removerPorId(UUID id);
}
