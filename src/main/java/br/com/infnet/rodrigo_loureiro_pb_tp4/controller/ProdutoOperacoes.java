package br.com.infnet.rodrigo_loureiro_pb_tp4.controller;

import br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto.ProdutoRequestDTO;
import br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto.ProdutoResponsePayload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/produtos")
public interface ProdutoOperacoes {
    @GetMapping("/listar")
    ResponseEntity<ProdutoResponsePayload> listar();

    @GetMapping("/listar/id/{id}")
    ResponseEntity<ProdutoResponsePayload> buscarPorId(@PathVariable UUID id);

    @GetMapping("/listar/nome/{nome}")
    ResponseEntity<ProdutoResponsePayload> buscarPorNome(@PathVariable String nome);

    @PostMapping("/salvar")
    ResponseEntity<ProdutoResponsePayload> salvar(@RequestBody ProdutoRequestDTO produto);

    @PutMapping("/editar/{id}")
    ResponseEntity<ProdutoResponsePayload> editar(@PathVariable UUID id, @RequestBody ProdutoRequestDTO produto);

    @DeleteMapping("/remover/{id}")
    ResponseEntity<ProdutoResponsePayload> removerPorId(@PathVariable UUID id);
}
