package br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto;

import java.math.BigDecimal;
import java.util.UUID;

public interface Produto {
    UUID getId();
    String getNome();
    String getDescricao();
    BigDecimal getPreco();
    int getQuantidade();
    boolean isNulo();
}
