package br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
@ToString
public class ProdutoDTO {
    private UUID id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private int quantidade;
}
