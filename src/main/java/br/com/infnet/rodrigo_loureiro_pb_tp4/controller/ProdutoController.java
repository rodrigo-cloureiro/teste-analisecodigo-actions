package br.com.infnet.rodrigo_loureiro_pb_tp4.controller;

import br.com.infnet.rodrigo_loureiro_pb_tp4.exception.EntradaInvalidaException;
import br.com.infnet.rodrigo_loureiro_pb_tp4.exception.ProdutoNaoEncontradoException;
import br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto.ProdutoDTO;
import br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto.ProdutoRequestDTO;
import br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto.ProdutoResponsePayload;
import br.com.infnet.rodrigo_loureiro_pb_tp4.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Semaphore;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE,
        RequestMethod.OPTIONS
})
@RequiredArgsConstructor
public class ProdutoController implements ProdutoOperacoes {
    private final ProdutoService produtoService;
    private final Semaphore semaphore = new Semaphore(5);

    @Override
    public ResponseEntity<ProdutoResponsePayload> listar() {
        // Condicional implementada para simular sobrecarga no sistema
        if (!semaphore.tryAcquire())
            return ResponseEntity.status(429).body(ProdutoResponsePayload.builder()
                    .mensagem("Limite de requisições simultâneas atingido. Tente novamente mais tarde.")
                    .build()
            );

        try {
            List<ProdutoDTO> produtos = produtoService.listar();
            return ResponseEntity.ok(ProdutoResponsePayload.builder()
                    .mensagem(HttpStatus.OK.toString())
                    .produtos(produtos)
                    .build()
            );
        } finally {
            semaphore.release();
        }
    }

    @Override
    public ResponseEntity<ProdutoResponsePayload> buscarPorId(UUID id) {
        try {
            ProdutoDTO produtoDTO = produtoService.buscarPorId(id);
            return ResponseEntity.ok(ProdutoResponsePayload.builder()
                    .mensagem(HttpStatus.OK.toString())
                    .produtos(List.of(produtoDTO))
                    .build()
            );
        } catch (ProdutoNaoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ProdutoResponsePayload.builder()
                    .mensagem(ex.getMessage())
                    .build()
            );
        }
    }

    @Override
    public ResponseEntity<ProdutoResponsePayload> buscarPorNome(String nome) {
        try {
            List<ProdutoDTO> produtosDTO = produtoService.buscarPorNome(nome);
            return ResponseEntity.ok(ProdutoResponsePayload.builder()
                    .mensagem(HttpStatus.OK.toString())
                    .produtos(produtosDTO)
                    .build()
            );
        } catch (ProdutoNaoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ProdutoResponsePayload.builder()
                    .mensagem(ex.getMessage())
                    .build()
            );
        }
    }

    @Override
    public ResponseEntity<ProdutoResponsePayload> salvar(ProdutoRequestDTO produto) {
        try {
            ProdutoDTO produtoDTO = produtoService.salvar(produto);
            return ResponseEntity.status(HttpStatus.CREATED).body(ProdutoResponsePayload.builder()
                    .mensagem(HttpStatus.CREATED.toString())
                    .produtos(List.of(produtoDTO))
                    .build()
            );
        } catch (EntradaInvalidaException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ProdutoResponsePayload.builder()
                    .mensagem(ex.getMessage())
                    .build()
            );
        }
    }

    @Override
    public ResponseEntity<ProdutoResponsePayload> editar(UUID id, ProdutoRequestDTO produto) {
        try {
            ProdutoDTO produtoDTO = produtoService.editar(id, produto);
            return ResponseEntity.ok(ProdutoResponsePayload.builder()
                    .mensagem(HttpStatus.OK.toString())
                    .produtos(List.of(produtoDTO))
                    .build()
            );
        } catch (ProdutoNaoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ProdutoResponsePayload.builder()
                    .mensagem(ex.getMessage())
                    .build()
            );
        } catch (EntradaInvalidaException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ProdutoResponsePayload.builder()
                    .mensagem(ex.getMessage())
                    .build()
            );
        }
    }

    @Override
    public ResponseEntity<ProdutoResponsePayload> removerPorId(UUID id) {
        try {
            produtoService.removerPorId(id);
            return ResponseEntity.noContent().build();
        } catch (ProdutoNaoEncontradoException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ProdutoResponsePayload.builder()
                    .mensagem(ex.getMessage())
                    .build()
            );
        }
    }
}
