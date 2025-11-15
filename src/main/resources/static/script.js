const API_URL = 'http://localhost:8080';

const form = document.querySelector('#produto-form');
const tabela = document.querySelector('#tabela-produtos');
const idInput = document.querySelector('#produto-id');
const nomeInput = document.querySelector('#nome');
const descricaoInput = document.querySelector('#descricao');
const precoInput = document.querySelector('#preco');
const quantidadeInput = document.querySelector('#quantidade');
const cancelarButton = document.querySelector('.cancel');
const pesquisaForm = document.querySelector('#pesquisa-form');
const pesquisaIdInput = document.querySelector('#pesquisa-id');
const ordenacaoSelect = document.querySelector('#order-by');
const mensagemErro = document.querySelector('.mensagem-erro');

let produtos = [];

function validarInput(texto) {
    const tagHtmlRegex = /<[^>]*>/g;
    if (tagHtmlRegex.test(texto)) {
        return false;
    }

    const sqlInjectionRegex =
        /('|--|;|\/\*|\*\/|drop|select|insert|delete|update|union|alter|create|shutdown)/i;
    if (sqlInjectionRegex.test(texto)) {
        return false;
    }

    const scriptRegex = /(alert|onerror|onload|eval|javascript:)/i;
    if (scriptRegex.test(texto)) {
        return false;
    }

    return true;
}

function mostrarErro(input, mensagem) {
    mensagemErro.textContent = mensagem;
    input.classList.add('invalido');
}

function limparErro(input) {
    mensagemErro.textContent = '';
    input.classList.remove('invalido');
}

function clear() {
    idInput.value = null;
    form.reset();
    mensagemErro.textContent = '';
}

async function listarProdutos() {
    const resposta = await fetch(`${API_URL}/produtos/listar`);
    const data = await resposta.json();
    produtos = data.produtos;

    renderizarTabela(produtos);
}

function renderizarTabela(lista) {
    tabela.innerHTML = '';
    lista.forEach((produto) => {
        const tr = document.createElement('tr');

        tr.innerHTML = `
      <td><a href="produto?id=${produto.id}">${produto.nome}</a></td>
      <td>R$ ${produto.preco.toFixed(2)}</td>
      <td>${produto.quantidade}</td>
      <td class="actions">
          <button onclick="editarProduto('${
              produto.id
          }')" class="button">Editar</button>
          <button onclick="removerProduto('${
              produto.id
          }')" class="button remove">Remover</button>
      </td>
    `;

        tabela.appendChild(tr);
    });
}

async function editarProduto(id) {
    const resposta = await fetch(`${API_URL}/produtos/listar/id/${id}`);
    const data = await resposta.json();
    const produto = data.produtos[0];

    idInput.value = produto.id;
    nomeInput.value = produto.nome;
    descricaoInput.value = produto.descricao || '';
    precoInput.value = produto.preco;
    quantidadeInput.value = produto.quantidade;
}

async function removerProduto(id) {
    if (confirm('Tem certeza que deseja remover este produto?')) {
        await fetch(`${API_URL}/produtos/remover/${id}`, { method: 'DELETE' });
        listarProdutos();
    }
}

form.addEventListener('submit', async (e) => {
    e.preventDefault();

    [nomeInput, descricaoInput, precoInput, quantidadeInput].forEach(
        limparErro
    );

    if (!validarInput(nomeInput.value.trim())) {
        mostrarErro(nomeInput, 'Nome do produto contém caracteres inválidos.');
        return;
    }

    if (!validarInput(descricaoInput.value.trim())) {
        mostrarErro(
            descricaoInput,
            'Descrição do produto contém caracteres inválidos.'
        );
        return;
    }

    if (precoInput.value <= 0) {
        mostrarErro(precoInput, 'Informe um preço válido e maior que zero.');
        return;
    }

    if (quantidadeInput.value < 0) {
        mostrarErro(quantidadeInput, 'Quantidade não pode ser negativa.');
        return;
    }

    const id = idInput.value;
    const produto = {
        nome: nomeInput.value.trim(),
        descricao: descricaoInput.value.trim(),
        preco: parseFloat(precoInput.value),
        quantidade: parseInt(quantidadeInput.value),
    };

    let response = {};

    if (id) {
        response = await fetch(`${API_URL}/produtos/editar/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(produto),
        });
    } else {
        response = await fetch(`${API_URL}/produtos/salvar`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(produto),
        });
    }

    const data = await response.json();

    if (response.status === 400) {
        mensagemErro.textContent = data.mensagem;
        return;
    }

    clear();
    listarProdutos();
});

cancelarButton.addEventListener('click', () => {
    clear();
});

pesquisaForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const id = pesquisaIdInput.value.trim();
    if (id) {
        window.location.href = `produto?id=${id}`;
    }
});

ordenacaoSelect.addEventListener('change', () => {
    const ordenacao = ordenacaoSelect.value;

    if (ordenacao === 'nome') {
        produtos.sort((a, b) => a.nome.localeCompare(b.nome));
    } else if (ordenacao === 'preco') {
        produtos.sort((a, b) => a.preco - b.preco);
    } else if (ordenacao === 'quantidade') {
        produtos.sort((a, b) => a.quantidade - b.quantidade);
    }

    renderizarTabela(produtos);
});

listarProdutos();
