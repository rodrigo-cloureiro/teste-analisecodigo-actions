package br.com.infnet.rodrigo_loureiro_pb_tp4.mock;

import br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto.Produto;
import br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto.ProdutoReal;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.UUID;

public class MockProduto {
    private static final int QUANTIDADE_TECLADO = 10;
    private static final int QUANTIDADE_MOUSE = 20;
    private static final int QUANTIDADE_MONITOR = 5;

    public static HashMap<UUID, Produto> gerarMockProdutos() {
        HashMap<UUID, Produto> produtos = new HashMap<>();

        UUID idProduto1 = UUID.fromString("e5256b50-c2ab-42d4-8c00-232cce1cda8b");
        produtos.put(idProduto1, criarProduto(
                idProduto1,
                "Teclado Gamer",
                "Teclado mecânico com RGB",
                new BigDecimal("250.00"),
                QUANTIDADE_TECLADO)
        );

        UUID idProduto2 = UUID.randomUUID();
        produtos.put(idProduto2, criarProduto(
                idProduto2,
                "Mouse",
                "Mouse sem fio",
                new BigDecimal("120.00"),
                QUANTIDADE_MOUSE)
        );

        UUID idProduto3 = UUID.randomUUID();
        produtos.put(idProduto3, criarProduto(
                idProduto3,
                "Monitor",
                "Monitor 24 polegadas Full HD",
                new BigDecimal("900.00"),
                QUANTIDADE_MONITOR)
        );

        return produtos;
    }

    private static Produto criarProduto(UUID id, String nome, String descricao, BigDecimal preco, int quantidade) {
        return new ProdutoReal.Builder()
                .id(id)
                .nome(nome)
                .descricao(descricao)
                .preco(preco)
                .quantidade(quantidade)
                .build();
    }
}
