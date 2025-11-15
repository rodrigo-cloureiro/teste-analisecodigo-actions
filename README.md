# 🧩 Projeto

## 🚀 Visão Geral

Este projeto é uma aplicação Java Spring Boot integrada com pipelines automatizados via GitHub Actions.
O objetivo é fornecer uma base sólida para desenvolvimento, testes e deploy contínuo.

## 🏗️ Como Executar a Aplicação

### ✅ Pré-requisitos

- Java 21
- Maven 3.5.6
- Junit 5.13.4
- Spring Boot 3.5.6
- Mockito 5.20.0
- Selenium 4.36.0
- WebDriverManager 6.3.2
- JaCoCo 0.8.12

### 💻 Como executar a aplicação

#### 1. Clonar o repositório

```bash
git clone https://github.com/rodrigo-cloureiro/Rodrigo_Loureiro_PB_TP4
cd Rodrigo_Loureiro_PB_TP4
```

#### 2. Compilar e executar

```bash
mvn spring-boot:run
```

#### 3. Acessar a aplicação

```bash
http://localhost:8080
```

### 🧪 Como executar os testes

#### 1. Executar testes

```bash
mvn test
```

#### 2. Executar testes ignorando testes com Selenium

```bash
mvn test -Dgroups=!Selenium -B
```

### ▶️ Como executar e interpretar o workflow

O workflow é acionado automaticamente nos seguintes eventos:

| Evento              | Descrição                                      |
|---------------------|------------------------------------------------|
| `push`              | Quando há push no branch `main`                |
| `pull_request`      | Ao abrir ou atualizar PRs para o branch `main` |
| `workflow_dispatch` | Execução manual pelo GitHub                    |

A pipeline CI/CD está descrita no arquivo *.github/workflows/ci.yml* e é composta por três jobs principais:

### 🧠 1. analyse — Análise com CodeQL

Realiza a análise estática de segurança e qualidade do código.

### Etapas principais:

- Checkout do código
- Configuração do ambiente (JDK 21)
- Inicialização do CodeQL
- Compilação do projeto
- Execução da análise CodeQL

### Resultado esperado:

Identificar vulnerabilidades e problemas de qualidade no código. Os resultados podem ser visualizados na aba Security →
Code scanning alerts do GitHub.

### 🏗️ 2. build — Build e Testes

Executa o build da aplicação e gera os artefatos (.jar).

### Etapas principais:

- Compilação do projeto
- Execução de testes unitários (exceto testes Selenium)
- Geração de relatório de cobertura (JaCoCo)
- Upload dos artefatos (relatório e .jar)

### Relatórios disponíveis:

- target/site/jacoco/index.html — relatório de cobertura de testes
- target/*.jar — pacote final da aplicação

### Interpretação:

Caso o build ou os testes falhem, o workflow exibirá mensagens de erro no log da execução.

### 🚀 3. deploy — Simulação de Deploy

Simula o processo de deploy usando o artefato gerado no job anterior.

### Etapas principais:

- Download do artefato .jar
- Simulação do deploy
- Finalização do processo

### Objetivo:

Garantir que o fluxo de build → artefato → deploy esteja funcional, ainda que o deploy real não ocorra.

## 🛠️ Refatoração

#### 1. [Remoção de código morto](https://github.com/rodrigo-cloureiro/Rodrigo_Loureiro_PB_TP4/commit/6d2f58b30c8acd942287af9944b8c257c74c8d16)

#### 2. [Substituição de declarações lambda por expressões lambda](https://github.com/rodrigo-cloureiro/Rodrigo_Loureiro_PB_TP4/commit/7c15e9744ddc9f72c3cd3078ecf93c19ac930bb7)

#### 3. [Implementação de try-with-resources](https://github.com/rodrigo-cloureiro/Rodrigo_Loureiro_PB_TP4/commit/97c434514eba6bd9589c8252518b00f7d176f698)

#### 4. [Adicionando verificação de cobertura mínima de 85% no jacoco](https://github.com/rodrigo-cloureiro/Rodrigo_Loureiro_PB_TP4/commit/9fad96a827cfbe42894f9ebc021ed3d266dcce77)

#### 5. [Tornando o nome do método do mais intuitivo, implementando método privado para criar o produto e substituindo valores mágicos por constantes](https://github.com/rodrigo-cloureiro/Rodrigo_Loureiro_PB_TP4/commit/c221db528bb48c32f75f1e36621379c01aea1656)

#### 6. [Tornando o método buscarPorNome mais legível e moderno, constante ProdutoNulo para evitar criação a cada chamada de buscarPorId e implementando validação de existência no método removerPorId](https://github.com/rodrigo-cloureiro/Rodrigo_Loureiro_PB_TP4/commit/f18379f3b4fa34e407458df72f4de770bfd7fd22)

#### 7. [Utilizando método buscarPorId para evitar lógica de verificação duplicada](https://github.com/rodrigo-cloureiro/Rodrigo_Loureiro_PB_TP4/commit/cf790523ba9f24a81a778cbfdaaccec5f07c1769)

#### 8. [Separação de responsabilidades: Repository é responsável apenas pelo CRUD. Validação/lógica é realizada pelo Service](https://github.com/rodrigo-cloureiro/Rodrigo_Loureiro_PB_TP4/commit/7f040e1ed6cfd1e7eed9700c88c48dacd0ad4aa0)