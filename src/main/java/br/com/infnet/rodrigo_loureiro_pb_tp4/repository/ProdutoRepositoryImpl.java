package br.com.infnet.rodrigo_loureiro_pb_tp4.repository;

import br.com.infnet.rodrigo_loureiro_pb_tp4.mock.MockProduto;
import br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto.Produto;
import br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto.ProdutoNulo;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class ProdutoRepositoryImpl implements ProdutoRepository {
    private static final Produto PRODUTO_NULO = new ProdutoNulo();
    private final HashMap<UUID, Produto> produtos;

    public ProdutoRepositoryImpl() {
        this.produtos = MockProduto.gerarMockProdutos();
    }

    @Override
    public List<Produto> listar() {
        return List.copyOf(produtos.values()
                .stream()
                .sorted(Comparator.comparing(Produto::getNome))
                .toList()
        );
    }

    @Override
    public Produto buscarPorId(UUID id) {
        return Optional.ofNullable(produtos.get(id))
                .orElse(PRODUTO_NULO);
    }

    @Override
    public List<Produto> buscarPorNome(String nome) {
        return produtos.values()
                .stream()
                .filter(p -> p.getNome().toLowerCase().contains(nome.toLowerCase()))
                .toList();
    }

    @Override
    public Produto salvar(Produto produto) {
        produtos.put(produto.getId(), produto);
        return produto;
    }

    @Override
    public Produto editar(UUID id, Produto produto) {
        produtos.put(id, produto);
        return produto;
    }

    @Override
    public void removerPorId(UUID id) {
        produtos.remove(id);
    }
}
