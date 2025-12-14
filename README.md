# 💻 Atividade avaliativa (15%) - Projeto de Sistemas de Software - Criação e Manutenção de usuários

## 🌟 Visão Geral do Projeto

Este projeto consiste em uma **Atividade Avaliativa (15%)** da disciplina de **Projeto de Sistemas de Software**, focada na **Criação e Manutenção de Usuários**. O sistema foi desenvolvido em Java, utilizando o Maven e o banco de dados SQLite.

A arquitetura implementa o padrão **MVP (Model-View(Passive)-Presenter)** para garantir a separação clara de responsabilidades, facilitando a manutenibilidade e escalabilidade do código.

## 👥 Integrantes do Grupo

* **Natan Gomes Vieira**
* **Pedro Eugênio**

Obs: A dupla adotou em grande parte do desenvolvimento o método Pair Programming, sendo assim a justificava pelo histórico de commits.

## 🛠️ Stack Tecnológica e Ferramentas

O projeto foi desenvolvido com as seguintes tecnologias e ferramentas:

| Categoria | Detalhes |
| :--- | :--- |
| **Ambiente de Desenvolvimento (IDE)** | NetBeans |
| **Gerenciador de Dependências** | Maven |
| **Linguagem de Programação** | Java 17 |
| **Banco de Dados** | SQLite (Embutido) |

## 📦 Dependências Externas

O projeto utiliza as seguintes bibliotecas externas (listadas no `pom.xml`):

### 1. Validador de Senha

* **Descrição:** Biblioteca para validar a força e complexidade de senhas de usuário.
* **Repositório:** [validador-senha](https://github.com/claytonfraga/validadorsenha)

```xml
<dependency>
    <groupId>com.github.claytonfraga</groupId>
    <artifactId>validadorsenha</artifactId>
    <version>-SNAPSHOT</version>
</dependency>
```
### 2. Sistema de Logs

* **Descrição:** Biblioteca para processar os logs da aplicação.
* **Repositório:** [sistema-log](https://github.com/natangomesvieira/sistema-log)

```xml
  <dependency>
    <groupId>com.github.natangomesvieira</groupId>
    <artifactId>sistema-log</artifactId>
    <version>1.0.4</version>
</dependency>
```

## 🖼️ A estrutura de pacotes e suas responsabilidades são:

* **Model**: Contém as entidades (objetos de domínio).
* **View**: Responsável pela interface do usuário (telas).
* **Presenter**: Atua como o comunicador central. Orquestra a lógica de negócio (`Service`) e atualiza a View com os resultados.
* **Service**: Contém as regras de negócio e a lógica transacional do sistema.
* **Repository**: Abstrai e gerencia a comunicação com o banco de dados (CRUD - Create, Read, Update, Delete).
* **Factory**: Responsável pela criação de instâncias (principalmente de Views e seus respectivos Presenters), injetando as dependências necessárias para a aplicação.
