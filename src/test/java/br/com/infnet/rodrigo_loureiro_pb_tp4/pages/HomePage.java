package br.com.infnet.rodrigo_loureiro_pb_tp4.pages;

import br.com.infnet.rodrigo_loureiro_pb_tp4.core.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.ArrayList;
import java.util.List;

public class HomePage extends BasePage {
    private final By titulo = By.tagName("h1");
    private final By inputNome = By.id("nome");
    private final By inputDescricao = By.id("descricao");
    private final By inputQuantidade = By.id("quantidade");
    private final By inputPreco = By.id("preco");

    private final By inputPesquisaId = By.id("pesquisa-id");
    private final By selectOrdenacao = By.name("order-by");

    private final By botaoSalvar = By.cssSelector("#produto-form button[type='submit']");
    private final By botaoCancelar = By.cssSelector("button[type='button']");
    private final By botaoPesquisar = By.cssSelector("#pesquisa-form button[type='submit']");
    private final By botaoEditar = By.xpath("//td[@class='actions']/button[text()='Editar']");
    private final By botaoRemover = By.xpath("//td[@class='actions']/button[text()='Remover']");

    private final By tabelaProdutos = By.id("tabela-produtos");
    private final By mensagemErro = By.className("mensagem-erro");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage abrir() {
        driver.get(BASE_URL);
        return this;
    }

    public String getTitulo() {
        return $(titulo).getText();
    }

    public String getInputNome() {
        return $(inputNome).getDomProperty("value");
    }

    public String getInputDescricao() {
        return $(inputDescricao).getDomProperty("value");
    }

    public String getInputQuantidade() {
        return $(inputQuantidade).getDomProperty("value");
    }

    public String getInputPreco() {
        return $(inputPreco).getDomProperty("value");
    }

    public String getMensagemErro() {
        return $(mensagemErro).getText();
    }

    public HomePage preencherNome(String nome) {
        preencherInput(inputNome, nome);
        return this;
    }

    public HomePage preencherDescricao(String descricao) {
        preencherInput(inputDescricao, descricao);
        return this;
    }

    public HomePage preencherPreco(String preco) {
        preencherInput(inputPreco, preco);
        return this;
    }

    public HomePage preencherQuantidade(String quantidade) {
        preencherInput(inputQuantidade, quantidade);
        return this;
    }

    public HomePage preencherPesquisaId(String pesquisaId) {
        preencherInput(inputPesquisaId, pesquisaId);
        return this;
    }

    public ProdutoPage pesquisar() {
        $(botaoPesquisar).click();
        return new ProdutoPage(driver);
    }

    public HomePage ordenar(String textoVisivel) {
        Select ordenacao = new Select($(selectOrdenacao));
        ordenacao.selectByVisibleText(textoVisivel);
        return this;
    }

    public HomePage salvarProduto() {
        ((JavascriptExecutor) driver).executeScript(
                "document.querySelectorAll('input[required]').forEach(el => el.removeAttribute('required'));"
        );

        $(botaoSalvar).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(tabelaProdutos));
        wait.until(ExpectedConditions.elementToBeClickable(botaoEditar));
        return this;
    }

    public HomePage iniciarEdicao() {
        $(botaoEditar).click();
        return this;
    }

    public HomePage removerProduto() {
        $(botaoRemover).click();
        driver.switchTo().alert().accept();
        return this;
    }

    public HomePage cancelar() {
        $(botaoCancelar).click();
        return this;
    }

    public ProdutoPage visualizarProduto() {
        $(tabelaProdutos).findElement(By.tagName("a")).click();
        return new ProdutoPage(driver);
    }

    public List<String> obterInfoDosProdutos(int index) {
        List<WebElement> linhas = $(tabelaProdutos).findElements(By.tagName("tr"));
        List<String> info = new ArrayList<>();

        for (WebElement linha : linhas) {
            String nome = linha.findElement(By.cssSelector(String.format("td:nth-child(%d)", index))).getText();
            info.add(nome.trim());
        }
        return info;
    }

    public int quantidadeProdutos() {
        try {
            return $(tabelaProdutos).findElements(By.tagName("tr")).size();
        } catch (TimeoutException e) {
            return 0;
        }
    }

    private void preencherInput(By by, String conteudo) {
        $(by).clear();
        $(by).sendKeys(conteudo);
    }
}
