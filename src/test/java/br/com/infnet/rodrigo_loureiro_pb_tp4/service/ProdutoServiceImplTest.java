package br.com.infnet.rodrigo_loureiro_pb_tp4.service;

import br.com.infnet.rodrigo_loureiro_pb_tp4.exception.EntradaInvalidaException;
import br.com.infnet.rodrigo_loureiro_pb_tp4.exception.ProdutoNaoEncontradoException;
import br.com.infnet.rodrigo_loureiro_pb_tp4.mapper.ProdutoMapper;
import br.com.infnet.rodrigo_loureiro_pb_tp4.model.produto.*;
import br.com.infnet.rodrigo_loureiro_pb_tp4.repository.ProdutoRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class ProdutoServiceImplTest {
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private ProdutoMapper produtoMapper;
    @InjectMocks
    private ProdutoServiceImpl produtoService;

    private static final String nome = "Mouse";
    private static final String descricao = "Antigo";
    private static final BigDecimal preco = BigDecimal.TEN;
    private static final int quantidade = 10;

    private Produto produtoPadrao;
    private ProdutoDTO produtoDTOPadrao;
    private ProdutoRequestDTO produtoRequestDTOPadrao;
    private ProdutoNulo produtoNuloPadrao;
    private UUID idPadrao;

    @BeforeEach
    public void setUp() {
        this.idPadrao = UUID.randomUUID();
        this.produtoPadrao = new ProdutoReal.Builder()
                .id(idPadrao)
                .nome(nome)
                .descricao(descricao)
                .preco(preco)
                .quantidade(quantidade)
                .build();
        this.produtoDTOPadrao = new ProdutoDTO(idPadrao, nome, descricao, preco, quantidade);
        this.produtoRequestDTOPadrao = new ProdutoRequestDTO(nome, descricao, preco, quantidade);
        this.produtoNuloPadrao = new ProdutoNulo();
    }

    @AfterEach
    public void tearDown() {
        reset(produtoRepository, produtoMapper);
    }

    @Test
    public void listarDeveRetornarListaDeProdutos() {
        List<Produto> produtos = List.of(produtoPadrao);
        List<ProdutoDTO> produtoDTOs = produtos.stream()
                .map(produtoMapper::paraProdutoDTO)
                .toList();

        when(produtoRepository.listar()).thenReturn(produtos);

        List<ProdutoDTO> resultado = produtoService.listar();

        assertEquals(produtoDTOs.size(), resultado.size());
        verify(produtoRepository, times(1)).listar();
    }

    @Test
    public void buscarPorIdDeveRetornarDTOQuandoProdutoExiste() {
        produtoPadrao = mock(ProdutoReal.class);

        when(produtoRepository.buscarPorId(idPadrao)).thenReturn(produtoPadrao);
        when(produtoPadrao.isNulo()).thenReturn(false);
        when(produtoMapper.paraProdutoDTO(produtoPadrao)).thenReturn(produtoDTOPadrao);

        ProdutoDTO resultado = produtoService.buscarPorId(idPadrao);

        assertEquals(produtoDTOPadrao, resultado);
        verify(produtoRepository, times(1)).buscarPorId(idPadrao);
        verify(produtoMapper, times(1)).paraProdutoDTO(produtoPadrao);
    }

    @Test
    public void buscarPorIdDeveLancarExcecaoQuandoProdutoNaoExiste() {
        when(produtoRepository.buscarPorId(idPadrao)).thenReturn(produtoNuloPadrao);
        ProdutoNaoEncontradoException exception = assertThrows(
                ProdutoNaoEncontradoException.class, () -> produtoService.buscarPorId(idPadrao)
        );
        assertEquals(String.format("Produto com ID %s não encontrado!", idPadrao), exception.getMessage());
        verify(produtoRepository, times(1)).buscarPorId(idPadrao);
    }

    @Test
    public void buscaPorNomeDeveRetornarDTOQuandoProdutosExistem() {
        String nome = "Arroz";

        List<Produto> produtos = List.of(produtoPadrao);

        when(produtoRepository.buscarPorNome(nome)).thenReturn(produtos);
        when(produtoMapper.paraProdutoDTO(any())).thenReturn(produtoDTOPadrao);

        List<ProdutoDTO> resultado = produtoService.buscarPorNome(nome);

        assertEquals(produtoDTOPadrao, resultado.getFirst());
        assertEquals(1, resultado.size());
        verify(produtoRepository, times(1)).buscarPorNome(nome);
    }

    @Test
    public void buscarPorNomeDeveLancarExcecaoQuandoProdutoNaoExiste() {
        String nome = "Arroz";

        when(produtoRepository.buscarPorNome(nome)).thenReturn(Collections.emptyList());

        ProdutoNaoEncontradoException exception = assertThrows(
                ProdutoNaoEncontradoException.class, () -> produtoService.buscarPorNome(nome)
        );
        assertEquals(String.format("Produto com nome '%s' não encontrado!", nome), exception.getMessage());
    }

    @Test
    public void salvarDeveRetornarDTOAposSucesso() {
        when(produtoMapper.paraProduto(any(UUID.class), eq(produtoRequestDTOPadrao))).thenReturn(produtoPadrao);
        when(produtoMapper.paraProdutoDTO(produtoPadrao)).thenReturn(produtoDTOPadrao);
        when(produtoRepository.salvar(produtoPadrao)).thenReturn(produtoPadrao);

        ProdutoDTO resultado = produtoService.salvar(produtoRequestDTOPadrao);

        assertNotNull(resultado);
        assertEquals(produtoDTOPadrao, resultado);
    }

    @Test
    public void editarDeveAtualizarProdutoQuandoProdutoExistir() {
        ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO(nome, descricao, preco, 5);
        ProdutoDTO produtoDTO = new ProdutoDTO(idPadrao, nome, descricao, preco, 5);
        Produto produtoEditado = new ProdutoReal.Builder()
                .id(idPadrao)
                .nome(nome)
                .descricao(descricao)
                .preco(preco)
                .quantidade(5)
                .build();

        when(produtoRepository.buscarPorId(idPadrao)).thenReturn(produtoPadrao);
        when(produtoMapper.paraProduto(idPadrao, produtoRequestDTO)).thenReturn(produtoEditado);
        when(produtoMapper.paraProdutoDTO(produtoEditado)).thenReturn(produtoDTO);

        ProdutoDTO resultado = produtoService.editar(idPadrao, produtoRequestDTO);

        assertEquals(produtoDTO, resultado);
        verify(produtoRepository).editar(idPadrao, produtoEditado);
    }

    @Test
    public void editarDeveLancarExcecaoQuandoProdutoNaoExistir() {
        Produto produtoNulo = mock(ProdutoNulo.class);

        when(produtoRepository.buscarPorId(idPadrao)).thenReturn(produtoNulo);
        when(produtoNulo.isNulo()).thenReturn(true);

        ProdutoNaoEncontradoException exception = assertThrows(
                ProdutoNaoEncontradoException.class, () -> produtoService.editar(idPadrao, produtoRequestDTOPadrao)
        );
        assertEquals(String.format("Produto com ID %s não encontrado!", idPadrao), exception.getMessage());
    }

    @Test
    public void removerDeveRemoverProdutoQuandoProdutoExistir() {
        Produto produtoExistente = mock(ProdutoReal.class);

        when(produtoRepository.buscarPorId(idPadrao)).thenReturn(produtoExistente);
        when(produtoExistente.isNulo()).thenReturn(false);

        assertDoesNotThrow(() -> produtoService.removerPorId(idPadrao));
        verify(produtoRepository).removerPorId(idPadrao);
    }

    @Test
    public void removerDeveLancarExcecaoQuandoProdutoNaoExistir() {
        UUID idInexistente = UUID.randomUUID();
        Produto produtoNulo = mock(ProdutoNulo.class);

        when(produtoRepository.buscarPorId(idInexistente)).thenReturn(produtoNulo);
        when(produtoNulo.isNulo()).thenReturn(true);

        ProdutoNaoEncontradoException exception = assertThrows(
                ProdutoNaoEncontradoException.class, () -> produtoService.removerPorId(idInexistente)
        );
        assertEquals(String.format("Produto com ID %s não encontrado!", idInexistente), exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "Arroz, Tipo 1, 10.00, 10",
            "Feijão, Carioca, 12.0, 0",
            "Arroz, Tipo 1, 0, 10"
    })
    public void salvarDeveRetornarDTOComProdutosValidosDiferentes(
            String nome, String descricao, BigDecimal preco, int quantidade
    ) {
        ProdutoRequestDTO produtoRequestDTO = new ProdutoRequestDTO(nome, descricao, preco, quantidade);
        Produto novoProduto = new ProdutoReal.Builder()
                .id(idPadrao)
                .nome(nome)
                .descricao(descricao)
                .preco(preco)
                .quantidade(quantidade)
                .build();
        ProdutoDTO produtoDTO = new ProdutoDTO(idPadrao, nome, descricao, preco, quantidade);

        when(produtoMapper.paraProduto(any(UUID.class), eq(produtoRequestDTO))).thenReturn(novoProduto);
        when(produtoRepository.salvar(novoProduto)).thenReturn(novoProduto);
        when(produtoMapper.paraProdutoDTO(novoProduto)).thenReturn(produtoDTO);

        ProdutoDTO resultado = produtoService.salvar(produtoRequestDTO);

        assertNotNull(resultado);
        assertEquals(produtoDTO, resultado);
        verify(produtoMapper).paraProduto(any(UUID.class), eq(produtoRequestDTO));
        verify(produtoRepository).salvar(novoProduto);
        verify(produtoMapper).paraProdutoDTO(novoProduto);
    }

    @ParameterizedTest
    @CsvSource(value = {
            "null, Arroz, Tipo 1, 10.00, 10",
            "e5256b50-c2ab-42d4-8c00-232cce1cda8b, null, Tipo 1, 10.00, 10",
            "e5256b50-c2ab-42d4-8c00-232cce1cda8b, EMPTY, Tipo 1, 10.00, 10",
            "e5256b50-c2ab-42d4-8c00-232cce1cda8b, Arroz, null, 10.00, 10",
            "e5256b50-c2ab-42d4-8c00-232cce1cda8b, Arroz, EMPTY, 10.00, 10",
            "e5256b50-c2ab-42d4-8c00-232cce1cda8b, Arroz, Tipo 1, null, 10",
            "e5256b50-c2ab-42d4-8c00-232cce1cda8b, Arroz, Tipo 1, -10, 10",
            "e5256b50-c2ab-42d4-8c00-232cce1cda8b, Arroz, Tipo 1, 10.00, -5",
    }, nullValues = {"null"})
    public void salvarDeveRetornarExcecaoComProdutosInvalidos(
            UUID id, String nome, String descricao, BigDecimal preco, int quantidade
    ) {
        final String nomeFinal = "EMPTY".equals(nome) ? "" : nome;
        final String descricaoFinal = "EMPTY".equals(descricao) ? "" : descricao;

        Exception exception = assertThrows(
                EntradaInvalidaException.class, () -> new ProdutoReal.Builder()
                        .id(id)
                        .nome(nomeFinal)
                        .descricao(descricaoFinal)
                        .preco(preco)
                        .quantidade(quantidade)
                        .build()
        );
        System.out.println(exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "'; DROP TABLE produtos; --",
            "SELECT * FROM produtos",
            "1 OR 1=1",
            "'; OR '1'='1' --",
            "Produto's Special",
            "prod_uto",
            "path/to/produto",
            "<script>alert(1)</script>",
            "select id from produtos",
    })
    public void buscarPorNomeDeveFalharComEntradaMaliciosa(String entradaMaliciosa) {
        EntradaInvalidaException exception = assertThrows(
                EntradaInvalidaException.class, () -> produtoService.buscarPorNome(entradaMaliciosa)
        );
        assertEquals("O conteúdo não é permitido.", exception.getMessage());
        verify(produtoRepository, never()).buscarPorNome(anyString());
    }
}
