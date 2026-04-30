# Sistema de Gerenciamento de Biblioteca - Etapa 1

Projeto desenvolvido para a atividade prática de **Arquiteturas de Software com Java**.

Esta versão implementa a **Etapa 1 - Arquitetura em Camadas**, com separação entre domínio, infraestrutura, aplicação e apresentação.

## Tecnologias utilizadas

- Java 17
- Maven
- Java puro, sem frameworks externos
- Repositórios em memória com `HashMap`

## Estrutura do projeto

```text
src/main/java/biblioteca/
  dominio/
    Livro.java
    Usuario.java
    Emprestimo.java
    SituacaoEmprestimo.java
    SituacaoUsuario.java
  infraestrutura/
    LivroRepositorio.java
    UsuarioRepositorio.java
    EmprestimoRepositorio.java
  aplicacao/
    LivroServico.java
    UsuarioServico.java
    EmprestimoServico.java
  apresentacao/
    Main.java
```

## Como compilar

Na raiz do projeto, execute:

```bash
mvn clean compile
```

## Como executar

Pelo IntelliJ, abra a classe abaixo e execute o método `main`:

```text
biblioteca.apresentacao.Main
```

Também é possível executar pelo terminal após compilar:

```bash
java -cp target/classes biblioteca.apresentacao.Main
```

## Fluxo demonstrado no console

A classe `Main` demonstra o fluxo completo exigido na atividade:

1. Cadastro de livro
2. Cadastro de usuário
3. Realização de empréstimo
4. Listagem de empréstimos ativos
5. Verificação de atrasos
6. Registro de devolução

## Decisões de design

### Domínio sem dependência de infraestrutura ou aplicação

As classes do pacote `biblioteca.dominio` não importam classes das camadas `infraestrutura`, `aplicacao` ou `apresentacao`.

Isso atende à restrição da Etapa 1, preservando o domínio como uma camada independente de detalhes externos.

### Regra de negócio dentro da entidade Livro

A regra de disponibilidade do livro foi implementada no método:

```java
public void realizarEmprestimo()
```

Esse método verifica se existe quantidade disponível antes de decrementar o estoque.

### Repositórios em memória

Os repositórios usam `HashMap` e implementam os métodos exigidos:

- `salvar()`
- `buscarPorId()`
- `listarTodos()`
- `remover()`

### Serviços de aplicação

A camada de aplicação coordena os casos de uso. O serviço principal é `EmprestimoServico`, contendo os métodos obrigatórios:

- `realizarEmprestimo(Long usuarioId, Long livroId)`
- `registrarDevolucao(Long emprestimoId)`
- `listarEmprestimosAtivos()`
- `verificarAtrasos()`

## Observação sobre records

Embora o projeto esteja configurado com Java 17, a Etapa 1 solicita que `Livro`, `Usuario` e `Emprestimo` sejam implementados como POJOs. Por isso, records não foram usados nesta etapa.

Os records serão mais adequados para a Etapa 3, nos eventos `EmprestimoRealizadoEvento` e `DevolucaoRegistradaEvento`, conforme o enunciado.

## Sugestão de commits para o repositório

Como a atividade penaliza ausência de histórico no Git, uma sugestão de organização é:

```text
git add pom.xml README.md
git commit -m "Configura projeto Java 17 com Maven"

git add src/main/java/biblioteca/dominio
git commit -m "Implementa entidades e enums do dominio"

git add src/main/java/biblioteca/infraestrutura
git commit -m "Implementa repositorios em memoria"

git add src/main/java/biblioteca/aplicacao
git commit -m "Implementa servicos e casos de uso da etapa 1"

git add src/main/java/biblioteca/apresentacao
git commit -m "Adiciona demonstracao do fluxo completo no console"
```
