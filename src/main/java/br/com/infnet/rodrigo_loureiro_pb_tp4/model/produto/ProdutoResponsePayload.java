package br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ProdutoResponsePayload {
    private String mensagem;
    private List<ProdutoDTO> produtos;
    @Builder.Default
    private LocalDateTime dataHora = LocalDateTime.now(ZoneId.of("America/Sao_Paulo"));
}
