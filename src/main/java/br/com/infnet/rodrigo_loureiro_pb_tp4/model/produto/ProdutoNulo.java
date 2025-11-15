package br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto;

import java.math.BigDecimal;
import java.util.UUID;

public class ProdutoNulo implements Produto {
    @Override
    public UUID getId() {
        return new UUID(0, 0);
    }

    @Override
    public String getNome() {
        return "Produto Indefinido";
    }

    @Override
    public String getDescricao() {
        return "Sem descrição";
    }

    @Override
    public BigDecimal getPreco() {
        return BigDecimal.ZERO;
    }

    @Override
    public int getQuantidade() {
        return 0;
    }

    @Override
    public boolean isNulo() {
        return true;
    }

    @Override
    public String toString() {
        return "ProdutoNulo {}";
    }
}
