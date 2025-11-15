const API_URL = "http://localhost:8080";

const produtoId = document.querySelector("#produto-id");
const produtoNome = document.querySelector("#produto-nome");
const produtoDescricao = document.querySelector("#produto-descricao");
const produtoPreco = document.querySelector("#produto-preco");
const produtoQuantidade = document.querySelector("#produto-quantidade");

const titulo = document.querySelector("#titulo");
const detalhesProduto = document.querySelector("#detalhes-produto");
const produtoNaoEncontrado = document.querySelector("#produto-nao-encontrado");
const mensagemErro = document.querySelector("#mensagem-erro");

const params = new URLSearchParams(window.location.search);
const id = params.get("id");

async function carregarProduto() {
  if (!id) {
    exibirErro("ID do produto não fornecido.");
    return;
  }

  try {
    const resposta = await fetch(`${API_URL}/produtos/listar/id/${id}`);
    const data = await resposta.json();
    
    if (data.status === 400) {
        exibirErro(`${data.error}: A requisição não está no formato correto.`);
        return;
    }
    
    const produto = data.produtos ? data.produtos[0] : data;

    if (!produto || !produto.id) {
      exibirErro(produto.mensagem);
      return;
    }

    produtoId.textContent = produto.id;
    produtoNome.textContent = produto.nome;
    produtoDescricao.textContent = produto.descricao;
    produtoPreco.textContent = `R$ ${produto.preco.toFixed(2)}`;
    produtoQuantidade.textContent = produto.quantidade;

    detalhesProduto.style.display = "block";
    titulo.textContent = "Detalhes do Produto";
  } catch (error) {
    console.error("Erro ao buscar produto:", error);
    exibirErro("Houve um erro ao tentar acessar o produto.");
  }
}

function exibirErro(mensagem) {
  produtoNaoEncontrado.style.display = "block";
  mensagemErro.textContent = mensagem;
  titulo.textContent = "Produto Não Encontrado";
}

carregarProduto();
