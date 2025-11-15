package br.com.infnet.rodrigo_loureiro_pb_tp4.service;

import br.com.infnet.rodrigo_loureiro_pb_tp4.exception.ProdutoNaoEncontradoException;
import br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto.Produto;
import br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto.ProdutoDTO;
import br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto.ProdutoRequestDTO;
import br.com.infnet.rodrigo_loureiro_pb_tp4.mapper.ProdutoMapper;
import br.com.infnet.rodrigo_loureiro_pb_tp4.repository.ProdutoRepository;
import br.com.infnet.rodrigo_loureiro_pb_tp4.validation.ProdutoValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {
    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    @Override
    public List<ProdutoDTO> listar() {
        return produtoRepository.listar()
                .stream()
                .map(produtoMapper::paraProdutoDTO)
                .toList();
    }

    @Override
    public ProdutoDTO buscarPorId(UUID id) {
        Produto produto = produtoRepository.buscarPorId(id);
        if (produto.isNulo())
            throw new ProdutoNaoEncontradoException("Produto com ID " + id + " não encontrado!");

        return produtoMapper.paraProdutoDTO(produto);
    }

    @Override
    public List<ProdutoDTO> buscarPorNome(String nome) {
        ProdutoValidator.validarTexto(nome, "nome");

        List<Produto> produtos = produtoRepository.buscarPorNome(nome);
        if (produtos.isEmpty())
            throw new ProdutoNaoEncontradoException("Produto com nome '" + nome + "' não encontrado!");

        return produtos.stream()
                .map(produtoMapper::paraProdutoDTO)
                .toList();
    }

    @Override
    public ProdutoDTO salvar(ProdutoRequestDTO request) {
        UUID novoId = UUID.randomUUID();
        Produto novoProduto = produtoMapper.paraProduto(novoId, request);
        Produto produtoSalvo = produtoRepository.salvar(novoProduto);

        return produtoMapper.paraProdutoDTO(produtoSalvo);
    }

    @Override
    public ProdutoDTO editar(UUID id, ProdutoRequestDTO request) {
        buscarPorId(id);
        Produto produtoEditado = produtoMapper.paraProduto(id, request);
        produtoRepository.editar(id, produtoEditado);
        return produtoMapper.paraProdutoDTO(produtoEditado);
    }

    @Override
    public void removerPorId(UUID id) {
        buscarPorId(id);
        produtoRepository.removerPorId(id);
    }
}
