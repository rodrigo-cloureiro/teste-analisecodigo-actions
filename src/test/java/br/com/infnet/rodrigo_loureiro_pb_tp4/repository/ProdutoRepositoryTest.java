package br.com.infnet.rodrigo_loureiro_pb_tp4.repository;

import br.com.infnet.rodrigo_loureiro_pb_tp4.exception.ProdutoNaoEncontradoException;
import br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ProdutoRepositoryTest {
    private ProdutoRepository produtoRepository;

    private static final String nome = "Headset";
    private static final String descricao = "HyperX Cloud II";
    private static final BigDecimal preco = BigDecimal.valueOf(399.90);
    private static final int quantidade = 10;

    private Produto produtoPadrao;
    private UUID idPadrao;
    private int tamanhoLista;

    @BeforeEach
    public void setUp() {
        this.produtoRepository = new ProdutoRepositoryImpl();

        this.idPadrao = UUID.randomUUID();
        this.produtoPadrao = new ProdutoReal.Builder()
                .id(idPadrao)
                .nome(nome)
                .descricao(descricao)
                .preco(preco)
                .quantidade(quantidade)
                .build();

        this.produtoRepository.salvar(produtoPadrao);
        this.tamanhoLista = produtoRepository.listar().size();
    }

    @AfterEach
    public void tearDown() {
        if (this.produtoRepository != null) {
            this.produtoRepository = null;
        }
    }

    @Test
    public void buscaProdutoPorIdComSucesso() {
        Produto resultado = produtoRepository.buscarPorId(idPadrao);

        assertNotNull(resultado);
        assertEquals(produtoPadrao, resultado);
    }

    @Test
    public void deveRetornarProdutoNuloQuandoIdNaoExistir() {
        Produto resultado = produtoRepository.buscarPorId(UUID.randomUUID());

        assertNotNull(resultado);
        assertInstanceOf(ProdutoNulo.class, resultado);
        assertTrue(resultado.isNulo());
    }

    @Test
    public void buscaProdutoPorNomeComSucesso() {
        String busca = "Head";

        List<Produto> resultado = produtoRepository.buscarPorNome(busca);
        assertNotNull(resultado);
        assertEquals(produtoPadrao, resultado.getFirst());
    }

    @Test
    public void deveRetornarListaVaziaQuandoNomeNaoExistir() {
        String busca = "Rodrigo";

        List<Produto> resultado = produtoRepository.buscarPorNome(busca);
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    public void removerProdutoPorIdComSucesso() {
        produtoRepository.removerPorId(idPadrao);

        List<Produto> novoTamanhoLista = produtoRepository.listar();
        Produto produto = produtoRepository.buscarPorId(idPadrao);

        assertEquals(tamanhoLista - 1, novoTamanhoLista.size());
        assertInstanceOf(ProdutoNulo.class, produto);
    }

    @Test
    public void naoDeveRemoverProdutoPorIdNaoExistir() {
        produtoRepository.removerPorId(UUID.randomUUID());
        List<Produto> novoTamanhoLista = produtoRepository.listar();
        assertEquals(tamanhoLista, novoTamanhoLista.size());
    }

    @Test
    public void deveAtualizarProdutoPorIdComSucesso() {
        Produto atualizado = new ProdutoReal.Builder()
                .id(idPadrao)
                .nome(nome)
                .descricao(descricao)
                .preco(preco)
                .quantidade(2)
                .build();
        Produto resultado = produtoRepository.editar(idPadrao, atualizado);

        assertNotNull(resultado);
        assertEquals(atualizado, resultado);
        assertEquals(2, resultado.getQuantidade());
    }
}
