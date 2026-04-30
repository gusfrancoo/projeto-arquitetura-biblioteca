# Sistema de Gerenciamento de Biblioteca

Projeto da atividade pratica de **Arquiteturas de Software com Java**.

Este README foi organizado como **linha do tempo** para manter o historico da evolucao entre as etapas.

## Linha do Tempo do Projeto

## Etapa 1 - Arquitetura em Camadas (Concluida)

### Objetivo da etapa

Separar o sistema em camadas com responsabilidades claras:
- Dominio (regras de negocio)
- Aplicacao (orquestracao de casos de uso)
- Infraestrutura (persistencia em memoria)
- Main como ponto de entrada da aplicacao

### O que foi implementado

- Entidades de dominio: `Livro`, `Usuario`, `Emprestimo`
- Enums de estado: `SituacaoEmprestimo`, `SituacaoUsuario`
- Servicos de aplicacao: `LivroServico`, `UsuarioServico`, `EmprestimoServico`
- Repositorios em memoria com `HashMap`
- Fluxo completo no console pela classe `Main`

### Arquitetura atual (Etapa 1)

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
  infraestrutura/
    LivroRepositorio.java
    UsuarioRepositorio.java
    EmprestimoRepositorio.java
  Main.java
```

### Fluxo demonstrado no console

1. Cadastro de livro
2. Cadastro de usuario
3. Realizacao de emprestimo
4. Listagem de emprestimos ativos
5. Verificacao de atrasos
6. Registro de devolucao

### Decisoes de design da Etapa 1

- Dominio sem dependencia de framework
- Regra de disponibilidade no proprio `Livro`
- Caso de uso principal centralizado em `EmprestimoServico`
- Persistencia simples em memoria para foco arquitetural

---

## Etapa 2 - Adaptacao Arquitetural (Planejada)

### Objetivo da etapa

Evoluir da separacao em camadas para uma estrutura com menor acoplamento entre aplicacao e infraestrutura, sem perder as regras ja consolidadas na Etapa 1.

### O que sera adicionado na Etapa 2

- Interfaces de repositorio (portas) para a camada de aplicacao
- Adaptadores de infraestrutura implementando essas interfaces
- Dependencia dos servicos para interfaces, nao para classes concretas
- Ajustes no `Main` para montar as dependencias na nova estrutura

### O que permanece da Etapa 1

- Entidades e regras de negocio do dominio
- Fluxo funcional de emprestimo e devolucao
- Repositorios em memoria (como primeira implementacao de adaptador)

### Status atual

- README preparado para timeline da evolucao
- Implementacao da Etapa 2: **pendente** (sera feita na proxima fase)

---

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
java -cp target/classes biblioteca.Main
```
