package br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@ToString
public class ProdutoRequestDTO {
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private int quantidade;
}
