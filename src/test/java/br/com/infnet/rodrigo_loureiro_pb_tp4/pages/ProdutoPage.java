package br.com.infnet.rodrigo_loureiro_pb_tp4.pages;

import br.com.infnet.rodrigo_loureiro_pb_tp4.core.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProdutoPage extends BasePage {
    private final By titulo = By.tagName("h1");
    private final By produtoNome = By.id("produto-nome");
    private final By produtoDescricao = By.id("produto-descricao");
    private final By produtoQuantidade = By.id("produto-quantidade");
    private final By produtoPreco = By.id("produto-preco");
    private final By mensagemErro = By.id("mensagem-erro");

    private final By botaoVoltar = By.tagName("a");

    public ProdutoPage(WebDriver driver) {
        super(driver);
    }

    public ProdutoPage abrir() {
        driver.get(BASE_URL + "/produto");
        return this;
    }

    public ProdutoPage abrir(String id) {
        driver.get(BASE_URL + "/produto?id=" + id);
        return this;
    }

    public String getTitulo() {
        return getText(titulo);
    }

    public String getProdutoNome() {
        return getText(produtoNome);
    }

    public String getProdutoDescricao() {
        return getText(produtoDescricao);
    }

    public String getProdutoPreco() {
        return getText(produtoPreco);
    }

    public String getProdutoQuantidade() {
        return getText(produtoQuantidade);
    }

    public String getMensagemErro() {
        return getText(mensagemErro);
    }

    public HomePage voltar() {
        $(botaoVoltar).click();
        return new HomePage(driver);
    }

    private String getText(By by) {
        return $(by).getText();
    }
}
