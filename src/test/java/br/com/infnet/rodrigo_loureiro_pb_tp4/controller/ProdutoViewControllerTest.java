package br.com.infnet.rodrigo_loureiro_pb_tp4.controller;

import br.com.infnet.rodrigo_loureiro_pb_tp4.core.BaseTest;
import br.com.infnet.rodrigo_loureiro_pb_tp4.pages.HomePage;
import br.com.infnet.rodrigo_loureiro_pb_tp4.pages.ProdutoPage;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Tag("Selenium")
public class ProdutoViewControllerTest extends BaseTest {
    @Test
    public void deveRetornarMensagemAoSalvarComNomeInvalido() {
        String mensagemErro = new HomePage(driver)
                .abrir()
                .preencherDescricao("Descrição")
                .preencherPreco("50.00")
                .preencherQuantidade("1")
                .salvarProduto()
                .getMensagemErro();

        assertEquals("O campo nome não pode ser vazio ou nulo.", mensagemErro);
    }

    @Test
    public void deveRetornarMensagemAoSalvarComDescricaoInvalida() {
        String mensagemErro = new HomePage(driver)
                .abrir()
                .preencherNome("Nome")
                .preencherPreco("50.00")
                .preencherQuantidade("1")
                .salvarProduto()
                .getMensagemErro();

        assertEquals("O campo descrição não pode ser vazio ou nulo.", mensagemErro);
    }

    @Test
    public void deveRetornarMensagemAoSalvarComQuantidadeInvalida() {
        String mensagemErro = new HomePage(driver)
                .abrir()
                .preencherNome("Nome")
                .preencherDescricao("Descrição")
                .preencherPreco("50.00")
                .preencherQuantidade("-1")
                .salvarProduto()
                .getMensagemErro();

        assertEquals("Quantidade não pode ser negativa.", mensagemErro);
    }

    @Test
    public void deveRetornarMensagemAoSalvarComPrecoInvalido() {
        String mensagemErro = new HomePage(driver)
                .abrir()
                .preencherNome("Nome")
                .preencherDescricao("Descrição")
                .preencherQuantidade("1")
                .salvarProduto()
                .getMensagemErro();

        assertEquals("Informe um preço válido e maior que zero.", mensagemErro);
    }

    @Test
    public void deveCarregarAPaginaDeProdutos() {
        HomePage homePage = new HomePage(driver)
                .abrir();

        assertEquals("Gerenciador de Produtos", homePage.getTitulo());
    }

    @Test
    public void deveRetornarPaginaProduto() {
        ProdutoPage produtoPage = new HomePage(driver)
                .abrir()
                .visualizarProduto();

        assertEquals("Detalhes do Produto", produtoPage.getTitulo());
        assertInstanceOf(HomePage.class, produtoPage.voltar());
    }

    @Test
    public void deveRetornarPaginaProdutoNaoFornecido() {
        ProdutoPage produtoPage = new ProdutoPage(driver)
                .abrir();

        assertEquals("Produto Não Encontrado", produtoPage.getTitulo());
        assertEquals("ID do produto não fornecido.", produtoPage.getMensagemErro());
    }

    @Test
    public void deveRetornarPaginaProdutoNaoEncontrado() {
        String id = "e5256b50-c2ab-42d4-8c00-232cce1cda8c";

        ProdutoPage produtoPage = new ProdutoPage(driver)
                .abrir(id);

        assertEquals("Produto Não Encontrado", produtoPage.getTitulo());
        assertEquals("Produto com ID " + id + " não encontrado!", produtoPage.getMensagemErro());
    }

    @Test
    public void deveRetornarPaginaProdutoPesquisaIdSucesso() {
        String id = "e5256b50-c2ab-42d4-8c00-232cce1cda8b";

        ProdutoPage produtoPage = new HomePage(driver)
                .abrir()
                .preencherPesquisaId(id)
                .pesquisar();

        assertEquals("Teclado Gamer", produtoPage.getProdutoNome());
        assertEquals("Teclado mecânico com RGB", produtoPage.getProdutoDescricao());
        assertEquals("10", produtoPage.getProdutoQuantidade());
        assertEquals("R$ 250.00", produtoPage.getProdutoPreco());
        assertInstanceOf(HomePage.class, produtoPage.voltar());
    }

    @Test
    public void deveRetornarPaginaProdutoNaoEncontradoViaPesquisaPorId() {
        String id = "e5256b50-c2ab-42d4-8c00-232cce1cda8c";

        ProdutoPage produtoPage = new HomePage(driver)
                .abrir()
                .preencherPesquisaId(id)
                .pesquisar();

        assertEquals("produto não encontrado", produtoPage.getTitulo().toLowerCase());
        assertEquals("Produto com ID " + id + " não encontrado!", produtoPage.getMensagemErro());
    }

    @Test
    public void deveRetornarPaginaProdutoRequisicaoInvalida() {
        String id = "ABCDE";

        ProdutoPage produtoPage = new HomePage(driver)
                .abrir()
                .preencherPesquisaId(id)
                .pesquisar();

        assertEquals("produto não encontrado", produtoPage.getTitulo().toLowerCase());
        assertEquals("Bad Request: A requisição não está no formato correto.", produtoPage.getMensagemErro());
    }

    @Test
    public void deveCriarProduto() throws InterruptedException {
        HomePage homePage = new HomePage(driver)
                .abrir();
        int quantidadeProdutos = homePage.quantidadeProdutos();

        homePage
                .preencherNome("Mousepad Razer")
                .preencherDescricao("com RGB, USB-C e apoio para o punho")
                .preencherPreco("290.00")
                .preencherQuantidade("7")
                .salvarProduto();
        Thread.sleep(100);

        assertEquals(quantidadeProdutos + 1, homePage.quantidadeProdutos());
    }

    @Test
    public void deveCancelarCriacaoDeProduto() {
        HomePage homePage = new HomePage(driver)
                .abrir();
        int quantidadeProdutos = homePage.quantidadeProdutos();

        homePage
                .preencherNome("Mousepad Razer")
                .preencherDescricao("com RGB!")
                .preencherPreco("290.00")
                .preencherQuantidade("7")
                .cancelar();

        assertEquals(quantidadeProdutos, homePage.quantidadeProdutos());
        assertEquals("", homePage.getInputNome());
        assertEquals("", homePage.getInputDescricao());
        assertEquals("", homePage.getInputPreco());
        assertEquals("", homePage.getInputQuantidade());
    }

    @Test
    public void deveEditarProduto() throws InterruptedException {
        HomePage homePage = new HomePage(driver)
                .abrir()
                .iniciarEdicao();

        String inputQuantidade = homePage.getInputQuantidade();
        String com = String.valueOf(50 + new Random().nextInt(51));

        homePage
                .preencherQuantidade(com)
                .salvarProduto();

        Thread.sleep(200);
        homePage.iniciarEdicao();

        assertNotEquals(inputQuantidade, homePage.getInputQuantidade());
        assertEquals(com, homePage.getInputQuantidade());
    }

    @Test
    public void deveRemoverProduto() {
        HomePage homePage = new HomePage(driver)
                .abrir();
        int quantidadeProdutos = homePage.quantidadeProdutos();

        homePage.removerProduto();

        assertEquals(quantidadeProdutos - 1, homePage.quantidadeProdutos());
    }

    @Test
    public void deveOrdenarProdutoPeloNome() throws InterruptedException {
        HomePage homePage = new HomePage(driver)
                .abrir()
                .ordenar("Nome");

        Thread.sleep(100);

        List<String> nomesExibidos = homePage.obterInfoDosProdutos(1);
        List<String> nomesOrdenados = new ArrayList<>(nomesExibidos);
        nomesOrdenados.sort(String.CASE_INSENSITIVE_ORDER);

        assertEquals(nomesExibidos, nomesOrdenados);
    }

    @Test
    public void deveOrdenarProdutoPeloPreco() throws InterruptedException {
        HomePage homePage = new HomePage(driver)
                .abrir()
                .ordenar("Preço");

        Thread.sleep(100);

        List<Double> precosExibidos = homePage.obterInfoDosProdutos(2)
                .stream()
                .map(ProdutoViewControllerTest::paraDouble)
                .toList();
        List<Double> precosOrdenados = new ArrayList<>(precosExibidos);
        precosOrdenados.sort(Double::compareTo);

        assertEquals(precosExibidos, precosOrdenados);
    }

    @Test
    public void deveOrdenarProdutoPeloQuantidade() throws InterruptedException {
        HomePage homePage = new HomePage(driver)
                .abrir()
                .ordenar("Quantidade");

        Thread.sleep(100);

        List<Integer> quantidadesExibidas = homePage.obterInfoDosProdutos(3)
                .stream()
                .map(Integer::parseInt)
                .toList();
        List<Integer> quantidadesOrdenados = new ArrayList<>(quantidadesExibidas);
        quantidadesOrdenados.sort(Integer::compareTo);

        assertEquals(quantidadesExibidas, quantidadesOrdenados);
    }

    private static Double paraDouble(String valor) {
        String valorLimpo = valor
                .replace("R$", "")
                .replace(",", ".");

        return Double.parseDouble(valorLimpo);
    }
}
