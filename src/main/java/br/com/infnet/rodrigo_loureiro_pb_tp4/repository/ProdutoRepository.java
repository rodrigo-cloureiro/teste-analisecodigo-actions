package br.com.infnet.rodrigo_loureiro_pb_tp4.repository;

import br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto.Produto;

import java.util.List;
import java.util.UUID;

public interface ProdutoRepository {
    List<Produto> listar();

    Produto buscarPorId(UUID id);

    List<Produto> buscarPorNome(String nome);

    Produto salvar(Produto produto);

    Produto editar(UUID id, Produto produto);

    void removerPorId(UUID id);
}
