# Sistema de Gerenciamento de Biblioteca

Projeto da atividade pratica de **Arquiteturas de Software com Java**.

Este README esta em formato de **linha do tempo**, preservando o que foi feito em cada etapa.

## Linha do Tempo

## Etapa 1 - Arquitetura em Camadas (Concluida)

### Objetivo

Separar o sistema em camadas com responsabilidades claras:
- Dominio (regras de negocio)
- Aplicacao (orquestracao de casos de uso)
- Infraestrutura (persistencia em memoria)
- Main como ponto de entrada

### Entregas da etapa

- Entidades e enums de dominio
- Servicos de aplicacao com fluxo de emprestimo/devolucao
- Repositorios em memoria com `HashMap`
- Demonstracao no console

### Estrutura da Etapa 1

```text
src/main/java/biblioteca/
  dominio/
  aplicacao/
    LivroServico.java
    UsuarioServico.java
    EmprestimoServico.java
  infraestrutura/
    LivroRepositorio.java
    UsuarioRepositorio.java
    EmprestimoRepositorio.java
  Main.java
```

---

## Etapa 2 - Arquitetura Hexagonal (Concluida)

### Objetivo

Desacoplar casos de uso da infraestrutura usando **portas e adaptadores**.

### O que mudou da Etapa 1 para a Etapa 2

1. Foram criadas portas de saida na aplicacao:
   - `LivroRepositorioPort`
   - `UsuarioRepositorioPort`
   - `EmprestimoRepositorioPort`
   - `PortaNotificacao`
2. Foi criada a porta de entrada `PortaEmprestimo`, implementada por `EmprestimoServico`.
3. Os servicos passaram a depender dessas interfaces (portas), e nao de classes concretas.
4. Para cada porta de repositorio, existem dois adaptadores:
   - Memoria (`LivroRepositorio`, `UsuarioRepositorio`, `EmprestimoRepositorio`)
   - CSV (`LivroRepositorioCsv`, `UsuarioRepositorioCsv`, `EmprestimoRepositorioCsv`)
5. O `Main` demonstra a troca de adaptador em tempo de inicializacao:
   - primeiro executa com memoria;
   - depois executa com CSV;
   - sem alterar regra de negocio dos servicos.

### O que foi preservado

- Regras de negocio do dominio
- Fluxo funcional da Etapa 1
- Persistencia em memoria como implementacao inicial dos adaptadores

### Estrutura da Etapa 2

```text
src/main/java/biblioteca/
  dominio/
    Livro.java
    Usuario.java
    Emprestimo.java
    SituacaoEmprestimo.java
    SituacaoUsuario.java
  aplicacao/
    LivroServico.java
    UsuarioServico.java
    EmprestimoServico.java
    porta/
      entrada/
        PortaEmprestimo.java
      saida/
        LivroRepositorioPort.java
        UsuarioRepositorioPort.java
        EmprestimoRepositorioPort.java
        PortaNotificacao.java
  infraestrutura/
    LivroRepositorio.java        (adaptador de saida)
    LivroRepositorioCsv.java     (adaptador de saida)
    UsuarioRepositorio.java      (adaptador de saida)
    UsuarioRepositorioCsv.java   (adaptador de saida)
    EmprestimoRepositorio.java   (adaptador de saida)
    EmprestimoRepositorioCsv.java (adaptador de saida)
    NotificacaoConsole.java      (adaptador de notificacao)
  apresentacao/
    Main.java                    (composicao/entrada)
```

---

## Fluxo demonstrado no console

1. Cadastro de livro
2. Cadastro de usuario
3. Realizacao de emprestimo
4. Listagem de emprestimos ativos
5. Verificacao de atrasos
6. Registro de devolucao

## Tecnologias

- Java 17
- Maven
- Java puro (sem frameworks externos)

## Como compilar

```bash
mvn clean compile
```

## Como executar

```bash
java -cp target/classes biblioteca.apresentacao.Main
```
