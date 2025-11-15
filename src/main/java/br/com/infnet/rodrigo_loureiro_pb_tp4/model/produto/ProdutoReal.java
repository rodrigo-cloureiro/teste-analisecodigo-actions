package br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto;

import br.com.infnet.rodrigo_loureiro_pb_tp4.validation.ProdutoValidator;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@ToString
public class ProdutoReal implements Produto {
    private final UUID id;
    private final String nome;
    private final String descricao;
    private final BigDecimal preco;
    private final int quantidade;
    private final boolean nulo = false;

    private ProdutoReal(Builder builder) {
        this.id = builder.id;
        this.nome = builder.nome;
        this.descricao = builder.descricao;
        this.preco = builder.preco;
        this.quantidade = builder.quantidade;
    }

    public static class Builder {
        private UUID id = UUID.randomUUID();
        private String nome = "Nome não definido";
        private String descricao = "Descrição não definida";
        private BigDecimal preco = BigDecimal.ZERO;
        private int quantidade = 0;

        public Builder id(UUID id) {
            ProdutoValidator.validarId(id, "ID");
            this.id = id;
            return this;
        }

        public Builder nome(String nome) {
            ProdutoValidator.validarTexto(nome, "nome");
            this.nome = nome;
            return this;
        }

        public Builder descricao(String descricao) {
            ProdutoValidator.validarTexto(descricao, "descrição");
            this.descricao = descricao;
            return this;
        }

        public Builder preco(BigDecimal preco) {
            ProdutoValidator.validarPreco(preco);
            this.preco = preco;
            return this;
        }

        public Builder quantidade(int quantidade) {
            ProdutoValidator.validarQuantidade(quantidade);
            this.quantidade = quantidade;
            return this;
        }

        public ProdutoReal build() {
            return new ProdutoReal(this);
        }
    }
}
