package br.com.infnet.rodrigo_loureiro_pb_tp4.mapper;

import br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto.Produto;
import br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto.ProdutoDTO;
import br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto.ProdutoRequestDTO;

import java.util.UUID;

public interface ProdutoMapper {
    ProdutoDTO paraProdutoDTO(Produto produto);

    Produto paraProduto(UUID id, ProdutoRequestDTO produtoRequestDTO);
}
